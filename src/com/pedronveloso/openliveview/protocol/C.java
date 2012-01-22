package com.pedronveloso.openliveview.protocol;

/*
 * Contains Constants
 */
public interface C {

    public static String LOG_TAG = "OpenLiveView";

	// For Information about the protocol see:
	// https://github.com/BurntBrunch/LivelierView/blob/master/protocol.txt
	
	public static final String STRING_ENCODING = "iso-8859-1";
	public static final byte[] PROTOCOL_VERSION = Utils.prepareString("0.0.3");
	
	public static final int MSG_OK = 0; 
	
	public static final int SIZE_BYTE = 1;
	public static final int SIZE_SHORT = 2;
	public static final int SIZE_INT = 4;	
	
	public static final int STATE_AWAKE = 2;
	public static final int STATE_CLOCK = 1;
	public static final int STATE_SLEEP = 0;
	
	public static final byte REQUEST_SCREEN_PROPERTIES = 1;
	public static final byte RESPONSE_SCREEN_PROPERTIES = 2;
	public static final byte REQUEST_STANDBY = 7;
	public static final byte RESPONSE_STANDBY = 8;
	public static final byte REQUEST_LED = 40;
	public static final byte RESPONSE_LED = 41;
	public static final byte REQUEST_VIBRATE = 42;
	public static final byte RESPONSE_VIBRATE = 43;
}
