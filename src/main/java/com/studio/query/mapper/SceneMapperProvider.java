package com.studio.query.mapper;

import com.studio.query.entity.Scene;
import com.studio.query.util.StringUtil;

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
		if (!StringUtil.isNullOrEmpty(scene.getSceneUUID())) {
			buffer.append("and a.scene_uuid =#{sceneUUID} ");
		}
		if (!StringUtil.isNullOrEmpty(scene.getSceneName())) {
			buffer.append("and a.scene_name =#{sceneName} ");
		}
		if(scene.getSceneEnable()!=1){
			buffer.append("and a.scene_enable =#{sceneEnable} ");
		}

		return buffer.toString();
	}
}
