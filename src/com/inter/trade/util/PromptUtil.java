package com.inter.trade.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.IntentCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.inter.trade.AsyncLoadWork.LoginTimeoutListener;
import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.fragment.agent.util.AgentBindTask;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.SafetyAccountChangeActivity;
import com.inter.trade.ui.fragment.checking.SafetyAccountChangeOnceActivity;
import com.inter.trade.ui.fragment.checking.pwdsafety.PwdSafetySettingActivity;

public class PromptUtil {
	public static void showToast(Context context,String text){
		if(context==null){
			return;
		}
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	public static void showLongToast(Context context,String text){
		if(context==null){
			return;
		}
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	
	
	public static ProgressDialog dialog;
	public static void  showDialog(Context context,String text) {
		if(context==null){
			return ;
		}
		dissmiss();//test
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if(text!= null && !"".equals(text)){
			dialog.setMessage(text);
		}
		dialog.show();
	}
	public static void  showNoCancelableDialog(Context context,String text) {
		if(context==null){
			return ;
		}
		dissmiss();//test
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		if(text!= null && !"".equals(text)){
			dialog.setMessage(text);
		}
//		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	public static void dissmiss(){
		if(dialog != null){
			dialog.dismiss();
			dialog=null;
		}
	}
	public static void showQuit(final Context context){
		new AlertDialog.Builder(context).setMessage("确定退出手机通付宝？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//退出应用可下次进入再弹出通知公告
				PreferenceConfig.instance((FragmentActivity)context).putString(Constants.IS_SHOWED_NOTICE, "0");
				
				((FragmentActivity)context).finish();
				System.exit(0);
				
				//Process.killProcess(Process.myPid());
				
//				((Activity)context).finish();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
	public static void showRealFail(final Activity context,String message){
		new AlertDialog.Builder(context).setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				LoginUtil.isLogin=false;
				LoginUtil.mLoginStatus.cancel();
				
				//启动超时状态，说明客户端已经超时，这时BaseFragment的onTimeout ()会得到调用
				LoginTimeoutUtil.get().startTimeoutState();
				
//				showLogin(context);
				showLogin2(context);
				arg0.dismiss();
//				context.finish();
//				Intent intent = new Intent();
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setClass(context, FunctionActivity.class);
//				intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LOGIN_FRAGMENT_INDEX);
//				context.startActivity(intent);
			}
		}).setCancelable(false).show();
		
	}
	
	/**
	 * 超时处理，新增超时侦听器
	 * 
	 * @param context
	 * @param message
	 * @param timeoutListener 超时侦听器
	 */
	public static void showTimeoutRealFail(final Activity context,String message, final LoginTimeoutListener timeoutListener){
		new AlertDialog.Builder(context).setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				LoginUtil.isLogin=false;
				LoginUtil.mLoginStatus.cancel();
				
				//进入手势密码登录
				Intent intent = new Intent();
				intent.setClass(context, LockActivity.class);
		        intent.putExtra("isLoadMain", false);
//		        intent.putExtra("timeoutListener", timeoutListener);//超时侦听器
		        
		        
		        Bundle b = new Bundle();
		        b.putSerializable("timeoutListener", timeoutListener);
		        intent.putExtras(b);
				context.startActivity(intent);
				
				arg0.dismiss();
			}
		}).setCancelable(false).show();
		
	}
	public static void showLogin(Context context){
//		Intent intent = new Intent();
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setClass(context, LoginActivity.class);
//		context.startActivity(intent);
		Intent intent = new Intent();
		intent.setClass(context, LockActivity.class);
        intent.putExtra("isLoadMain", false);
		context.startActivity(intent);
	}
	
	public static void showLogin2(Context context){
		Intent intent = new Intent();
		intent.setClass(context, LockActivity.class);
        intent.putExtra("isLoadMain", false);
		context.startActivity(intent);
	}
	
	public static void showPwdSafetyDialog(final Activity context){
		new AlertDialog.Builder(context).setTitle("账户安全性提示").setMessage("由于您还没设置密保，考虑到密码的安全性，及找回密码，建议设置！")
		.setPositiveButton("现在设置", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				showPwdSafety(context);
				
			}
		}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//当用户点了以后再说，程序默认标识该用户已经设过密码了，之后就不会弹框提示了。
				PreferenceConfig.instance(context).putBoolean(Constants.IS_SET_PWDSAFETY, true);
				dialog.dismiss();
			}
		}).setCancelable(false).show();
		
	}
	
	private static void showPwdSafety(Context context){
		Intent intent = new Intent();
		intent.setClass(context, PwdSafetySettingActivity.class);
		context.startActivity(intent);
	}
	
	
	/*
	 * 代理商绑定，激活码
	 */
	public static void showBindAgentDialog (final FragmentActivity context) {
		Logger.d("showBindAgentDialog", "function call start");
		final EditText agent_applying_code_edit;
		String code;
		LayoutInflater factory = LayoutInflater.from(context);
		final View DialogView = factory.inflate(R.layout.agent_bind_layout, null);
		agent_applying_code_edit = (EditText)DialogView.findViewById(R.id.agent_applying_code_edit);
		AlertDialog dlg = new AlertDialog.Builder(
		context)
		.setTitle("使用前，请您先填写使用的服务区域代号：")
		.setView(DialogView)
		.setPositiveButton("确定",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					try { 
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
						field.setAccessible(true); 
						field.set(dialog, false);

					} catch (Exception e) { 
						e.printStackTrace(); 
					}
					
					String code,code2;
					code=agent_applying_code_edit.getText().toString();
					code2=agent_applying_code_edit.getHint().toString();
					if(code == null || code != null && "".equals(code)){
						code=code2;
					}
					else if(code.length() !=6){
						showToast(context, "服务代号不正确");
						return;
					}
					
					new AgentBindTask(context, code+"", dialog).execute("");
				}
			})
		.setNegativeButton("使用默认",
			new DialogInterface.OnClickListener() {

				@Override
				public void onClick(
						DialogInterface dialog,
						int which) {
					try { 
						Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing"); 
						field.setAccessible(true); 
						field.set(dialog, false);

					} catch (Exception e) { 
						e.printStackTrace(); 
					}
					
					new AgentBindTask(context, "020001", dialog).execute("");
//					dialog.dismiss();
//					context.finish();
				}
			}).create();
