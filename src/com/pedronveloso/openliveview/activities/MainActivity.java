package com.pedronveloso.openliveview.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.nio.ByteOrder;
import java.util.Set;
import java.util.UUID;

import com.pedronveloso.openliveview.R;
import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.protocol.*;

public class MainActivity extends Activity
{
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice myLiveView;
    TextView output;
    
    public void addToOutput(final String line){
    	Log.d(Constants.LOG_TAG, line);
    	runOnUiThread(new Runnable() {
			
			public void run() {
				output.setText(output.getText()+"\n"+line);	
			}
		});
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        output = (TextView) findViewById(R.id.tv_output);
        
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
            addToOutput("Current platform byte order is: BigEndian");
        else
            addToOutput("Current platform byte order is: LittleEndian");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(Constants.LOG_TAG,"does not support bluetooth devices");
            finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Log.e(Constants.LOG_TAG, "bluetooth not enabled");
            finish();
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if ("LiveView".equals(device.getName())) {
                	addToOutput(device.getName() + " : " + device.getAddress());
                	myLiveView = device;
                }
            }
        }

        if (myLiveView != null) {
        	ConnectThread con = new ConnectThread(myLiveView);
        	con.start();
        }

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {            	
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.LIVEVIEW_UUID));
                addToOutput("Socket created");
            } catch (IOException e) {
                e.printStackTrace();
                addToOutput("Fail to create RFCOMM");
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                addToOutput("Socket connected");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    addToOutput("close exception");
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                addToOutput("Fail on Cancel");
            }
        }
    }
    
    
    public void manageConnectedSocket(BluetoothSocket mmSocket){
        addToOutput("reached manageConnectSocket");
        DataInputStream tmpIn = null;
        DataOutputStream tmpOut = null;
        try {
            tmpIn = new DataInputStream(mmSocket.getInputStream());
            tmpOut = new DataOutputStream(mmSocket.getOutputStream());
        } catch (IOException e) {
            addToOutput("Failed to get In and/or Out stream(s)");
            e.printStackTrace();
        }

        try {
        	VibrateRequest request = new VibrateRequest((short)1000, (short)500);
        	//LEDRequest request = new LEDRequest(Color.YELLOW, (short)100, (short)5000);
        	//ScreenPropertiesRequest request = new ScreenPropertiesRequest();
        	request.Write(tmpOut);
        } catch (IOException e) {
            e.printStackTrace();
            addToOutput("FAIL TO WRITE");
        }

        while (true) {
            try {
        		int msgId = tmpIn.read();
        		if (msgId != -1) {
        			Response resp = Response.parse((byte)msgId, tmpIn);
        			
        			if (resp instanceof LiveViewRequest) {
        				// LiveView asks us to answer something!
        				Request request = ((LiveViewRequest)resp).answer();
        				request.Write(tmpOut);
        			}
        			
        			
        			
	                if (resp instanceof VibrateResponse)
	                	addToOutput("Vibrate:" + ((VibrateResponse)resp).getOk());
	                else if (resp instanceof LEDResponse)
	                	addToOutput("LED: "+((LEDResponse)resp).getOk());
	                else if (resp instanceof ScreenPropertiesResponse)
	                	addToOutput("Got Screen Infos :" + ((ScreenPropertiesResponse)resp).getWidth() + "x" + ((ScreenPropertiesResponse)resp).getHeight());
	                else if (resp instanceof StandByRequest) 
	                	addToOutput("New StandBy State: "+ ((StandByRequest)resp).getState());
	                else
	                	addToOutput("Unknown Response :" + msgId);
            	}
            } catch (IOException e) {
                addToOutput("FAIL TO READ");
                addToOutput(e.getMessage());
                break;
            }
        }
    }

}
