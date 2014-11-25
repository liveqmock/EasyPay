package com.gdseed.mobilereader.swap;

import java.text.DecimalFormat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.gdseed.mobilereader.MobileReader;
import com.gdseed.mobilereader.MobileReader.ReaderStatus;
import com.inter.trade.R;

public class MobileReaderMenu extends Activity {

	// -----------------------------------------------------------------------
	// Constants
	// -----------------------------------------------------------------------
	private static final String Algorithm = "DESede/ECB/NOPADDING";
	private final static String INTENT_ACTION_CALL_STATE = "com.gdseed.mobilereader.CALL_STATE";
	private final static String modelMS62x = "MS62x";
	private final static String modelMS22x = "MS22x";
	private String curModel = new String("MS62x");
	private TextView outMsg;
	private Button decodeButton;
	private Button sleepButton;
	private Button weakUpButton;
	private Spinner spinnerModel;
	private MobileReader mobileReader;
	private IncomingCallServiceReceiver incomingCallServiceReceiver;
	private String mCmd = null;
	private String phoneModel = new String();
	private String phoneSysCode = new String();
	private String phoneCountry = new String();
	private String phoneManufacturer = new String();
	private boolean playSounds = true;
	private byte[] cmd = new byte[2];
	private int fileCount = 0;
	private String state = null;
	private String getCurrentFileName() {
		Time t = new Time();
		t.setToNow();
		int year = t.year % 100;
		int month = t.month + 1;
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
	
	private static void log(String str) {
		Log.d("Zhangjd", str + '\n');
	}

	// -----------------------------------------------------------------------
	// Interface
	// -----------------------------------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobilereader_menu);
		MobileReader.setDebugOn("Zhangjd", true);
		mobileReader = new MobileReader(MobileReaderMenu.this);
		mobileReader.setOnDataListener(new MobileReader.CallInterface() {
			@Override
			public void call(ReaderStatus state) {
				Intent intent = new Intent(INTENT_ACTION_CALL_STATE);
				int tmp = state.ordinal();
				intent.putExtra("ReaderStatus", tmp);
				MobileReaderMenu.this.sendBroadcast(intent);
			}
		});

		setTitle("Mobile Reader Demo:1.23" + " Lib:" + mobileReader.getVersion());
		initViews();
		startCallStateService();
		//mobileReader.setTimerout(5 * 1000); // ����5s��ʱ
	}

	@Override 
	public void onResume() {
		startCallStateService();
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		endCallStateService();
		mobileReader.close();
		super.onDestroy();
	}
	
	@Override
	public void onPause() {
		mobileReader.close();
		endCallStateService();
		resetUIControls();
		super.onPause();
	}

	@Override
	public void onStop() {
		mobileReader.close();
		endCallStateService();
		resetUIControls();
		super.onStop();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mobileReader.close();
			endCallStateService();
			resetUIControls();
			mobileReader.setOnDataListener(null);
		}
		
