package com.studio.query.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.studio.query.util.CacheUtil;

public class Configure implements ServletContextListener {
	public static String gitRepositoryPath;
	public static String systemEmail;
	public static String rootPath;
	public static String imagePath;
	public static String htmlPath;
	public static String uploadPath;

	public static String esBootstrapServiceUrl;
	public static String esQueryServiceUrl;
	public static String esHintServiceUrl;
	public static String esGeocodingServiceUrl;
	public static String esTalbleServiceUrl;

	public static String systemSessionAccount = "zq_query_account";
	public static String systemSessionUser = "zq_query_user";

	public static boolean isDevelopment;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Properties prop = new Properties();
		InputStream in = arg0.getServletContext().getResourceAsStream("/WEB-INF/classes/query.properties");
		try {
			prop.load(in);
			gitRepositoryPath = prop.getProperty("account.repository.root.path").trim();
			systemEmail = prop.getProperty("account.email.default").trim();
			isDevelopment = Boolean.valueOf(prop.getProperty("server.development").trim());
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		rootPath = arg0.getServletContext().getRealPath("/");
		imagePath = rootPath + "/images/";
		htmlPath = rootPath + "/html/";
		uploadPath = rootPath + "/upload";

		in = arg0.getServletContext().getResourceAsStream("/WEB-INF/classes/es.properties");
		try {
			prop.load(in);
			esBootstrapServiceUrl = prop.getProperty("esBootstrapServiceUrl").trim();
			esQueryServiceUrl = prop.getProperty("esQueryServiceUrl").trim();
			esHintServiceUrl = prop.getProperty("esHintServiceUrl").trim();
			esGeocodingServiceUrl = prop.getProperty("esGeocodingServiceUrl").trim();
			esTalbleServiceUrl = prop.getProperty("esTalbleServiceUrl").trim();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isDevelopment) {
			// 测试初始化数据源
			CacheUtil cacheUtil = new CacheUtil();
			String testPath = arg0.getServletContext().getRealPath("/") + "WEB-INF/classes/source.txt";
			cacheUtil.getIndexDocTypes(testPath);
			cacheUtil.getIndexDocTypesOrigin(testPath);
		} else {
			// 正式环境
			CacheUtil cacheUtil = new CacheUtil();
			cacheUtil.initIndexDocTypes();
		}
	}

}
