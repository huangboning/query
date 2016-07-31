package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.Scene;
import com.studio.query.entity.SceneDesc;
import com.studio.query.mapper.SceneDescMapperProvider;

public interface SceneDescDao {

	@Results(value = { @Result(column = "scene_desc_id", property = "sceneDescId"),
			@Result(column = "scene_id", property = "sceneId"),
			@Result(column = "scene_uuid", property = "sceneUUID"),
			@Result(column = "scene_version", property = "sceneVersion"),
			@Result(column = "scene_desc", property = "sceneDesc"),
			@Result(column = "scene_desc_date", property = "sceneDescDate") })
	@SelectProvider(type = SceneDescMapperProvider.class, method = "findSceneDesc")
	public List<SceneDesc> findSceneDesc(SceneDesc sceneDesc);

	@Insert("insert into t_scene_desc(scene_id,scene_uuid,scene_version,scene_desc,scene_desc_date) values(#{sceneId},#{sceneUUID},#{sceneVersion},#{sceneDesc},now())")
	public int insertSceneDesc(SceneDesc sceneDesc);

	@Update("update t_scene_desc set scene_desc=#{sceneDesc} where scene_id=#{sceneId} and scene_version=#{sceneVersion}")
	public int updateScene(Scene scene);
}
