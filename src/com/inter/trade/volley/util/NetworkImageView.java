package com.inter.trade.volley.util;

import com.inter.trade.volley.VolleyError;
import com.inter.trade.volley.util.ImageLoader.ImageContainer;
import com.inter.trade.volley.util.ImageLoader.ImageListener;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


public class NetworkImageView extends ImageView
{
	private String mUrl;

	private int mDefaultImageId;

	private int mErrorImageId;

	private ImageLoader mImageLoader;

	private ImageContainer mImageContainer;

	public NetworkImageView(Context context)
	{
		this(context, null);
	}

	public NetworkImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public NetworkImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public void setImageUrl(String url, ImageLoader imageLoader)
	{
		mUrl = url;
		mImageLoader = imageLoader;
		loadImageIfNecessary(false);
	}

	public void setDefaultImageResId(int defaultImage)
	{
		mDefaultImageId = defaultImage;
	}

	public void setErrorImageResId(int errorImage)
	{
		mErrorImageId = errorImage;
	}

	void loadImageIfNecessary(final boolean isInLayoutPass)
	{
		int width = getWidth();
		int height = getHeight();

		boolean wrapWidth = false, wrapHeight = false;
		if (getLayoutParams() != null)
		{
			wrapWidth = getLayoutParams().width == LayoutParams.WRAP_CONTENT;
			wrapHeight = getLayoutParams().height == LayoutParams.WRAP_CONTENT;
		}

		boolean isFullyWrapContent = wrapWidth && wrapHeight;
		if (width == 0 && height == 0 && !isFullyWrapContent)
		{
			return;
		}

		if (TextUtils.isEmpty(mUrl))
		{
			if (mImageContainer != null)
			{
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			setDefaultImageOrNull();
			return;
		}

		if (mImageContainer != null && mImageContainer.getRequestUrl() != null)
		{
			if (mImageContainer.getRequestUrl().equals(mUrl))
			{
				return;
			}
			else
			{
				mImageContainer.cancelRequest();
				setDefaultImageOrNull();
			}
		}

		int maxWidth = wrapWidth ? 0 : width;
		int maxHeight = wrapHeight ? 0 : height;

		ImageContainer newContainer = mImageLoader.get(mUrl,
				new ImageListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						if (mErrorImageId != 0)
						{
							setImageResource(mErrorImageId);
						}
					}

					@Override
					public void onResponse(final ImageContainer response,
							boolean isImmediate)
					{

						if (isImmediate && isInLayoutPass)
						{
							post(new Runnable()
							{
								@Override
								public void run()
								{
									onResponse(response, false);
								}
							});
							return;
						}

						if (response.getBitmap() != null)
						{
							setImageBitmap(response.getBitmap());
						}
						else if (mDefaultImageId != 0)
						{
							setImageResource(mDefaultImageId);
						}
					}
				}, maxWidth, maxHeight);

		mImageContainer = newContainer;
	}

	private void setDefaultImageOrNull()
	{
		if (mDefaultImageId != 0)
		{
			setImageResource(mDefaultImageId);
		}
		else
		{
			setImageBitmap(null);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow()
	{
		if (mImageContainer != null)
		{
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged()
	{
		super.drawableStateChanged();
		invalidate();
	}
}
