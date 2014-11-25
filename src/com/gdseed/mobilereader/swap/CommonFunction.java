package com.gdseed.mobilereader.swap;

import java.text.DecimalFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.inter.trade.data.CardData;
import com.inter.trade.ui.PayApp;

import android.os.Build;
import android.text.format.Time;
import android.util.Log;

/**
 * 刷卡器解密类
 * @author apple
 *
 */
public class CommonFunction {

	private static int fileCount = 0;
	private static String phoneSysCode = Build.VERSION.RELEASE;
	private static String phoneModel = Build.MODEL;
	private static String phoneManufacturer = Build.MANUFACTURER;
	private final static String modelMS62x = "MS62x";
	private final static String modelMS22x = "MS22x";
	
	public static String passPackageToString(byte input[], int track[],
			String curModel) {

		int panLength = 0;
		int index = 0;
		String version = new String();
		String encryptMode = new String();
		String first6Pan = new String();
		String last4Pan = new String();
		String expiryDate = new String();
		String userName = new String();
		String ksn = new String();
		String encrypedData = new String();
		String ret = new String();
		String xxx = new String();
		String trackInfo = new String();
		byte byEncrypedData[];
		String decrypedData = new String();
		int tmp = 0;

		index = 1;
		version += (char) ('0' + input[index]);
		version += '.';
		//version += (char) ('0' + input[++index]);
		version += String.format("%02d", input[++index]);

		tmp = input[++index];
		if (curModel.equals(modelMS22x)) {
			if (1 == tmp) {
				encryptMode = "fixed key";
			} else if (2 == tmp){
				encryptMode = "dukpt";
			} else {
				encryptMode = "unknown";
			}
			
		} else if (curModel.equals(modelMS62x)){
			if (0 == tmp) {
				encryptMode = "fixed key";
			} else if (1 == tmp) {
				encryptMode = "diperse I";
			} else if (0xFE == (int)(tmp&0xff)){
				encryptMode = "dukpt";
			} else {
				encryptMode = "unknown";
			}
		}

		panLength = input[++index];
		// xxx
		for (int i = 0; i < panLength - 10; i++) {
			xxx += "x";
		}

		// fist 6 pan
		index++;
		for (int i = 0; i < 6; i++) {
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			first6Pan += (char) tmp;
		}
		
		if (panLength < 6) {
			first6Pan = first6Pan.substring(0, panLength);
		}
		index += 3;
		
		// last 4 pan or under
		for (int i = 0; i < 4; i++) {
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			last4Pan += (char) tmp;
		}
		
		if (panLength < 11) {
			xxx = "";
			if (panLength < 7) {
				last4Pan = "";
			} else {
				last4Pan = last4Pan.substring(10 - panLength, 4);
			}
		}
		index += 2;
		
		
		// expiry data
		for (int i = 0; i < 4; i++) {
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			expiryDate += (char) tmp;
		}

		// User name
		index += 2;
		for (int i = 0; i < 26; i++) {
			userName += (char) input[i + index];
		}

		// ksn
		index += 26;
		for (int i = 0; i < 20; i++) {
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >>> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			ksn += (char) tmp;
		}
		PayApp.mKeyCode = ksn;

		// encrypted data
		index += 10;
		for (int i = 0; i < 160; i++) {
			if (0 != i && 0 == (i % 32))
				encrypedData += '\n';
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'a' : tmp + '0';
			encrypedData += (char) tmp;
		}
		
		byEncrypedData = new byte[80];
		for (int i = 0; i < 80; i++) {
			byEncrypedData[i] = input[index + i];
		}

		index += 80;
		byte tmpTrackInfo = input[index];
		int trackCount = 0;
		for (int i = 0; i < 3; i++) {
			byte info = (byte) (tmpTrackInfo & (0x01 << i));
			if (0 != info) {
				trackInfo += (char) ('1' + i);
				trackCount++;
			}
		}
		
		if (null != track) {
			track[0] = trackCount;
		}

		ret = ("Firmware Version:" + version + "\n" + "Encryption Mode:"
				+ encryptMode + "\n" + "Track Info:" + trackInfo + "\n"
				+ "PAN:" + first6Pan + xxx + last4Pan + "\n" + "Expiry Date:"
				+ expiryDate + "\n" + "User Name:" + userName + "\n" + "KSN:"
				+ ksn + "\n" + "Encrypted Data:" + "\n" + encrypedData + "\n");
		
		
		if (false) {
			return ret;
		}

		byte[] tmp_key = {0x01, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef, (byte)0xfe, (byte)0xdc, (byte)0xba, (byte)0x98, 0x76, 0x54, 0x32, 0x10};
		byte[] temp_key = {(byte)0x53, (byte)0xef, (byte)0x0e, (byte)0x3b, (byte)0x07, (byte)0x84, (byte)0xcb, (byte)0x17, (byte)0xac, (byte)0x40, (byte)0xc7, (byte)0xbd, (byte)0xc8, (byte)0x99, (byte)0x21, (byte)0xdf};
		
		for(int i = 0; i < byEncrypedData.length; i++) {
			if (0 != i && 0 == (i % 16)) {
				System.out.format("\n");
			}
			
			System.out.format("%02x ", byEncrypedData[i]);
		}
		byte[] srcBytes = decryptMode(tmp_key, byEncrypedData);
		
		
		if(!checkData(srcBytes, first6Pan, last4Pan, panLength)){
			srcBytes = decryptMode(temp_key, byEncrypedData);
		}
		for(int i = 0; i < srcBytes.length; i++) {
			if (0 != i && 0 == (i % 16)) {
				System.out.format("\n");
			}
			
			System.out.format("%02x ", srcBytes[i]);
		}
		
		for (int i = 0; i < 160; i++) {
			if (0 != i && 0 == (i % 32))
				decrypedData += '\n';
			tmp = srcBytes[(i >> 1)] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'a' : tmp + '0';
			decrypedData += (char) tmp;
		}
		
		// ȡpan
		int panIndex = 1;
		String realPan = new String();
		for(int i = 0; i < panLength; i++) {
			tmp = srcBytes[(i + panIndex) >> 1];
			if (0 != (i % 2)) {
				tmp >>= 4;
			}
			
			tmp &= 0x0f;
			realPan += tmp;
		}
		
		CardData data = new CardData();
		ret = ("Firmware Version:" + version + "\n" + "Encryption Mode:"
				+ encryptMode + "\n" + "Track Info:" + trackInfo + "\n"
				+ "PAN:" + first6Pan + xxx + last4Pan + "\n" + "Expiry Date:"
				+ expiryDate + "\n" + "User Name:" + userName + "\n" + "KSN:"
				+ ksn + "\n" + "Encrypted Data:" + "\n" + encrypedData + "\n");
		data.firmwareVersion = version;
		data.encryptionMode = encryptMode;
		data.trackInfo = trackInfo;
		data.first6Pan = first6Pan;
		data.xxx = xxx;
		data.last4Pan = last4Pan;
		data.expiryDate = expiryDate;
		data.userName = userName;
		data.ksn = ksn;
		data.encrypedData = encrypedData;
		
		
		data.pan = realPan;
		data.decrypedData = decrypedData;
		
		/**
		 * ****************************获取磁道信息--start--****************************
		 */
		String strTrack1 = new String();
		String strTrack23 = new String();
		byte[] track1 = decodeTrack1(srcBytes, 20, 80);
		
		for (int i = 0; i < track1.length;  i++) {
			track1[i] += 0x20;
			strTrack1 += (char)track1[i];
		}
		data.oneTrack1InfoSRC = strTrack1;//第一磁道信息(刷卡后获取的原始数据)
		
		byte[] track23 = decodeTrack23(srcBytes, 0, 20);
		for (int i = 0; i < track23.length;  i++) {
			track23[i] += 0x30;
			strTrack23 += (char)track23[i];
			
		}
		data.twoTrack23InfoSRC = strTrack23;//第二磁道信息(刷卡后获取的原始数据)
		data.twoTrack23Info = formatTwoTrack23Info(strTrack23);//第二磁道信息(格式化后的信息)
		/**
		 * ****************************获取磁道信息--end--****************************
		 */
		
		PayApp.merReserved.append("{");
		PayApp.merReserved.append("Firmware Version:" + data.firmwareVersion);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("Encryption Mode:" + data.encryptionMode);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("Track Info:" + data.trackInfo);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("encryptionMode:" + data.encryptionMode);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("xxx:" + data.xxx);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("last4Pan:" + data.last4Pan);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("Expiry Date:" + data.expiryDate);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("User Name:" + data.userName);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("KSN:" + data.ksn);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("Encrypted Data:" + data.encrypedData);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("PAN:" + data.pan);
		PayApp.merReserved.append("|");
		PayApp.merReserved.append("decrypedData" + data.decrypedData);
		PayApp.merReserved.append("}");
		// PromptUtil.showToast(getActivity(), realPan);
		if (PayApp.mSwipListener != null) {
			PayApp.mSwipListener.recieveCard(data);
			PayApp.mSwipListener.progress(PayApp.SWIPING_SUCCESS, PayApp.ReaderSUCCESS);
		}
		return ret;
	
	}
	
