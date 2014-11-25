package com.unionpay.uppayplugin.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.inter.trade.log.Logger;
import com.unionpay.UPPayAssistEx;

public class UnionpayUtil {
	private static final String TAG ="UnionpayUtil";
	private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;

    /*****************************************************************
     * mMode参数解释：
     *      "00" - 启动银联正式环境
     *      "01" - 连接银联测试环境
     *****************************************************************/
    public static String mMode = "00";
    
    public static void startUnionPlungin(String tn,final Activity activity){
    	 // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
//        tn = "201308200357000011802";6226440123456785
//    	String resverString = "{UC000000000000000001|招商银行|6226********6785}";
//        int ret = UPPayAssistEx.startPay(activity, null, Base64Pay.encode(resverString.getBytes()), tn, mMode);
    	Logger.d("UnionpayUtil", "mMode"+mMode);
    	int ret = UPPayAssistEx.startPay(activity, null, null, tn, mMode);
//    	int ret = UPPayAssistEx.startPay(activity, null, null, tn, "01"); 
    	if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            Log.e(TAG, " plugin not found or need upgrade!!!");

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("提示");
            builder.setMessage("完成支付需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                int which) {
                        	UPPayAssistEx.installUPPayPlugin(activity);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,
                                int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
    }
}
