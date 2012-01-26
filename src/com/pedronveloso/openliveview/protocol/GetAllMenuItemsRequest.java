package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class GetAllMenuItemsRequest extends LiveViewRequest {

	@Override
	public Request answer() {
		return new GetMenuIconResponse();
	}

	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
	}

	
	
}
