package com.studio.zqquery.util;

import java.io.File;
import java.io.FileWriter;

public class FileUtil {

	public static void updateFile(String filePath, String content) {
		try {
			File file = new File(filePath);
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
