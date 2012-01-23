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
		return 0;
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
	}

}
