package com.inter.trade.ui.fragment.salaryget;

import android.view.View;
import android.view.View.OnClickListener;

import com.inter.trade.ui.activity.IFragment;
import com.inter.trade.ui.activity.SalaryGetMainActivity;
import com.inter.trade.ui.activity.SalaryPayMainActivity;
import com.inter.trade.ui.manager.core.UIConstantDefault;

/**
 * 签收工资模块的基本Fragment 
 * @author  chenguangchi
 * @data:  2014年9月5日 下午4:47:16
 * @version:  V1.0
 */
public abstract class SalaryGetBaseFragment extends IFragment {
	
	
	@Override
	public void setRightButtonIconOnClickListener(int isVisible,
			OnClickListener listener) {
		title_right_icon.setVisibility(View.GONE);
		title_right_btn.setVisibility(isVisible);
		title_right_btn.setText("历史记录");
		title_right_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFragmentToStack(UIConstantDefault.UI_CONSTANT_SALARYGET_HISTORY, 1, null);
			}
		});
	}

	@Override
	public void removeFragmentToStack() {
		((SalaryPayMainActivity) getActivity()).removeFragmentToStack();
	}

	@Override
	public void backHomeFragment() {
		((SalaryPayMainActivity) getActivity()).backHomeFragment();
	}
	
	
}
