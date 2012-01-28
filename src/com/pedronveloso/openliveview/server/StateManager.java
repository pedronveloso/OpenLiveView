package com.pedronveloso.openliveview.server;

import com.pedronveloso.openliveview.protocol.*;

/*
 * Keeps track of the device state
 */
public class StateManager implements BtServer.Callback {

	private String mSoftwareVersion;
	private int mScreenState;
	
	private static StateManager sInstance;
	
	private StateManager() {
		super();
	}
	
	public static StateManager instance() {
		if (sInstance == null)
			sInstance = new StateManager();
		return sInstance;
	}

	public void handleResponse(Response aResponse) {
		if (aResponse instanceof SWVersionResponse) {
			mSoftwareVersion = ((SWVersionResponse)aResponse).getVersion();
		} else if (aResponse instanceof ScreenPropertiesResponse) {
			// TODO
		} else if (aResponse instanceof StandByRequest) {
			mScreenState = ((StandByRequest)aResponse).getState();
		}
	}

	public void isReadyChanged(boolean isReady) {
		// ignore... 
	}
	
	public int getScreenState() {
		return mScreenState;
	}
	
	public String getSoftwareVersion() {
		return mSoftwareVersion;
	}
	
}
