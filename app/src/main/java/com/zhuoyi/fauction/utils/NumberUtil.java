package com.zhuoyi.fauction.utils;

public class NumberUtil {
	public static String number2Wan(String number){
		int numberInt = Integer.parseInt(number);
		if(numberInt<10000){
			return numberInt+"元";
		}else{
			int wangNum=numberInt/10000;
			return wangNum+"万元";
		}
	}
	public static String number2Wan(Integer number){
		
		if(number<10000){
			return number+"元";
		}else{
			int wangNum=number/10000;
			return wangNum+"万元";
		}
	}
}
