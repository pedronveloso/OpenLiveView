package com.pedronveloso.openliveview.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.*;
import java.nio.ByteOrder;
import java.util.Set;
import java.util.UUID;

import com.pedronveloso.openliveview.R;
import com.pedronveloso.openliveview.Utils.CommandResult;
import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.protocol.*;

public class MainActivity extends Activity
{
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice myLiveView;
    private int commandID = 0; //increments with each sent command
    TextView output;
    private BluetoothSocket mmSocket;
    
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

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;

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
				addToOutput("Failed to connect");
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    addToOutput("close exception");
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            runOnUiThread(new Runnable() {

                public void run() {
                    new SendCommand().execute((int) Constants.REQUEST_VIBRATE);
                }
            });
            //manageConnectedSocket(mmSocket);
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


    //
    // AsyncTasks, Threads and stuff like that
    //

    /**
     * AsyncTask that send a command in a non-blocking UI call, and returns
     */
    private class SendCommand extends AsyncTask<Integer,Integer, CommandResult> {

        @Override
        protected CommandResult doInBackground(Integer... args) {
            int thisCommandID = commandID;
            commandID++;

            //get output stream
            DataOutputStream tmpOut;
            try {
                tmpOut = new DataOutputStream(mmSocket.getOutputStream());
            } catch (IOException e) {
                addToOutput("Failed to get Output stream");
                e.printStackTrace();
                return new CommandResult(Constants.SC_FAIL_IO_STREAM,thisCommandID);
            }

            //send command to device
            try{
                switch (args[0]){
                    case (int) Constants.REQUEST_SCREEN_PROPERTIES:
                        Log.d(Constants.LOG_TAG,"Will try screen properties request");
                        ScreenPropertiesRequest screen_request = new ScreenPropertiesRequest();
                        screen_request.Write(tmpOut);
                        break;
                    case (int) Constants.REQUEST_VIBRATE:
                        VibrateRequest vibrate_request = new VibrateRequest((short)1000, (short)500);
                        vibrate_request.Write(tmpOut);
                        break;
                    default:
                        return new CommandResult(Constants.SC_FAIL_UNRECOGNIZED_COMMAND,thisCommandID);
                }

            }catch (IOException e){
                addToOutput("Failed to get Output stream");
                e.printStackTrace();
                return new CommandResult(Constants.SC_FAIL_IO_STREAM,thisCommandID);
            }
            return new CommandResult(Constants.SC_SUCCESS,thisCommandID);
        }
        
        @Override
        protected void onPostExecute(CommandResult commandResult){
            if (commandResult.getCommandSuccessCode()==Constants.SC_SUCCESS) //command was sent with success
            {
                DataInputStream tmpIn = null;
                byte[] buffer = new byte[1024];  // buffer store for the stream
                try {
                    tmpIn = new DataInputStream(mmSocket.getInputStream());
                } catch (IOException e) {
                    addToOutput("Failed to get Input stream");
                    e.printStackTrace();
                }
                int bytes;
                try {
                    bytes = tmpIn.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    addToOutput(Integer.toString(bytes));
                } catch (IOException e) {
                    e.printStackTrace();
                    addToOutput("FAILED to read input from cmd ID: " + commandResult.getCommandID());
                }
            }
            else
            {
                addToOutput("FAILED! Reason: "+commandResult.getCommandSuccessCode());
            }
        }
    }

}
