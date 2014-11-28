package com.inter.trade.volley.util;

import com.inter.trade.volley.AuthFailureError;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class AndroidAuthenticator implements Authenticator
{
	private final Context mContext;
	private final Account mAccount;
	private final String mAuthTokenType;
	private final boolean mNotifyAuthFailure;

	public AndroidAuthenticator(Context context, Account account,
			String authTokenType)
	{
		this(context, account, authTokenType, false);
	}

	public AndroidAuthenticator(Context context, Account account,
			String authTokenType, boolean notifyAuthFailure)
	{
		mContext = context;
		mAccount = account;
		mAuthTokenType = authTokenType;
		mNotifyAuthFailure = notifyAuthFailure;
	}

	
	public Account getAccount()
	{
		return mAccount;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getAuthToken() throws AuthFailureError
	{
		final AccountManager accountManager = AccountManager.get(mContext);
		AccountManagerFuture<Bundle> future = accountManager.getAuthToken(
				mAccount, mAuthTokenType, mNotifyAuthFailure, null, null);
		Bundle result;
		try
		{
			result = future.getResult();
		}
		catch (Exception e)
		{
			throw new AuthFailureError("Error while retrieving auth token", e);
		}
		String authToken = null;
		if (future.isDone() && !future.isCancelled())
		{
			if (result.containsKey(AccountManager.KEY_INTENT))
			{
				Intent intent = result.getParcelable(AccountManager.KEY_INTENT);
				throw new AuthFailureError(intent);
			}
			authToken = result.getString(AccountManager.KEY_AUTHTOKEN);
		}
		if (authToken == null)
		{
			throw new AuthFailureError("Got null auth token for type: "
					+ mAuthTokenType);
		}

		return authToken;
	}

	@Override
	public void invalidateAuthToken(String authToken)
	{
		AccountManager.get(mContext).invalidateAuthToken(mAccount.type,
				authToken);
	}
}
