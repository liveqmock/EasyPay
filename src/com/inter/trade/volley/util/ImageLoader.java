package com.inter.trade.volley.util;

import java.util.HashMap;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.inter.trade.volley.Request;
import com.inter.trade.volley.RequestQueue;
import com.inter.trade.volley.Response.ErrorListener;
import com.inter.trade.volley.Response.Listener;
import com.inter.trade.volley.VolleyError;

public class ImageLoader
{
	private final RequestQueue mRequestQueue;

	private int mBatchResponseDelayMs = 100;

	private final ImageCache mCache;

	private final HashMap<String, BatchedImageRequest> mInFlightRequests = new HashMap<String, BatchedImageRequest>();

	private final HashMap<String, BatchedImageRequest> mBatchedResponses = new HashMap<String, BatchedImageRequest>();

	private final Handler mHandler = new Handler(Looper.getMainLooper());

	private Runnable mRunnable;

	public interface ImageCache
	{
		public Bitmap getBitmap(String url);

		public void putBitmap(String url, Bitmap bitmap);
	}

	public ImageLoader(RequestQueue queue, ImageCache imageCache)
	{
		mRequestQueue = queue;
		mCache = imageCache;
	}

	public static ImageListener getImageListener(final ImageView view,
			final int defaultImageResId, final int errorImageResId)
	{
		return new ImageListener()
		{
			@Override
			public void onErrorResponse(VolleyError error)
			{
				if (errorImageResId != 0)
				{
					view.setImageResource(errorImageResId);
				}
			}

			@Override
			public void onResponse(ImageContainer response, boolean isImmediate)
			{
				if (response.getBitmap() != null)
				{
					view.setImageBitmap(response.getBitmap());
				}
				else if (defaultImageResId != 0)
				{
					view.setImageResource(defaultImageResId);
				}
			}
		};
	}

	public interface ImageListener extends ErrorListener
	{

		public void onResponse(ImageContainer response, boolean isImmediate);
	}

