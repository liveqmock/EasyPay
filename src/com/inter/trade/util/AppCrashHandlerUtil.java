package com.inter.trade.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * App崩溃异常处理类
 * 
 * @author zhichao.huang
 *
 */
public class AppCrashHandlerUtil implements UncaughtExceptionHandler{

	private static final String TAG = AppCrashHandlerUtil.class.getName();

	private static AppCrashHandlerUtil crashHandler = null;

	private Context mContext;

	/**
	 * 日志存储路径
	 */
	public static final  String DIR = Environment.getExternalStorageDirectory()
			.getAbsolutePath() +  "/tfbpay/log/" ;

	/**
	 * 错误日志保存的文件名字
	 */
	public static final String NAME = getCurrentDateString() +  ".txt" ;

	private AppCrashHandlerUtil () {

	}

	/**
	 * 获取实例
	 * @return
	 */
	public static AppCrashHandlerUtil get () {
		if(AppCrashHandlerUtil.crashHandler == null) {
			crashHandler = new AppCrashHandlerUtil();
		}
		return crashHandler;
	}

	public void init(Context context) {
		mContext = context;

		Thread.setDefaultUncaughtExceptionHandler(this);
	}



	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		Log.i(TAG, "崩溃了");

		String info =  null ;

		ByteArrayOutputStream baos =  null ;

		PrintStream printStream =  null ;

		try  {

			baos =  new  ByteArrayOutputStream();

			printStream =  new  PrintStream(baos);

			ex.printStackTrace(printStream);

			byte [] data = baos.toByteArray();

			info =  new  String(data);

			data =  null ;

		}  catch  (Exception e) {

			e.printStackTrace();

		}  finally  {

			try  {

				if (printStream !=  null ) {
					printStream.close();
				}

				if (baos !=  null ) {
					baos.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		writeErrorLog(info);
		
		//退出程序  ,或者弹框提示用户
        android.os.Process.killProcess(android.os.Process.myPid());  
        System.exit(1); 
	}

	/**
	 * 向文件中写入错误信息
	 * 
	 * @param info
	 */

	protected void writeErrorLog(String info) {

		File dir =  new  File(DIR);

		if  (!dir.exists()) {
			dir.mkdirs();
		}

		File file =  new  File(dir, NAME);

		try {
			FileOutputStream fileOutputStream =  new  FileOutputStream(file,  true );
			fileOutputStream.write(info.getBytes());
			fileOutputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**

	 * 获取当前日期
	 * 
	 * @return
	 */
	private static String getCurrentDateString() {
		
		String result =  null ;
		SimpleDateFormat sdf =  new  SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date nowDate =  new  Date();
		result = sdf.format(nowDate);
		return result;
	}

	/**
	 * 销毁实例
	 * 
	 */
	public void onDestroy () {
		if(crashHandler != null) {
			crashHandler = null;
		}
	}

}
