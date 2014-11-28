package com.inter.trade.volley;


@SuppressWarnings("serial")
public class ParseError extends VolleyError
{
	public ParseError()
	{
	}

	public ParseError(NetworkResponse networkResponse)
	{
		super(networkResponse);
	}

	public ParseError(Throwable cause)
	{
		super(cause);
	}
}
