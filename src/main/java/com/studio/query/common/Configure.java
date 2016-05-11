package com.studio.query.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Configure implements ServletContextListener {
	public static String gitRepositoryPath;
	public static String systemEmail;
	public static String rootPath;
	public static String imagePath;
	public static String htmlPath;
	public static String uploadPath;
	public static String systemSessionAccount = "zq_query_account";
	public static String systemSessionUser = "zq_query_user";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Properties prop = new Properties();
		InputStream in = arg0.getServletContext().getResourceAsStream(
				"/WEB-INF/classes/query.properties");
		try {
			prop.load(in);
			gitRepositoryPath = prop
					.getProperty("account.repository.root.path").trim();
			systemEmail= prop
					.getProperty("account.email.default").trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		rootPath = arg0.getServletContext().getRealPath("/");
		imagePath = rootPath + "/images/";
		htmlPath = rootPath + "/html/";
		uploadPath = rootPath + "/upload";
	}

}
