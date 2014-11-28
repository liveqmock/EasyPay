package com.inter.trade.volley;

public interface Network
{

	public NetworkResponse performRequest(Request<?> request)
			throws VolleyError;
}
