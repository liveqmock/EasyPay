package com.inter.trade.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class XmlUtil
{
	/**
	 * 将HashMap对象转换为XML格式字符串,带XML命名空间
	 * 
	 * @author hzx
	 * @param rootTag
	 * @param map
	 * @return
	 */
	public static String parseXmlToStr(String rootTag,
			HashMap<String, String> map)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		sb.append("<" + rootTag + ">");
		if ((null != map) && !map.isEmpty())
		{
			Set<String> set = map.keySet();
			String key;
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();)
			{
				key = iterator.next();
				if (map.get(key) instanceof String)
				{
					sb.append(joinXmlTag(key, map.get(key)));
				}
			}
		}
		sb.append("</" + rootTag + ">");
		return sb.toString();
	}

	/**
	 * 将HashMap对象转换为XML格式字符串,带XML命名空间
	 * 
	 * @author hzx
	 * @param rootTag
	 * @param map
	 * @return
	 */
	public static String MapListToXmlStr(String rootTag,
			HashMap<String, Object> map)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
		sb.append("<" + rootTag + ">");
		if ((null != map) && !map.isEmpty())
		{
			Set<String> set = map.keySet();
			String key;
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();)
			{
				key = iterator.next();
				if (map.get(key) instanceof Map)
				{
					sb.append("<" + key + ">");
					Map<String, String> itemMap = (Map<String, String>) map
							.get(key);
					if ((null != itemMap) && !itemMap.isEmpty())
					{
						Set<String> itemSet = itemMap.keySet();
						String itemKey;
						for (Iterator<String> itemIterator = itemSet.iterator(); itemIterator
								.hasNext();)
						{
							itemKey = itemIterator.next();
							sb.append(joinXmlTag(itemKey, itemMap.get(itemKey)));
						}
					}
					sb.append("</" + key + ">");
				}
			}
		}
		sb.append("</" + rootTag + ">");
		return sb.toString();
	}

	/**
	 * 将HashMap对象转换为XML格式字符串，没有父节点
	 * 
	 * @author hzx
	 * @param map
	 * @return
	 */
	public static String parseXmlToNodeStr(HashMap<String, String> map)
	{
		StringBuffer sb = new StringBuffer();
		if ((null != map) && !map.isEmpty())
		{
			Set<String> set = map.keySet();
			String key;
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext();)
			{
				key = iterator.next();
				if (map.get(key) instanceof String)
				{
					sb.append(joinXmlTag(key, map.get(key)));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 组织Xml节点
	 * 
	 * @author hzx
	 * @param key
	 * @param value
	 * @return
	 */
	public static String joinXmlTag(String key, Object value)
	{
		StringBuffer sb = new StringBuffer();
		if ((null != key) && (null != value))
		{
			sb.append("<");
			sb.append(key);
			sb.append(">");
			sb.append(value);
			sb.append("</");
			sb.append(key);
			sb.append(">");
		}
		return sb.toString();
	}

}
