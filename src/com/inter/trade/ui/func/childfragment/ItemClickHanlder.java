/*
 * @Title:  ItemClickHanlder.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月28日 下午1:24:36
 * @version:  V1.0
 */
package com.inter.trade.ui.func.childfragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.inter.trade.log.Logger;
import com.inter.trade.ui.CridetActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.WebViewActivity;
import com.inter.trade.ui.activity.SalaryGetMainActivity;
import com.inter.trade.ui.activity.SalaryPayMainActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.SafetyLoginActivity;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.ui.func.task.CountTask;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.VersionUtil;

/**
 * 处理主页中的所有功能的处理(除了更多页面)
 * 
 * @author ChenGuangChi
 * @data: 2014年7月28日 下午1:24:36
 * @version: V1.0
 */
public class ItemClickHanlder {

	public CountTask handleOnclickItem(FragmentActivity activity, int arg2,
			ArrayList<FuncData> mDatas) {
		CountTask task = null;
		if (VersionUtil.isNeedUpdate(activity)) {
			VersionUtil.showUpdate(activity);
			return null;
		}
		Intent intent = new Intent();
		FuncData data = mDatas.get(arg2);
		if (data.identify == -1) {
			PromptUtil.showToast(activity, data.name + "功能正在开发中，敬请期待...");
			return null;
		}

		// 没有登录，跳到手势登录
		if (!LoginUtil.isLogin) {

			String user_name = PreferenceConfig.instance(activity).getString(
					Constants.USER_NAME, "");
			String user_pw = PreferenceConfig.instance(activity).getString(
					Constants.USER_PASSWORD, "");
			String user_gesture_pwd = PreferenceConfig.instance(activity)
					.getString(Constants.USER_GESTURE_PWD, "");

			if (user_name != null && !"".equals(user_name) && user_pw != null
					&& !"".equals(user_pw) && user_gesture_pwd != null
					&& !"".equals(user_gesture_pwd)) {
				// 进入手势密码登录页面
				intent.setClass(activity, LockActivity.class);
				intent.putExtra("isLoadMain", false);
			} else {
				// 进入系统登录页面
				intent.setClass(activity, SafetyLoginActivity.class);
				intent.putExtra("isLoadMain", false);
			}

			activity.startActivity(intent);
			return null;
		}

		// 飞机票
		if (data.mnuid == FuncMap.AIR_TICKET_INDEX_FUNC) {
			// 添加点击记录
			task = insert(activity, data.id);
			intent.setClass(activity, UIManagerActivity.class);
			// intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("targetFragment",
					UIConstantDefault.UI_CONSTANT_AIR_TICKET/**
			 * 
			 * 
			 * UI_CONSTANT_AIR_TICKET_CLEARINGL
			 */
			);
			activity.startActivity(intent);
			return task;
		}

		// 酒店
//		if (data.mnuid == FuncMap.HOTEL_INDEX_FUNC) {
//			// 添加点击记录
//			task = insert(activity, data.id);
//			intent.setClass(activity, UIManagerActivity.class);
//			// intent.setClass(activity, WebViewActivity.class);
//			intent.putExtra("targetFragment",
//					UIConstantDefault.UI_CONSTANT_HOTEL);
//			activity.startActivity(intent);
//			return task;
//		}

		// 发工资
		if (data.mnuid == FuncMap.SALARYPAY_INDEX_FUNC) {
			// 添加点击记录
			task = insert(activity, data.id);
			intent.setClass(activity, SalaryPayMainActivity.class);
			intent.putExtra("targetFragment",
					UIConstantDefault.UI_CONSTANT_SALARY_MAIN);
			activity.startActivity(intent);
			return task;
		}

		// 签收工资
		if (data.mnuid == FuncMap.SALARYGET_INDEX_FUNC) {
			// 添加点击记录
			task = insert(activity, data.id);
			intent.setClass(activity, SalaryGetMainActivity.class);
			intent.putExtra("targetFragment",
					UIConstantDefault.UI_CONSTANT_SALARYGET_MAIN);
			activity.startActivity(intent);
			return task;
		}
		
		// 转账汇款
		if (data.mnuid == FuncMap.TRANSFER_INDEX_FUNC) {
			// 添加点击记录
			task = insert(activity, data.id);
			intent.setClass(activity, UIManagerActivity.class);
			intent.putExtra("targetFragment",
					UIConstantDefault.UI_CONSTANT_TRANSFER_MAIN
					);
			activity.startActivity(intent);
			return task;
		}

