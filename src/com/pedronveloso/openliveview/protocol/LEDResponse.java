package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataInputStream;
import java.io.IOException;

public class LEDResponse extends Response {

	private boolean mOk = false;
	
	@Override
	protected void readPayload(DataInputStream input, int payloadLength) throws IOException {
		mOk = (payloadLength == Constants.SIZE_BYTE) && input.readByte() == Constants.MSG_OK;
	}

	public boolean getOk() {
		return mOk;
	}

	@Override
	public boolean shouldSendAck() {
		return true;
	}
}
