/**
 * 
 */
package com.inter.trade.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inter.trade.data.Group;
import com.inter.trade.data.SunType;

/**
 * @author LiGuohui
 * @since 2012-12-4 下午1:08:24
 * @version 1.0.0
 */
public interface Parser<T extends SunType> {
	public abstract T parse(JSONObject json) throws JSONException;
    @SuppressWarnings("rawtypes")
	public Group parse(JSONArray array) throws JSONException;
}