		// 飞机票、火车票、酒店
		if (data.mnuid == FuncMap.AIR_TICKET_INDEX_FUNC
				|| data.mnuid == FuncMap.TRAIN_TICKET_INDEX_FUNC
				|| data.mnuid == FuncMap.HOTEL_INDEX_FUNC
				) {

			intent.setClass(activity, WebViewActivity.class);

			switch (data.mnuid) {
			case FuncMap.AIR_TICKET_INDEX_FUNC:// 飞机票
				// 添加点击记录
				task = insert(activity, data.id);
				intent.putExtra(
						"buylink",
						"http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=615&sid=451200&allianceid=20230&ouid="+(LoginUtil.mLoginStatus.login_name==null?"":LoginUtil.mLoginStatus.login_name));
				break;

			case FuncMap.TRAIN_TICKET_INDEX_FUNC:// 火车票
				// 添加点击记录
				task = insert(activity, data.id);
				intent.putExtra(
						"buylink",
						"http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=2&sid=451200&allianceid=20230&OUID="+(LoginUtil.mLoginStatus.login_name==null?"":LoginUtil.mLoginStatus.login_name)+"&jumpUrl=http://m.ctrip.com/webapp/train/");
				break;

			case FuncMap.HOTEL_INDEX_FUNC:// 酒店
				// 添加点击记录
				task = insert(activity, data.id);
				intent.putExtra(
						"buylink",
						"http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=636&sid=451200&allianceid=20230&ouid="+(LoginUtil.mLoginStatus.login_name==null?"":LoginUtil.mLoginStatus.login_name));
				break;
				
			case FuncMap.SUNINGMALL_INDEX_FUNC:// 苏宁商城
				// 添加点击记录
				task = insert(activity, data.id);
				
				String path = "http://union.suning.com/aas/open/vistorAd.action?userId=1343832&webSiteId=0&adInfoId=2&adBookId=0&channel=11&vistURL=http://m.suning.com/&subUserEx=";
				
				intent.putExtra(
						"buylink",
						path+(LoginUtil.mLoginStatus.login_name != null ? LoginUtil.mLoginStatus.login_name : ""));
				break;

			default:
				break;
			}
			activity.startActivity(intent);
			return task;
		}

		//
		if (data.mnuid == FuncMap.AGENT_INDEX_FUNC) {
			String agentid = LoginUtil.mLoginStatus.agentid;
			Logger.d("ItemClickHanlder", "agentid==" + agentid);

			if (agentid == null) {
				agentid = PreferenceConfig.instance(activity).getString(
						Constants.AGENT_ID, "0");
				LoginUtil.mLoginStatus.agentid = agentid;
				Logger.d("PreferenceConfig", "agentid==" + agentid);
			}

			// if (agentid == null || "".equals(agentid)
			// || Integer.parseInt(agentid) <= 0) {
			// // 不是代理商用户
			// PromptUtil.showToast(getActivity(), "您不是代理商用户，暂不能访问此功能");
			// // ((MainActivity) getActivity()).replaceAgentApply();//代理商申请
			// } else {
			// if (getActivity() instanceof MainActivity) {
			// //添加点击记录
			// insert(FuncMap.AGENT_INDEX_FUNC);
			// ((MainActivity) getActivity()).replaceCommonTab(1);
			// }
			// }

			if (agentid != null && !("".equals(agentid))
					&& Integer.parseInt(agentid) > 0) {
				if (activity instanceof MainActivity) {
					// 添加点击记录
					task = insert(activity, data.id);
					((MainActivity) activity).replaceCommonTab(1);
				}
				return task;
			}

		}

		if (data.mnuid == FuncMap.CRIDET_INDEX_FUNC) {
			if (LoginUtil.isLogin) {
				intent.setClass(activity, CridetActivity.class);
			} else {
				intent.putExtra(FragmentFactory.INDEX_KEY, data.mnuid);
				intent.setClass(activity, IndexActivity.class);
			}
		} else {
			intent.putExtra(FragmentFactory.INDEX_KEY, data.mnuid);
			intent.setClass(activity, IndexActivity.class);
		}
		// 添加点击记录
		task = insert(activity, data.id);
		activity.startActivity(intent);
		return task;
	}

	/***
	 * 添加点击记录
	 * 
	 * @param key
	 * @throw
	 * @return void
	 */
	private CountTask insert(Context context, String key) {
		// DBHelper.getInstance(getActivity()).insert(key);
		System.out.println("插入了key为：" + key + "的id");
		CountTask task = new CountTask(context, null);
		task.execute(key, "");

		return task;
	}
}
