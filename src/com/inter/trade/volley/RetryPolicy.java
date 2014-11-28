package com.inter.trade.volley;

public interface RetryPolicy
{

	public int getCurrentTimeout();

	public int getCurrentRetryCount();

	public void retry(VolleyError error) throws VolleyError;
}