	/**
	 * 格式化二磁道信息
	 * @param twoTrack23Info
	 * @return
	 */
	private static String formatTwoTrack23Info (String twoTrack23InfoSRC) {
		if(twoTrack23InfoSRC == null || twoTrack23InfoSRC.length() <= 0) return null;
		
		String twoTrack23Info = twoTrack23InfoSRC.substring(1, twoTrack23InfoSRC.indexOf("?"));
		
		return twoTrack23Info;
	}
	
	/**
	 * 解码第一磁道信息
	 * @param buffer
	 * @param startOffset
	 * @param endOffset
	 * @return
	 */
	private static byte[] decodeTrack1(byte[] buffer, int startOffset, int endOffset) {
		byte[] tmpBuffer = new byte[(endOffset - startOffset) * 2];
		int bit_count = 0;
	    int byte_count = 0;
	    byte cur_byte = 0;
	    for(int i = startOffset; i < endOffset; i++) {
	        byte tmp = buffer[i];
	        for(int j = 0; j < 8; j++) {
	            if (0x00 != (tmp & 0x80)) {
	                cur_byte |= (0x01 << (5 - bit_count));
	            }
	            
	            bit_count++;
	            if (6 == bit_count) {
	            	tmpBuffer[byte_count] = cur_byte;
	                byte_count++;
	                bit_count = 0;
	                cur_byte = 0;
	            }
	            
	            tmp <<= 1;
	        }
	    }
		
		return tmpBuffer;
	}
	
