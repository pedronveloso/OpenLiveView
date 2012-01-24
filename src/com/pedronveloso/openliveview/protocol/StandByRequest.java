package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class StandByRequest extends LiveViewRequest {

	private int mState;
	
	public int getState() {
		return mState;
	}
	
	@Override
	public Request answer() {
		return new StandByResponse();
	}

	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
		mState = input.readUnsignedByte();
	}
}
