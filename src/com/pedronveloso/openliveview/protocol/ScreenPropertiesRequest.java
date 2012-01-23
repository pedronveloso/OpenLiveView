package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class ScreenPropertiesRequest extends Request {

	@Override
	protected byte getMessageId() {
		return Constants.REQUEST_SCREEN_PROPERTIES;
	}

	@Override
	protected int getPayloadSize() {		
		return Constants.PROTOCOL_VERSION.length +
               Constants.SIZE_BYTE; // String Terminator
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.write(Constants.PROTOCOL_VERSION);
		writer.writeByte(0); // String Terminator
	}

}
