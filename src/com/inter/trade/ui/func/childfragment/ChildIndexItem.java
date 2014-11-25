package com.inter.trade.ui.func.childfragment;

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
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.ui.func.FunctionFragment;
import com.inter.trade.ui.func.task.CountTask;
import com.inter.trade.ui.manager.UIManagerActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.Constants;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.PromptUtil;
import com.inter.trade.util.VersionUtil;

public class ChildIndexItem extends Fragment implements OnItemClickListener {

	private GridView mGridView;

	public static ImageFetcher mImageFetcher;
	private int mImageThumbSize;
	public static final String FUNDATA = "FUNDATA";
	private static final String IMAGE_DATA_EXTRA = "IMAGE_DATA_EXTRA";
	private ArrayList<FuncData> mDatas;

	private CountTask task;//添加点击记录的异步任务
	
	public static ChildIndexItem create(ArrayList<FuncData> mList) {
		final ChildIndexItem f = new ChildIndexItem();

		final Bundle args = new Bundle();
		args.putSerializable(FUNDATA, mList);
		f.setArguments(args);
		return f;
	}

	public ChildIndexItem() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initFetcher();
		View v = inflater.inflate(R.layout.index_item, container, false);
		
		if(getArguments()!=null){
			mDatas=(ArrayList<FuncData>) getArguments().getSerializable(FUNDATA);
		}
		

		mGridView = (GridView) v.findViewById(R.id.func_grid);
		mGridView.setAdapter(new ChildIndexAdapter(getActivity(), mImageFetcher,
				mDatas));
		mGridView.setOnItemClickListener(this);

		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		task =new ItemClickHanlder().handleOnclickItem(getActivity(),arg2,mDatas);
	}

	
	

	private void initFetcher() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		final int width = displayMetrics.widthPixels;
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(),
				FUNDATA);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		//DBHelper.getInstance(getActivity()).closeDB();//关闭数据库
		if(task!=null && !task.isCancelled()){
			task.cancel(true);
		}
	}
	
	
}
