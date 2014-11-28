package com.inter.trade.volley;

import java.util.Collections;
import java.util.Map;

import com.inter.trade.volley.VolleyLog.MarkerLog;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;

public abstract class Request<T> implements Comparable<Request<T>>
{

	private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

	public interface Method
	{
		int DEPRECATED_GET_OR_POST = -1;
		int GET = 0;
		int POST = 1;
		int PUT = 2;
		int DELETE = 3;
		int HEAD = 4;
		int OPTIONS = 5;
		int TRACE = 6;
		int PATCH = 7;
	}

	private final MarkerLog mEventLog = MarkerLog.ENABLED ? new MarkerLog()
			: null;

	private final int mMethod;

	private final String mUrl;
	private final int mDefaultTrafficStatsTag;

	private final Response.ErrorListener mErrorListener;

	private Integer mSequence;

	private RequestQueue mRequestQueue;

	private boolean mShouldCache = true;

	private boolean mCanceled = false;

	private boolean mResponseDelivered = false;

	private long mRequestBirthTime = 0;

	private static final long SLOW_REQUEST_THRESHOLD_MS = 3000;

	private RetryPolicy mRetryPolicy;

	private Cache.Entry mCacheEntry = null;

	private Object mTag;

	@Deprecated
	public Request(String url, Response.ErrorListener listener)
	{
		this(Method.DEPRECATED_GET_OR_POST, url, listener);
	}

	public Request(int method, String url, Response.ErrorListener listener)
	{
		mMethod = method;
		mUrl = url;
		mErrorListener = listener;
		setRetryPolicy(new DefaultRetryPolicy());

		mDefaultTrafficStatsTag = findDefaultTrafficStatsTag(url);
	}

	public int getMethod()
	{
		return mMethod;
	}

	public Request<?> setTag(Object tag)
	{
		mTag = tag;
		return this;
	}

	public Object getTag()
	{
		return mTag;
	}

	public int getTrafficStatsTag()
	{
		return mDefaultTrafficStatsTag;
	}

	private static int findDefaultTrafficStatsTag(String url)
	{
		if (!TextUtils.isEmpty(url))
		{
			Uri uri = Uri.parse(url);
			if (uri != null)
			{
				String host = uri.getHost();
				if (host != null) { return host.hashCode(); }
			}
		}
		return 0;
	}

	public Request<?> setRetryPolicy(RetryPolicy retryPolicy)
	{
		mRetryPolicy = retryPolicy;
		return this;
	}

	public void addMarker(String tag)
	{
		if (MarkerLog.ENABLED)
		{
			mEventLog.add(tag, Thread.currentThread().getId());
		}
		else if (mRequestBirthTime == 0)
		{
			mRequestBirthTime = SystemClock.elapsedRealtime();
		}
	}

	void finish(final String tag)
	{
		if (mRequestQueue != null)
		{
			mRequestQueue.finish(this);
		}
		if (MarkerLog.ENABLED)
		{
			final long threadId = Thread.currentThread().getId();
			if (Looper.myLooper() != Looper.getMainLooper())
			{
				Handler mainThread = new Handler(Looper.getMainLooper());
				mainThread.post(new Runnable()
				{
					@Override
					public void run()
					{
						mEventLog.add(tag, threadId);
						mEventLog.finish(this.toString());
					}
				});
				return;
			}

			mEventLog.add(tag, threadId);
			mEventLog.finish(this.toString());
		}
		else
		{
			long requestTime = SystemClock.elapsedRealtime()
					- mRequestBirthTime;
			if (requestTime >= SLOW_REQUEST_THRESHOLD_MS)
			{
				VolleyLog.d("%d ms: %s", requestTime, this.toString());
			}
		}
	}

	public Request<?> setRequestQueue(RequestQueue requestQueue)
	{
		mRequestQueue = requestQueue;
		return this;
	}

	public final Request<?> setSequence(int sequence)
	{
		mSequence = sequence;
		return this;
	}

