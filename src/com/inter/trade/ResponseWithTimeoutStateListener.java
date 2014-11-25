/*
 * @Title:  ResponseMoreStateListener.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月15日 下午6:07:38
 * @version:  V1.0
 */
package com.inter.trade;

import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;

/**
 * 添加更多的状态
 * @author  chenguangchi
 * @data:  2014年9月15日 下午6:07:38
 * @version:  V1.0
 */
public interface ResponseWithTimeoutStateListener extends ResponseMoreStateListener{
	public void onTimeOut(Object obj,Class cla);
}
