package com.inter.trade.ui.fragment.smsreceivepayment;

import com.inter.trade.R;
import com.inter.trade.view.dialog.MySweetDialog.MyAppListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class SmsDialogFragment extends DialogFragment { 
	private MyAppListener listener;
	private TextView tv_note;
	
	/**
     * 【步骤1】：通过newInstance()创建实例并返回
    */ 
    public static SmsDialogFragment newInstance(String title,String message, String negative, String positive){
    	SmsDialogFragment smsdf = new SmsDialogFragment(); 
        Bundle bundle = new Bundle(); 
        bundle.putString("alert-title", title); 
        bundle.putString("alert-message", message); 
        bundle.putString("alert-negative", negative); 
        bundle.putString("alert-positive", positive); 
        smsdf.setArguments(bundle); 
        return smsdf; 
    }  
         
    public void setListener(MyAppListener listener) {
		this.listener = listener;
		
	}
    
    private String getTitle(){
        return getArguments().getString("alert-title"); 
    } 
    
    private String getMessage(){ 
        return getArguments().getString("alert-message"); 
    } 
    
    private String getNegative(){ 
    	return getArguments().getString("alert-negative"); 
    } 
    
    private String getPositive(){ 
    	return getArguments().getString("alert-positive"); 
    } 
    
//    @Override //在onCreate中设置对话框的风格、属性等
//    public void onCreate(Bundle savedInstanceState) { 
//	    super.onCreate(savedInstanceState); 
//	    //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true 
//	    setCancelable(true); 
//	    //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
//	    int style = DialogFragment.STYLE_NO_TITLE, theme = 0;//R.style.Translucent_NoTitle 
//	    setStyle(style,theme); 
//    } 
    
    /* 【步骤2】创建view可以通过两个途径，一是fragment中的onCreateView()，二是DialogFragment中的onCreateDialog()。
     * 前者适合对自定义的layout进行设置，具有更大的灵活性 
     * 而后者适合对简单dialog进行处理，可以利用Dialog.Builder直接返回Dialog对象 
     * 从生命周期的顺序而言，先执行onCreateDialog()，后执行oonCreateView()，我们不应同时使用两者。 
     * */ 
    @Override 
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
	        View view = View.inflate(getActivity(), R.layout.sms_toast_layout, null);
			tv_note = (TextView) view.findViewById(R.id.tv_note);
			tv_note.setText(getMessage());
//			builder.setView(view);
			AlertDialog alertDialog = builder.create();
			alertDialog.setView(view, 0, 0, 0, 0);
			alertDialog.setCancelable(true);
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();
			return alertDialog;
			
//                                    .setTitle(getTitle()) 
//                                    .setMessage(getMessage()) 
//                                    .setPositiveButton(getPositive(), new OnClickListener() {
//                            			
//                            			@Override
//                            			public void onClick(DialogInterface dialog, int which) {
//                            				dismiss();
//                            				if(listener!=null){
//                            					listener.onPositive();
//                            				}
//                            			}
//                            		})  //设置回调函数 
//                                    .setNegativeButton(getNegative(),new OnClickListener() {
//                            			
//                            			@Override
//                            			public void onClick(DialogInterface dialog, int which) {
//                            				dismiss();
//                            				if(listener!=null){
//                            					listener.onNegative();
//                            				}
//                            			}
//                            		}); //设置回调函数
//        return b.create(); 
   }  

//    @Override //按键触发的回调函数
//    public void onClick(DialogInterface dialog, int which) {  
//        boolean isCancel = false; 
//        if(which == AlertDialog.BUTTON_POSITIVE){ //判断用户所按何键 
//            isCancel = true; 
//        }else if(which == AlertDialog.BUTTON_NEGATIVE){ //判断用户所按何键 
//            isCancel = true; 
//        }
////        MyActivity act = (MyActivity) getActivity(); 
////        act.onDialogDone(getTag(), isCancel, "CLick OK, Alert dismissed"); 
//    } 

    public interface MyAppListener {
		void onNegative();
		void onPositive();
	}
}