/**
 * Copyright 2009 Joe LaPenna
 */

package com.inter.trade.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

/**
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public interface HttpApi {


    abstract public String doHttpPost(String url, NameValuePair... nameValuePairs)
            throws SunException,
            IOException;

    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
    
    abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary)
            throws IOException; 
}
