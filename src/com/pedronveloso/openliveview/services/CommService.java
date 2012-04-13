package com.pedronveloso.openliveview.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.StaticImages;
import com.pedronveloso.openliveview.Utils.Utils;
import com.pedronveloso.openliveview.protocol.Response;
import com.pedronveloso.openliveview.protocol.SWVersionResponse;
import com.pedronveloso.openliveview.protocol.ScreenPropertiesResponse;
import com.pedronveloso.openliveview.protocol.UnknownResponse;
import com.pedronveloso.openliveview.server.BtServer;
import com.pedronveloso.openliveview.server.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Set;

/**
 * Communication server. Handlers communication in the background
 */
public class CommService extends Service implements BtServer.Callback{

    private BluetoothAdapter mBluetoothAdapter;

    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Executed just once
     */
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            InputStream is = getAssets().open("test36.png");
            StaticImages.staticIconAllEvents = new byte[is.available()];
            is.read(StaticImages.staticIconAllEvents);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            Utils.logError("Could NOT create icon from Assets");
        }
        Menu.instance().initDefaultItems();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra(Constants.EXTRA_START_SERVICE)){
            Utils.log("CommService received StartService");
            if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN))
                Utils.log("Current platform byte order is: BigEndian");
            else
                Utils.log("Current platform byte order is: LittleEndian");

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Utils.logError( "does not support bluetooth devices");
                stopSelf();
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Utils.logError( "bluetooth not enabled");
                stopSelf();
            }


            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            BluetoothDevice liveView = null;
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice device : pairedDevices) {
                    // Add the name and address to an array adapter to show in a ListView
                    if ("LiveView".equals(device.getName())) {
                        Utils.log(device.getName() + " : " + device.getAddress());
                        liveView = device;
                    }
                }
            }

            if (liveView != null) {
                BtServer.instance().setContext(this);
                BtServer.instance().addCallback(this);
                BtServer.instance().start(liveView);
            }else{
                Utils.logError("Failed to obtain liveview device.");
            }
        }
        return START_STICKY;
    }


    public void isReadyChanged(boolean isReady) {
        if (isReady)
            mBluetoothAdapter.cancelDiscovery();
        /*btnVibrate.setEnabled(isReady);
        btnLEDred.setEnabled(isReady);
        btnLEDgreen.setEnabled(isReady);
        btnLEDblue.setEnabled(isReady);*/
        Utils.log("IsReadyChanged: "+isReady);
    }

    public void handleResponse(Response aResponse) {
        if (aResponse instanceof ScreenPropertiesResponse) {
            Utils.log("Protocol Version: "+((ScreenPropertiesResponse)aResponse).getProtocolVersion());
        } else if (aResponse instanceof SWVersionResponse) {
            Utils.log("SW Version: "+((SWVersionResponse)aResponse).getVersion());
        } else if (aResponse instanceof UnknownResponse) {
            Utils.log("Unknown Response: "+(aResponse).getMsgId());
        } else
            Utils.log("handling: "+ aResponse.getClass().getSimpleName());
    }
}
