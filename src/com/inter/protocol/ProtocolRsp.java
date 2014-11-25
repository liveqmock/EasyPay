package com.inter.protocol;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author WuWangchun
 * @create 2012-2-28 下午2:37:45
 * @last_modify
 * @last_modify_time
 */
public class ProtocolRsp {
	public int mCode;
	public String mDetail="";
	public List<ProtocolData> mActions=new ArrayList<ProtocolData>();
	
	public static List<ProtocolData> find(String path , ProtocolRsp rsp){
		List<ProtocolData> cpds=null;
		if (rsp!=null) {
			List<ProtocolData> mActions = rsp.mActions;
			if (mActions!=null&&mActions.size()>0) {
				ProtocolData pd = mActions.get(0);
				if(pd!=null){
					cpds = pd.find(path);
				}
			}
		}
		return cpds;
	}
	
	public ProtocolData getA(String name) {
		ProtocolData aData=null;
		if (mActions!=null&&mActions.size()>0) {
			for (ProtocolData protocolData : mActions) {
				if (protocolData.mKey.compareTo(name)==0) {
					aData=protocolData;
					break;
				}
			}
		}
		return aData;
	}
	

}
