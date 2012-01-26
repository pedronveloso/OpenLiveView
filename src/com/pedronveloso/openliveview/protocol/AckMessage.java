package com.pedronveloso.openliveview.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

import com.pedronveloso.openliveview.Utils.Constants;

public class AckMessage extends Request {

	private byte mAckMessage;	
	
	public AckMessage(byte ackMessage) {
		mAckMessage = ackMessage;
	}
	
	@Override
	protected byte getMessageId() {
		// TODO Auto-generated method stub
		return Constants.RESPONSE_ACK;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_BYTE;
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.writeByte(mAckMessage);
		
	}

}
