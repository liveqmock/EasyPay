package com.inter.trade.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 *自定义该ListView，解决把ListView放到ScrollView出现显示不完全
 *用于作为ScrollView的子view
 * @see onMeasure
 * @author zhichao.huang
 *
 */
public class IListView extends ListView {
	

	public IListView(Context context) {
		this(context , null);
	}

	public IListView(Context context, AttributeSet attrs) {
		this(context, attrs , 0);
	}


	public IListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec( 
				Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
		super.onMeasure(widthMeasureSpec, expandSpec); 
	}

}
