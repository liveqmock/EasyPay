package com.inter.trade.ui.fragment.gamerecharge.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.views.wheelwidget.OnWheelChangedListener;
import com.inter.trade.ui.views.wheelwidget.WheelView;
import com.inter.trade.ui.views.wheelwidget.adapters.ArrayWheelAdapter;

/**
 * 
 * @author chenguanchi
 * 
 */
public class WheelWidgetDialog extends Dialog  implements android.view.View.OnClickListener {

	private Context context;

	private Button btnCancle;//取消按钮

	private Button btnSure;//确定按钮

	private WheelView mWheelView;
	
	private String[] items;
	
	private ClickSureButtonListener listener;
	
	
	public WheelWidgetDialog(Context context, int theme,String[] items){
			this(context, theme);
			this.items=items;
			
	}

	public WheelWidgetDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		setCanceledOnTouchOutside(true);

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display defaultDisplay = wm.getDefaultDisplay();
		int height = defaultDisplay.getHeight();
		int width = defaultDisplay.getWidth();
		Window win = getWindow();
		LayoutParams params = new LayoutParams();
		params.x = 0;
		params.y = height / 2;
		// params.width=width;
		// params.height=height/2;
		win.setAttributes(params);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(context, R.layout.dialog_wheel_widget, null);

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
		
		setContentView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel://取消
			if(isShowing())
				dismiss();
			break;
		case R.id.btn_sure://确定
			if(isShowing())
				dismiss();
			int position = mWheelView.getCurrentItem();
			if(listener!=null){
				listener.clickSureBtn(position);
			}
			break;

		default:
			break;
		}
	}
	
	public void setClickSureButtonListener(ClickSureButtonListener listener){
		this.listener=listener;
	}
	
	public interface ClickSureButtonListener{
		/**
		 *  点击确定按钮的时候调用
		 * @param position  列表中选中的位置
		 */
		public void clickSureBtn(int position);
	}
}