	public boolean isCached(String requestUrl, int maxWidth, int maxHeight)
	{
		throwIfNotOnMainThread();

		String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);
		return mCache.getBitmap(cacheKey) != null;
	}

	public ImageContainer get(String requestUrl, final ImageListener listener)
	{
		return get(requestUrl, listener, 0, 0);
	}

	public ImageContainer get(String requestUrl, ImageListener imageListener,
			int maxWidth, int maxHeight)
	{
		throwIfNotOnMainThread();

		final String cacheKey = getCacheKey(requestUrl, maxWidth, maxHeight);

		Bitmap cachedBitmap = mCache.getBitmap(cacheKey);
		if (cachedBitmap != null)
		{
			ImageContainer container = new ImageContainer(cachedBitmap,
					requestUrl, null, null);
			imageListener.onResponse(container, true);
			return container;
		}

		ImageContainer imageContainer = new ImageContainer(null, requestUrl,
				cacheKey, imageListener);

		imageListener.onResponse(imageContainer, true);

		BatchedImageRequest request = mInFlightRequests.get(cacheKey);
		if (request != null)
		{
			request.addContainer(imageContainer);
			return imageContainer;
		}

		Request<?> newRequest = new ImageRequest(requestUrl,
				new Listener<Bitmap>()
				{
					@Override
					public void onResponse(Bitmap response)
					{
						onGetImageSuccess(cacheKey, response);
					}
				}, maxWidth, maxHeight, Config.RGB_565, new ErrorListener()
				{
					@Override
					public void onErrorResponse(VolleyError error)
					{
						onGetImageError(cacheKey, error);
					}
				});

		mRequestQueue.add(newRequest);
		mInFlightRequests.put(cacheKey, new BatchedImageRequest(newRequest,
				imageContainer));
		return imageContainer;
	}

	public void setBatchedResponseDelay(int newBatchedResponseDelayMs)
	{
		mBatchResponseDelayMs = newBatchedResponseDelayMs;
	}

	private void onGetImageSuccess(String cacheKey, Bitmap response)
	{
		mCache.putBitmap(cacheKey, response);

		BatchedImageRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null)
		{
			request.mResponseBitmap = response;

			batchResponse(cacheKey, request);
		}
	}

	private void onGetImageError(String cacheKey, VolleyError error)
	{
		BatchedImageRequest request = mInFlightRequests.remove(cacheKey);

		if (request != null)
		{
			request.setError(error);

			batchResponse(cacheKey, request);
		}
	}

	public class ImageContainer
	{

		private Bitmap mBitmap;

		private final ImageListener mListener;

		private final String mCacheKey;

		private final String mRequestUrl;

		public ImageContainer(Bitmap bitmap, String requestUrl,
				String cacheKey, ImageListener listener)
		{
			mBitmap = bitmap;
			mRequestUrl = requestUrl;
			mCacheKey = cacheKey;
			mListener = listener;
		}

		public void cancelRequest()
		{
			if (mListener == null) { return; }

			BatchedImageRequest request = mInFlightRequests.get(mCacheKey);
			if (request != null)
			{
				boolean canceled = request
						.removeContainerAndCancelIfNecessary(this);
				if (canceled)
				{
					mInFlightRequests.remove(mCacheKey);
				}
			}
			else
			{
				request = mBatchedResponses.get(mCacheKey);
				if (request != null)
				{
					request.removeContainerAndCancelIfNecessary(this);
					if (request.mContainers.size() == 0)
					{
						mBatchedResponses.remove(mCacheKey);
					}
				}
			}
		}

		public Bitmap getBitmap()
		{
			return mBitmap;
		}

		public String getRequestUrl()
		{
			return mRequestUrl;
		}
	}

	private class BatchedImageRequest
	{
		private final Request<?> mRequest;

		private Bitmap mResponseBitmap;

		private VolleyError mError;

		private final LinkedList<ImageContainer> mContainers = new LinkedList<ImageContainer>();

		public BatchedImageRequest(Request<?> request, ImageContainer container)
		{
			mRequest = request;
			mContainers.add(container);
		}

		public void setError(VolleyError error)
		{
			mError = error;
		}

		public VolleyError getError()
		{
			return mError;
		}

		public void addContainer(ImageContainer container)
		{
			mContainers.add(container);
		}

		public boolean removeContainerAndCancelIfNecessary(
				ImageContainer container)
		{
			mContainers.remove(container);
			if (mContainers.size() == 0)
			{
				mRequest.cancel();
				return true;
			}
			return false;
		}
	}

	private void batchResponse(String cacheKey, BatchedImageRequest request)
	{
		mBatchedResponses.put(cacheKey, request);
		if (mRunnable == null)
		{
			mRunnable = new Runnable()
			{
				@Override
				public void run()
				{
					for (BatchedImageRequest bir : mBatchedResponses.values())
					{
						for (ImageContainer container : bir.mContainers)
						{

							if (container.mListener == null)
							{
								continue;
							}
							if (bir.getError() == null)
							{
								container.mBitmap = bir.mResponseBitmap;
								container.mListener
										.onResponse(container, false);
							}
							else
							{
								container.mListener.onErrorResponse(bir
										.getError());
							}
						}
					}
					mBatchedResponses.clear();
					mRunnable = null;
				}

			};
			mHandler.postDelayed(mRunnable, mBatchResponseDelayMs);
		}
	}

	private void throwIfNotOnMainThread()
	{
		if (Looper.myLooper() != Looper.getMainLooper()) { throw new IllegalStateException(
				"ImageLoader must be invoked from the main thread."); }
	}

	private static String getCacheKey(String url, int maxWidth, int maxHeight)
	{
		return new StringBuilder(url.length() + 12).append("#W")
				.append(maxWidth).append("#H").append(maxHeight).append(url)
				.toString();
	}
}
