package com.pedronveloso.openliveview.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.protocol.*;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

public class BtServer {
	
	public interface Callback {
		void handleResponse(Response aResponse);
		void isReadyChanged(boolean isReady);
	}
	
	private Handler mHandler = new Handler();
	
	private Callback mCallback = null;
	private BluetoothDevice mLiveView;
	private BluetoothSocket mSocket = null;
	private DataOutputStream mOutput = null;
	private ConnectedThread mThread = null;
	private Context mContext = null;
	private static BtServer mInstance = null;
	
	private BtServer() {
	}
	
	public static BtServer instance() {
		if (mInstance == null)
			mInstance = new BtServer();
		return mInstance;
	}
	
	public void start(BluetoothDevice liveView) {
		if (isReady())
			stop();
		mLiveView = liveView;
		ConnectThread ct = new ConnectThread(mLiveView);
		ct.start();
	}
	
	public void stop() {
		mThread = null;
		if (mCallback != null)
			mCallback.isReadyChanged(isReady());
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isReady() {
		return mSocket != null && mOutput != null && mThread != null;
	}
	
	public void setCallback(Callback callback) {
		mCallback = callback;
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	
	private void HandleResponse(Response response) {
		if (mCallback != null && response != null) {
			mCallback.handleResponse(response);
		}
	}
	
	public synchronized boolean write(Request aRequest) {
		if (!isReady())
			return false;
		try {
			aRequest.Write(mOutput);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private class ConnectedThread extends Thread {
		private DataInputStream mInput;
		
		public ConnectedThread(DataInputStream stream) {
			mInput = stream;
		}
		
		@Override
		public void run() {
			write(new ScreenPropertiesRequest());
			mHandler.post(new Runnable() {
				
				public void run() {
					if (mCallback != null)
						mCallback.isReadyChanged(isReady());
				}
			});
			
			while(mThread == this) {
				try {
	        		int msgId = mInput.read();
	        		if (msgId != -1) {
	        			final Response resp = Response.parse((byte)msgId, mInput);
	        			
	        			if (resp.shouldSendAck()) {
	        				AckMessage ack = new AckMessage(resp.getMsgId());
	        				write(ack);
	        			}
	        			
	        			if (resp instanceof LiveViewRequest) {
	        				// LiveView asks us to answer something!
	        				Request request = ((LiveViewRequest)resp).answer();
							if (request != null)
								write(request);
	        			}
	        			mHandler.post(new Runnable() {
							public void run() {
								HandleResponse(resp);
							}
	        			});
	            	}
	            } catch (IOException e) {
        			mHandler.post(new Runnable() {
						public void run() {
							if (mThread == this)
								BtServer.this.stop();
						}
        			});
	                break;
	            }
			}
		}
	}
	
	private class ConnectThread extends Thread {
		
        public ConnectThread(BluetoothDevice device) {
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.LIVEVIEW_UUID));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mSocket.connect();
                mOutput = new DataOutputStream(mSocket.getOutputStream());
                DataInputStream input = new DataInputStream(mSocket.getInputStream());
                mThread = new ConnectedThread(input);
                mThread.start();
                
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
        }
    }
}
