package com.studio.query.mapper;

import com.studio.query.entity.SceneDesc;
import com.studio.query.util.StringUtil;

public class SceneDescMapperProvider {
	public static String findSceneDesc(SceneDesc sceneDesc) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_scene_desc a where 1=1 ");
		if (sceneDesc.getSceneDescId() != 0) {
			buffer.append("and a.scene_desc_id = #{sceneDescId} ");
		}
		if (sceneDesc.getSceneId() != 0) {
			buffer.append("and a.scene_id = #{sceneId} ");
		}
		if (!StringUtil.isNullOrEmpty(sceneDesc.getSceneUUID())) {
			buffer.append("and a.scene_uuid =#{sceneUUID} ");
		}
		if (!StringUtil.isNullOrEmpty(sceneDesc.getSceneVersion())) {
			buffer.append("and a.scene_version =#{sceneVersion} ");
		}
		buffer.append(" order by a.scene_desc_date asc ");
		return buffer.toString();
	}
}
