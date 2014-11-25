package com.inter.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author WuWangchun
 * @create 2012-2-29 上午9:45:32
 * @last_modify
 * @last_modify_time
 */
public class ProtocolData {
	public String mKey;
	public String mValue;
	public String getmValue() {
		return mValue;
	}
	public void setmValue(String mValue) {
		this.mValue = mValue;
	}

	public HashMap<String, List<ProtocolData>> mChildren=null;

	public ProtocolData(String key,String value){
		mKey=key;
		mValue=value;
	}
	public void addChild(ProtocolData child) {
		if (mChildren==null) {
			mChildren=new HashMap<String, List<ProtocolData>>();
		}
		List<ProtocolData> children = mChildren.get(child.mKey);
		if (children==null) {
			children=new ArrayList<ProtocolData>();
			mChildren.put(child.mKey, children);
		}
		children.add(child);
	}
	
	public ProtocolData getChild(String key,int index){
		List<ProtocolData> children=getChildren(key);
		if (children!=null&&children.size()>index) {
			return children.get(index);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取指定child名称的第一个对象
	 * @param key
	 * @return
	 */
	public ProtocolData getFirstChild(String key){
		return getChild(key, 0);
	}
	public List<ProtocolData> getChildren(String key){
		if (mChildren==null) {
			return null;
		}else{
			return mChildren.get(key);
		}
	}
	
	/**
	 * 取指定路径下的所有ProtocolData
	 * @param path 路径，/r_n/c_n
	 */
	public List<ProtocolData> find(String path) {
		// TODO Auto-generated method stub
		int pos=path.indexOf('/');
		String temp=path;
		if (pos==0) {  
			temp=path.substring(1);
			pos=temp.indexOf('/');
		}
		if (pos<0) {
			return getChildren(temp);
		}else if (pos>0) {
			String cur=null;
			cur=temp.substring(0, pos);
			if (mChildren!=null) {
				List<ProtocolData> res=mChildren.get(cur);
				if (res==null||res.size()==0) {
					return null;
				}else{
					temp=temp.substring(pos);
					List<ProtocolData> datas=new ArrayList<ProtocolData>();
					for (ProtocolData protocolData : res) {
						List<ProtocolData> t=protocolData.find(temp);
						if (t!=null&&t.size()>0) {
							datas.addAll(t);
						}
					}
					if (datas.size()>0) {
						return datas;
					}else{
						return null;
					}
				}
			}else{
				return null;
			}
		}else{
			return null;
		}

	}
	
	/**
	 * 取指定路径下的指定的ProtocolData
	 * @param path 路径，/r_n/c_n
	 * @param index 指定的第几个ProtocolData
	 */
	public ProtocolData find(String path,int index) {
		// TODO Auto-generated method stub
		List<ProtocolData> protocolDatas= find(path);
		if (protocolDatas!=null&&protocolDatas.size()>index) {
			return protocolDatas.get(index);
		}else{
			return null;
		}
	}
	
	/**
	 * 取指定路径下的首个ProtocolData
	 * @param path 路径，/r_n/c_n
	 */
	public ProtocolData findFirst(String path) {
		// TODO Auto-generated method stub
		return find(path, 0);
	}
}
