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
 * @since 2012-12-4 下午11:16:49
 * @version 1.0.0
 */
public abstract class AbstractParser<T extends SunType> implements Parser<T> {

	@Override
	public abstract T parse(JSONObject json) throws JSONException ;
		// TODO Auto-generated method stub

	@Override
	public Group parse(JSONArray array) throws JSONException {
		// TODO Auto-generated method stub
		throw new JSONException("Unexpected JSONArray parse type encountered.");
	}
	

}
