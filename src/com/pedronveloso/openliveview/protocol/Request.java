package com.pedronveloso.openliveview.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Request {
	
	protected static final int SIZE_BYTE = 1;
	protected static final int SIZE_SHORT = 2;
	protected static final int SIZE_INT = 4;
	
	
	public static final int REQUEST_VIBRATE = 42;
	
	
	protected abstract byte getMessageId();
	
	protected abstract int getPayloadSize();
	
	protected abstract void WritePayload(DataOutputStream writer) throws IOException;
	
	
	public void Write(DataOutputStream stream) throws IOException {
		stream.writeByte(getMessageId());
		stream.writeByte(SIZE_INT);
		stream.writeInt(getPayloadSize());
		WritePayload(stream);
		stream.flush();
	}
}
