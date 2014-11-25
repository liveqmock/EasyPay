package com.inter.trade.ui.fragment.tfbcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.CouponFirstFragment;

/**
 * 通付卡的主页面 
 * @author  chenguangchi
 * @data:  2014年9月2日 下午3:38:06
 * @version:  V1.0
 */
public class TFBcardMainFragment extends BaseFragment implements OnClickListener{
	
	private Button btnGoto,btn_gotorecharge;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_tfbcard_main, null);
		setBackVisible();
		hideTitleLine();
		initView(view);
		return view;
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		setTitle("优惠卡");
	}

	private void initView(View view) {
		btnGoto=(Button) view.findViewById(R.id.btn_gototgbcard);
		btn_gotorecharge=(Button) view.findViewById(R.id.btn_gotorecharge);
		btnGoto.setOnClickListener(this);
		btn_gotorecharge.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gototgbcard:
			getFragmentManager().beginTransaction().replace(R.id.func_container, new CouponFirstFragment()).commit();
			break;
		case R.id.btn_gotorecharge://充值
			getFragmentManager().beginTransaction().replace(R.id.func_container, new HTBRechargeFragment()).commit();
			break;

		default:
			break;
		}
	}
		
}
