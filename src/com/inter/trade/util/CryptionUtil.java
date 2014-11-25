package com.inter.trade.util;

import android.content.Context;

public class CryptionUtil {
	public static native byte[] encrypt(byte[] data, int key, Context context);

	public static native byte[] decrypt(byte[] buf, int key, Context context);

	static {
		System.loadLibrary("EasyPayCryption");
	}
}
