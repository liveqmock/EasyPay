/*
 * @Title:  FavourFragment.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月25日 上午10:17:20
 * @version:  V1.0
 */
package com.inter.trade.ui.func.childfragment;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.text.format.Formatter;
import android.text.method.DialerKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.IndexActivity;
import com.inter.trade.ui.MyBankCardActivity;
import com.inter.trade.ui.fragment.FragmentFactory;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.ui.func.data.Ad;
import com.inter.trade.ui.func.data.AdData;
import com.inter.trade.ui.func.task.AdListTask;
import com.inter.trade.ui.func.task.CountTask;
import com.inter.trade.ui.func.util.FormatUtil;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.view.slideplayview.AbSlidingPlayView;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 最爱的页面
 * 
 * @author ChenGuangChi
 * @data: 2014年7月25日 上午10:17:20
 * @version: V1.0
 */
public class FavourFragment extends Fragment implements OnClickListener,
		OnItemClickListener, ResponseStateListener {
	private Button btnWallet;
	private Button btnBuySwipe;
	private AbSlidingPlayView playView;
	private GridView mGridView;
	private ImageView pic;
	private ArrayList<FuncData> mData;

	private CountTask task;

	private static final String DATAS = "datas";

	private AdListTask adTask;

	private ImageLoader loader;

	private Context context;

	public static FavourFragment newInstance(ArrayList<FuncData> list) {
		FavourFragment f = new FavourFragment();
		Bundle b = new Bundle();
		b.putSerializable(DATAS, list);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ad_unload)
				// resource or drawable
				.showImageOnFail(R.drawable.ad_unload)
				// resource or drawable
				.showImageOnLoading(R.drawable.image)
				.resetViewBeforeLoading(false) // default
				.cacheInMemory(true) // default
				.cacheOnDisk(false) // default
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.build();

		File cacheDir = new File("");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity())
				.memoryCacheExtraOptions(480, 800)
				// default = device screen dimensions
				.diskCacheExtraOptions(480, 800, null)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				//.diskCacheSize(2 * 1024 * 1024).diskCacheFileCount(10)
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.defaultDisplayImageOptions(options) // default
				.writeDebugLogs().build();

		loader = ImageLoader.getInstance();
		loader.init(config);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_favour, null);
		initview(view);
		initData(inflater);
		adTask = new AdListTask(getActivity(), this);
		adTask.execute();

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	private void initData(LayoutInflater inflater) {
		if (getArguments() != null) {
			mData = (ArrayList<FuncData>) getArguments().getSerializable(DATAS);
			updateFun();
		}

	}

	private void updateFun() {
		mGridView.setAdapter(new ChildIndexAdapter(getActivity(), null, mData));
		mGridView.setOnItemClickListener(this);
	}

	private void initview(View view) {
		btnWallet = (Button) view.findViewById(R.id.btn_wallet);
		btnBuySwipe = (Button) view.findViewById(R.id.btn_buyswipe);
		playView = (AbSlidingPlayView) view.findViewById(R.id.slidingplay);
		mGridView = (GridView) view.findViewById(R.id.func_grid);
		pic=(ImageView) view.findViewById(R.id.iv_default);

		btnWallet.setOnClickListener(this);
		btnBuySwipe.setOnClickListener(this);

		playView.setPageLineHorizontalGravity(Gravity.RIGHT);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
		}
		if (adTask != null && !adTask.isCancelled()) {
			adTask.cancel(true);
		}
		if(loader!=null){
			loader.destroy();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_wallet:// 我的钱包
			//PromptUtil.showToast(getActivity(), "我的钱包功能正在开发中，敬请期待...");
			startActivity(new Intent(getActivity(), MyBankCardActivity.class));
			break;
		case R.id.btn_buyswipe:// 购买刷卡器
			Intent intent = new Intent();
			intent.putExtra(FragmentFactory.INDEX_KEY,
					FuncMap.BUY_SWIPCARD_INDEX_FUNC);
			intent.setClass(getActivity(), IndexActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		task = new ItemClickHanlder().handleOnclickItem(getActivity(),
				position, mData);
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		AdData adData = (AdData) obj;
		ArrayList<Ad> adList = adData.getAdList();
		if (adData != null) {
			for (int i = 0; i < adList.size(); i++) {

				View mPlayView = View.inflate(context, R.layout.play_view_item,
						null);
				ImageView image = (ImageView) mPlayView
						.findViewById(R.id.mPlayImage);
				playView.addView(mPlayView);
//				if (i == 0) {
//					loader.displayImage("assets://image.jpg",image);
//				} else {
					loader.displayImage(adList.get(i).getAdpicurl(), image);
				//}
			}
			playView.startPlay();
			pic.setVisibility(View.GONE);
			playView.setVisibility(View.VISIBLE);
		}
	}

}
