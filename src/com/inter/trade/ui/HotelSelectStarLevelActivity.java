package com.inter.trade.ui;

import java.util.Calendar;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.qmoney.util.QMoneyData;
import com.inter.trade.ui.views.wheelwidget.OnWheelChangedListener;
import com.inter.trade.ui.views.wheelwidget.WheelView;
import com.inter.trade.ui.views.wheelwidget.adapters.ArrayWheelAdapter;
import com.inter.trade.ui.views.wheelwidget.adapters.NumericWheelAdapter;
import com.inter.trade.util.PromptUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class HotelSelectStarLevelActivity extends Activity implements OnClickListener{
	private int mPrice;
	private TextView cancel_tv;
	private TextView ok_tv;
	private Button star_nolimit;
	private Button star_kuaijie;
	private Button star_level2;
	private Button star_level3;
	private Button star_level4;
	private Button star_level5;
	
	private int mStarLevel=0;
	private int mStarLevelOld=mStarLevel;
	private String strarLevelArrays[];
	private Bundle bundle;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hotel_starlevel_layout);
		
		WindowManager wm = getWindowManager();
		Display defaultDisplay = wm.getDefaultDisplay();
		int height = defaultDisplay.getHeight();//为获取屏幕高    
		int width = defaultDisplay.getWidth();//为获取屏幕宽
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.4f;//弹出一个窗口，让背后的窗口变暗一点
		lp.x = 0;
		lp.y = height/2;
		lp.width=width;
		lp.height=height/2;
		
		getWindow().setAttributes(lp);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		
		cancel_tv = (TextView) findViewById(R.id.cancel_tv);
		ok_tv = (TextView) findViewById(R.id.ok_tv);
		cancel_tv.setOnClickListener(this);
		ok_tv.setOnClickListener(this);
		
		star_nolimit = (Button) findViewById(R.id.star_nolimit);
		star_kuaijie = (Button) findViewById(R.id.star_kuaijie);
		star_level2 = (Button) findViewById(R.id.star_level2);
		star_level3 = (Button) findViewById(R.id.star_level3);
		star_level4 = (Button) findViewById(R.id.star_level4);
		star_level5 = (Button) findViewById(R.id.star_level5);
		
		star_nolimit.setOnClickListener(this);
		star_kuaijie.setOnClickListener(this);
		star_level2.setOnClickListener(this);
		star_level3.setOnClickListener(this);
		star_level4.setOnClickListener(this);
		star_level5.setOnClickListener(this);

		mStarLevel=0;
		mStarLevelOld=mStarLevel;
		star_nolimit.setSelected(true);
		star_kuaijie.setSelected(false);
		star_level2.setSelected(false);
		star_level3.setSelected(false);
		star_level4.setSelected(false);
		star_level5.setSelected(false);
		
		bundle = getIntent().getBundleExtra("hotelStarLevel");
		if(bundle != null){
			strarLevelArrays = bundle.getStringArray("starLevelArray");
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.cancel_tv://取消
			finish();
			break;
		case R.id.ok_tv://确定
			getStarLevel();
			break;
		case R.id.star_nolimit://不限
			mStarLevel=0;
			star_nolimit.setSelected(true);
			selectStarLevel();
			break;
		case R.id.star_kuaijie://快捷连锁
			mStarLevel=1;
			star_kuaijie.setSelected(true);
			selectStarLevel();
			break;
		case R.id.star_level2://二星级以下
			mStarLevel=2;
			star_level2.setSelected(true);
			selectStarLevel();
			break;
		case R.id.star_level3://三星级
			mStarLevel=3;
			star_level3.setSelected(true);
			selectStarLevel();
			break;
		case R.id.star_level4://四星级
			mStarLevel=4;
			star_level4.setSelected(true);
			selectStarLevel();
			break;
		case R.id.star_level5://五星级
			mStarLevel=5;
			star_level5.setSelected(true);
			selectStarLevel();
			break;
		}
	}
	
	public void selectStarLevel(){
		if(mStarLevelOld != mStarLevel){
			switch (mStarLevelOld) {
			case 0://不限
				star_nolimit.setSelected(false);
				break;
			case 1://快捷连锁
				star_kuaijie.setSelected(false);
				break;
			case 2://二星级以下
				star_level2.setSelected(false);
				break;
			case 3://三星级
				star_level3.setSelected(false);
				break;
			case 4://四星级
				star_level4.setSelected(false);
				break;
			case 5://五星级
				star_level5.setSelected(false);
				break;
			}
			mStarLevelOld = mStarLevel;
		}
	}
	
	public void getStarLevel(){
		Intent intent = new Intent();
		if(strarLevelArrays != null && mStarLevel<strarLevelArrays.length){
			intent.putExtra("starLevel", strarLevelArrays[mStarLevel]);
			intent.putExtra("starId", mStarLevel);
		}
		setResult(RESULT_OK, intent);
		finish();
	}
	
	
}
