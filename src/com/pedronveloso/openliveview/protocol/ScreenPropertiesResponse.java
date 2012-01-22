package com.pedronveloso.openliveview.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class ScreenPropertiesResponse extends Response {

	private int mWidth;
	private int mHeight;
	private int mStatusBarWidth;
	private int mStatusBarHeight;
	private int mViewWidth;
	private int mViewHeight;
	private int mAnnounceWidth;
	private int mAnnounceHeight;
	private int mTextChunkSize;
	private int mIdleTimer;
	private int mStopByte;
	private String mProtocolVersion;
	
	public int getWidth() {
		return mWidth;
	}
	
	public int getHeight() {
		return mHeight;
	}
	
	public int getStatusBarWidth() {
		return mStatusBarWidth;
	}
	
	public int getStatusBarHeight() {
		return mStatusBarHeight;
	}
	
	public int getViewWidth() {
		return mViewWidth;
	}
	
	public int getViewHeight() {
		return mViewHeight;
	}
	
	public int getAnnounceWidth() {
		return mAnnounceWidth;
	}
	
	public int getAnnounceHeight() {
		return mAnnounceHeight;
	}
	
	public int getTextChunkSize() {
		return mTextChunkSize;
	}
	
	public int getIdleTimer() {
		return mIdleTimer;
	}
	
	public int getStopByte() {
		return mStopByte;
	}
	
	public String getProtocolVersion() {
		return mProtocolVersion;
	}
	
	@Override
	protected void readPayload(DataInputStream input, int payloadLength)
			throws IOException {
		mWidth = input.readUnsignedByte();
		mHeight = input.readUnsignedByte();
		mStatusBarWidth = input.readUnsignedByte();
		mStatusBarHeight = input.readUnsignedByte();
		mViewWidth = input.readUnsignedByte();
		mViewHeight = input.readUnsignedByte();
		mAnnounceWidth = input.readUnsignedByte();
		mAnnounceHeight = input.readUnsignedByte();
		mTextChunkSize = input.readUnsignedByte();
		mIdleTimer = input.readUnsignedByte();
		mStopByte = input.readUnsignedByte();
		byte[] buffer = new byte[5];
		input.read(buffer);
		mProtocolVersion = Utils.getString(buffer);
	}

}