	public final int getSequence()
	{
		if (mSequence == null) { throw new IllegalStateException(
				"getSequence called before setSequence"); }
		return mSequence;
	}

	public String getUrl()
	{
		return mUrl;
	}

	public String getCacheKey()
	{
		return getUrl();
	}

	public Request<?> setCacheEntry(Cache.Entry entry)
	{
		mCacheEntry = entry;
		return this;
	}

	public Cache.Entry getCacheEntry()
	{
		return mCacheEntry;
	}

	public void cancel()
	{
		mCanceled = true;
	}

	public boolean isCanceled()
	{
		return mCanceled;
	}

	public Map<String, String> getHeaders() throws AuthFailureError
	{
		return Collections.emptyMap();
	}

	@Deprecated
	protected Map<String, String> getPostParams() throws AuthFailureError
	{
		return getParams();
	}

	@Deprecated
	protected String getPostParamsEncoding()
	{
		return getParamsEncoding();
	}

	@Deprecated
	public String getPostBodyContentType()
	{
		return getBodyContentType();
	}

	@Deprecated
	public byte[] getPostBody() throws AuthFailureError
	{

		Map<String, String> postParams = getPostParams();
		if (postParams != null && postParams.size() > 0) { return encodeParameters(
				postParams, getPostParamsEncoding()); }
		return null;
	}

	protected Map<String, String> getParams() throws AuthFailureError
	{
		return null;
	}

	protected String getParamsEncoding()
	{
		return DEFAULT_PARAMS_ENCODING;
	}

	public String getBodyContentType()
	{
		return "application/x-www-form-urlencoded; charset="
				+ getParamsEncoding();
	}

	public byte[] getBody() throws AuthFailureError
	{
		Map<String, String> params = getParams();
		if (params != null && params.size() > 0) { return encodeParameters(
				params, getParamsEncoding()); }
		return null;
	}

	private byte[] encodeParameters(Map<String, String> params,
			String paramsEncoding)
	{
		StringBuilder encodedParams = new StringBuilder();
		byte[] secur = null;
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			encodedParams.append(entry.getValue());
			String content = encodedParams.toString();
			String index = entry.getKey();
			byte[] data = content.getBytes();
			secur = new byte[data.length + 1];
			secur[0] = index.getBytes()[0];
			System.arraycopy(data, 0, secur, 1, data.length);
		}
		return secur;
	}

	public final Request<?> setShouldCache(boolean shouldCache)
	{
		mShouldCache = shouldCache;
		return this;
	}

	public final boolean shouldCache()
	{
		return mShouldCache;
	}

	public enum Priority
	{
		LOW, NORMAL, HIGH, IMMEDIATE
	}

	public Priority getPriority()
	{
		return Priority.NORMAL;
	}

	public final int getTimeoutMs()
	{
		return mRetryPolicy.getCurrentTimeout();
	}

	public RetryPolicy getRetryPolicy()
	{
		return mRetryPolicy;
	}

	public void markDelivered()
	{
		mResponseDelivered = true;
	}

	public boolean hasHadResponseDelivered()
	{
		return mResponseDelivered;
	}

	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	protected VolleyError parseNetworkError(VolleyError volleyError)
	{
		return volleyError;
	}

	abstract protected void deliverResponse(T response);

	public void deliverError(VolleyError error)
	{
		if (mErrorListener != null)
		{
			mErrorListener.onErrorResponse(error);
		}
	}

	@Override
	public int compareTo(Request<T> other)
	{
		Priority left = this.getPriority();
		Priority right = other.getPriority();

		return left == right ? this.mSequence - other.mSequence : right
				.ordinal() - left.ordinal();
	}

	@Override
	public String toString()
	{
		String trafficStatsTag = "0x"
				+ Integer.toHexString(getTrafficStatsTag());
		return (mCanceled ? "[X] " : "[ ] ") + getUrl() + " " + trafficStatsTag
				+ " " + getPriority() + " " + mSequence;
	}
}
