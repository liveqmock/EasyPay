package com.inter.trade.volley.util;

import java.io.File;

import com.inter.trade.volley.Network;
import com.inter.trade.volley.RequestQueue;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;


public class Volley
{

	private static final String DEFAULT_CACHE_DIR = "volley";

	public static RequestQueue newRequestQueue(Context context, HttpStack stack)
	{
		File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

		String userAgent = "volley/0";
		try
		{
			String packageName = context.getPackageName();
			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		}
		catch (NameNotFoundException e)
		{
		}

		if (stack == null)
		{
			if (Build.VERSION.SDK_INT >= 9)
			{
				stack = new HurlStack();
			}
			else
			{
				stack = new HttpClientStack(
						AndroidHttpClient.newInstance(userAgent));
			}
		}

		Network network = new BasicNetwork(stack);

		RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir),
				network);
		queue.start();

		return queue;
	}

	public static RequestQueue newRequestQueue(Context context)
	{
		return newRequestQueue(context, null);
	}
}
