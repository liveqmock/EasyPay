/*
 * @Title:  UserLevelIntroductionFragment.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月9日 上午10:02:14
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment;

import com.inter.trade.R;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * 用户级别说明页面
 * 
 * @author chenguangchi
 * @data: 2014年9月9日 上午10:02:14
 * @version: V1.0
 */
public class UserLevelIntroductionFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_userlevel_introduction,
				null);

		setBackButton();

		return view;
	}

	private void setBackButton() {
		if (getActivity() == null) {
			return;
		}
		back = (Button) getActivity().findViewById(R.id.title_back_btn);

		if (back == null) {
			return;
		}
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = getActivity()
						.getSupportFragmentManager();
				int len = manager.getBackStackEntryCount();
				if (len > 0) {
					manager.popBackStack();
				} else {
					getActivity().finish();
				}
			}
		});

		menu = (Button) getActivity().findViewById(R.id.title_menu_btn);
		menu.setVisibility(View.GONE);
		Button right = (Button) getActivity()
				.findViewById(R.id.title_right_btn);
		right.setVisibility(View.GONE);
	}

	@Override
	public void onResume() {
		super.onResume();
		setTitle("用户等级说明");
	}

}
