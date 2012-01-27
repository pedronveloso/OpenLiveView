package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

import com.pedronveloso.openliveview.Utils.Constants;

public class GetAllMenuItemsRequest extends LiveViewRequest {

	private boolean mOk;
	
	@Override
	public Request answer() {
		return null; // Should be handled somewhere else 
		             // cause there will be more then one response
	}

	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
		 mOk = input.readByte() == Constants.MSG_OK;
	}

	public boolean getOk() {
		return mOk;
	}
	
}
