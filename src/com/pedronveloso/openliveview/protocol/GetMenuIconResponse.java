package com.pedronveloso.openliveview.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

import com.pedronveloso.openliveview.Utils.Constants;
import com.pedronveloso.openliveview.Utils.Utils;

public class GetMenuIconResponse extends Request {
		
	private boolean mAlertItem;
	private int mUnreadCount;
	private short mMenuItemId;
	private boolean mPlainText;
	private byte[] mTitle;
	private byte[] mBitmap;
	
	public GetMenuIconResponse(boolean alertItem, int unreadCount, short menuItemId, String title, byte[] bitmap)
	{
		mAlertItem = alertItem;
		mUnreadCount = unreadCount;
		mMenuItemId = (short)(menuItemId + 3);
		mTitle = Utils.prepareString(title);
		mBitmap = bitmap;
		mPlainText = true;
	}
	
	@Override
	protected byte getMessageId() {
		return Constants.RESPONSE_MENU_GET_ITEM;
	}
	
	@Override
	protected int getPayloadSize() {
		
		return Constants.SIZE_BYTE + // AlertItem (0) or OtherItem(1) 
			Constants.SIZE_SHORT + // unknown 
			Constants.SIZE_SHORT + // unread count 
			Constants.SIZE_SHORT + // unknown 
			Constants.SIZE_BYTE + // MenuItemId 
			Constants.SIZE_BYTE + // Plaintext (0) or Bitmap (1) 
		  	Constants.SIZE_INT + // unknown 
			Constants.SIZE_SHORT + // Text length 
			mTitle.length + 
			mBitmap.length;
	}

	@Override
	protected void WritePayload(DataOutputStream writer) throws IOException {
		writer.writeByte(mAlertItem ? 0 : 1);
		writer.writeShort(0);
		writer.writeShort(mUnreadCount);
		writer.writeShort(0);
		writer.writeByte(mMenuItemId);
		writer.writeByte(mPlainText ? 0 : 1);
		writer.writeInt(0);
		writer.writeShort(mTitle.length);
		writer.write(mTitle);
		writer.write(mBitmap);
	}

}
