package com.inter.trade.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 获取手机设备信息
 * 需要添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 * @author zhichao.huang
 *
 */
public class PhoneInfoUtil {
	
    /** 
     * 获取设备ID
     */  
    public static String getNativePhoneDeviceId(Context context) {
        String deviceId = null;
        deviceId = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return deviceId;
    }
    
    /** 
     * 获取电话号码  
     */  
    public static String getNativePhoneNumber(Context context) {  
        String nativePhoneNumber=null;
        nativePhoneNumber = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
        if(nativePhoneNumber != null && nativePhoneNumber.startsWith("+86")){
        	nativePhoneNumber=nativePhoneNumber.substring(3, nativePhoneNumber.length());
        }
        return nativePhoneNumber;
    }
  
    /** 
     * 获取手机服务商信息 
     */  
    public static String getProvidersName(Context context) {
        String providersName = "N/A";
        /** 
         * 国际移动用户识别码 
         */  
        String IMSI;
        try{
        IMSI = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。  
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
        	providersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
        	providersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
        	providersName = "中国电信";
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return providersName;
    }
    
    /**
     * 获取手机的所有信息，组成String返回
     * @param context
     * @return
     */
    public static String getPhoneInfo(Context context){
    	TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
    	StringBuilder sb = new StringBuilder();

    	sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
    	sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
    	sb.append("\nLine1Number = " + tm.getLine1Number());
    	sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
    	sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
    	sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
    	sb.append("\nNetworkType = " + tm.getNetworkType());
    	sb.append("\nPhoneType = " + tm.getPhoneType());
    	sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
    	sb.append("\nSimOperator = " + tm.getSimOperator());
    	sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
    	sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
    	sb.append("\nSimState = " + tm.getSimState());
    	sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
    	sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
    	return  sb.toString();
    }
}
