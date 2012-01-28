package com.pedronveloso.openliveview.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.Utils;
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
	
	private List<Callback> mCallbacks = new ArrayList<Callback>();
    private BluetoothSocket mSocket = null;
	private DataOutputStream mOutput = null;
	private ConnectedThread mThread = null;
	private Context mContext = null;
	private static BtServer sInstance = null;
	
	private BtServer() {
	}
	
	public static BtServer instance() {
		if (sInstance == null)
            sInstance = new BtServer();
		return sInstance;
	}
	
	public void start(BluetoothDevice liveView) {
		if (isReady())
			stop();
        ConnectThread ct = new ConnectThread(liveView);
		ct.start();
	}
	
	public void stop() {
		mThread = null;
		for (Callback c : mCallbacks)
			c.isReadyChanged(isReady());
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isReady() {
        if (Constants.VERBOSE_LOGCAT){
            if (mSocket==null)
                Utils.log("isReady will fail because mSocket is NULL");
            if (mOutput==null)
            	Utils.log("isReady will fail because mOutput is NULL");
            if (mThread==null)
            	Utils.log("isReady will fail because mThread is NULL");
        }
		return mSocket != null && mOutput != null && mThread != null;
	}
	
	public void addCallback(Callback callback) {
		mCallbacks.add(callback);		
	}
	
	public void removeCallback(Callback callback) {
		mCallbacks.remove(callback);
	}
	
	public void setContext(Context context) {
		mContext = context;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	private void HandleResponse(Response response) {
		if (response != null) {
			for (Callback c : mCallbacks)
				c.handleResponse(response);
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

    /**
     * Running Thread that handles the I/O with a connected device
     */
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
					for (Callback c : mCallbacks)
						c.isReadyChanged(isReady());
				}
			});
			
			while(mThread == this) {
				try {
	        		int msgId = mInput.read();
	        		if (msgId != -1) {
	        			final Response resp = Response.parse((byte)msgId, mInput);
	        			if (resp == null)
	        				continue;
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
							if (mThread == ConnectedThread.this)
								BtServer.this.stop();
						}
        			});
	                break;
	            } catch (NullPointerException nullE){
                    // ignore?
                    Utils.log("Null exception when trying to read response: "+nullE.getMessage());
                    nullE.printStackTrace();
                }
			}
		}
	}

    /**
     * Thread responsible for handling the effective connection to the device
     */
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
            	// Make sure the state manager is registered.
            	if (!mCallbacks.contains(StateManager.instance()))
            		addCallback(StateManager.instance());
            	
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
                } catch (IOException ignored) { }
            }
        }
    }
}
