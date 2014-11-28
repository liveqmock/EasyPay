package com.inter.trade.volley;

public class DefaultRetryPolicy implements RetryPolicy
{
	private int mCurrentTimeoutMs;

	private int mCurrentRetryCount;

	private final int mMaxNumRetries;

	private final float mBackoffMultiplier;

	public static final int DEFAULT_TIMEOUT_MS = 2500;

	public static final int DEFAULT_MAX_RETRIES = 1;

	public static final float DEFAULT_BACKOFF_MULT = 1f;

	public DefaultRetryPolicy()
	{
		this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
	}

	public DefaultRetryPolicy(int initialTimeoutMs, int maxNumRetries,
			float backoffMultiplier)
	{
		mCurrentTimeoutMs = initialTimeoutMs;
		mMaxNumRetries = maxNumRetries;
		mBackoffMultiplier = backoffMultiplier;
	}

	@Override
	public int getCurrentTimeout()
	{
		return mCurrentTimeoutMs;
	}

	@Override
	public int getCurrentRetryCount()
	{
		return mCurrentRetryCount;
	}

	@Override
	public void retry(VolleyError error) throws VolleyError
	{
		mCurrentRetryCount++;
		mCurrentTimeoutMs += (mCurrentTimeoutMs * mBackoffMultiplier);
		if (!hasAttemptRemaining())
		{
			throw error;
		}
	}

	protected boolean hasAttemptRemaining()
	{
		return mCurrentRetryCount <= mMaxNumRetries;
	}
}
