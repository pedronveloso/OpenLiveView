package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Request {
	
	protected abstract byte getMessageId();
	
	protected abstract int getPayloadSize();
	
	protected abstract void WritePayload(DataOutputStream writer) throws IOException;
	
	
	public void Write(DataOutputStream stream) throws IOException {
		stream.writeByte(getMessageId());
		stream.writeByte(Constants.SIZE_INT);
		stream.writeInt(getPayloadSize());
		WritePayload(stream);
		stream.flush();
	}
}
