package com.inter.trade.ui.func;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.inter.trade.R;
import com.inter.trade.db.DBHelper;
import com.inter.trade.imageframe.ImageCache.ImageCacheParams;
import com.inter.trade.imageframe.ImageFetcher;
import com.inter.trade.ui.CridetActivity;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MainActivity;
import com.inter.trade.ui.WebViewActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.checking.LockActivity;
import com.inter.trade.ui.fragment.checking.SafetyLoginActivity;
import com.inter.trade.ui.func.task.CountTask;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.VersionUtil;

public class IndexItem extends Fragment implements OnItemClickListener {

	private GridView mGridView;

	public static ImageFetcher mImageFetcher;
	private int mImageThumbSize;
	public static final String IMAGE_CACHE_DIR = "image";
	private static final String IMAGE_DATA_EXTRA = "IMAGE_DATA_EXTRA";
	private ArrayList<FuncData> mDatas = new ArrayList<FuncData>();

	private CountTask task;//添加点击记录的异步任务
	
	public static IndexItem create(int index) {
		final IndexItem f = new IndexItem();

		final Bundle args = new Bundle();
		args.putInt(IMAGE_DATA_EXTRA, index);
		f.setArguments(args);
		return f;
	}

	public IndexItem() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initFetcher();
		View v = inflater.inflate(R.layout.index_item, container, false);
		int index = getArguments() != null ? getArguments().getInt(
				IMAGE_DATA_EXTRA) : null;

		mGridView = (GridView) v.findViewById(R.id.func_grid);
		if (FunctionFragment.mDatas.size() == 0) {
			return v;
		}
		mDatas = FunctionFragment.mDatas.get(index);
		mGridView.setAdapter(new IndexAdapter(getActivity(), mImageFetcher,
				mDatas));
		mGridView.setOnItemClickListener(this);

		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (VersionUtil.isNeedUpdate(getActivity())) {
			VersionUtil.showUpdate(getActivity());
			return;
		}
		Intent intent = new Intent();
		FuncData data = mDatas.get(arg2);
		if (data.identify == -1) {
			PromptUtil.showToast(getActivity(), data.name + "功能正在开发中，敬请期待...");
			return;
		}

		// 没有登录，跳到手势登录
		if (!LoginUtil.isLogin) {

			String user_name = PreferenceConfig.instance(getActivity())
					.getString(Constants.USER_NAME, "");
			String user_pw = PreferenceConfig.instance(getActivity())
					.getString(Constants.USER_PASSWORD, "");
			String user_gesture_pwd = PreferenceConfig.instance(getActivity())
					.getString(Constants.USER_GESTURE_PWD, "");

			if (user_name != null && !"".equals(user_name) && user_pw != null
					&& !"".equals(user_pw) && user_gesture_pwd != null
					&& !"".equals(user_gesture_pwd)) {
				// 进入手势密码登录页面
				intent.setClass(getActivity(), LockActivity.class);
				intent.putExtra("isLoadMain", false);
			} else {
				// 进入系统登录页面
				intent.setClass(getActivity(), SafetyLoginActivity.class);
				intent.putExtra("isLoadMain", false);
			}

			getActivity().startActivity(intent);
			return;
		}
		
