package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class VibrateResponse extends Response {

	private static final int MSG_OK = 0; 
	private boolean mOk = false;
	
	@Override
	protected void readPayload(DataInputStream input, int payloadLength) throws IOException {
		mOk = (payloadLength == Request.SIZE_BYTE) && input.readByte() == MSG_OK;
	}

	public boolean getOk() {
		return mOk;
	}
}
