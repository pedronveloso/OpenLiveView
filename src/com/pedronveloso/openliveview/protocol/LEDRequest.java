package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.Utils;

import java.io.DataOutputStream;
import java.io.IOException;

public class LEDRequest extends Request{

	private int mColor;
	private short mDelay;
	private short mDuration;
	
	public LEDRequest(int color, short delay, short duration) {
		mColor = color;
		mDelay = delay;
		mDuration = duration;
	}
	
	@Override
	protected byte getMessageId() {
		return Constants.REQUEST_LED;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_SHORT + // Color
               Constants.SIZE_SHORT + // Delay
               Constants.SIZE_SHORT;  // Duration
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.writeShort(Utils.colorToRGB565(mColor));
		writer.writeShort(mDelay);
		writer.writeShort(mDuration);
	}

}
