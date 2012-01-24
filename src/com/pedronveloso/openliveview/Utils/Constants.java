package com.pedronveloso.openliveview.Utils;

public interface Constants {
    
    public static String LOG_TAG = "OpenLV";
    
    public static String LIVEVIEW_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    
    // Command Success Codes
    public static int SC_SUCCESS = 1;
    public static int SC_FAIL_IO = 2; //failed because of a regular IO Exception
    public static int SC_FAIL_IO_STREAM = 3; //failed because of IO Exception when declaring the Stream
    public static int SC_FAIL_UNRECOGNIZED_COMMAND = 4; //failed the identifier of the given command is unrecognized



    // For Information about the protocol see:
    // https://github.com/BurntBrunch/LivelierView/blob/master/protocol.txt

    public static final String STRING_ENCODING = "iso-8859-1";
    public static final byte[] PROTOCOL_VERSION = Utils.prepareString("0.0.6");

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
	public static final byte REQUEST_DATE_TIME = 38;
	public static final byte RESPONSE_DATE_TIME = 39;
    public static final byte REQUEST_LED = 40;
    public static final byte RESPONSE_LED = 41;
    public static final byte REQUEST_VIBRATE = 42;
    public static final byte RESPONSE_VIBRATE = 43;
	public static final byte REQUEST_SW_VERSION = 68;
	public static final byte RESPONSE_SW_VERSION = 69;
}
