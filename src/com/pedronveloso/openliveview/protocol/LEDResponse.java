package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class LEDResponse extends Response {

	private boolean mOk = false;
	
	@Override
	protected void readPayload(DataInputStream input, int payloadLength) throws IOException {
		mOk = (payloadLength == C.SIZE_BYTE) && input.readByte() == C.MSG_OK;
	}

	public boolean getOk() {
		return mOk;
	}

}
