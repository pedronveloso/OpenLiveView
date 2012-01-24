package com.pedronveloso.openliveview.protocol;

import com.pedronveloso.openliveview.Utils.Constants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;


public class DateTimeResponse extends Request {

	@Override
	protected byte getMessageId() {
		return Constants.RESPONSE_DATE_TIME;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_INT + // Time
			   Constants.SIZE_BYTE; // Display 24h format
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		Calendar rightNow = Calendar.getInstance();
		int time =(int)((rightNow.get(Calendar.ZONE_OFFSET) + 
						 rightNow.get(Calendar.DST_OFFSET) + 
						 new Date().getTime()) / 1000L);
		writer.writeInt(time);
		writer.writeByte(1); // <-- TODO 24h Format or not?
	}

}
