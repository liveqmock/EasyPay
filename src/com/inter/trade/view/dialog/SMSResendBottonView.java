/*
 * @Title:  WheelWidgetBottonView.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月30日 下午1:57:26
 * @version:  V1.0
 */
package com.inter.trade.view.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.gamerecharge.dialog.WheelWidgetDialog.ClickSureButtonListener;
import com.tandong.bottomview.view.BottomView;

/**
 * 短信重新发送dialog
 * @author  ChenGuangChi
 * @data:  2014年6月30日 下午1:57:26
 * @version:  V1.0
 */
public class SMSResendBottonView implements OnClickListener{
		
		private BottomView bottomView;
		
		private  Button btnCancle;//取消按钮

		private Button btnSure;//确定按钮

		private DialogButtonPositive listener;
		
		private Context context;
		
		public SMSResendBottonView(Context context, DialogButtonPositive listener) {
			super();
			this.context = context;
			this.listener=listener;
		}

		public  void show(){
			bottomView=new BottomView(context, R.style.BottomViewTheme_Defalut, R.layout.dialog_sms_resend);
			bottomView.setAnimation(R.style.BottomToTopAnim);
			View view = bottomView.getView();
			
			btnCancle = (Button) view.findViewById(R.id.btn_cancel);
			btnSure = (Button) view.findViewById(R.id.btn_sure);
			btnCancle.setOnClickListener(this);
			btnSure.setOnClickListener(this);

			bottomView.showBottomView(true);
			
		}

		/**
		 * 重载方法
		 * @param v
		 */
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_cancel://取消
				bottomView.dismissBottomView();
				break;
			case R.id.btn_sure://确定
				bottomView.dismissBottomView();
				if(listener!=null){
					listener.onPositive();
				}
				break;

			default:
				break;
			}
			
		}
		
		/**
		 * 点击确定重新发送的按钮 
		 * @author  chenguangchi
		 * @data:  2014年10月27日 下午1:05:04
		 * @version:  V1.0
		 */
		public interface DialogButtonPositive{
			public void onPositive();
		}
}
