package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class DateTimeRequest extends LiveViewRequest {

	@Override
	public Request answer() {
		return new DateTimeResponse();
	}

	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
	}
}
