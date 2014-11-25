/*
 * @Title:  BankFragment.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月25日 上午10:18:35
 * @version:  V1.0
 */
package com.inter.trade.ui.func.childfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.inter.trade.R;
import com.inter.trade.VersionTask;
import com.inter.trade.log.Logger;
import com.inter.trade.qrsacncode.CaptureActivity;
import com.inter.trade.ui.FunctionActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.ui.func.util.MoreFunUtil;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;

/**
 * 更多的页面
 * 
 * @author ChenGuangChi
 * @data: 2014年7月25日 上午10:18:35
 * @version: V1.0
 */
public class MoreFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	private Button ibDimen;// 扫描二维码
	private GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_more_new, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ibDimen = (Button) view.findViewById(R.id.iv_dimencode);
		ibDimen.setOnClickListener(this);

		gridView = (GridView) view.findViewById(R.id.func_grid);
		gridView.setAdapter(new ChildIndexAdapter(getActivity(), null,
				MoreFunUtil.getMoreFun()));
		gridView.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (!LoginUtil.isLogin) {
			showLogin();
			return;
		}
		switch (position) {
		case 0:// 账户管理
			showMore();
			break;
		case 1:// 激活信息
			showBindAgent();
			break;
		case 2:// 代理商
			String agentid = LoginUtil.mLoginStatus.agentid;
			Logger.d("MoreFragment", "agentid=="+agentid);
			
			if(agentid == null){
				agentid=PreferenceConfig.instance(getActivity()).getString(Constants.AGENT_ID, "0");
				LoginUtil.mLoginStatus.agentid = agentid;
				Logger.d("PreferenceConfig", "agentid=="+agentid);
			}
			
			if (agentid != null && !("".equals(agentid))
					&& Integer.parseInt(agentid) > 0) {
				if (getActivity() instanceof MainActivity) {
					((MainActivity) getActivity()).replaceCommonTab(1);
				}
			} else {
				Intent intent = new Intent();
				intent.putExtra(FragmentFactory.INDEX_KEY,
						FuncMap.AGENT_INDEX_FUNC);
				intent.setClass(getActivity(), IndexActivity.class);
				startActivity(intent);
			}
			break;
		case 3:// 帮助中心
			showHelp();
			break;
		case 4:// 意见反馈
			showfeedback();
			break;
		case 5:// 关于我们
			showAout();
			break;
		case 6:// 检查更新
			new VersionTask(getActivity(), false,false).execute("");
			break;
		case 7:// APP下载
			showDownload();
			break;
		case 8:// 应用推荐
			showRecommend();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_dimencode:// 扫描二维码
			// PromptUtil.showToast(getActivity(), "该功能正在开发中,敬请期待...");
			Intent intent = new Intent(getActivity(), CaptureActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 打开应用推荐
	 * 
	 * @throw
	 * @return void
	 */
	private void showRecommend() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_APP_RECOMMEND);
		startActivity(intent);
	}

	private void showDownload() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_DOWNLOAD);
		startActivity(intent);
	}

	private void showLogin() {
		Intent intent = new Intent();
		// 进入手势密码登录页面
		intent.setClass(getActivity(), LockActivity.class);
		intent.putExtra("isLoadMain", false);
		startActivity(intent);
	}

	/**
	 * 激活信息（绑定代理）
	 */
	private void showBindAgent() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_BIND_AGENT_INDEX);
		startActivity(intent);
	}

	private void showMore() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_MORE_INDEX);
		startActivityForResult(intent, 0);
	}

	private void showActive() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_OPEN_INDEX);
		startActivity(intent);
	}

	private void showHelp() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_HELP_INDEX);
		startActivity(intent);
	}

	private void showAout() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_ABOUT_INDEX);
		startActivity(intent);
	}

	private void showUserInfo() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LOGIN_FRAGMENT_INDEX);
		startActivity(intent);
	}

	private void showfeedback() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_FEEDBACK_INDEX);
		startActivity(intent);
	}

	private void showPwd() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY,
				FragmentFactory.LEFT_PWD_INDEX);
		startActivityForResult(intent, 3);
	}

}
