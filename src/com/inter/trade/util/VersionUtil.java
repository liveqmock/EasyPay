/**
 * 
 */
package com.inter.trade.util;

import com.inter.trade.VersionTask;
import com.inter.trade.ui.func.childfragment.UpdateAppFragment;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * @author LiGuohui
 * @since 2013-1-28 下午10:33:30
 * @version 1.0.0
 */
public class VersionUtil {
	/**
	 * 获取当前版本号
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context)
	   {
		try {
			 PackageManager packageManager = context.getPackageManager();
	           // getPackageName()是你当前类的包名，0代表是获取版本信息
	           PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
	           String version = packInfo.versionName;
	           return version;
		} catch (Exception e) {
			// TODO: handle exception
		}
	           // 获取packagemanager的实例
	          
	          return "1.0";
	}
	/**
	 * 获取程序的versionCode
	 * @param context
	 * @return
	 * @throw
	 * @return String
	 */
	public static String getVersionCode(Context context){
		 PackageManager packageManager = context.getPackageManager();
         // getPackageName()是你当前类的包名，0代表是获取版本信息
         try {
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			int versionCode = packInfo.versionCode;
			return versionCode+"";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        return "1.0"; 
	}
	
	/**
	 * 判断是否需要强制升级
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isNeedUpdate(Context context){
		String newVersion = PreferenceConfig.instance(context).getString(Constants.VERSION_KEY, "");
		String oldVersion = getVersionName(context);
		String isNeed = PreferenceConfig.instance(context).getString(Constants.VERSION_MSUT_UPDATE, "0");
		//强制升级
		if(isNeed.equals("1")){
			int i = newVersion.compareTo(oldVersion);
			if( i ==0 || i<0)
			{
				return false;
			}else{
				return true;
			}
		}
		
		return false;
	}
	
	public static void showUpdate(FragmentActivity context){
		if(isNeedUpdate(context)){
//			String message =PreferenceConfig.instance(context).getString(Constants.VERSION_UPDATE_MESSAGE, "");
//			String url = PreferenceConfig.instance(context).getString(Constants.VERSION_UPDATE_URL, "");
//			
//			showDialog(context, message, url);
			showNeedUpdate(context);
		}
	}
	
	public static  void startDownload(Context context ,String url){
		try {
			/*
			 Intent intent= new Intent();        
			    intent.setAction(Intent.ACTION_VIEW);    
			    intent.addCategory("android.intent.category.BROWSABLE");
			    Uri content_url = Uri.parse(url);   
			    intent.setData(content_url);  
			    context.startActivity(intent);*/
				Intent intent = new Intent();
				intent.setClass(context, UpdateAppFragment.class);
				Bundle bundle=new Bundle();
				bundle.putString("Url", url);
				intent.putExtras(bundle);
				context.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(context, "未检测到浏览器！", Toast.LENGTH_SHORT).show();
		}
	}
	
	public static  void showDialog(final Context context,String message,final String url){
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("发现新版本");
	        builder.setMessage(message);
	        builder.setInverseBackgroundForced(true);
	        builder.setCancelable(false);
	        //builder.setCustomTitle();
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                startDownload(context,url);
	            }
	        });
	        builder.create().show();
	}
	
	public static  void showNeedUpdate(final FragmentActivity context){
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("发现新版本");
	        builder.setMessage("请升级至最新版本。");
	        builder.setInverseBackgroundForced(true);
	        builder.setCancelable(false);
	        //builder.setCustomTitle();
	        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	            }
	        });
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.dismiss();
	                new VersionTask(context,false,false).execute("")
	                ;
	            }
	        });
	        builder.create().show();
	}
}
