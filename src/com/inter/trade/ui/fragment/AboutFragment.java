package com.inter.trade.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inter.trade.R;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.WebViewActivity;
import com.inter.trade.util.VersionUtil;

/**
 * 关于界面
 * @author apple
 *
 */
public class AboutFragment extends BaseFragment implements OnClickListener{
	private RelativeLayout phone_layout;
	private RelativeLayout fuwu_protocol;
	private RelativeLayout wallet_protocol;
	private RelativeLayout about_android_opener;
	private RelativeLayout about_tfb_weixin_rl;
	private TextView versionTv;
	
	public static String mProtocolType="4";
	public AboutFragment()
	{
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.about_title));
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.about_layout, container, false);
		phone_layout = (RelativeLayout)view.findViewById(R.id.phone_layout);
		fuwu_protocol = (RelativeLayout)view.findViewById(R.id.fuwu_protocol);
		wallet_protocol = (RelativeLayout)view.findViewById(R.id.wallet_protocol);
		about_android_opener = (RelativeLayout)view.findViewById(R.id.about_android_opener);
		about_tfb_weixin_rl = (RelativeLayout)view.findViewById(R.id.about_tfb_weixin_rl);
		versionTv = (TextView)view.findViewById(R.id.versionTv);
		
		versionTv.setText("版本号："+VersionUtil.getVersionName(getActivity()));
		
		phone_layout.setOnClickListener(this);
		wallet_protocol.setOnClickListener(this);
		fuwu_protocol.setOnClickListener(this);
		about_android_opener.setOnClickListener(this);
		about_tfb_weixin_rl.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle(getString(R.string.about_title));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.phone_layout:
			try {
				Intent intent = new Intent();
				//intent.setAction(Intent.ACTION_DIAL);
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + "4006868956"));
				getActivity().startActivity(intent);
			}catch(SecurityException ex){
				Toast.makeText(getActivity(),
						"拨号失败，请检查相关权限。", Toast.LENGTH_SHORT).show();
			}catch (Exception e) {
				Toast.makeText(getActivity(),
						"拨号失败。", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.fuwu_protocol:
			mProtocolType = "1";
			showProtocol();
			break;
		case R.id.wallet_protocol:
			mProtocolType = "2";
			showProtocol();
			break;
		case R.id.about_android_opener:
			mProtocolType = "4";
			showProtocol();
			break;
		case R.id.about_tfb_weixin_rl:
			showWebViewWeixin();
			break;
		default:
			break;
		}
		
	}
	private void showProtocol(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, FragmentFactory.PROTOCOL_LIST_INDEX);
		getActivity().startActivity(intent);
	}
	private void showWebViewWeixin(){
		Intent intent = new Intent();
		intent.setClass(getActivity(), WebViewActivity.class);
//		intent.putExtra("buylink", "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=615&sid=451200&allianceid=20230&ouid=");
		intent.putExtra("buylink", "http://www.tfbpay.cn/mobilepay/wechat.php");
		getActivity().startActivity(intent);
	}
	
}
