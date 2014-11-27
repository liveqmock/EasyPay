package com.inter.trade.ui.fragment.smsreceivepayment.util;

public class SmsUtil {  
	  
    public static void mainbbb() {  
        String str1 = "a-b";          
        String str2 = "a-b-";  
        String str3 = "-a-b";  
        String str4 = "-a-b-";  
        String str5 = "a";  
        String str6 = "-";  
        String str7 = "--";  
        String str8 = "";   //等同于new String()  
        String str9 = "a-";
        String str10 = "-a";
        String str11 = " a";
        String str12 = "a ";
        String str13 = " ";
        String str14 = "  ";
          
        getSplitLen(str1);  
        getSplitLen(str2);  
        getSplitLen(str3);  
        getSplitLen(str4);  
        getSplitLen(str5);  
        getSplitLen(str6);  
        getSplitLen(str7);  
        getSplitLen(str8);  
        getSplitLen(str9);  
        getSplitLen(str10);  
        getSplitLen(str11);  
        getSplitLen(str12);  
        getSplitLen(str13);  
        getSplitLen(str14);  
        
        System.out.print("\n \""+str8+"\""+"字符串长度："+str8.length()); 
        System.out.print("\n \""+str12+"\""+"字符串长度："+str12.length()); 
        System.out.print("\n \""+str13+"\""+"字符串长度："+str13.length()); 
        System.out.print("\n \""+str14+"\""+"字符串长度："+str14.length()); 
    }  
      
    public static void getSplitLen(String demo){  
        String[] array = demo.split("-");  
        int len = array.length;  
        System.out.print("\"" + demo + "\"长度为：" + len);  
        if(len >= 0){  
            for(int i=0; i<len; i++){  
                System.out.print(" \""+array[i]+"\"");  
            }             
        }  
        System.out.println();  
    }  
  
}  
