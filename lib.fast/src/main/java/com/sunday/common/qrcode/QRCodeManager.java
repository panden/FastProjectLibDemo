package com.sunday.common.qrcode;

public class QRCodeManager {

	static {
		System.loadLibrary("qrcode");
	}

	public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);

}
