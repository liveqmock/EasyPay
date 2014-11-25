/*
 * @Title:  ModleData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月4日 下午5:10:18
 * @version:  V1.0
 */
package com.inter.trade.ui.func.data;

import java.util.ArrayList;

/**
 * 功能菜单
 * @author  ChenGuangChi
 * @data:  2014年7月4日 下午5:10:18
 * @version:  V1.0
 */
public class ModleData {
		private String version;
		/**1代表有新功能菜单 0代表没有新更新*/
		private String isnew;
		private ArrayList<IconData> iconList;
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public String getIsnew() {
			return isnew;
		}
		public void setIsnew(String isnew) {
			this.isnew = isnew;
		}
		public ArrayList<IconData> getIconList() {
			return iconList;
		}
		public void setIconList(ArrayList<IconData> iconList) {
			this.iconList = iconList;
		}
}
