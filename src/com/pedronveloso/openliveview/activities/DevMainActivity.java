package com.pedronveloso.openliveview.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.pedronveloso.openliveview.R;
import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.StaticImages;
import com.pedronveloso.openliveview.Utils.Utils;
import com.pedronveloso.openliveview.protocol.*;
import com.pedronveloso.openliveview.server.BtServer;
import com.pedronveloso.openliveview.server.Menu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Set;

public class DevMainActivity extends Activity
                          implements BtServer.Callback, OnClickListener
                          
{
    BluetoothAdapter mBluetoothAdapter;
    TextView output;
    private Button btnVibrate;
    private Button btnLEDred;
    private Button btnLEDgreen;
    private Button btnLEDblue;
    
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
        
        setContentView(R.layout.test_activity);

		TestActivityAdapter adapter = new TestActivityAdapter();
        ViewPager myPager = (ViewPager) findViewById(R.id.testactivitypager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);


    }

    public void isReadyChanged(boolean isReady) {
    	if (isReady)
    		mBluetoothAdapter.cancelDiscovery();
    	btnVibrate.setEnabled(isReady);
    	btnLEDred.setEnabled(isReady);
        btnLEDgreen.setEnabled(isReady);
        btnLEDblue.setEnabled(isReady);
    	addToOutput("IsReadyChanged: "+isReady);
    }
	
    public void handleResponse(Response aResponse) {
		if (aResponse instanceof ScreenPropertiesResponse) {
			addToOutput("Protocol Version: "+((ScreenPropertiesResponse)aResponse).getProtocolVersion());
		} else if (aResponse instanceof SWVersionResponse) {
			addToOutput("SW Version: "+((SWVersionResponse)aResponse).getVersion());
		} else if (aResponse instanceof UnknownResponse) {
			addToOutput("Unknown Response: "+(aResponse).getMsgId());
		} else
			addToOutput("handling: "+ aResponse.getClass().getSimpleName());
	}

    @Override
    protected void onDestroy() {
    	BtServer.instance().removeCallback(this);
    	BtServer.instance().stop();
    	super.onDestroy();
    }

	public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_test_vibrate:
                BtServer.instance().write(new VibrateRequest((short)0, (short)500));
                break;
            case R.id.btn_test_led_red:
                BtServer.instance().write(new LEDRequest(Color.RED, (short)0, (short)500));
                break;
            case R.id.btn_test_led_green:
                BtServer.instance().write(new LEDRequest(Color.GREEN, (short)0, (short)500));
                break;
            case R.id.btn_test_led_blue:
                BtServer.instance().write(new LEDRequest(Color.BLUE, (short)0, (short)500));
                break;
        }
	}

    private class TestActivityAdapter extends PagerAdapter{
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }

        public Object instantiateItem(View collection, int position) {

                    LayoutInflater inflater = (LayoutInflater) collection.getContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    int resId = 0;
                    switch (position) {
                    case 0:
                        resId = R.layout.test_activity_left;
                        break;
                    case 1:
                        resId = R.layout.test_activity_right;
                        break;
                    }

                    View view = inflater.inflate(resId, null);

                    ((ViewPager) collection).addView(view, 0);

                    // link to layout stuff
                    if (position==0){
                        output = (TextView) view.findViewById(R.id.tv_output);
                    }
                    if (position==1){
                        btnVibrate = (Button) view.findViewById(R.id.btn_test_vibrate);
                        btnVibrate.setOnClickListener(DevMainActivity.this);
                        btnLEDred = (Button) view.findViewById(R.id.btn_test_led_red);
                        btnLEDred.setOnClickListener(DevMainActivity.this);
                        btnLEDgreen = (Button) view.findViewById(R.id.btn_test_led_green);
                        btnLEDgreen.setOnClickListener(DevMainActivity.this);
                        btnLEDblue = (Button) view.findViewById(R.id.btn_test_led_blue);
                        btnLEDblue.setOnClickListener(DevMainActivity.this);

                        //continue loading the app logic after UI has been instantiated
                        continueLoading();
                    }

                    return view;
                }

        public int getCount() {
            return 2;
        }


    }


    /**
     * Continue app execution after UI elements have been instantiated
     */
    private void continueLoading(){
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
        BluetoothDevice liveView = null;
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                if ("LiveView".equals(device.getName())) {
                    addToOutput(device.getName() + " : " + device.getAddress());
                    liveView = device;
                }
            }
        }

        if (liveView != null) {
            BtServer.instance().setContext(this);
            BtServer.instance().addCallback(this);
            BtServer.instance().start(liveView);
        }else{
            addToOutput("Failed to obtain liveview device.");
        }
    }

}