		//飞机票
		if (data.mnuid == FuncMap.AIR_TICKET_INDEX_FUNC) {
			//添加点击记录
			intent.setClass(getActivity(), UIManagerActivity.class);
			//		 intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("targetFragment",
					UIConstantDefault.UI_CONSTANT_AIR_TICKET);
			getActivity().startActivity(intent);
			return;
		}
		
		//酒店
		if (data.mnuid == FuncMap.HOTEL_INDEX_FUNC) {
			//添加点击记录
			intent.setClass(getActivity(), UIManagerActivity.class);
			//		 intent.setClass(getActivity(), WebViewActivity.class);
			intent.putExtra("targetFragment",
					UIConstantDefault.UI_CONSTANT_HOTEL);
			getActivity().startActivity(intent);
			return;
		}
		
		//飞机票、火车票、酒店
		if(data.mnuid == FuncMap.AIR_TICKET_INDEX_FUNC || data.mnuid == FuncMap.TRAIN_TICKET_INDEX_FUNC
				|| data.mnuid == FuncMap.HOTEL_INDEX_FUNC) {
			
			intent.setClass(getActivity(), WebViewActivity.class);
			
			switch (data.mnuid) {
			case FuncMap.AIR_TICKET_INDEX_FUNC://飞机票
				//添加点击记录
				insert(data.id);
				intent.putExtra("buylink", "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=615&sid=451200&allianceid=20230&ouid=");
				break;

			case FuncMap.TRAIN_TICKET_INDEX_FUNC://火车票
				//添加点击记录
				insert(data.id);
				intent.putExtra("buylink", "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=2&sid=451200&allianceid=20230&OUID=&jumpUrl=http://m.ctrip.com/webapp/train/");
				break;

			case FuncMap.HOTEL_INDEX_FUNC://酒店
				//添加点击记录
				insert(data.id);
				intent.putExtra("buylink", "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=636&sid=451200&allianceid=20230&ouid=");
				break;

			default:
				break;
			}
			getActivity().startActivity(intent);
			return;
		}

		//
		if (data.mnuid == FuncMap.AGENT_INDEX_FUNC) {
			String agentid = LoginUtil.mLoginStatus.agentid;
//			if (agentid == null || "".equals(agentid)
//					|| Integer.parseInt(agentid) <= 0) {
//				// 不是代理商用户
//				PromptUtil.showToast(getActivity(), "您不是代理商用户，暂不能访问此功能");
////				((MainActivity) getActivity()).replaceAgentApply();//代理商申请
//			} else {
//				if (getActivity() instanceof MainActivity) {
//					//添加点击记录
//					insert(FuncMap.AGENT_INDEX_FUNC);
//					((MainActivity) getActivity()).replaceCommonTab(1);
//				}
//			}
			
			if (agentid != null && !("".equals(agentid))
					&& Integer.parseInt(agentid) > 0) {
				if (getActivity() instanceof MainActivity) {
					//添加点击记录
					insert(data.id);
					((MainActivity) getActivity()).replaceCommonTab(1);
				}
				return;
			}
			
		}

		if (data.mnuid == FuncMap.CRIDET_INDEX_FUNC) {
			if (LoginUtil.isLogin) {
				intent.setClass(getActivity(), CridetActivity.class);
			} else {
				intent.putExtra(FragmentFactory.INDEX_KEY, data.mnuid);
				intent.setClass(getActivity(), IndexActivity.class);
			}
		} else {
			intent.putExtra(FragmentFactory.INDEX_KEY, data.mnuid);
			intent.setClass(getActivity(), IndexActivity.class);
		}
		//添加点击记录
		insert(data.id);
		getActivity().startActivity(intent);
	}
	
	

	private void initFetcher() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		final int width = displayMetrics.widthPixels;
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				IMAGE_CACHE_DIR);
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of
													// app memory
		mImageThumbSize = getResources().getDimensionPixelSize(
				R.dimen.ad_pager_height);
		mImageFetcher = new ImageFetcher(getActivity(), width, mImageThumbSize);
		mImageFetcher.setLoadingImage(R.drawable.default_index);
		mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(),
				cacheParams);
	}

	public ImageFetcher getFetcher() {
		return mImageFetcher;
	}

	/***
	 *添加点击记录 
	 * @param key
	 * @throw
	 * @return void
	 */
	private void insert(String key) {
		//DBHelper.getInstance(getActivity()).insert(key);
		System.out.println("插入了key为："+key+"的id");
		task=new CountTask(getActivity(), null);
		task.execute(key,"");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DBHelper.getInstance(getActivity()).closeDB();//关闭数据库
		if(task!=null && !task.isCancelled()){
			task.cancel(true);
		}
	}
	
	
}
