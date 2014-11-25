package com.inter.trade.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.util.LoginTimeoutUtil;
import com.inter.trade.util.LoginUtil;

/**
 * 基本Fragment 
 * @author  chenguangchi
 * @data:  2014年9月5日 下午4:47:16
 * @version:  V1.0
 */
public abstract class IFragment extends Fragment {

	protected static final int SWIPING_START = 1;
	protected static final int SWIPING_FAIL = 2;
	protected static final int SWIPING_SUCCESS = 3;

	/**
	 * 返回按钮
	 */
	public Button backButton;

	/**
	 * 标题栏 右侧按钮
	 */
	public Button title_right_btn, title_right_icon;

	/**
	 * 标题
	 */
	public TextView title_name;
	
	public RelativeLayout rlBack;

	private View rootView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 屏蔽输入框一进入页面弹出输入键盘
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// LoginUtil.detection(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LoginUtil.detection(getActivity());
		View base = inflater.inflate(R.layout.fragment_my, null);
		FrameLayout flContainer = (FrameLayout) base
				.findViewById(R.id.fl_container);

		setBackButton(base);

		title_name = (TextView) base.findViewById(R.id.title_name);
		title_right_btn = (Button) base.findViewById(R.id.title_right_btn);
		title_right_icon = (Button) base.findViewById(R.id.title_right_btn_two);

		if (rootView == null) {
			rootView = onInitView(inflater, container, savedInstanceState);
			onAsyncLoadData();
		}
		
		ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }  
		
		
		flContainer.removeAllViews();
		flContainer.addView(rootView);

		// onInitDatas ();
		return base;
	}
	
	/**
	 * 设置返回按钮 
	 * @param base
	 * @throw
	 * @return void
	 */
	protected void setBackButton(View base) {
		backButton = (Button) base.findViewById(R.id.back_btn);
		if(backButton!=null)
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeFragmentToStack();
			}
		});
		
		rlBack=(RelativeLayout) base.findViewById(R.id.rl_back);
		rlBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeFragmentToStack();
			}
		});
	}

	/**
	 * 子类继承初始化view,(带缓存)
	 * 
	 * @return
	 */
	abstract protected View onInitView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	/**
	 * 异步加载数据（第一次装载Fragment会调用onAsyncLoadData()；
	 * 当前Fragment跳到新的Fragment，新的Fragment回到当前Fragment，onAsyncLoadData()不会被调用）
	 */
	abstract protected void onAsyncLoadData();

	// /**
	// * 子类继承初始化数据,如获取上一个页面返回来的值（一般情况配合onRefreshDatas()一起使用）
	// */
	// abstract protected void onInitDatas () ;

	/**
	 * 子类继承刷新数据 运用场景： 如当前fragment跳到选城市的fragment，选完城市后保存数据，
	 * 回到当前fragment会调用onRefreshDatas ()刷新数据。
	 */
	abstract public void onRefreshDatas();

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (LoginTimeoutUtil.get().isTimeout()) {// 是超时
			onTimeout();// 启动超时处理
			LoginTimeoutUtil.get().cleanTimeoutState();// 关闭超时
		}

		if (getActivity() == null)
			return;// 防止手机内存不足，再次getActivity()获取数据出现空指针问题
		onRefreshDatas();
	}

	/**
	 * 超时处理
	 */
	abstract public void onTimeout();

	/**
	 * 添加Fragment到堆栈
	 * 
	 * @param uiarg
	 *            UI标签，用于控制添加哪个fragement
	 * @param isAddToBackStack
	 *            用于标识fragement是否添加到堆栈，0：不添加到堆栈；1：添加到堆栈
	 * @param data
	 */
	public void addFragmentToStack(int uiarg, int isAddToBackStack, Bundle data) {
		IMainHandlerManager.handlerUI(uiarg, isAddToBackStack, data);
	}

	/**
	 * 从堆栈中移除Fragment
	 */
	abstract public void removeFragmentToStack();

	/**
	 * 返回首页
	 */
	abstract public void backHomeFragment();

	/**
	 * 设置顶部标题
	 * 
	 * @param title
	 */
	public void setTopTitle(String title) {
		if (title_name != null) {
			title_name.setText(title);
		}
	}

	/**
	 * 设置返回按钮监听
	 * 
	 * @param listener
	 *            ,如为null,返回按钮默认操作是removeFragmentToStack();
	 */
	public void setBackButtonOnClickListener(OnClickListener listener) {
		if (backButton != null && listener != null) {
			backButton.setOnClickListener(listener);
		}

		if (backButton != null && listener == null) {
			backButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					removeFragmentToStack();
				}
			});
		}
		if(rlBack!=null && listener == null){
			rlBack.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removeFragmentToStack();
				}
			});
		}
	}

	/**
	 * 设置顶部右侧按钮监听
	 * 
	 * @param title
	 * @param isVisible
	 * @param listener
	 */
	public void setRightButtonOnClickListener(String title, int isVisible,
			OnClickListener listener) {
		setRightButtonIconOnClickListener(View.GONE, null);
		if (title_right_btn == null || listener == null)
			return;
		if (title != null && !title.equals("")) {
			title_right_btn.setText(title);
		}
		title_right_btn.setVisibility(isVisible);
		title_right_btn.setOnClickListener(listener);
	}

	/**
	 * 设置顶部右侧按钮图标监听
	 * 
	 * @param isVisible
	 * @param listener
	 */
	public void setRightButtonIconOnClickListener(int isVisible,
			OnClickListener listener) {
		title_right_btn.setVisibility(View.GONE);
		title_right_icon.setVisibility(isVisible);
		title_right_icon.setOnClickListener(listener);
	}

}
