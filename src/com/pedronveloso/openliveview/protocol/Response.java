package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class Response {
		
	protected abstract void readPayload(DataInputStream input, int payloadLength) throws IOException;
	
	public static Response parse(DataInputStream input) throws IOException {
		Response result = null;
		byte msgId = (byte)input.readByte();
		int lengthSize = (byte)input.readByte();
		int payloadlength = 0;
		switch(lengthSize) {
			case C.SIZE_BYTE:
				payloadlength = input.readByte(); break;
			case C.SIZE_SHORT:
				payloadlength = input.readShort(); break;
			case C.SIZE_INT:
				payloadlength = input.readInt(); break;
			default:
				return null; // Unknown Datatype!
		}
		
		switch(msgId) {
			case C.RESPONSE_VIBRATE:
				result = new VibrateResponse(); break;
			case C.RESPONSE_LED:
				result = new LEDResponse(); break;
		}
		
		if (result != null && payloadlength > 0) {
			result.readPayload(input, payloadlength);
		}
		return result;
	}
}
