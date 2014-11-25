package com.inter.trade.ui.func;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.inter.trade.R;
import com.inter.trade.ui.func.childfragment.BankFragment;
import com.inter.trade.ui.func.childfragment.FavourFragment;
import com.inter.trade.ui.func.childfragment.MoreFragment;

/**
 * 新版主页
 * 
 * @author ChenGuangChi
 * @data: 2014年7月28日 下午1:12:48
 * @version: V1.0
 */
public class TwoFunctionFragment extends Fragment implements
		OnCheckedChangeListener {

	private Context context;
	private RadioGroup rgNavigation;
	private static int selected = 0;

	private static final String[] TAGS = { "favour", "bank", "convince", "more" };

	private boolean isLocal = false;// 記錄是否採用本地菜單

	private ArrayList<FuncData> favour = null;
	private ArrayList<ArrayList<FuncData>> bank = null;
	private ArrayList<ArrayList<FuncData>> convince = null;

	private FavourFragment favourFragment;
	private BankFragment bankFragment;
	private BankFragment convinceFragment;
	private MoreFragment moreFragment;

	public static TwoFunctionFragment createFragment(Bundle bundle) {
		TwoFunctionFragment fragment = new TwoFunctionFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	public TwoFunctionFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.function_layout_new_two, null);
		FrameLayout fl=(FrameLayout) view.findViewById(R.id.fl_container);
		fl.removeAllViews();
		initView(view);
		initPageFragment();
		if (savedInstanceState != null) {
			selected = savedInstanceState.getInt("selected");
		}
		return view;
	}

	private void initPageFragment() {
		Bundle b = getArguments();
		if (b != null) {
			favour = (ArrayList<FuncData>) b.getSerializable("favour");
			bank = (ArrayList<ArrayList<FuncData>>) b.getSerializable("bank");
			convince = (ArrayList<ArrayList<FuncData>>) b
					.getSerializable("convince");
			selected=b.getInt("selected",selected);
		}

		// switchContent(selected);
		rgNavigation.setOnCheckedChangeListener(TwoFunctionFragment.this);
		rgNavigation.getChildAt(selected).performClick();
	}

	private void initView(View view) {
		rgNavigation = (RadioGroup) view.findViewById(R.id.rg_navigation);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("selected", selected);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_favour:// 最爱
			selected = 0;
			if (favourFragment == null) {
				favourFragment = FavourFragment.newInstance(favour);
			}
			switchContent(favourFragment);
			break;
		case R.id.rb_bank:// 掌上银行
			selected = 1;
			if (bankFragment == null) {
				bankFragment = BankFragment.newInstance(bank);
			}
			switchContent(bankFragment);
			break;
		case R.id.rb_convince:// 便民服务
			selected = 2;
			if (convinceFragment == null) {
				convinceFragment = BankFragment.newInstance(convince);
			}
			switchContent(convinceFragment);
			break;
		case R.id.rb_more:// 更多
			selected = 3;
			if (moreFragment == null) {
				moreFragment = new MoreFragment();
			}
			switchContent(moreFragment);
			break;
		default:
			break;
		}
	}

	public void switchContent(Fragment to) {

		FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

		/* transaction.replace(R.id.fl_container, to).commit(); */

		if (currentFragment == null) {
			transaction.add(R.id.fl_container, to).commit();

			currentFragment = to;
			return;
		}

		if (currentFragment != to) {

			// to.getLoaderManager().hasRunningLoaders();
			// 先判断是否被add过
			if (!to.isAdded()) {

				// 隐藏当前的fragment，add下一个到Activity中
				transaction.hide(currentFragment).add(R.id.fl_container, to)
						.commit();
			} else {
				// 隐藏当前的fragment，显示下一个
				transaction.hide(currentFragment).show(to).commit();
			}
		}
		currentFragment = to;
	}

//	public void switchContent(int index) {
//		System.out.println("当前是:" + index);
//		FragmentManager fm = getActivity().getSupportFragmentManager();
//		FragmentTransaction ft = fm.beginTransaction();
//		for (int i = 0; i < TAGS.length; i++) {
//			Fragment temp = fm.findFragmentByTag(TAGS[i]);
//			if (temp != null) {
//				ft.hide(temp);
//			}
//		}
//		Fragment current = fm.findFragmentByTag(TAGS[index]);
//		System.out.println("current:" + current + "Page" + pageList.get(index));
//		if (current != null) {
//			ft.show(current);
//		} else {
//			ft.add(R.id.fl_container, pageList.get(index), TAGS[index]);
//		}
//		ft.commitAllowingStateLoss();
//	}

	private Fragment currentFragment = null;
}