		return super.onKeyDown(keyCode, event);
	}

	private void startCallStateService() {
		startService(new Intent(INTENT_ACTION_CALL_STATE));
		if (incomingCallServiceReceiver == null) {
			incomingCallServiceReceiver = new IncomingCallServiceReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(INTENT_ACTION_CALL_STATE);
			this.registerReceiver(incomingCallServiceReceiver, intentFilter);
		}
	}

	private void endCallStateService() {
		stopService(new Intent(INTENT_ACTION_CALL_STATE));
		if (incomingCallServiceReceiver != null) {
			this.unregisterReceiver(incomingCallServiceReceiver);
			incomingCallServiceReceiver = null;
		}
	}

	public void resetUIControls() {
		outMsg.setText("");
		decodeButton.setEnabled(true);
		decodeButton.setText("START");
	}

	// -----------------------------------------------------------------------
	// Private classes
	// -----------------------------------------------------------------------

	private void initViews() {
		phoneSysCode = Build.VERSION.RELEASE;
		phoneModel = Build.MODEL;
		phoneCountry = getResources().getConfiguration().locale.getCountry();
		phoneManufacturer = Build.MANUFACTURER;
		
		if("unknown".equals(phoneManufacturer)) {
			phoneManufacturer = Build.PRODUCT;
		}
		((TextView) findViewById(R.id.system_version)).setText(phoneSysCode);
		((TextView)findViewById(R.id.cellphone_version)).setText(phoneManufacturer + " " + phoneModel);

		outMsg = (TextView) findViewById(R.id.displayMsg);
		decodeButton = (Button) findViewById(R.id.decodeButton);
		sleepButton = (Button) findViewById(R.id.sleep);
		weakUpButton = (Button) findViewById(R.id.wakeUp);
		spinnerModel = (Spinner) findViewById(R.id.spinnerModel);
		ArrayAdapter< String> adapter = new ArrayAdapter< String>(this,android.R.layout.simple_spinner_item);  
		adapter.add(modelMS62x);
		adapter.add(modelMS22x);
		playSounds = false;
		spinnerModel.setAdapter(adapter);
		spinnerModel.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				log("$$$$$$$$$$$$$$" + parent.getItemAtPosition(position).toString());
				curModel = parent.getItemAtPosition(position).toString();
				if (curModel.equals(modelMS62x)) {
					playSounds = false;
				} else {
					playSounds = true;
				}
				
				outMsg.setText("");
				mobileReader.close();
				decodeButton.setText("START");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			 
		decodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (decodeButton.getText() == "START") {
					log(mobileReader.open(playSounds)+"");
					if (mobileReader.open(playSounds)) {
						resetUIControls();
						decodeButton.setText("STOP");
						outMsg.setTextColor(Color.WHITE);
						outMsg.setText("Swipe card please ...");
					}
				} else {
					mobileReader.close();
					resetUIControls();
					decodeButton.setText("START");
				}
			}
		});
		
		sleepButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCmd = "0f";
				sendCmd(mCmd);
			}});
		
		weakUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCmd = "0c";
				sendCmd(mCmd);
				
			}

		});

		resetUIControls();
	}
	
	
	

	// -----------------------------------------------------------------------
	// Inner classes
	// -----------------------------------------------------------------------

	private class IncomingCallServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			byte rawData[] = new byte[1024];
			int trackCount[] = new int[1];
			trackCount[0] = 0;
			int defaultValue = ReaderStatus.DEVICE_PLUGOUT.ordinal();
			ReaderStatus state = ReaderStatus.values()[intent.getIntExtra(
					"ReaderStatus", defaultValue)];
			if (ReaderStatus.BEGIN_RECEIVE == state) {
				outMsg.setTextColor(Color.WHITE);
				outMsg.setText("Receiving ...");
			} else if (ReaderStatus.TIMER_OUT == state) {
				mobileReader.close();
				resetUIControls();
				outMsg.setTextColor(Color.WHITE);
				outMsg.setText("Timer out ...");
				return;
			} else if (ReaderStatus.END_RECEIVE == state) {
				outMsg.setTextColor(Color.WHITE);
				outMsg.setText("Decoding ...");
			} else if (ReaderStatus.DEVICE_PLUGIN == state) {
				boolean back = true;
				if (back) return;
				mobileReader.close();
				resetUIControls();
				if (mobileReader.open(playSounds)) {
					outMsg.setTextColor(Color.WHITE);
					decodeButton.setText("STOP");
					outMsg.setText("Swipe card please ...");
				}
				
				return;
			} else if (ReaderStatus.DEVICE_PLUGOUT == state) {
				mobileReader.close();
				resetUIControls();
			} else if (ReaderStatus.DECODE_OK == state) {
				int len = mobileReader.read(rawData);
				if (len < 1) {
					outMsg.setTextColor(Color.RED);
					outMsg.setText("Reader Error.");
					if (len < 1) {
						mobileReader.writeRecoderToFile(getCurrentFileName() + "_null.raw");
					} else {
						mobileReader.writeRecoderToFile(getCurrentFileName() + "_crc.raw");
					}
				} else {
					String display = new String();
					
					if (0x07 == rawData[0] || 0x50 == rawData[0]
							|| 0x48 == rawData[0] || 0x08 == rawData[0]
							|| 0x49 == rawData[0]) {
						outMsg.setTextColor(Color.RED);
						display = "Please update the lastest hardware!";
					} else if (0x60 == rawData[0]) {
						display = Lib12_ParseTrack(rawData, trackCount);
						if (trackCount[0] < 2) {
							outMsg.setTextColor(Color.RED);
						} else {
							outMsg.setTextColor(Color.WHITE);
						}
					} else if (0x77 == rawData[0]) {
						display = "Error Code:" + String.format("%02x", (int)(rawData[1]&0xff));
						/*switch (rawData[1]) {
						case 0x01: 
							display = "EepromWriteError";
							break;
						case 0x02: 
							display = "MemoryOverflow";
							break;
						case 0x03: 
							display = "EepromLack";
							break;
						case 0x04: 
							display = "KeyIsExist";
							break;
						case 0x05: 
							display = "ERROR";
							break;
						case 0x06: 
							display = "TrackDecodeError";
							break;
						case 0x07: 
							display = "KeyReadError";
							break;
						case 0x08: 
							display = "KeyCanntEncrypted";
							break;
						case 0x09: 
							display = "DisperseCanntSupport";
							break;
						case 0x0a: 
							display = "DukptCountFlow";
							break;
						case 0x0b: 
							display = "CmdReceiveError";
							break;
						case 0x0c: 
							display = "CmdUnknown";
							break;
						case 0x0d: 
							display = "RandomIsNULL";
							break;
						case 0x0e: 
							display = "CmdUnimplemented";
							break;
						case 0x0f: 
							display = "CmdFormatError";
							break;
						case 0x10: 
							display = "ErrorTest";
							break;
						case 0x11: 
							display = "DeveceIsUnLocked";
							break;
						case 0x12: 
							display = "TrackPwdError";
							break;
						case 0x13: 
							display = "SuperPwdError";
							break;
						case 0x14: 
							display = "FlagCanntSptHostMode";
							break;
						case 0x15: 
							display = "DeviceIsLocked";
							break;
						default:
							display = "unknown Error";
						} */
					}  else if (0x90 == (rawData[0] & 0xff)) {
						if (mCmd.equals("0c")) {
							display = "Device is woke up";
						} else {
							display = "Device is slept";
						}
					} else {
						outMsg.setTextColor(Color.RED);
						display = "Unknown Format !!";
						mobileReader.writeRecoderToFile(getCurrentFileName() + "_unknown.raw");
					}
					if (0 == display.length()) {
						outMsg.setTextColor(Color.RED);
						outMsg.setText("Parse Data Error");
					} else {
						outMsg.setText(display);
						mobileReader.writeRecoderToFile("ok.raw");
					}
				}

				mobileReader.close();
				decodeButton.setText("START");
				if (mobileReader.open(playSounds)) {
					decodeButton.setText("STOP");
				}
			} else {
				String errorInfo = new String();
				outMsg.setTextColor(Color.RED);
				if (ReaderStatus.DECODE_ERROR == state) {
					errorInfo = "Decode Error!";
				} else if (ReaderStatus.RECEIVE_ERROR == state) {
					errorInfo = "Receive Error!";
				} else if (ReaderStatus.BUF_OVERFLOW == state) {
					errorInfo = "Buf Overflow !";
				}

				mobileReader.writeRecoderToFile(getCurrentFileName() + "_error.raw");
				outMsg.setText(errorInfo);
				mobileReader.close();
				decodeButton.setText("START");
			}
		} // end-of onReceive
	}

	private String Lib12_ParseTrack(byte input[], int track[]) {
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
		
//		if (true) {
//			return ret;
//		}

		byte[] tmp_key = {0x01, 0x23, 0x45, 0x67, (byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef, (byte)0xfe, (byte)0xdc, (byte)0xba, (byte)0x98, 0x76, 0x54, 0x32, 0x10};
		for(int i = 0; i < byEncrypedData.length; i++) {
			if (0 != i && 0 == (i % 16)) {
				System.out.format("\n");
			}
			
			System.out.format("%02x ", byEncrypedData[i]);
		}
		byte[] srcBytes = decryptMode(tmp_key, byEncrypedData);
//		byte[] srcBytes = decryptMode(key.getBytes(), byEncrypedData);
		try {
			Log.d("decrypt", new  String(srcBytes));
			
		} catch (Exception e) {
			// TODO: handle exception
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
		
		//
		ret+="decrypedData\n";
		ret += decrypedData;
		ret += "\nPan:" + realPan;
		return ret;
	}

	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
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
	
	private int StringToHex(String str, byte[] dst) {
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
	
	private int GetHexChar(char c) {
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
	
	protected void sendCmd(String mCmd) {
		byte[] tmp = new byte[128];
		Log.d("IO", "Send:" + mCmd);
		if (null == mCmd || mCmd.length() < 1)
			return;
		int len = StringToHex(mCmd, tmp);
		sendCmd(tmp, len);
	}
	
	protected void sendCmd(byte[] cmd, int len) {
		if (null == cmd) return;
		if (len < 1) return;
		if (!mobileReader.deviceIsAvailable()) return;
		mobileReader.open(true);
		mobileReader.write(cmd, len);
	}
	
}
