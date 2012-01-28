package com.pedronveloso.openliveview.server;

import java.util.ArrayList;
import java.util.List;

import com.pedronveloso.openliveview.Utils.StaticImages;
import com.pedronveloso.openliveview.protocol.GetAllMenuItemsRequest;
import com.pedronveloso.openliveview.protocol.GetMenuIconResponse;
import com.pedronveloso.openliveview.protocol.Response;

public class Menu implements BtServer.Callback {

	public static class MenuItem {
		
		private short mId;
		private String mTitle;
		private byte[] mImage;
		
		public MenuItem(short id, String title, byte[] image) {
			mId = id;
			mTitle = title;
			mImage = image;
		}
		
		protected int getUnreadCount() {
			return 0; // Only Alert Items have unread messages
		}
		
		public GetMenuIconResponse toRequest() {
			return new GetMenuIconResponse(this instanceof AlertMenuItem,
					getUnreadCount(), mId, mTitle, mImage);
		}		
	}
	
	public static class AlertMenuItem extends MenuItem {

		private int mUnread;
		
		public AlertMenuItem(short id, String title, byte[] image) {
			super(id, title, image);
		}
		
		public int getUnread() {
			return mUnread;
		}
		
		public void setUnread(int unread) {
			mUnread = unread;
		}

		@Override
		protected int getUnreadCount() {		
			return getUnread();
		}
	}

	private static Menu sInstance;
	
	private List<MenuItem> mItems = new ArrayList<MenuItem>();
	
	private Menu() {
		super();
	}
	
	public static Menu instance() {
		if (sInstance == null) {
			sInstance = new Menu();
			BtServer.instance().addCallback(sInstance);
		}
		return sInstance;
	}
	
	public void initDefaultItems() {
		// ToDo
		for(int i = 0; i < 5; i++) {
			AlertMenuItem ami = new AlertMenuItem((short)i, "Item "+i, StaticImages.staticIconAllEvents);
			ami.setUnread(5-i);
			addMenuItem(ami);
		}
	}
	
	public void addMenuItem(MenuItem item) {
		mItems.add(item);
	}
	
	public void handleResponse(Response aResponse) {
		if (aResponse instanceof GetAllMenuItemsRequest) {
			sendAllMenuItems();
		}
	}

	public void isReadyChanged(boolean isReady) {
		// TODO Auto-generated method stub
	}

	private void sendAllMenuItems() {
		for (MenuItem item : mItems) {
			BtServer.instance().write(item.toRequest());
		}
	}
	
	public int size() {
		return mItems.size();
	}
}