	/**
	 * 解码第二磁道信息
	 * @param buffer
	 * @param startOffset
	 * @param endOffset
	 * @return
	 */
	private static byte[] decodeTrack23(byte[] buffer, int startOffset, int endOffset) {
		byte[] tmpBuffer = new byte[(endOffset - startOffset + 1) * 2];
		int byte_count = 0;
	    for(int i = startOffset; i < endOffset; i++) {
	    	tmpBuffer[byte_count++] = (byte) ((buffer[i] & 0xff) >> 4);
	        tmpBuffer[byte_count++] = (byte) (buffer[i] & 0x0f);
	    }
	    
	    return tmpBuffer;
	}
	
	static String getCurrentFileName() {
		Time t = new Time();
		t.setToNow(); // ȡ��ϵͳʱ�䡣
		int year = t.year % 100;
		int month = t.month + 1;//�·�0��11
		int day = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		String date = new String();
		//String.format("%02d-%02d-%02d_%02d:%02d:%02d", year, month, day, hour, minute, second);
		date = String.format("%02d%02d%02d_%02d%02d%02d", year, month, day, hour, minute, second);
		fileCount = (fileCount + 1) % 20;
		DecimalFormat df=new DecimalFormat("00");
	    String strFileCount=df.format(fileCount);
	    System.out.println(strFileCount);
		return phoneManufacturer + phoneModel + "_" + phoneSysCode + "_" + strFileCount ;
	}
	
	static byte[] decryptMode(byte[] keybyte, byte[] src) {
		final String Algorithm = "DESede/ECB/NOPADDING";
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			Log.e("aa", e1.getMessage());
		} catch (javax.crypto.NoSuchPaddingException e2) {
			Log.e("aa", e2.getMessage());
		} catch (java.lang.Exception e3) {
			Log.e("aa", e3.getMessage());
		}
		
		return null;
	}
	
    static boolean checkData(byte[] text, String first6Pan, String last4Pan, int panLength) {
    	
		// check track 2 header: ';'
		if (0xb0 != (text[0] & 0xf0)) {
			return false;
		}
		
		// ȡpan
		String realPan = new String();
		int panIndex = 1;
		byte tmp = 0;
		for(int i = 0; i < panLength; i++) {
			tmp = text[(i + panIndex) >> 1];
			if (0 != (i % 2)) {
				tmp >>= 4;
			}
			
			tmp &= 0x0f;
			realPan += tmp;
		}
		
		if (!realPan.substring(0, 6).equals(first6Pan)){
			return false;
		}
		
		if (!realPan.substring(panLength - 4, panLength).equals(last4Pan)) {
			return false;
		}
		
		return true;
	}
	
	static int StringToHex(String str, byte[] dst) {
		int count = 0;
		int tmp = 0;
		for(int i = 0; i < str.length(); i++) {
			tmp = GetHexChar(str.charAt(i));
			if (tmp < 0) continue;
			if (count < dst.length * 2) {
				if (0 == (count % 2)) {
					dst[count >> 1] = (byte)(tmp * 16);
				} else {
					dst[count >> 1] += (byte)tmp;
				}
				
				count++;
			} else {
				return -1;
			}
		}
		
		return count >> 1;
	
	}
	
	static int GetHexChar(char c) {
		switch(c) {
		case '0':case '1':case '2':case '3':case '4':
		case '5':case '6':case '7':case '8':case '9':
			return c - '0';
		case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':
			return c - 'a' + 10;
		case 'A':case 'B':case 'C':case 'D':case 'E':case 'F':
			return c - 'A' + 10;
		default:
			return -1;
		}
	}

}
