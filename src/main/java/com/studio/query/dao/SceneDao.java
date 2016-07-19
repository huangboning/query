package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.Scene;
import com.studio.query.mapper.SceneMapperProvider;

public interface SceneDao {

	@Results(value = { @Result(column = "scene_id", property = "sceneId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "scene_uuid", property = "sceneUUID"),
			@Result(column = "scene_name", property = "sceneName"),
			@Result(column = "scene_desc", property = "sceneDesc"),
			@Result(column = "scene_comment", property = "sceneComment"),
			@Result(column = "scene_git", property = "sceneGit"),
			@Result(column = "scene_scope", property = "sceneScope"),
			@Result(column = "scene_active", property = "sceneActive"),
			@Result(column = "scene_enable", property = "sceneEnable"),
			@Result(column = "scene_date", property = "sceneDate") })
	@SelectProvider(type = SceneMapperProvider.class, method = "findScene")
	public List<Scene> findScene(Scene scene);

	@Insert("insert into t_scene(account_id,scene_uuid,scene_name,scene_desc,scene_comment,scene_git,scene_scope,scene_active,scene_enable,scene_date) values(#{accountId},#{sceneUUID},#{sceneName},#{sceneDesc},#{sceneComment},#{sceneGit},#{sceneScope},#{sceneActive},#{sceneEnable},now())")
	public int insertScene(Scene scene);

	@Update("update t_scene set scene_desc=#{sceneDesc} where scene_id=#{sceneId}")
	public int updateScene(Scene scene);
	
	@Update("update t_scene set scene_enable=-1 where scene_uuid=#{sceneUUID}")
	public int closeScene(Scene scene);
	
	@Update("update t_scene set scene_enable=0 where scene_uuid=#{sceneUUID}")
	public int openScene(Scene scene);

}
