package com.pedronveloso.openliveview.protocol;

import java.io.ByteArrayOutputStream;
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
	
	
	public byte[] serialize() {
		byte[] result = new byte[0];
		try
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			DataOutputStream writer = new DataOutputStream(data);
			writer.writeByte(getMessageId());
			writer.writeByte(SIZE_INT);
			writer.writeInt(getPayloadSize());
			WritePayload(writer);
			writer.flush();
			result = data.toByteArray();
			data.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