//		dlg.setCanceledOnTouchOutside(false);
		dlg.setCancelable(false);
		dlg.show();
		Logger.d("showBindAgentDialog", "function call end");
	}
	
	
	/**
	 * 弹出一个有确定按钮的dialog 
	 * @param title  标题
	 * @param message  提示内容
	 * @param positiveListener
	 * @throw
	 * @return void
	 */
	public static void showNoticeDialog(String title,String message,final PositiveListener positiveListener,final NegativeListener negativeListener,final Activity context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message)
		.setPositiveButton("确定",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
					if(positiveListener!=null){
						positiveListener.onPositive();
					}
					dialog.dismiss();
			}
		}).setCancelable(false);
		builder.show();

		
	}
	
	/**
	 * 弹出一个有确定、取消按钮的dialog 
	 * @param title  标题
	 * @param message  提示内容
	 * @param positiveListener
	 * @param negativeListener
	 * @throw
	 * @return void
	 */
	public static void showTwoButtonDialog(String title,String message,final PositiveListener positiveListener,final NegativeListener negativeListener,Activity context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message)
		.setPositiveButton("信用卡支付",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(positiveListener!=null){
					positiveListener.onPositive();
				}
				dialog.dismiss();
			}
		}).setNegativeButton("借记卡支付",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(negativeListener!=null){
					negativeListener.onNegative();
				}
				dialog.dismiss();
			}
		}).setCancelable(false);
		builder.show();
		
		
	}
	
	/**
	 * 灵活弹框，根据传的参数显示对话框
	 * @param title
	 * @param message
	 * @param positive
	 * @param negative
	 * @param positiveEnable
	 * @param negativeEnable
	 * @param positiveListener
	 * @param negativeListener
	 * @param context
	 * @param cancel
	 */
	public static void showSweetDialog(String title,String message,String positive,String negative,boolean positiveEnable,boolean negativeEnable,
			final PositiveListener positiveListener,final NegativeListener negativeListener,Activity context,boolean cancel){
		if(context == null){
			Logger.d("showSweetDialog", "Activity==null");
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		if(!TextUtils.isEmpty(title)){
			builder.setTitle(title);
		}
		
		if(!TextUtils.isEmpty(message)){
			builder.setMessage(message);
		}
		
		builder.setPositiveButton(positive,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(positiveListener!=null){
					positiveListener.onPositive();
				}
				dialog.dismiss();
			}
		}).setNegativeButton(negative,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(negativeListener!=null){
					negativeListener.onNegative();
				}
				dialog.dismiss();
			}
		}).setCancelable(cancel);
		
		AlertDialog dialog = builder.create();
		dialog.show();
		
		Logger.d("dialog.getButton", dialog.getButton(AlertDialog.BUTTON_POSITIVE)+"");
		
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(positiveEnable);
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(negativeEnable);
	}
	
	public interface PositiveListener{
		void onPositive();
	}
	
	public interface NegativeListener{
		void onNegative();
	}
	
	
	
	/**
	 * 弹出一个有确定按钮的dialog 
	 * @param title  标题
	 * @param message  提示内容
	 * @param positiveListener
	 * @throw
	 * @return void
	 */
	public static void showNoticeDialog(String title,String message,Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message)
		.setPositiveButton("确定",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
			}
		}).setCancelable(false);
		builder.show();

		
	}
	
	/**
	 * 弹出注销登录的提示框
	 * @param context
	 * @param message
	 * @throw
	 * @return void
	 */
	public static void showLogoutTips(final Activity context,String message){
		new AlertDialog.Builder(context).setMessage(message)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				LoginUtil.isLogin=false;
				LoginUtil.mLoginStatus.cancel();
				
				
				Intent intent =new Intent(context,SafetyAccountChangeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
				
				
//				showLogin(context);
//				showLogin2(context);
				arg0.dismiss();
//				context.finish();
//				Intent intent = new Intent();
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.setClass(context, FunctionActivity.class);
//				intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.LOGIN_FRAGMENT_INDEX);
//				context.startActivity(intent);
			}
		}).setCancelable(false).show();
	}
	
	
	/**
	 * 弹出忘记手势密码的窗 
	 * @param context
	 * @throw
	 * @return void
	 */
	public static void showForgetPassword(final Activity context){
		new AlertDialog.Builder(context).setMessage("忘记手势密码,需重新登录")
		.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				Intent intent=new Intent(context,SafetyAccountChangeActivity.class);
				context.startActivity(intent);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setCancelable(false).show();
		
	}
	
	/**
	 * 弹出提示是否返回的提示
	 * @param context
	 * @throw
	 * @return void
	 */
	public static void showSMSBackTip(final Activity context){
		new AlertDialog.Builder(context).setMessage("验证码可能略有延迟,确定返回并重新开始?")
		.setPositiveButton("返回", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				context.finish();
			}
		}).setNegativeButton("等待", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setCancelable(false).show();
		
	}
}
