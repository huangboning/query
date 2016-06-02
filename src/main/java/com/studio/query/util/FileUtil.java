package com.studio.query.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class FileUtil {

	public static void writeFile(String filePath, String content) {
		try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath), "utf-8");
			out.write(content);
			out.flush();
			out.close();
			// FileWriter fw = new FileWriter(filePath);
			// PrintWriter out = new PrintWriter(fw);
			// out.write(content);
			// fw.close();
			// out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFile(String path) {
		// File file = new File(path);
		BufferedReader reader = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				strBuffer.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return strBuffer.toString();
	}
}
