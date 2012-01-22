package com.pedronveloso.openliveview.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

public class ScreenPropertiesRequest extends Request {

	@Override
	protected byte getMessageId() {
		return C.REQUEST_SCREEN_PROPERTIES;
	}

	@Override
	protected int getPayloadSize() {		
		return C.PROTOCOL_VERSION.length +
			   C.SIZE_BYTE; // String Terminator
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.write(C.PROTOCOL_VERSION);
		writer.writeByte(0); // String Terminator
	}

}
