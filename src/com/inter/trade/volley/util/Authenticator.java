package com.inter.trade.volley.util;

import com.inter.trade.volley.AuthFailureError;


public interface Authenticator
{

	public String getAuthToken() throws AuthFailureError;

	public void invalidateAuthToken(String authToken);
}
