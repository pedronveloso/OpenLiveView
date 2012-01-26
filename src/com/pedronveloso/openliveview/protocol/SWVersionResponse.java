package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

import com.pedronveloso.openliveview.Utils.Utils;

public class SWVersionResponse extends LiveViewRequest {

	private String mVersion;
	
	@Override
	protected void readPayload(DataInputStream input, int payloadLength) throws IOException {
		byte[] buffer = new byte[payloadLength];
		input.read(buffer);
		mVersion = Utils.getString(buffer);
	}

	public String getVersion() {
		return mVersion;
	}

	@Override
	public Request answer() {
		return new MenuItemCountRequest(1);
	}
}
