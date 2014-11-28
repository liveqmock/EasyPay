package com.inter.trade.volley;

@SuppressWarnings("serial")
public class VolleyError extends Exception
{
	public final NetworkResponse networkResponse;

	public VolleyError()
	{
		networkResponse = null;
	}

	public VolleyError(NetworkResponse response)
	{
		networkResponse = response;
	}

	public VolleyError(String exceptionMessage)
	{
		super(exceptionMessage);
		networkResponse = null;
	}

	public VolleyError(String exceptionMessage, Throwable reason)
	{
		super(exceptionMessage, reason);
		networkResponse = null;
	}

	public VolleyError(Throwable cause)
	{
		super(cause);
		networkResponse = null;
	}
}
