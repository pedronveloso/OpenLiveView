package com.pedronveloso.openliveview.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.StaticImages;
import com.pedronveloso.openliveview.Utils.Utils;

public class GetMenuIconResponse extends Request {

	@Override
	protected byte getMessageId() {
		return Constants.RESPONSE_MENU_GET_ITEM;
	}
	
	private static final byte[] UNKNOW_CONST = new BigInteger("0000000000000003000000000000", 16).toByteArray();
	private static final byte[] TITLE = Utils.prepareString("FOOBAR");
	
	@Override
	protected int getPayloadSize() {
		return UNKNOW_CONST.length + 
			   Constants.SIZE_BYTE + 
			   TITLE.length + 
			   StaticImages.staticIconAllEvents.length;
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.write(UNKNOW_CONST);
		writer.writeByte(TITLE.length);
		writer.write(TITLE);
		writer.write(StaticImages.staticIconAllEvents);
	}

}
