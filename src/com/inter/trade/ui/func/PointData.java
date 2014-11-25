/**
 * 
 */
package com.inter.trade.ui.func;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

import com.inter.trade.data.SunType;

/**
 * @author LiGuohui
 * @since 2013-1-3 下午11:49:59
 * @version 1.0.0
 */
public class PointData implements SunType,Serializable{
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	private boolean selected;
	private Drawable drawable;
	
}
