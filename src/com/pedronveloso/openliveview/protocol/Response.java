package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class Response {
		
	protected abstract void readPayload(DataInputStream input, int payloadLength) throws IOException;
	
	public static Response parse(byte msgId, DataInputStream input) throws IOException {
		Response result = null;
		int lengthSize = (byte)input.readByte();
		int payloadlength = 0;
		switch(lengthSize) {
			case Constants.SIZE_BYTE:
				payloadlength = input.readByte(); break;
			case Constants.SIZE_SHORT:
				payloadlength = input.readShort(); break;
			case Constants.SIZE_INT:
				payloadlength = input.readInt(); break;
			default:
				return null; // Unknown Datatype!
		}
		
		switch(msgId) {
			case Constants.RESPONSE_VIBRATE:
				result = new VibrateResponse(); break;
			case Constants.RESPONSE_LED:
				result = new LEDResponse(); break;
			case Constants.RESPONSE_SCREEN_PROPERTIES:
				result = new ScreenPropertiesResponse(); break;
			case Constants.REQUEST_STANDBY:
				result = new StandByRequest();
		}
		
		if (result != null && payloadlength > 0) {
			result.readPayload(input, payloadlength);
		}
		return result;
	}
}
