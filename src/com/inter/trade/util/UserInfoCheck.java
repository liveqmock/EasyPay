package com.inter.trade.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class UserInfoCheck {
	
	public static boolean checkBandCid(String bandCid) {
		 System.out.println("checkBandCid---1 ");
//		String regexBandCid = "^(^\\d{16}$|^\\d{19}$)$";
//
//		if (!Pattern.matches(regexBandCid, bandCid) || bandCid == null)
//			return false;
//		
//		return luhn(bandCid);
		return  true;
	}
	
	
   private static boolean luhn(String bandCid){
       int s1 = 0, s2 = 0;
       String reverse = new StringBuffer(bandCid).reverse().toString();
       
       System.out.println("reverse = " + reverse);
       
       for(int i = 0 ;i < reverse.length();i++){
           int digit = Character.digit(reverse.charAt(i), 10);
           if(i % 2 == 0){//this is for odd digits, they are 1-indexed in the algorithm
               s1 += digit;
           }else{//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
               s2 += 2 * digit;
               if(digit >= 5){
                   s2 -= 9;
               }
           }
       }
       return (s1 + s2) % 10 == 0;
   }
   
   /**
    * 校验银行卡卡号
    * @param cardId
    * @return
    */
   public static boolean checkBankCard(String cardId) {
   		 char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
   		 if(bit == 'N'){
   			 return false;
   		 }
   		 return cardId.charAt(cardId.length() - 1) == bit;
   }
  
   /**
    * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
    * @param nonCheckCodeCardId
    * @return
    */
   public static char getBankCardCheckCode(String nonCheckCodeCardId){
       if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
               || !nonCheckCodeCardId.matches("\\d+")) {
       	//如果传的不是数据返回N
           return 'N';
       }
       char[] chs = nonCheckCodeCardId.trim().toCharArray();
       int luhmSum = 0;
       for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
           int k = chs[i] - '0';
           if(j % 2 == 0) {
               k *= 2;
               k = k / 10 + k % 10;
           }
           luhmSum += k;           
       }
       return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
   }

    public static boolean checkIdentity(String cid){
    	   if(checkCid(cid) ==0){
    		   return true;
    	   }
    	   return false;
    }
    
	public static int checkCid(String cid) {
		/**
		 * 0：合法 1：非法格式 2：非法地区 3：非法生日 4：非法校验
		 * */

		String[] areas = new String[] { null, null, null, null, null, null, null, null, null, null, null, "北京", "天津", "河北", "山西", "内蒙古", null, null,
				null, null, null, "辽宁", "吉林", "黑龙江", null, null, null, null, null, null, null, "上海", "江苏", "浙江", "安微", "福建", "江西", "山东", null, null,
				null, "河南", "湖北", "湖南", "广东", "广西", "海南", null, null, null, "重庆", "四川", "贵州", "云南", "西藏", null, null, null, null, null, null, "陕西",
				"甘肃", "青海", "宁夏", "新疆", null, null, null, null, null, "台湾", null, null, null, null, null, null, null, null, null, "香港", "澳门", null,
				null, null, null, null, null, null, null, "国外", null, null, null, null, null, null, null, null };
	
		String regexCid = "^(^\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";

		if (Pattern.matches(regexCid, cid)) {
			// 地区
			int areacode = Integer.parseInt(cid.substring(0, 2));
			if (areas[areacode] == null)
				return 2;
			System.out.println("area = " + areas[areacode]);
			// 生日
			String dateStrSrc = null;
			if (cid.length() == 18) {
				dateStrSrc = cid.substring(6, 6 + 8);
			} else {
				dateStrSrc = 19 + cid.substring(6, 6 + 6);
			}
			String dateStr = dateStrSrc.substring(0, 4) + "-" + dateStrSrc.substring(4, 6) + "-" + dateStrSrc.substring(6, 8);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			try {
				Date date = format.parse(dateStr);
				if(date==null)
					return 3;
				System.out.println("birthday = " + date.toString());		
			} catch (ParseException e) {
				e.printStackTrace();
				return 3;
			}
			
//			DateFormat dateFormat = DateFormat.getDateInstance();
//			dateFormat.setLenient(false);
//			try {
//				Date date = dateFormat.parse(dateStr);
//				System.out.println("---------------------3");
//				if (date == null)
//					return 3;
//				System.out.println("birthday = " + date.toString());
//			} catch (ParseException e) {
//				return 3;
//			}
			// 校验位比对
			if (cid.length() == 18) {
				byte[] wi = new byte[17];
				for (int i = 0; i < wi.length; i++) {
					int k = (int) Math.pow(2, (wi.length - i));
					wi[i] = (byte) (k % 11);
				}

				int sum = 0;
				// 进行加权求和
				for (int i = 0; i < 17; i++) {
					sum += Integer.parseInt(cid.substring(i, i + 1)) * wi[i];
				}
				// 取模运算，得到模值
				byte code = (byte) (sum % 11);
				String checkCode = "10X98765432";
				String check = checkCode.substring(code, code + 1);
				System.out.println("check = " + check);
				if (!cid.substring(17, 18).equalsIgnoreCase(check)) {
					return 4;
				}
			}
			// 男女
			if (cid.length() == 18) {
				System.out.println(cid.substring(16, 17));
				if (Integer.parseInt(cid.substring(16, 17)) % 2 == 1) {
					System.out.println("sex = " + "男");
				} else {
					System.out.println("sex = " + "女");
				}
			} else {
				System.out.println(cid.substring(13, 14));
				if (Integer.parseInt(cid.substring(13, 14)) % 2 == 1) {
					System.out.println("sex = " + "男");
				} else {
					System.out.println("sex = " + "女");
				}
			}
		} else {
			return 1;
		}

		return 0;
	}

	public static boolean checkMobilePhone(String mobilePhone) {
		String[] mobilePhoneStarts = { "106|移动", "130|联通", "131|联通", "132|联通", "133|电信", "134|移动", "135|移动", "136|移动", "137|移动", "138|移动", "139|移动",
				"145|联通", "147|移动", "150|移动","151|移动", "152|移动", "153|电信", "155|联通", "156|联通", "157|移动", "158|移动", "159|移动","180|电信", "181|电信", "182|移动", "183|移动",
				"184|移动", "185|联通", "186|联通", "187|移动", "188|移动", "189|电信" };

		String regex1MobilePhone = "^\\d{11}$";
		String regex2MobilePhone = "^(86)\\d{11}$";
		String regex3MobilePhone = "^(\\+86)\\d{11}$";

		if (Pattern.matches(regex1MobilePhone, mobilePhone)) {
			for (String mobilePhoneStart : mobilePhoneStarts) {
				if (mobilePhoneStart.substring(0, 3).equalsIgnoreCase(mobilePhone.substring(0, 3))) {
					return true;
				}
			}
		}

		if (Pattern.matches(regex2MobilePhone, mobilePhone)) {
			for (String mobilePhoneStart : mobilePhoneStarts) {
				if (mobilePhoneStart.substring(0, 3).equalsIgnoreCase(mobilePhone.substring(2, 5))) {
					return true;
				}
			}
		}

		if (Pattern.matches(regex3MobilePhone, mobilePhone)) {
			for (String mobilePhoneStart : mobilePhoneStarts) {
				if (mobilePhoneStart.substring(0, 3).equalsIgnoreCase(mobilePhone.substring(3, 6))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkName(String name) {
//		String regexName = "^[\u4e00-\u9fa5]([\u4e00-\u9fa5]|[a-zA-Z]){1,6}$";
		String regexName = "^[\u4e00-\u9fa5]{1,6}$";
		if (Pattern.matches(regexName, name)) {
			return true;
		}
		return false;
	}
	
	public static boolean checkPassword(String password){
		
		String regexPassword = "^[a-zA-Z0-9_]{6,20}$";
		if (Pattern.matches(regexPassword, password) && password != null)
			return true;
		
		return false;
	}
	
	public static boolean checkEmail(String email){
		String regexEmail = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

		if (Pattern.matches(regexEmail, email) && email != null)
			return true;
		
		return false;
	}
}
