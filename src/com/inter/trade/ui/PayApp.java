package com.inter.trade.ui;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

import com.gdseed.mobilereader.MobileReader;
import com.gdseed.mobilereader.MobileReader.ReaderStatus;
import com.gdseed.mobilereader.swap.CommonFunction;
import com.inter.trade.SwipKeyTask;
import com.inter.trade.data.CardData;
import com.inter.trade.data.KeyData;
import com.inter.trade.data.ResultData;
import com.inter.trade.db.DaoMaster.DevOpenHelper;
import com.inter.trade.log.Logger;
import com.inter.trade.net.NetworkUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.EncryptUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.volley.RequestQueue;
import com.inter.trade.volley.Response;
import com.inter.trade.volley.VolleyError;
import com.inter.trade.volley.util.HttpClientStack;
import com.inter.trade.volley.util.Volley;

public class PayApp extends Application
{
	public static PayApp pay;
	public static MobileReader mMobileReader;
	public static DevOpenHelper helper;
	private RequestQueue requestQueue;
	private DefaultHttpClient mHttpClient;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		// Log.i("PayApp", "onCreate");
		super.onCreate();
		pay = this;
		initReader();
		startCallStateService();
		// open();
		openReader();
		
	}
	private void login()
	{
		Response.Listener<String> listener = new Response.Listener<String>()
		{

			@SuppressLint("InflateParams")
			@Override
			public void onResponse(String response)
			{
				String ecryptString = response.substring(1,response.length());
				byte[] decrypt = ecryptString.getBytes();
				String indexByte=response.substring(0,1);
				String result =EncryptUtil.decrypt(new String(decrypt), 
						Integer.parseInt(indexByte));
				String s= result;
			}
		};
		Response.ErrorListener errorListener = new Response.ErrorListener()
		{

			@Override
			public void onErrorResponse(VolleyError error)
			{
			}

		};
		NetworkUtil.checkUpdate(pay, listener, errorListener);
	}
	@Override
	public void onTerminate()
	{
		Logger.d("PayApp", "onTerminate");
		super.onTerminate();
	}

	public static DevOpenHelper getHelper()
	{
		return helper;
	}

	public static void setHelper(DevOpenHelper helper)
	{
		PayApp.helper = helper;
	}

	private static final String TAG = "BaseFragment";
	private static final String Algorithm = "DESede/ECB/NOPADDING";
	public final static String INTENT_ACTION_CALL_STATE = "com.gdseed.mMobileReader.CALL_STATE";
	private final static String modelMS62x = "MS62x";
	private final static String modelMS22x = "MS22x";
	private static String curModel = new String("MS62x");
	private static IncomingCallServiceReceiver incomingCallServiceReceiver;
	public static String phoneModel = new String();
	private static String phoneSysCode = new String();
	private static String phoneCountry = new String();
	private static String phoneManufacturer = new String();
	private static boolean playSounds = false;
	private static int fileCount = 0;
	public static SwipListener mSwipListener; // 刷卡回掉接口
	private static final int OPEN_READER = 1;
	private static final int WAKEUP_READER = 2;
	private static final int CHECK_READER = 3;
	private static final int DELAY = 3000;
	public static Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			if (msg.what == OPEN_READER)
			{
				isOpen = mMobileReader.open(false);
				log("flag: " + isOpen);

			}
			else if (msg.what == WAKEUP_READER)
			{
				// 发送命令唤醒监听器
				sendCmd("0c");
				initLitterTimer();
			}
			else if (msg.what == CHECK_READER)
			{
				if (isSwipIn && !isOpen)
				{
					PromptUtil.showToast(pay, "刷卡器休眠中，请重新插拔刷卡器");
				}
			}
		}
	};

	public static boolean isSwipIn = false; // 是否插入刷卡器
	public static String mKeyCode = null; // 刷卡器设备号
	public static boolean isOpen = false;// 是否已经打开

	// public static boolean isSwipIn=true;
	// public static String mKeyCode="00000000000000";

	public static StringBuffer merReserved = new StringBuffer();// 银行卡保留域

	public static final int SWIPING_START = 1;
	public static final int SWIPING_FAIL = 2;
	public static final int SWIPING_SUCCESS = 3;
	public static final int CMD_KSN = 4;// 获取到刷卡器设备号

	public static final String INSERTCARD = "请插入刷卡器";
	public static final String DECODING = "校验刷卡器中";
	public static final String ReaderError = "银行卡号写入失败，多次尝试失败请重新插拔刷卡器";
	public static final String ReaderSUCCESS = "银行卡号写入成功";

	private static boolean isSendCmd = false;// true发送命令读取设备号。
	public static final String CMD_KNS = "07";// 读取设备号命令
	private static Timer mTimer;
	private static Timer littleTimer;
	public static KeyData mKeyData = new KeyData();
	public SwipKeyTask mKeyTask;

	/**
	 * 开启定时器，5分钟唤醒一次刷卡器
	 */
	public static void startTimer()
	{
		mTimer = new Timer();
		mTimer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(WAKEUP_READER);
				log("wake up mMobileReader");
			}
		}, 1000, 3 * 60 * 1000);
	}

	public static void initLitterTimer()
	{
		littleTimer = new Timer();
		littleTimer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(CHECK_READER);
				log("check up mMobileReader");
			}
		}, 5 * 1000);
	}

	public static void cancelLittle()
	{
		if (littleTimer != null)
		{
			littleTimer.cancel();
		}
	}

	public static void cancelTimer()
	{
		if (mTimer != null) mTimer.cancel();
	}

	public static void initReader()
	{
		// mMobileReader.setDebugOn("Zhangjd", true);
		// if(null == mMobileReader){
		mMobileReader = new MobileReader(PayApp.pay);
		// }
		// mMobileReader = mMobileReaderCreator.createmMobileReader();
		mMobileReader.setOnDataListener(new MobileReader.CallInterface()
		{
			@Override
			public void call(ReaderStatus state)
			{
				Intent intent = new Intent(INTENT_ACTION_CALL_STATE);
				int tmp = state.ordinal();
				intent.putExtra("ReaderStatus", tmp);
				PayApp.pay.sendBroadcast(intent);
			}
		});
		startCallStateService();
		if (isSwipIn)
		{
			isOpen = mMobileReader.open(playSounds);
			log("open status:" + isOpen);
		}

	}

	/**
	 * 延时打开读卡器
	 */
	public static void openReader()
	{
		mHandler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(OPEN_READER);
			}
		}, DELAY);
	}

	public static boolean openReaderNow()
	{
		if (isSwipIn && !isOpen) { return isOpen = mMobileReader
				.open(playSounds); }
		return false;
	}

	/**
	 * 将请求提交到队列 add by hzx
	 * 
	 * @param req
	 */
	public <T> void addToRequestQueue(com.inter.trade.volley.Request<T> req)
	{
		req.setTag(TAG);
		if (null != mHttpClient)
		{
			setCookie();
		}
		getRequestQueue().add(req);
	}

	public RequestQueue getRequestQueue()
	{
		if (null == requestQueue)
		{
			requestQueue = Volley.newRequestQueue(pay);
		}
		return requestQueue;
	}

	public RequestQueue getRequestQueueCookie()
	{
		if (null == requestQueue)
		{
			mHttpClient = new DefaultHttpClient();
			requestQueue = Volley.newRequestQueue(pay, new HttpClientStack(
					mHttpClient));
		}
		return requestQueue;
	}

	public void setCookie()
	{
		CookieStore cookieStore = mHttpClient.getCookieStore();
		cookieStore.addCookie(new BasicClientCookie2("cookie", "pay"));
	}

	public void cancelPendingRequests(Object tag)
	{
		if (requestQueue != null)
		{
			requestQueue.cancelAll(tag);
		}
	}

	/**
	 * 监听刷卡器返回的数据
	 * 
	 * @author apple
	 * 
	 */
	private static class IncomingCallServiceReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			byte rawData[] = new byte[1024];
			int trackCount[] = new int[1];
			trackCount[0] = 0;
			int defaultValue = ReaderStatus.DEVICE_PLUGOUT.ordinal();
			ReaderStatus state = ReaderStatus.values()[intent.getIntExtra(
					"ReaderStatus", defaultValue)];
			// 正在接受信息
			if (ReaderStatus.BEGIN_RECEIVE == state)
			{

				// outMsg.setTextColor(Color.WHITE);
				// outMsg.setText("Receiving ...");
				// 超时
				// log("超时");
			}
			else if (ReaderStatus.TIMER_OUT == state)
			{
				log("超时");
				// mMobileReader.close();
				// resetUIControls();
				// outMsg.setTextColor(Color.WHITE);
				// outMsg.setText("Timer out ...");
				return;
				// 已经接受到信息，解析中
			}
			else if (ReaderStatus.END_RECEIVE == state)
			{
				// if(!isSendCmd)
				// if(mSwipListener !=null){
				// mSwipListener.progress(SWIPING_START,DECODING);
				// }
				// outMsg.setTextColor(Color.WHITE);
				// outMsg.setText("Decoding ...");
			}
			else

			if (ReaderStatus.DEVICE_PLUGIN == state)
			{
				isSwipIn = true;
				openReaderNow();
				startTimer();
				if (mSwipListener != null)
				{

					mSwipListener.checkedCard(true);

				}
				// sendCmd(CMD_KNS);
				if (mKeyCode == null)
				{
					sendCmdKSN(CMD_KNS);
				}

				// PromptUtil.showToast(getActivity(),
				// getString(R.string.has_checked_swip));
				boolean back = true;
				if (back) return;
				mMobileReader.close();
				if (mMobileReader.open(playSounds))
				{
					// outMsg.setTextColor(Color.WHITE);
					// decodeButton.setText("STOP");
					// outMsg.setText("Swipe card please ...");
				}

				return;
			}
			else if (ReaderStatus.DEVICE_PLUGOUT == state)
			{
				// boolean back = true;
				// if (back) return;w
				// mMobileReader.close();
				isSwipIn = false; // 测试代码，需要去掉
				isOpen = false;
				// mKeyCode = null;
				cancelTimer();
				cancelLittle();
				if (mSwipListener != null)
				{

					Logger.d("BaseFragment", "checkedCard" + false);
					mSwipListener.checkedCard(false);

					// isSwipIn=true;
					// PromptUtil.showToast(getActivity(),
					// getString(R.string.swip_out));
				}
				// resetUIControls();
				// 解析成功
			}
			else if (ReaderStatus.DECODE_OK == state)
			{
				int len = mMobileReader.read(rawData);
				byte ok = (byte) 0x90;
				byte error = (byte) 0x77;
				log("error" + error);
				log("ok" + ok);
				log("rawData[0]" + rawData[0]);
				if (ok == rawData[0] || error == rawData[0])
				{
					isSendCmd = true;
				}
				if (error == rawData[0])
				{
					sendCmd("0c");
				}
				if (len < 1)
				{
					// if (!isSendCmd)
					// if (mSwipListener != null) {
					// mSwipListener.progress(SWIPING_FAIL, ReaderError);
					// }
					// outMsg.setTextColor(Color.RED);
					// outMsg.setText("Reader Error.");
					if (len < 1)
					{
						mMobileReader.writeRecoderToFile(getCurrentFileName()
								+ "_null.raw");
					}
					else
					{
						mMobileReader.writeRecoderToFile(getCurrentFileName()
								+ "_crc.raw");
					}
				}
				else
				{
					String display = new String();

					if (0x07 == rawData[0] || 0x50 == rawData[0]
							|| 0x48 == rawData[0] || 0x08 == rawData[0]
							|| 0x49 == rawData[0])
					{
						// outMsg.setTextColor(Color.RED);
						display = "Please update the lastest hardware!";
						if (!isSendCmd) if (mSwipListener != null)
						{
							mSwipListener.progress(SWIPING_FAIL, display);
						}
					}
					else if (0x60 == rawData[0])
					{
						// display = Lib12_ParseTrack(rawData, trackCount);
						display = CommonFunction.passPackageToString(rawData,
								trackCount, curModel);
						// 轨道数小余2
						if (trackCount[0] < 2)
						{
							// outMsg.setTextColor(Color.RED);
						}
						else
						{
							// outMsg.setTextColor(Color.WHITE);
						}
					}
					else if (0x77 == rawData[0])
					{
						display = "Error Code:"
								+ String.format("%02x",
										(int) (rawData[1] & 0xff));
						switch (rawData[1])
						{
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
						}
						if (!isSendCmd) if (mSwipListener != null)
						{
							mSwipListener.progress(SWIPING_FAIL, ReaderError);
						}
					}
					else if (0x90 == (rawData[0] & 0xff))
					{
						// if (mCmd.equals("0c")) {
						// display = "Device is woke up";
						// } else {
						// display = "Device is slept";
						// }
					}
					else
					{
						// outMsg.setTextColor(Color.RED);
						display = "Unknown Format !!";
						mMobileReader.writeRecoderToFile(getCurrentFileName()
								+ "_unknown.raw");
					}
					if (0 == display.length() && rawData.length == 0)
					{
						// 解析失败
						// outMsg.setTextColor(Color.RED);
						// outMsg.setText("Parse Data Error");
						display = "Parse Data Error";
						if (mSwipListener != null)
						{
							mSwipListener.progress(SWIPING_FAIL, display);
						}
					}
					else
					{
						if (rawData[0] != ok)
						{
							// log("唤醒失败");
						}
						// outMsg.setText(display);
						if (rawData[0] == ok)
						{
							display = HexToString(rawData, len, 16);
							mMobileReader.writeRecoderToFile("ok.raw");
							if (display.length() > 22)
							{
								mKeyCode = display.substring(6, 20);// 设备信息
								isSwipIn = true;
								Logger.d(TAG, "命令设备信息：" + mKeyCode);
								if (mSwipListener != null)
								{
									mSwipListener.progress(CMD_KSN, mKeyCode);
								}
							}
						}
						// else {
						// // PromptUtil.showToast(getActivity(),
						// // "设备信息："+display);
						// }
						//
						// }

					}
				}

				mMobileReader.close();
				isOpen = false;
				// decodeButton.setText("START");
				if (isOpen = mMobileReader.open(playSounds))
				{
					// decodeButton.setText("STOP");
				}
			}
			else
			{
				String errorInfo = new String();
				// outMsg.setTextColor(Color.RED);
				isOpen = false;
				if (ReaderStatus.DECODE_ERROR == state)
				{
					errorInfo = "Decode Error!";
					if (mSwipListener != null)
					{
						mSwipListener.progress(SWIPING_FAIL, ReaderError);
					}
				}
				else if (ReaderStatus.RECEIVE_ERROR == state)
				{
					errorInfo = "Receive Error!";
					if (mSwipListener != null)
					{
						mSwipListener.progress(SWIPING_FAIL, ReaderError);
					}
				}
				else if (ReaderStatus.BUF_OVERFLOW == state)
				{
					errorInfo = "Buf Overflow !";
				}

				mMobileReader.writeRecoderToFile(getCurrentFileName()
						+ "_error.raw");
				// outMsg.setText(errorInfo);
				mMobileReader.close();
				// decodeButton.setText("START");
			}
		} // end-of onReceive
	}

	public void closeReader()
	{
		mMobileReader.close();
		endCallStateService();
		mMobileReader.setOnDataListener(null);
		mMobileReader.releaseRecorderDevice();
	}

	/**
	 * 解析刷卡器数据
	 * 
	 * @param input
	 * @param track
	 * @return
	 */
	private static String Lib12_ParseTrack(byte input[], int track[])
	{
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
		// version += (char) ('0' + input[++index]);
		version += String.format("%02d", input[++index]);

		tmp = input[++index];
		if (curModel.equals(modelMS22x))
		{
			if (1 == tmp)
			{
				encryptMode = "fixed key";
			}
			else if (2 == tmp)
			{
				encryptMode = "dukpt";
			}
			else
			{
				encryptMode = "unknown";
			}

		}
		else if (curModel.equals(modelMS62x))
		{
			if (0 == tmp)
			{
				encryptMode = "fixed key";
			}
			else if (1 == tmp)
			{
				encryptMode = "diperse I";
			}
			else if (0xFE == (int) (tmp & 0xff))
			{
				encryptMode = "dukpt";
			}
			else
			{
				encryptMode = "unknown";
			}
		}

		panLength = input[++index];
		// xxx
		for (int i = 0; i < panLength - 10; i++)
		{
			xxx += "x";
		}

		// fist 6 pan
		index++;
		for (int i = 0; i < 6; i++)
		{
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			first6Pan += (char) tmp;
		}

		if (panLength < 6)
		{
			first6Pan = first6Pan.substring(0, panLength);
		}
		index += 3;

		// last 4 pan or under
		for (int i = 0; i < 4; i++)
		{
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			last4Pan += (char) tmp;
		}

		if (panLength < 11)
		{
			xxx = "";
			if (panLength < 7)
			{
				last4Pan = "";
			}
			else
			{
				last4Pan = last4Pan.substring(10 - panLength, 4);
			}
		}
		index += 2;

		// expiry data
		for (int i = 0; i < 4; i++)
		{
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
			expiryDate += (char) tmp;
		}

		// User name
		index += 2;
		for (int i = 0; i < 26; i++)
		{
			userName += (char) input[i + index];
		}

		// ksn
		index += 26;
		// for (int i = 0; i < 20; i++) {
		// tmp = input[(i >> 1) + index] & 0xff;
		// tmp = i % 2 == 0 ? tmp >>> 4 : tmp & 0x0f;
		// tmp = tmp > 9 ? tmp - 10 + 'A' : tmp + '0';
		// ksn += (char) tmp;
		// }
		for (int i = 0; i < 20; i++)
		{
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >>> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'a' : tmp + '0';
			ksn += (char) tmp;
		}

		// 设备号为刷卡器的key
		mKeyCode = ksn.substring(0, 14);
		Logger.d(TAG, "刷卡设备信息：" + mKeyCode);
		isSwipIn = true;
		// encrypted data
		index += 10;
		for (int i = 0; i < 160; i++)
		{
			if (0 != i && 0 == (i % 32)) encrypedData += '\n';
			tmp = input[(i >> 1) + index] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'a' : tmp + '0';
			encrypedData += (char) tmp;
		}

		byEncrypedData = new byte[80];
		for (int i = 0; i < 80; i++)
		{
			byEncrypedData[i] = input[index + i];
		}

		index += 80;
		byte tmpTrackInfo = input[index];
		int trackCount = 0;
		for (int i = 0; i < 3; i++)
		{
			byte info = (byte) (tmpTrackInfo & (0x01 << i));
			if (0 != info)
			{
				trackInfo += (char) ('1' + i);
				trackCount++;
			}
		}

		if (null != track)
		{
			track[0] = trackCount;
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
		// if (true) {
		// return ret;
		// }

		byte[] tmp_key =
		{ 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd,
				(byte) 0xef, (byte) 0xfe, (byte) 0xdc, (byte) 0xba,
				(byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		String key = "1234567890abcdeffedcba9876543210";
		for (int i = 0; i < byEncrypedData.length; i++)
		{
			if (0 != i && 0 == (i % 16))
			{
				System.out.format("\n");
			}

			System.out.format("%02x ", byEncrypedData[i]);
		}
		byte[] srcBytes = decryptMode(tmp_key, byEncrypedData);
		// byte[] srcBytes = decryptMode(key.getBytes(), byEncrypedData);
		try
		{
			Log.d("decrypt", new String(srcBytes));
			Log.d("decrypt", ret);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		for (int i = 0; i < srcBytes.length; i++)
		{
			if (0 != i && 0 == (i % 16))
			{
				System.out.format("\n");
			}

			System.out.format("%02x ", srcBytes[i]);

		}

		for (int i = 0; i < 160; i++)
		{

			if (0 != i && 0 == (i % 32)) decrypedData += '\n';
			tmp = srcBytes[(i >> 1)] & 0xff;
			tmp = i % 2 == 0 ? tmp >> 4 : tmp & 0x0f;
			tmp = tmp > 9 ? tmp - 10 + 'a' : tmp + '0';
			decrypedData += (char) tmp;
		}

		// ȡpan
		int panIndex = 1;
		String realPan = new String();
		for (int i = 0; i < panLength; i++)
		{
			tmp = srcBytes[(i + panIndex) >> 1];
			if (0 != (i % 2))
			{
				tmp >>= 4;
			}

			tmp &= 0x0f;
			realPan += tmp;
		}

		//
		ret += "decrypedData\n";
		ret += decrypedData;
		ret += "\nPan:" + realPan;

		data.pan = realPan;
		data.decrypedData = decrypedData;

		merReserved.append("{");
		merReserved.append("Firmware Version:" + data.firmwareVersion);
		merReserved.append("|");
		merReserved.append("Encryption Mode:" + data.encryptionMode);
		merReserved.append("|");
		merReserved.append("Track Info:" + data.trackInfo);
		merReserved.append("|");
		merReserved.append("encryptionMode:" + data.encryptionMode);
		merReserved.append("|");
		merReserved.append("xxx:" + data.xxx);
		merReserved.append("|");
		merReserved.append("last4Pan:" + data.last4Pan);
		merReserved.append("|");
		merReserved.append("Expiry Date:" + data.expiryDate);
		merReserved.append("|");
		merReserved.append("User Name:" + data.userName);
		merReserved.append("|");
		merReserved.append("KSN:" + data.ksn);
		merReserved.append("|");
		merReserved.append("Encrypted Data:" + data.encrypedData);
		merReserved.append("|");
		merReserved.append("PAN:" + data.pan);
		merReserved.append("|");
		merReserved.append("decrypedData" + data.decrypedData);
		merReserved.append("}");
		// PromptUtil.showToast(getActivity(), realPan);
		if (mSwipListener != null)
		{
			mSwipListener.recieveCard(data);
			mSwipListener.progress(SWIPING_SUCCESS, ReaderSUCCESS);
		}

		return ret;
	}

	public static byte[] decryptMode(byte[] keybyte, byte[] src)
	{
		try
		{
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		}
		catch (java.security.NoSuchAlgorithmException e1)
		{
			Log.e("aa", e1.getMessage());
		}
		catch (javax.crypto.NoSuchPaddingException e2)
		{
			Log.e("aa", e2.getMessage());
		}
		catch (java.lang.Exception e3)
		{
			Log.e("aa", e3.getMessage());
		}

		return null;
	}

	private static int StringToHex(String str, byte[] dst)
	{
		int count = 0;
		int tmp = 0;
		for (int i = 0; i < str.length(); i++)
		{
			tmp = GetHexChar(str.charAt(i));
			if (tmp < 0) continue;
			if (count < dst.length * 2)
			{
				if (0 == (count % 2))
				{
					dst[count >> 1] = (byte) (tmp * 16);
				}
				else
				{
					dst[count >> 1] += (byte) tmp;
				}

				count++;
			}
			else
			{
				return -1;
			}
		}

		return count >> 1;

	}

	private static int GetHexChar(char c)
	{
		switch (c)
		{
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return c - '0';
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
			return c - 'a' + 10;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
			return c - 'A' + 10;
		default:
			return -1;
		}
	}

	public static void sendCmd(String mCmd)
	{
		byte[] tmp = new byte[128];
		Log.d("IO", "Send:" + mCmd);
		if (null == mCmd || mCmd.length() < 1) return;
		int len = StringToHex(mCmd, tmp);
		sendCmd(tmp, len);
	}

	public static void sendCmd(byte[] cmd, int len)
	{
		if (null == cmd) return;
		if (len < 1) return;
		if (!mMobileReader.deviceIsAvailable()) return;
		if (!isOpen)
		{
			isOpen = mMobileReader.open(false);
		}

		mMobileReader.write(cmd, len);
	}

	/**
	 * 发送命令，获取设备号
	 * 
	 * @param cmd
	 */
	public static void sendCmdKSN(String cmd)
	{
		log("btnSend----------------setOnClickListener");
		if (!mMobileReader.deviceIsAvailable()) return;

		if (open())
		{
			log("Open----------------");
			// btnSend.setEnabled(false);
		}
		else
		{
			return;
		}

		if (!mMobileReader.deviceIsAvailable()) return;
		byte[] tmp = new byte[128];
		int len = StringToHex(cmd, tmp);
		String tmpLog = new String();
		for (int i = 0; i < len; i++)
		{
			tmpLog += String.format("%02x ", tmp[i]);
		}

		log("Len --> " + len);
		log("Send-- > " + tmpLog);

		if (!mMobileReader.deviceIsAvailable()) return;

		mMobileReader.write(tmp, len);

		if (!mMobileReader.deviceIsAvailable()) return;

		log("btnSend----------------setOnClickListener ====== end");
	}

	public static void log(String str)
	{
		if (Constants.DEBUG) Log.d("liguohui", str + '\n');
	}

	private static boolean open()
	{
		try
		{
			Thread.sleep(100);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (!mMobileReader.open(false)) { return false; }

		try
		{
			Thread.sleep(100);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return true;
	}

	public static String HexToString(byte input[], int len, int lineByte)
	{
		String ret = new String();
		for (int i = 0; i < len; i++)
		{
			if ((i % lineByte == 0) && i != 0)
			{
				ret += '\n';
			}

			ret += ByteToString(input[i]);
		}

		return ret;
	}

	// private String HexToString(byte input[], int len, int lineByte) {
	// String ret = new String();
	// for (int i = 0; i < len; i++) {
	// if ((i % lineByte == 0) && i != 0) {
	// ret += '\n';
	// }
	//
	// ret += ByteToString(input[i]);
	// }
	//
	// return ret;
	// }
	//
	// private String ByteToString(byte in) {
	// String ret = String.format("%02x", in);
	// return ret;
	// }
	private static String ByteToString(byte in)
	{
		String ret = String.format("%02x", in);
		return ret;
	}

	private static String getCurrentFileName()
	{
		Time t = new Time();
		t.setToNow(); // ȡ��ϵͳʱ�䡣
		int year = t.year % 100;
		int month = t.month + 1;// �·�0��11
		int day = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int second = t.second;
		String date = new String();
		// String.format("%02d-%02d-%02d_%02d:%02d:%02d", year, month, day,
		// hour, minute, second);
		date = String.format("%02d%02d%02d_%02d%02d%02d", year, month, day,
				hour, minute, second);
		fileCount = (fileCount + 1) % 20;
		DecimalFormat df = new DecimalFormat("00");
		String strFileCount = df.format(fileCount);
		System.out.println(strFileCount);
		return phoneManufacturer + phoneModel + "_" + phoneSysCode + "_"
				+ strFileCount;
	}

	public static PayApp getInstance()
	{
		if (null == pay)
		{
			pay = new PayApp();
		}
		return pay;
	}

	public static void startCallStateService()
	{
		PayApp.pay.startService(new Intent(INTENT_ACTION_CALL_STATE));
		if (incomingCallServiceReceiver == null)
		{
			incomingCallServiceReceiver = new IncomingCallServiceReceiver();
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(INTENT_ACTION_CALL_STATE);
			PayApp.pay.registerReceiver(incomingCallServiceReceiver,
					intentFilter);
		}
	}

	public void endCallStateService()
	{
		PayApp.pay.stopService(new Intent(INTENT_ACTION_CALL_STATE));
		if (incomingCallServiceReceiver != null)
		{
			log("unregisterReceiver");
			PayApp.pay.unregisterReceiver(incomingCallServiceReceiver);
			incomingCallServiceReceiver = null;
		}
	}

	public interface SwipListener
	{
		public void recieveCard(CardData data);// 获取到卡号

		public void checkedCard(boolean flag);// 检测到刷卡器

		public void progress(int status, String message);// 检测到刷卡器
	}

	/**
	 * 银联支付成功/失败/取消后-反馈的接口监听(异步通知后台状态)
	 * 
	 * @author zhichao.huang
	 * 
	 */
	public interface BuySuccessListener
	{

		/**
		 * 银联支付后的反馈通知(异步通知后台状态)
		 * 
		 * @param resultData
		 *            结果信息
		 * @param msg
		 *            结果msg提示
		 */
		public void requestBuySuccess(ResultData resultData, String msg);

	}
}
