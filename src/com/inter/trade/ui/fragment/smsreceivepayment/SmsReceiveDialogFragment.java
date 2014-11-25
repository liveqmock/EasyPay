package com.inter.trade.ui.fragment.smsreceivepayment;

import com.inter.trade.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SmsReceiveDialogFragment extends DialogFragment { 
	private final static String TAG = "esaypay";
	private SmsReceiveDialogListener listener;

	private Button btn_negative;
	private Button btn_positive;
	private boolean positiveEnable;
	private boolean negativeEnable;
	
    public void setListener(SmsReceiveDialogListener listener) {
		this.listener = listener;
		
	}
    
    public SmsReceiveDialogFragment(){
	}
	
	public static SmsReceiveDialogFragment newInstance(boolean negativeEnable,boolean positiveEnable){
		SmsReceiveDialogFragment smsdf = new SmsReceiveDialogFragment(); 
        Bundle bundle = new Bundle(); 
        bundle.putBoolean("alert-negative", negativeEnable); 
        bundle.putBoolean("alert-positive", positiveEnable); 
        smsdf.setArguments(bundle); 
        return smsdf; 
    }
	
	private boolean getNegativeEnable(){ 
    	return positiveEnable=getArguments().getBoolean("alert-negative"); 
    } 
    
    private boolean getPositiveEnable(){ 
    	return positiveEnable=getArguments().getBoolean("alert-positive"); 
    } 
    
    
    /* 【步骤2】创建view可以通过两个途径，一是fragment中的onCreateView()，二是DialogFragment中的onCreateDialog()。
     * 前者适合对自定义的layout进行设置，具有更大的灵活性 
     * 而后者适合对简单dialog进行处理，可以利用Dialog.Builder直接返回Dialog对象 
     * 从生命周期的顺序而言，先执行onCreateDialog()，后执行oonCreateView()，我们不应同时使用两者。 
     * */ 
    @Override 
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
        View view = View.inflate(getActivity(), R.layout.sms_receive_default_card_dialog, null);
		btn_negative = (Button) view.findViewById(R.id.btn_negative);
		btn_negative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					listener.onNegative(SmsReceiveDialogFragment.this);
				}
				dismiss();
			}
		});
		btn_positive = (Button) view.findViewById(R.id.btn_positive);
		btn_positive.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (listener != null) {
					listener.onPositive(SmsReceiveDialogFragment.this);
				}
				dismiss();
			}
		});
		
		btn_negative.setEnabled(getNegativeEnable());
		btn_positive.setEnabled(getPositiveEnable());
		
//			builder.setView(view);
			AlertDialog alertDialog = builder.create();
			alertDialog.setView(view, 0, 0, 0, 0);
			alertDialog.setCancelable(true);
			alertDialog.setCanceledOnTouchOutside(true);
			alertDialog.show();
			return alertDialog;
			
   }  

    public interface SmsReceiveDialogListener {
		void onPositive(SmsReceiveDialogFragment dialog);
		void onNegative(SmsReceiveDialogFragment dialog);
	}
    
}