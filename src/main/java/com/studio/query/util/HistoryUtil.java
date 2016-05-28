package com.studio.query.util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.studio.query.common.Configure;

import net.sf.json.JSONObject;

public class HistoryUtil {

	public static void createUserHistory(String userName) {

		try {
			File root = new File(Configure.gitRepositoryPath);
			if (!root.exists()) {
				root.mkdir();
			}
			root = new File(Configure.gitRepositoryPath + "/" + "history");
			if (!root.exists()) {
				root.mkdir();
			}
			root = new File(Configure.gitRepositoryPath + "/" + "history" + "/" + userName);
			if (!root.exists()) {
				root.mkdir();
			}

			// 创建用户场景历史文件和版本历史文件
			root = new File(Configure.gitRepositoryPath + "/" + "history" + "/" + userName + "/sceneHistory.txt");
			if (!root.exists()) {
				root.createNewFile();
				PrintWriter out = new PrintWriter(root);
				out.write("{}");
				out.close();
			}
			root = new File(Configure.gitRepositoryPath + "/" + "history" + "/" + userName + "/versionHistory.txt");
			if (!root.exists()) {
				root.createNewFile();
				PrintWriter out = new PrintWriter(root);
				out.write("{}");
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setUserSceneHistory(String userName, String content) {
		try {
			FileWriter fw = new FileWriter(
					Configure.gitRepositoryPath + "/" + "history" + "/" + userName + "/sceneHistory.txt");
			PrintWriter out = new PrintWriter(fw);
			out.write(content);
			fw.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getUserSceneHistory(String userName) {
		String sceneUUID = "";
		try {
			String str = FileUtil
					.readFile(Configure.gitRepositoryPath + "/" + "history" + "/" + userName + "/sceneHistory.txt");
			JSONObject sceneObj = JSONObject.fromObject(str);
			sceneUUID = sceneObj.optString("sceneUUID", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sceneUUID;
	}

}
