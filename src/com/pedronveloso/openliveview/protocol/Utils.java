package com.pedronveloso.openliveview.protocol;

import java.io.UnsupportedEncodingException;

import android.graphics.Color;

public class Utils {

	public static String getString(byte[] bytes) {
		try {
			return new String(bytes, C.STRING_ENCODING);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
			return "";
		}
	}
	
	public static byte[] prepareString(String string) {
		try {
			return string.getBytes(C.STRING_ENCODING);
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
			return new byte[0];
		}
	}
	
	public static short colorToRGB565(int color) {
		int r = Color.red(color) >> 3;
		int g = Color.green(color) >> 2;
		int b = Color.blue(color) >> 3;
		return (short)((r<<(5+6)) + (g << 5) + (b));
	}
}
