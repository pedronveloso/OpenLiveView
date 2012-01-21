package com.pedronveloso.openliveview.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public abstract class Response {
	public static final byte RESPONSE_VIBRATE = 43;
		
	protected abstract void readPayload(DataInputStream input, int payloadLength) throws IOException;
	
	public static Response parse(DataInputStream input) throws IOException {
		Response result = null;
		byte msgId = (byte)input.readByte();
		int lengthSize = (byte)input.readByte();
		int payloadlength = 0;
		switch(lengthSize) {
			case Request.SIZE_BYTE:
				payloadlength = input.readByte(); break;
			case Request.SIZE_SHORT:
				payloadlength = input.readShort(); break;
			case Request.SIZE_INT:
				payloadlength = input.readInt(); break;
			default:
				return null; // Unknown Datatype!
		}
		
		switch(msgId) {
			case RESPONSE_VIBRATE:
				result = new VibrateResponse(); break;					
		}
		
		if (result != null && payloadlength > 0) {
			result.readPayload(input, payloadlength);
		}
		return result;
	}
}
