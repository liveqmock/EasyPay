package com.inter.trade.data;

public class ResponseData implements SunType{
	
	private String req_seq;
	private String ope_seq;
	private String rettype;
	private String retcode;
	private String retmsg;
	private String mac;
	private String au_token;//动态码
	private String req_bkenv;//银联调用环境
	private String req_token;//授权码
	
	
	public String getReq_token() {
		return req_token;
	}
	public void setReq_token(String req_token) {
		this.req_token = req_token;
	}
	public String getReq_bkenv() {
		return req_bkenv;
	}
	public void setReq_bkenv(String req_bkenv) {
		this.req_bkenv = req_bkenv;
	}
	public String getAu_token() {
		return au_token;
	}
	public void setAu_token(String au_token) {
		this.au_token = au_token;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getReq_seq() {
		return req_seq;
	}
	public void setReq_seq(String req_seq) {
		this.req_seq = req_seq;
	}
	public String getOpe_seq() {
		return ope_seq;
	}
	public void setOpe_seq(String ope_seq) {
		this.ope_seq = ope_seq;
	}
	public String getRettype() {
		return rettype;
	}
	public void setRettype(String rettype) {
		this.rettype = rettype;
	}
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	
	
}
