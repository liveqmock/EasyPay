package com.inter.trade.data;

public class LoginStatus implements SunType{
	public ResponseData mResponseData;
	
	public String login_name;
	public String login_pwd;
	public String message;
	public String result;
	public String authorid;
	public String ispaypwd;//是否设置支付密码：0否1已设置
	public String au_token;//动态验证码
	public String agentid;//代理商ID
	public String agenttypeid;//代理商类型id, 0普通用户，1正式代理商，2虚拟代理商
	public String relateAgent;//是否绑定过代理商，0否，1已绑定过
	
	public String gesturepasswd;//手势密码
	
	public void cancel(){
		login_name=null;
		login_pwd=null;
		message=null;
		result=null;
		authorid=null;
		ispaypwd=null;
		mResponseData=null;
		gesturepasswd = null;
		agentid = null;
		agenttypeid = null;
		relateAgent = null;
	}
}
