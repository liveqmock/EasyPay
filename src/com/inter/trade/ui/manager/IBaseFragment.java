package com.inter.trade.ui.manager;

import com.inter.trade.ui.manager.core.IMainHandlerManager;
import com.inter.trade.ui.manager.core.UIConstantDefault;
import com.inter.trade.util.LoginTimeoutUtil;
import com.inter.trade.util.LoginUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * 基础的Fragment
 * @author zhichao.huang
 *
 */
abstract public class IBaseFragment extends Fragment {
	
	
	protected static final int SWIPING_START = 1;
	protected static final int SWIPING_FAIL = 2;
	protected static final int SWIPING_SUCCESS = 3;
	
	private View rootView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//屏蔽输入框一进入页面弹出输入键盘
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		LoginUtil.detection(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(rootView == null) {
			rootView = onInitView(inflater, container, savedInstanceState);
			onAsyncLoadData();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();  
        if (parent != null) {  
            parent.removeView(rootView);  
        }  
		
//        onInitDatas ();
		return rootView;
	}
	
	/**
	 * 子类继承初始化view,(带缓存)
	 * @return
	 */
	abstract protected View onInitView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	
	
	/**
	 * 异步加载数据（第一次装载Fragment会调用onAsyncLoadData()；
	 * 当前Fragment跳到新的Fragment，新的Fragment回到当前Fragment，onAsyncLoadData()不会被调用）
	 */
	abstract protected void onAsyncLoadData() ;
	
//	/**
//	 * 子类继承初始化数据,如获取上一个页面返回来的值（一般情况配合onRefreshDatas()一起使用）
//	 */
//	abstract protected void onInitDatas () ;
	
	/**
	 * 子类继承刷新数据
	 * 运用场景：
	 * 如当前fragment跳到选城市的fragment，选完城市后保存数据，
	 * 回到当前fragment会调用onRefreshDatas ()刷新数据。
	 */
	abstract public void onRefreshDatas () ;

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(LoginTimeoutUtil.get().isTimeout()) {//是超时
			onTimeout ();//启动超时处理
			LoginTimeoutUtil.get().cleanTimeoutState();//关闭超时
		}
		
		if(getActivity() == null) return;//防止手机内存不足，再次getActivity()获取数据出现空指针问题
		onRefreshDatas ();
	}
	
	/**
	 * 超时处理
	 */
	abstract public void onTimeout () ;
	
	/**
	 * 添加Fragment到堆栈
	 * @param uiarg UI标签，用于控制添加哪个fragement 
	 * @param isAddToBackStack 用于标识fragement是否添加到堆栈，0：不添加到堆栈；1：添加到堆栈
	 * @param data
	 */
	public void addFragmentToStack(int uiarg, int isAddToBackStack, Bundle data) {
		IMainHandlerManager.handlerUI(uiarg, isAddToBackStack, data);
	}
	
	/**
	 * 从堆栈中移除Fragment
	 */
	public void removeFragmentToStack() {
		((UIManagerActivity)getActivity()).removeFragmentToStack();
	}
	
	/**
	 * 返回首页
	 */
	public void backHomeFragment() {
		((UIManagerActivity)getActivity()).backHomeFragment();
	}

}
