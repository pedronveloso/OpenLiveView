package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class UnknownResponse extends Response {

	private byte[] mBuffer;
	
	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
		mBuffer = new byte[payloadLength];
		input.read(mBuffer);
	}
	
	public byte[] getBuffer() {
		return mBuffer;
	}

}
