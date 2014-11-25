package com.inter.trade.ui.fragment;

import com.inter.trade.R;
import com.inter.trade.util.LoginUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AdDetialFragment extends Fragment{
	private String mUrl =null;
	private int mId;
	private static IndexFragment mFragment;
	private static final String IMAGE_DATA_EXTRA="IMAGE_DATA_EXTRA";
	private static final String DEFAULT_ID="DEFAULT_ID";
	public static AdDetialFragment newInstance(String url,IndexFragment f){
		final AdDetialFragment fragment = new AdDetialFragment();
		final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, url);
        fragment.setArguments(args);
        mFragment = f;
		return fragment;
	}
	
	public static AdDetialFragment newInstanceLocal(int id,IndexFragment f){
		final AdDetialFragment fragment = new AdDetialFragment();
		final Bundle args = new Bundle();
        args.putInt(DEFAULT_ID, id);
        fragment.setArguments(args);
        mFragment = f;
		return fragment;
	}
	
	public AdDetialFragment(){
		
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
		if(mUrl == null){
			mId = getArguments() != null ? getArguments().getInt(DEFAULT_ID) : null;
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.detial_item, container, false);
		ImageView imageView =(ImageView)view.findViewById(R.id.detial_image_item);
		if(mUrl != null){
			mFragment.getFetcher().loadImage(mUrl, imageView);
		}else{
			imageView.setBackgroundDrawable(getResources().getDrawable(mId));
		}
		
		return view;
	}
	
}
