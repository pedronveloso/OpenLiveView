package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class StandByResponse extends Request {

	@Override
	protected byte getMessageId() {
		return Constants.RESPONSE_STANDBY;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_BYTE;
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.writeByte(Constants.MSG_OK);
	}

}
