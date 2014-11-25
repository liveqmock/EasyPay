package com.inter.trade.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConfig {
	private static final String CONFIG = "CONFIG";
	private static SharedPreferences mPreferences;
	private static Editor mEditor;
	private static PreferenceConfig mConfig;
	
	private PreferenceConfig(Context context){
		mPreferences = context.getSharedPreferences(CONFIG, 0);
		mEditor = mPreferences.edit();
	}
	
	public static PreferenceConfig instance(Context context){
		if(mConfig == null){
			mConfig = new PreferenceConfig(context);
		}
		return mConfig;
	}
	
	public void putBoolean(String key,Boolean value){
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
	
	public boolean getBoolean(String key,Boolean defValue){
		return mPreferences.getBoolean(key, defValue);
	}
	
	public void putString(String key,String value){
		if(key == null || key.equals("")) return;
		
		String tempValue = value;
		if(key.equals(Constants.USER_PASSWORD) || key.equals(Constants.USER_GESTURE_PWD)) {
			if("".equals(tempValue)){
				
			}else{
				tempValue = "x58abfghfghgf";
			}
			
		}
		mEditor.putString(key, tempValue);
		mEditor.commit();
	}
	
	public String getString(String key,String defValue){
		return mPreferences.getString(key, defValue);
	}
	public int getInt(String key,int defValue){
		return mPreferences.getInt(key, defValue);
	}
	public void putInt(String key,int value){
		mEditor.putInt(key, value);
		mEditor.commit();
	}
}
