package com.inter.trade.volley;

import android.content.Intent;

@SuppressWarnings("serial")
public class AuthFailureError extends VolleyError
{
	private Intent mResolutionIntent;

	public AuthFailureError()
	{
	}

	public AuthFailureError(Intent intent)
	{
		mResolutionIntent = intent;
	}

	public AuthFailureError(NetworkResponse response)
	{
		super(response);
	}

	public AuthFailureError(String message)
	{
		super(message);
	}

	public AuthFailureError(String message, Exception reason)
	{
		super(message, reason);
	}

	public Intent getResolutionIntent()
	{
		return mResolutionIntent;
	}

	@Override
	public String getMessage()
	{
		if (mResolutionIntent != null)
		{
			return "User needs to (re)enter credentials.";
		}
		return super.getMessage();
	}
}
