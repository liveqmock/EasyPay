package com.inter.trade.util;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DynamicEncryptUtil {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static byte[] encrypt(byte[] iv, byte[] data,int sortNum) throws Exception {
		byte[] pass = findkeyBySortNum(sortNum);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec dps = new IvParameterSpec(iv);
		SecretKeySpec skeySpec = new SecretKeySpec(pass, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, dps);
		byte[] buf = cipher.doFinal(data);
		return buf;
	}

	private static byte[] decrypt(byte[] iv, byte[] data,int sortNum) throws Exception {
		byte[] pass = findkeyBySortNum(sortNum);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec dps = new IvParameterSpec(iv);
		SecretKeySpec skeySpec = new SecretKeySpec(pass, "AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, dps);
		byte[] buf = cipher.doFinal(data);
		return buf;
	}

	/**
	 * @Description: 获取随机字节
	 * @param
	 * @return byte[]
	 */
	private static byte[] getRamdomBytes(int length) {
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = (byte) (Math.round(Math.round(Math.random() * 255)) - 128);
		}
		return result;
	}
	private static final byte[] PASSKEY0 =new byte[]{0x6D, 0x6E, 0x66, 0x61, 0x6E, 0x66, 0x6F, 0x73, 0x61, 0x6A, 0x66, 0x6F, 0x69, 0x73, 0x6A, 0x76 };  
	private static final byte[] PASSKEY1 =new byte[]{0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75, 0x72, 0x6C, 0x75 };  
	private static final byte[] PASSKEY2 =new byte[]{0x73, 0x65, 0x72, 0x72, 0x66, 0x73, 0x63, 0x66, 0x73, 0x66, 0x73, 0x61, 0x66, 0x61, 0x73, 0x66 };  
	private static final byte[] PASSKEY3 =new byte[]{0x72, 0x65, 0x66, 0x64, 0x63, 0x72, 0x73, 0x64, 0x66, 0x65, 0x61, 0x74, 0x76, 0x65, 0x72, 0x65 };  
	private static final byte[] PASSKEY4 =new byte[]{0x65, 0x76, 0x62, 0x68, 0x79, 0x73, 0x61, 0x63, 0x68, 0x72, 0x79, 0x62, 0x73, 0x61, 0x78, 0x7A };  
	private static final byte[] PASSKEY5 =new byte[]{0x65, 0x63, 0x72, 0x61, 0x73, 0x72, 0x66, 0x64, 0x73, 0x66, 0x68, 0x76, 0x61, 0x78, 0x6F, 0x65 };  
	private static final byte[] PASSKEY6 =new byte[]{0x6B, 0x6F, 0x6E, 0x6C, 0x6A, 0x69, 0x6A, 0x6E, 0x6F, 0x6A, 0x69, 0x6F, 0x6A, 0x64, 0x61, 0x73 };  
	private static final byte[] PASSKEY7 =new byte[]{0x61, 0x72, 0x65, 0x63, 0x75, 0x6B, 0x6E, 0x73, 0x72, 0x65, 0x72, 0x65, 0x72, 0x64, 0x7A, 0x6C };  
	private static final byte[] PASSKEY8 =new byte[]{0x6C, 0x6D, 0x69, 0x6A, 0x6D, 0x6E, 0x73, 0x66, 0x73, 0x66, 0x64, 0x66, 0x61, 0x6E, 0x69, 0x6F };  
	private static final byte[] PASSKEY9 =new byte[]{0x6E, 0x6C, 0x6A, 0x69, 0x6F, 0x6A, 0x61, 0x69, 0x66, 0x6F, 0x73, 0x69, 0x61, 0x6A, 0x71, 0x6D }; 
	
	private static byte[] findkeyBySortNum(int sortNum){
		byte[] randKey=null;
		switch (sortNum) {
		case 0:
			randKey=PASSKEY0;
			break;
		case 1:
			randKey=PASSKEY1;
			break;
		case 2:
			randKey=PASSKEY2;
			break;
		case 3:
			randKey=PASSKEY3;
			break;
		case 4:
			randKey=PASSKEY4;
			break;
		case 5:
			randKey=PASSKEY5;
			break;
		case 6:
			randKey=PASSKEY6;
			break;
		case 7:
			randKey=PASSKEY7;
			break;
		case 8:
			randKey=PASSKEY8;
			break;
		case 9:
			randKey=PASSKEY9;
			break;
		default:
			randKey= PASSKEY9;
			break;
	}
		return randKey;
		
	}
	
	 public static int createRandomkeySort(){
		   int randomNum=(int)(Math.random()*10);
		   return randomNum;
	   }
	 /**
	  * 加密
	  * @param data
	  * @return
	  */
	 public static byte[] encrypt(byte[] data) {
		 int sortNum=createRandomkeySort();
		 try {
				byte[] iv = getRamdomBytes(16);
				byte[] buf = encrypt(iv, data,sortNum);
				byte[] result = new byte[iv.length + buf.length+1];
				result[0]=(byte)sortNum;
				System.arraycopy(iv, 0, result, 1, iv.length);
				System.arraycopy(buf, 0, result, iv.length+1, buf.length);
				return result;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	 /**
	  * 解密
	  * @param srcData
	  * @return
	  */
		public static String decrypt(byte[] srcData) {
			 int sortNum=srcData[0];
			 byte[] data=new byte[srcData.length-1];
		     System.arraycopy(srcData, 1, data, 0, srcData.length-1);
			try {
				byte[] iv = new byte[16];
				System.arraycopy(data, 0, iv, 0, 16);
				byte[] src = new byte[data.length - iv.length];
				System.arraycopy(data, 16, src, 0, data.length - iv.length);
				byte[] tmp = decrypt(iv, src,sortNum);
				return new String(tmp, "utf-8");
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
		
	public static void main(String[] args) throws Exception {
		String s = "123";
		System.out.println("未加密"+s);
		System.out.println("加密"+encrypt(s.getBytes()));
		byte[] data = encrypt(s.getBytes());
		System.out.println("解密"+decrypt(data));
	}
}
