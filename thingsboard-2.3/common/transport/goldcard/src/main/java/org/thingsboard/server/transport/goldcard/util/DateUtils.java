package org.thingsboard.server.transport.goldcard.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ----------------------------------------------------------------
 * Copyright (C) 2017 金卡高科技股份有限公司
 * 版权所有。 
 * 
 * 文件名：DateUtils.java
 * 文件功能描述：时间转换工具类
 * 
 * 创建标识 1990 2017年2月23日
 * 
 * 修改标识：
 * 修改描述：
 * ----------------------------------------------------------------
*/
public class DateUtils {
	
	
	/**
	 * 时间转换
	 * @param parttern
	 * @param date
	 * @return
	 */
	public static String formatDate(DateStyleEnum parttern,Date date){	
		SimpleDateFormat sft=new SimpleDateFormat(parttern.getValue());
		return sft.format(date);
	}
	
	/**
	 * 时间字符串转换为时间
	 * @param parttern
	 * @param dateStr
	 * @return
	 */
	public static Date pareDate(DateStyleEnum parttern,String dateStr){
		SimpleDateFormat sft=new SimpleDateFormat(parttern.getValue());
		Date date=null;
		try {
		    date=sft.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

}
