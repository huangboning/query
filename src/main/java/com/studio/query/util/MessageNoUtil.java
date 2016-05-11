package com.studio.query.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class MessageNoUtil {
	/**
	 * 随机生成短信编号
	 * 
	 * @return
	 */
	public static String randomMessageNo() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateStr = format.format(calendar.getTime());
		dateStr = dateStr + randomCode();
		return dateStr;
	}

	public static String randomCode() {
		// String chars1 = "abcdefghijklmnopqrstuvwxyz";
		String chars2 = "1234567890";
		// String code = "yqq";
		// Random rand = new Random();
		// for (int i = 0; i < 5; i++) {
		// code = code + chars1.charAt((int) rand.nextInt(26));
		// }
		// rand = new Random();
		Random rand = new Random();
		String code = "";
		for (int i = 0; i < 3; i++) {
			code = code + chars2.charAt((int) rand.nextInt(10));
		}
		return code;
	}
	public static String randomCheckCode() {
		// String chars1 = "abcdefghijklmnopqrstuvwxyz";
		String chars2 = "1234567890";
		// String code = "yqq";
		// Random rand = new Random();
		// for (int i = 0; i < 5; i++) {
		// code = code + chars1.charAt((int) rand.nextInt(26));
		// }
		// rand = new Random();
		Random rand = new Random();
		String code = "";
		for (int i = 0; i < 5; i++) {
			code = code + chars2.charAt((int) rand.nextInt(10));
		}
		return code;
	}
}
