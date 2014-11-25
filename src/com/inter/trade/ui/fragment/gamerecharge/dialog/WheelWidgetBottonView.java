/*
 * @Title:  WheelWidgetBottonView.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月30日 下午1:57:26
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.gamerecharge.dialog.WheelWidgetDialog.ClickSureButtonListener;
import com.inter.trade.ui.views.wheelwidget.OnWheelChangedListener;
import com.inter.trade.ui.views.wheelwidget.WheelView;
import com.tandong.bottomview.view.BottomView;

/**
 * 底部选择窗口
 * @author  ChenGuangChi
 * @data:  2014年6月30日 下午1:57:26
 * @version:  V1.0
 */
public class WheelWidgetBottonView implements OnClickListener{
		
		private BottomView bottomView;
		
		private  Button btnCancle;//取消按钮

		private Button btnSure;//确定按钮

		private WheelView mWheelView;
		
		private ClickSureButtonListener listener;
		
		private Context context;
		
		public WheelWidgetBottonView(Context context,ClickSureButtonListener listener) {
			super();
			this.context = context;
			this.listener=listener;
		}

		public  void show(String[] items){
			bottomView=new BottomView(context, R.style.BottomViewTheme_Defalut, R.layout.dialog_wheel_widget);
			bottomView.setAnimation(R.style.BottomToTopAnim);
			View view = bottomView.getView();
			
			btnCancle = (Button) view.findViewById(R.id.btn_cancel);
			btnSure = (Button) view.findViewById(R.id.btn_sure);
			btnCancle.setOnClickListener(this);
			btnSure.setOnClickListener(this);

			mWheelView = (WheelView) view.findViewById(R.id.wheelview);
			mWheelView.setWheelBackground(R.drawable.wheel_bg_holo);// 设置背景的颜色
			mWheelView.setWheelForeground(R.drawable.wheel_val_holo);
			mWheelView.setShadowColor(0xFFFFFF, 0xFFFFFF, 0xFFFFFF);// 设置上下边缘的颜色值
			mWheelView.setCyclic(true);// 设置是否循环
			final MyWheelAdapter speedAdapter = new MyWheelAdapter(
					context, items);
			mWheelView.setViewAdapter(speedAdapter);
			mWheelView.addChangingListener(new OnWheelChangedListener() {
				
				@Override
				public void onChanged(WheelView wheel, int oldValue, int newValue) {
					speedAdapter.setCurrentItem(newValue);
				}
			});
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
				
				int position = mWheelView.getCurrentItem();
				if(listener!=null){
					listener.clickSureBtn(position);
				}
				break;

			default:
				break;
			}
			
		}
}
