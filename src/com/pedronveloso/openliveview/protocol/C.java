package com.pedronveloso.openliveview.protocol;

/*
 * Contains Constants
 */
public class C {
	// For Information about the protocol see:
	// https://github.com/BurntBrunch/LivelierView/blob/master/protocol.txt
	
	public static final int MSG_OK = 0; 
	
	public static final int SIZE_BYTE = 1;
	public static final int SIZE_SHORT = 2;
	public static final int SIZE_INT = 4;
	
	public static final byte REQUEST_LED = 40;
	public static final byte RESPONSE_LED = 41;
	public static final byte REQUEST_VIBRATE = 42;
	public static final byte RESPONSE_VIBRATE = 43;
	
}
