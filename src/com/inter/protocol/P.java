/**
 * 
 */
package com.inter.protocol;

/**
 * @author WuWangchun
 * @since 2012-4-5 下午3:23:21
 */
public class P {
	// 返回xml错误码定义
	public static final int XML_SUCCESS = 0;// 成功。
	public static final int XML_PROCESS_ERROR = 1001;// 处理请求消息异常。
	public static final int XML_FATCH_DATA_ERROR = 1002;// 无法获取关联数据。
	public static final int XML_NO_CLOUD_SERVICE = 1003;// 无法提供云服务。
	public static final int XML_BAD_REQ = 1004;// 非法请求。

	// 二进制流错误码定义binary
	public static final int BINARY_SUCCESS = 48;// 成功。
	public static final int BINARY_PROCESS_ERROR = 1;// 处理请求消息异常。
	public static final int BINARY_NO_CLOUD_SERVICE = 8;// 无法提供云服务。
	public static final int BINARY_BAD_REQ = 9;// 非法请求。

	// 常用的r节点名称
	public static final String normal_r = "info";

	public static class a_name {
		public static final String uwa = "uwa";// 白名单激活接口
		public static final String uac = "uac";// 短信验证码获取接口
		public static final String uia = "uia";// 检测短信激活接口
		public static final String ucp = "ucp";// 获取用户手机号码归属地

		public static final String vhl = "vhl";// 获取热点病毒列表接口
		public static final String vhn = "vhn";//获取最新热点病毒列表接口
		public static final String vhd = "vhd";// 获取病毒详情接口
		public static final String vcl = "vcl";// 获取云查杀病毒库接口
		public static final String vcs = "vcs";// 后台定时扫描接口

		public static final String psi = "psi";// 客户端升级检测接口,注意只有2.0版本用psi1,其他版本还是用psi
		public static final String pnli = "pnli";// 号码归属地库升级检测接口
		public static final String psli = "psli1";// 智能拦截规则升级检测接口
		public static final String pvli = "pvli";// 本地库,本地引擎升级检测接口
		public static final String pnl = "pnl";// 号码归属地库升级接口
		public static final String psl = "psl1";// 智能拦截规则库升级接口
		public static final String pvk = "pvk";// 本地杀毒引擎升级接口
		public static final String pvl = "pvl";// 本地病毒库升级接口
		public static final String phb = "phb";// 心跳检测接口
		public static final String pus = "pus";// 覆盖安装软件返回信息接口
		public static final String pum = "pum";// 精品软件请求接口
		public static final String pwli = "pwli1";//默认白名单拦截规则升级检测接口
		public static final String pwl = "pwl1";//默认白名单拦截规则库升级接口

		public static final String lfb = "lfb";// 用户反馈接口
		public static final String lkv = "lkv";// 病毒清除结果反馈接口
		public static final String lim = "lim";// 垃圾短信举报接口
		public static final String lom = "lom";// 漏拦短信举报接口
		public static final String lip = "lip";// 骚扰电话举报接口

		public static final String css = "css";// 获取服务器检测短信接口
		public static final String cis = "cis";// 检测终端屏蔽10086短信接口
		public static final String cls = "cls";// 云拦截设置接口
		public static final String cla = "cla";// 获取云拦截信息接口
		public static final String ced = "ced";// 获取恶意订购信息接口

		public static final String crf = "crf";// 获取流量校正接口
		public static final String caf = "caf";// 获取2G/3G流量校正接口
		
		public static final String lml = "lml";//记录客户端访问精品软件的请求情况
		public static final String lmm = "lmm";//6.4.12客户端下载MM精品软件的情况
		public static final String psri = "psri";//短信内容白规则升级库检测接口
		public static final String psr = "psr";//短信内容白规则库升级接口
		public static final String ltp = "ltp";//话费保镖设置状态接口
		public static final String lac = "lac";//手机安全先锋2.0激活反馈移动系统状态接口
		public static final String luc = "luc";//终端功能点击使用统计同步接口
		public static final String ltn ="ltn";//话费保镖开启用户数接口
		public static final String lcu ="lcu";//网址检测/二维码扫描回传接口
	}
	public static final class ltp{
		public static final String r_info="info";
		public static final class req{
			public static final String info="info";
			public static final String s = "s";
			public static final String t ="t";
		}
		public static final class rsp{
			
		}
	}
	public static final class psi {
		public static final String r_info = "info";

		public static final class req {
			public static final class info {
				public static final String ver = "ver";
			}
		}

		public static final class rsp {
			public static final class info {
				public static final String ver = "v";
				public static final String state = "s";
				public static final String size = "l";
				public static final String url = "a";
				public static final String forceUpdate = "f";
				public static final String type = "t";
			}
			public static final class tip{
				public static final String discription = "d";
			}
		}
	}

	public static final class file_update {

		public static final class req {
			public static final class info {
				public static final String ver = "v";
			}
		}

		public static final class rsp {
			public static final class info {
				public static final String has_new = "s";
				public static final String ver = "v";
				public static final String names = "f";
			}
		}

	}

	public static final class file_download {

		public static final class req {
			public static final class info {
				public static final String filename = "f";
			}
		}
	}
	public static final class lml{
		public static final class req{
			public static final class info{
				public static final String OWN_COUNT = "o";
				public static final String MM_COUNT = "m";
			}
		}
		public static final class rsp{
			public static final class info{
				public static final String RESULT = "s";
			}
		}
	}
	
	public static final class lmm{
		public static final class req{
			public static final class info{
				public static final String NAME = "n";
				public static final String VERSION = "v";
				public static final String LENGTH = "l";
				public static final String TIME = "t";
				public static final String TYPE = "w";
			}
		}
		public static final class rsp{
			public static final class info{
				public static final String RESULT = "s";
			}
		}
	}
}
