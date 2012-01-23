package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataOutputStream;
import java.io.IOException;

public class VibrateRequest extends Request {
	
	private short mDelay;
	private short mDuration;
	
	public VibrateRequest(short delay, short duration) {
		super();
		mDelay = delay;
		mDuration = duration;
	}

	@Override
	protected byte getMessageId() {
		return Constants.REQUEST_VIBRATE;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_SHORT  // Delay
		     + Constants.SIZE_SHORT; // Duration
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.writeShort(mDelay);
		writer.writeShort(mDuration);
	}
}
