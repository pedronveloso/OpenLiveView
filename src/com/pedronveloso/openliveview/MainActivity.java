package com.pedronveloso.openliveview;

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

import com.pedronveloso.openliveview.protocol.Response;
import com.pedronveloso.openliveview.protocol.VibrateRequest;
import com.pedronveloso.openliveview.protocol.VibrateResponse;

public class MainActivity extends Activity
{
    private static String LOG_TAG = "OpenLiveView";
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice myLiveView;
    TextView output;
    
    public void addToOuput(String line){
    	Log.d(LOG_TAG, line);
		output.setText(output.getText()+"\n"+line);		
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        output = (TextView) findViewById(R.id.tv_output);
        
        if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
            addToOuput("Current platform byte order is: BigEndian");
        else
            addToOuput("Current platform byte order is: LittleEndian");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(LOG_TAG,"does not support bluetooth devices");
            finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Log.e(LOG_TAG, "bluetooth not enabled");
            finish();
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if ("LiveView".equals(device.getName())) {
                	addToOuput(device.getName() + " : " + device.getAddress());
                	myLiveView = device;
                }
            }
        }

        if (myLiveView != null) {
        	ConnectThread con = new ConnectThread(myLiveView);
        	con.run();
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
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                e.printStackTrace();
                addToOuput("Fail to create RFCOMM");
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
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    addToOuput("close exception");
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
                addToOuput("Fail on Cancel");
            }
        }
    }

    public static String getHexString(byte[] b, int count) {
    	String result = "";
    	if (count == 0)
    		count = b.length;
    	for (int i=0; i < count; i++) {
    		result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
    	}
    	return result;
	}
    
    
    public void manageConnectedSocket(BluetoothSocket mmSocket){
        addToOuput("reached manageConnectSocket");
        DataInputStream tmpIn = null;
        DataOutputStream tmpOut = null;
        try {
            tmpIn = new DataInputStream(mmSocket.getInputStream());
            tmpOut = new DataOutputStream(mmSocket.getOutputStream());
        } catch (IOException e) {
            addToOuput("Failed to get In and/or Out stream(s)");
            e.printStackTrace();
        }


        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()


        try {
        	VibrateRequest request = new VibrateRequest((short)1000, (short)500);
        	request.Write(tmpOut);
        } catch (IOException e) {
            e.printStackTrace();
            addToOuput("FAIL TO WRITE");
        }

        while (true) {
            try {
                Response resp = Response.parse(tmpIn);
                if (resp instanceof VibrateResponse)
                	addToOuput("Vibrate:" + ((VibrateResponse)resp).getOk());
                else
                	addToOuput("Unknow Response!");
                break;
            } catch (IOException e) {
                addToOuput("FAIL TO READ");
                addToOuput(e.getMessage());
                break;
            }
        }

    }


}
