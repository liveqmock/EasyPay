package com.inter.protocol;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlSerializer;

public class BodyParser {
	//用户注册body转化为xml
	static public  void authorRegBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException{
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//手机号码
					if(data.mKey.equals("aumobile")){
						serializer.startTag("", "aumobile");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aumobile");
					}
					
					//登录密码
					if(data.mKey.equals("aupassword")){
						serializer.startTag("", "aupassword");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aupassword");
					}
					
					//真实姓名
					if(data.mKey.equals("autruename")){
						serializer.startTag("", "autruename");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "autruename");
					}
					
					//身份证号
					if(data.mKey.equals("auidcard")){
						serializer.startTag("", "auidcard");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auidcard");
					}
					
					//邮件地址
					if(data.mKey.equals("auemail")){
						serializer.startTag("", "auemail");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auemail");
					}
				}
			}
		}
	}
}
