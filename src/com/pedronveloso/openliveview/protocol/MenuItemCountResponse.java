package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class MenuItemCountResponse extends Response {

	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
	}

}
