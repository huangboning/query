package com.studio.zqquery.mapper;

import com.studio.zqquery.entity.Scene;
import com.studio.zqquery.util.StringUtil;

public class SceneMapperProvider {
	public static String findScene(Scene scene) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_scene a where 1=1 ");
		if (scene.getSceneId() != 0) {
			buffer.append("and a.scene_id = #{sceneId} ");
		}
		if (scene.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (!StringUtil.isNullOrEmpty(scene.getSceneNo())) {
			buffer.append("and a.scene_no =#{sceneNo} ");
		}
		if (!StringUtil.isNullOrEmpty(scene.getSceneName())) {
			buffer.append("and a.scene_name =#{sceneName} ");
		}

		return buffer.toString();
	}
}
