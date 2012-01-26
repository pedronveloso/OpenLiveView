package com.pedronveloso.openliveview.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

import com.pedronveloso.openliveview.Utils.Constants;

public class MenuItemCountRequest extends Request {

	private int mItemCount;
	
	public MenuItemCountRequest(int itemCount) {
		mItemCount = itemCount;
	}
	
	@Override
	protected byte getMessageId() {
		return Constants.REQUEST_MENU_ITEM_COUNT;
	}

	@Override
	protected int getPayloadSize() {
		return Constants.SIZE_BYTE;
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.write(mItemCount);
	}

}
