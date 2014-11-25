package com.inter.trade.util;

import com.inter.trade.ui.PayApp;

public class EncryptUtil {


	/**
	 * 加密
	 * 
	 * @param input
	 * @param key
	 * @return
	 */
	public static String encrypt(String input, int index) {
		byte[] crypted = null;
		try {
//			SecretKeySpec skey = new SecretKeySpec(key, "AES");
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, skey);
//			crypted = cipher.doFinal(input.getBytes());
			crypted = CryptionUtil.encrypt(input.getBytes(), index, PayApp.pay);
		} catch (Exception e) {
		}
		return new String(Base64.encode(crypted));
	}

	/**
	 * 解密
	 * 
	 * @param input
	 * @param key
	 * @return
	 */
	public static String decrypt(String input, int index) {
		byte[] output = null;
		try {
//			SecretKeySpec skey = new SecretKeySpec(key, "AES");
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			cipher.init(Cipher.DECRYPT_MODE, skey);
//			output = cipher.doFinal(Base64.decode(input));
			output = CryptionUtil.decrypt(Base64.decode(input), index, PayApp.pay);
			return new String(output, "UTF-8");
		} catch (Exception e) {

		}
		return "";
	}


	public static int createRandomkeySort() {
		int randomNum = (int) (Math.random() * 10);
		return randomNum;
	}
}
