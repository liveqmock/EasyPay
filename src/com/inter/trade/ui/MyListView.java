package com.inter.trade.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.MyListView.OnScrollOverListener;

public class MyListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 4;

	private LayoutInflater inflater;

	private LinearLayout headView;

	/**
	 * 底部布局
	 */
	private View mFooterView;
	private TextView mFooterTextView;
	private View mFooterLoadingView;
	private boolean mIsFetchMoreing; // 是否获取更多中
	private boolean mIsPullUpDone; // 是否回推完成
	private boolean mEnableAutoFetchMore; // 是否允许自动获取更多
	private boolean mIsAllowPull = true; // 是否允许下拉事件

	public void setIsAllowPull(boolean flag) {
		this.mIsAllowPull = flag;
	}

	private OnPullDownListener mOnPullDownListener;

	public void setIsFetchMoreing(boolean flag) {
		mIsFetchMoreing = flag;
	}

	public void setEnableAutoFetchMore(boolean flag) {
		mEnableAutoFetchMore = flag;
	}

	public void setFooterViewVisiblitiy(int visibility) {
		mFooterView.setVisibility(visibility);
	}

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	public MyListView(Context context) {
		super(context);
		init(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnPullDownListener(OnPullDownListener listener) {
		mOnPullDownListener = listener;
	}

	public View getFooterView() {
		return mFooterView;
	}

	private void init(Context context) {
		// setCacheColorHint(context.getResources().getColor(R.color.transparent));
		inflater = LayoutInflater.from(context);

		headView = (LinearLayout) inflater.inflate(R.layout.head, null);

		arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:"
				+ headContentHeight);

		addHeaderView(headView, null, false);

		mFooterView = LayoutInflater.from(context).inflate(
				R.layout.pulldown_footer, null);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pulldown_footer_text);
		mFooterLoadingView = mFooterView
				.findViewById(R.id.pulldown_footer_loading);
		mOnPullDownListener = new OnPullDownListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMore() {
				// TODO Auto-generated method stub

			}
		};
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if(!mIsFetchMoreing){
				// mIsFetchMoreing = true;
				// mFooterTextView.setText("加载更多中...");
				// mFooterLoadingView.setVisibility(View.VISIBLE);
				// mOnPullDownListener.onMore();
				// }
			}
		});
		addFooterView(mFooterView);
		mFooterView.setClickable(false);
		mFooterView.setEnabled(false);
		mFooterLoadingView.setVisibility(View.GONE);
		setOnScrollListener(this);
		setFooterViewVisiblitiy(View.GONE);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	public void setProgressGone() {
		mFooterLoadingView.setVisibility(View.GONE);
		mFooterView.setVisibility(View.GONE);
	}
	public void setLastText(){
		mFooterTextView.setText("已加载至最后一条");
		mFooterLoadingView.setVisibility(View.VISIBLE);
		mFooterView.setVisibility(View.VISIBLE);
		mFooterLoadingView.setVisibility(View.GONE);
	}
	/**
	 * 刷新事件接口
	 */
	public interface OnPullDownListener {
		void onRefresh();

		void onMore();
	}

	/**
	 * 条目是否填满整个屏幕
	 */
	private boolean isFillScreenItem() {
		final int firstVisiblePosition = this.getFirstVisiblePosition();
		final int lastVisiblePostion = this.getLastVisiblePosition()
				- this.getFooterViewsCount();
		final int visibleItemCount = lastVisiblePostion - firstVisiblePosition
				+ 1;
		final int totalItemCount = this.getCount() - this.getFooterViewsCount();

		if (visibleItemCount <= totalItemCount)
			return true;
		return false;
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,
			int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
	private boolean isUp=false;
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable && mIsAllowPull) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startY = (int) event.getY();
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v(TAG, "在down时候记录当前位置‘");
				}
				isUp = false;
				break;

			case MotionEvent.ACTION_UP:
				isUp=true;
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();

						Log.v(TAG, "由下拉刷新状态，到done状态");
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

						Log.v(TAG, "由松开刷新状态，到done状态");
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				/**
				 * 滑动底部刷新
				 */
				try {
					final int childCount = getChildCount();
					final int itemCount = getAdapter().getCount()
							- mBottomPosition;
					if (childCount == 0)
						return super.onTouchEvent(event);
					final int deltaY = tempY - startY;
					Logger.d("Listview", "deltaY"+deltaY+"   tempY "+tempY+"   startY "+startY);
					final int lastBottom = getChildAt(childCount - 1)
							.getBottom();
					final int end = getHeight() - getPaddingBottom();
					final int firstVisiblePosition = getFirstVisiblePosition();
					// DLog.d("lastBottom=%d end=%d deltaY=%d", lastBottom, end,
					// deltaY);
					if (firstVisiblePosition + childCount >= itemCount
							&& lastBottom <= end && deltaY < -50) {
						Logger.d("Listview", "load more");
						final boolean isHandleOnListViewBottomAndPullDown;
						isHandleOnListViewBottomAndPullDown = mOnScrollOverListener
								.onListViewBottomAndPullUp(deltaY);
						if (isHandleOnListViewBottomAndPullDown) {
							startY = tempY;
							return true;
						}
					}

					if (!isRecored && firstItemIndex == 0) {
						Log.v(TAG, "在move时候记录下位置");
						isRecored = true;
						startY = tempY;
					}

					if (state != REFRESHING && isRecored && state != LOADING) {

						// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

						// 可以松手去刷新了
						if (firstVisiblePosition == 0
								&& state == RELEASE_To_REFRESH) {

							// setSelection(0);

							// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
							if (((tempY - startY) / RATIO < headContentHeight)
									&& (tempY - startY) > 0) {
								state = PULL_To_REFRESH;
								changeHeaderViewByState();

								Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
							}
							// 一下子推到顶了
							else if (tempY - startY <= 0) {
								state = DONE;
								changeHeaderViewByState();

								Log.v(TAG, "由松开刷新状态转变到done状态");
							}
							// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
							else {
								// 不用进行特别的操作，只用更新paddingTop的值就行了
							}
						}
						// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
						if (state == PULL_To_REFRESH) {

							// setSelection(0);

							// 下拉到可以进入RELEASE_TO_REFRESH的状态
							if ((tempY - startY) / RATIO >= headContentHeight) {
								state = RELEASE_To_REFRESH;
								isBack = true;
								changeHeaderViewByState();

								Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
							}
							// 上推到顶了
							else if (tempY - startY <= 0) {
								state = DONE;
								changeHeaderViewByState();

								Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
							}
						}

						// done状态下
						if (state == DONE) {
							if (tempY - startY > 0) {
								state = PULL_To_REFRESH;
								changeHeaderViewByState();
							}
						}

						// 更新headView的size
						if (state == PULL_To_REFRESH) {
							headView.setPadding(0, -1 * headContentHeight
									+ (tempY - startY) / RATIO, 0, 0);

						}

						// 更新headView的paddingTop
						if (state == RELEASE_To_REFRESH) {
							headView.setPadding(0, (tempY - startY) / RATIO
									- headContentHeight, 0, 0);
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			}
		}

		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("更新于:" + date);
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开刷新");

			Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

				tipsTextview.setText("下拉刷新");
			} else {
				tipsTextview.setText("下拉刷新");
			}
			Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow_down);
			tipsTextview.setText("下拉刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态，done");
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		state = DONE;
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("更新于:" + date);
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
		String date = format.format(new Date());
		lastUpdatedTextView.setText("更新于:" + date);
		super.setAdapter(adapter);
	}

	/**
	 * *************************************************************************
	 * *****************
	 */
	// 以下增加滑动到底部刷新

	private int mTopPosition;
	private int mBottomPosition;

	public void setLoadText(String textString) {
		mFooterTextView.setText(textString);
	}

	/** 空的 */
	private OnScrollOverListener mOnScrollOverListener = new OnScrollOverListener() {

		@Override
		public boolean onListViewTopAndPullDown(int delta) {

			return false;
		}

		@Override
		public boolean onListViewBottomAndPullUp(int delta) {
			if (!mEnableAutoFetchMore || mIsFetchMoreing)
				return false;
			// 数量充满屏幕才触发
			if (isFillScreenItem()) {
				setFooterViewVisiblitiy(View.VISIBLE);
				mIsFetchMoreing = true;
				mFooterTextView.setText("加载更多中...");
				mFooterLoadingView.setVisibility(View.VISIBLE);
				mOnPullDownListener.onMore();
				return true;
			}
			return false;
		}

		@Override
		public boolean onMotionDown(MotionEvent ev) {
			return false;
		}

		@Override
		public boolean onMotionMove(MotionEvent ev, int delta) {
			return false;
		}

		@Override
		public boolean onMotionUp(MotionEvent ev) {
			return false;
		}

	};

	/**
	 * 滚动监听接口</br>
	 * 
	 * @see ScrollOverListView#setOnScrollOverListener(OnScrollOverListener)
	 * 
	 */
	public interface OnScrollOverListener {

		/**
		 * 到达最顶部触发
		 * 
		 * @param delta
		 *            手指点击移动产生的偏移量
		 * @return
		 */
		boolean onListViewTopAndPullDown(int delta);

		/**
		 * 到达最底部触发
		 * 
		 * @param delta
		 *            手指点击移动产生的偏移量
		 * @return
		 */
		boolean onListViewBottomAndPullUp(int delta);

		/**
		 * 手指触摸按下触发，相当于{@link MotionEvent#ACTION_DOWN}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionDown(MotionEvent ev);

		/**
		 * 手指触摸移动触发，相当于{@link MotionEvent#ACTION_MOVE}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionMove(MotionEvent ev, int delta);

		/**
		 * 手指触摸后提起触发，相当于{@link MotionEvent#ACTION_UP}
		 * 
		 * @return 返回true表示自己处理
		 * @see View#onTouchEvent(MotionEvent)
		 */
		boolean onMotionUp(MotionEvent ev);

	}

	/**
	 * 可以自定义其中一个条目为头部，头部触发的事件将以这个为准，默认为第一个
	 * 
	 * @param index
	 *            正数第几个，必须在条目数范围之内
	 */
	public void setTopPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setTopPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Top position must > 0");

		mTopPosition = index;
	}

	/**
	 * 可以自定义其中一个条目为尾部，尾部触发的事件将以这个为准，默认为最后一个
	 * 
	 * @param index
	 *            倒数第几个，必须在条目数范围之内
	 */
	public void setBottomPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setBottonPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Bottom position must > 0");

		mBottomPosition = index;
	}

	/**
	 * 设置这个Listener可以监听是否到达顶端，或者是否到达低端等事件</br>
	 * 
	 * @see OnScrollOverListener
	 */
	public void setOnScrollOverListener(
			OnScrollOverListener onScrollOverListener) {
		mOnScrollOverListener = onScrollOverListener;
	}
}
