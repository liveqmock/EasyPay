package com.inter.trade.ui.fragment.shoppingmall;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.inter.trade.R;
import com.inter.trade.ui.WebViewActivity;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.util.LoginUtil;

/**
 * 游戏充值主Fragment
 * 
 * @author Lihaifeng
 * 
 */
public class ShoppingMallMainFragment extends BaseFragment implements
		OnItemClickListener {

	private GridView mGridView;

	public ShoppingMallMainFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// LoginUtil.detection(getActivity());

		View view = inflater.inflate(R.layout.fragment_main_shoppingmall,
				container, false);
		initView(view);
		setTitle("手机购物");

		 setBackVisible();

		return view;
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.func_grid);
		mGridView.setAdapter(new ChildIndexAdapter(getActivity(), null,
				getIcons()));
		mGridView.setOnItemClickListener(this);
	}

	private ArrayList<FuncData> getIcons() {
		ArrayList<FuncData> fList = new ArrayList<FuncData>();

		FuncData cridetData = new FuncData();
		cridetData.imageId = R.drawable.icon_suning;
		cridetData.name = "苏宁易购";
		fList.add(cridetData);

		FuncData lefeng = new FuncData();
		lefeng.imageId = R.drawable.icon_lefeng;
		lefeng.name = "乐蜂网";
		fList.add(lefeng);
		
		FuncData meilishuo = new FuncData();
		meilishuo.imageId = R.drawable.icon_meilishuo;
		meilishuo.name = "美丽说";
		fList.add(meilishuo);
		
		FuncData dangdang = new FuncData();
		dangdang.imageId = R.drawable.icon_dangdang;
		dangdang.name = "当当网";
		fList.add(dangdang);
		
		FuncData lashou = new FuncData();
		lashou.imageId = R.drawable.icon_lashou;
		lashou.name = "拉手网";
		fList.add(lashou);

		return fList;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), WebViewActivity.class);
		String path = "";
		switch (position) {
		case 0://苏宁易购
			path = "http://union.suning.com/aas/open/vistorAd.action?userId=1343832&webSiteId=0&adInfoId=2&adBookId=0&channel=11&vistURL=http://m.suning.com/&subUserEx=";

			intent.putExtra(
					"buylink",
					path
							+ (LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name
									: ""));
			break;
		case 1://乐蜂网
			path = "http://m.lefeng.com/index.php/home/index/aid/4850/cid2/1959184/unistr/"
					+ (LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name
							: "");

			intent.putExtra("buylink", path);
			break;
			
		case 2://美丽说  http://m.meilishuo.com/?nmref=NM_s10936_0_13640070005
			path = "http://m.meilishuo.com/?nmref=NM_s10936_0_"
					+ (LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name
							: "") + "&channel=40106";

			intent.putExtra("buylink", path);
			break;
		case 3://当当网  http://m.dangdang.com/?unionid=P-325477-13431060402
			path = "http://m.dangdang.com/?unionid=P-325477-"
					+ (LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name
							: "");

			intent.putExtra("buylink", path);
			break;
			
		case 4://拉手网 http://m.lashou.com/?union_pid=707022118&src=lashouwap&uid=13431060402
			path = "http://m.lashou.com/?union_pid=707022118&src=lashouwap&uid="
					+ (LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name
							: "");

			intent.putExtra("buylink", path);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

}
