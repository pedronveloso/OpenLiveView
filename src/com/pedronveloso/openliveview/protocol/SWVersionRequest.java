package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;

public class SWVersionRequest extends Request{

	public SWVersionRequest() {
	}
	
	@Override
	protected byte getMessageId() {
		return Constants.REQUEST_SW_VERSION;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_BYTE; // MSG_OK
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.writeByte(Constants.MSG_OK);
	}

}
