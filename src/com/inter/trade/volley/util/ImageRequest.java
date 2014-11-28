package com.inter.trade.volley.util;

import com.inter.trade.volley.DefaultRetryPolicy;
import com.inter.trade.volley.NetworkResponse;
import com.inter.trade.volley.ParseError;
import com.inter.trade.volley.Request;
import com.inter.trade.volley.Response;
import com.inter.trade.volley.VolleyLog;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;


public class ImageRequest extends Request<Bitmap>
{
	private static final int IMAGE_TIMEOUT_MS = 1000;

	private static final int IMAGE_MAX_RETRIES = 2;

	private static final float IMAGE_BACKOFF_MULT = 2f;

	private final Response.Listener<Bitmap> mListener;
	private final Config mDecodeConfig;
	private final int mMaxWidth;
	private final int mMaxHeight;

	private static final Object sDecodeLock = new Object();

	public ImageRequest(String url, Response.Listener<Bitmap> listener,
			int maxWidth, int maxHeight, Config decodeConfig,
			Response.ErrorListener errorListener)
	{
		super(Method.GET, url, errorListener);
		setRetryPolicy(new DefaultRetryPolicy(IMAGE_TIMEOUT_MS,
				IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
		mListener = listener;
		mDecodeConfig = decodeConfig;
		mMaxWidth = maxWidth;
		mMaxHeight = maxHeight;
	}

	@Override
	public Priority getPriority()
	{
		return Priority.LOW;
	}

	private static int getResizedDimension(int maxPrimary, int maxSecondary,
			int actualPrimary, int actualSecondary)
	{
		if (maxPrimary == 0 && maxSecondary == 0)
		{
			return actualPrimary;
		}

		if (maxPrimary == 0)
		{
			double ratio = (double) maxSecondary / (double) actualSecondary;
			return (int) (actualPrimary * ratio);
		}

		if (maxSecondary == 0)
		{
			return maxPrimary;
		}

		double ratio = (double) actualSecondary / (double) actualPrimary;
		int resized = maxPrimary;
		if (resized * ratio > maxSecondary)
		{
			resized = (int) (maxSecondary / ratio);
		}
		return resized;
	}

	@Override
	protected Response<Bitmap> parseNetworkResponse(NetworkResponse response)
	{
		synchronized (sDecodeLock)
		{
			try
			{
				return doParse(response);
			}
			catch (OutOfMemoryError e)
			{
				VolleyLog.e("Caught OOM for %d byte image, url=%s",
						response.data.length, getUrl());
				return Response.error(new ParseError(e));
			}
		}
	}

	private Response<Bitmap> doParse(NetworkResponse response)
	{
		byte[] data = response.data;
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		if (mMaxWidth == 0 && mMaxHeight == 0)
		{
			decodeOptions.inPreferredConfig = mDecodeConfig;
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					decodeOptions);
		}
		else
		{
			decodeOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, decodeOptions);
			int actualWidth = decodeOptions.outWidth;
			int actualHeight = decodeOptions.outHeight;

			int desiredWidth = getResizedDimension(mMaxWidth, mMaxHeight,
					actualWidth, actualHeight);
			int desiredHeight = getResizedDimension(mMaxHeight, mMaxWidth,
					actualHeight, actualWidth);

			decodeOptions.inJustDecodeBounds = false;
			decodeOptions.inSampleSize = findBestSampleSize(actualWidth,
					actualHeight, desiredWidth, desiredHeight);
			Bitmap tempBitmap = BitmapFactory.decodeByteArray(data, 0,
					data.length, decodeOptions);

			if (tempBitmap != null
					&& (tempBitmap.getWidth() > desiredWidth || tempBitmap
							.getHeight() > desiredHeight))
			{
				bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
						desiredHeight, true);
				tempBitmap.recycle();
			}
			else
			{
				bitmap = tempBitmap;
			}
		}

		if (bitmap == null)
		{
			return Response.error(new ParseError(response));
		}
		else
		{
			return Response.success(bitmap,
					HttpHeaderParser.parseCacheHeaders(response));
		}
	}

	@Override
	protected void deliverResponse(Bitmap response)
	{
		mListener.onResponse(response);
	}

	static int findBestSampleSize(int actualWidth, int actualHeight,
			int desiredWidth, int desiredHeight)
	{
		double wr = (double) actualWidth / desiredWidth;
		double hr = (double) actualHeight / desiredHeight;
		double ratio = Math.min(wr, hr);
		float n = 1.0f;
		while ((n * 2) <= ratio)
		{
			n *= 2;
		}

		return (int) n;
	}
}
