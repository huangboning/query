package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.Fragment;
import com.studio.query.entity.ShareFragment;
import com.studio.query.mapper.FragmentMapperProvider;
import com.studio.query.mapper.ShareFragmentMapperProvider;

public interface FragmentDao {

	@Results(value = { @Result(column = "fragment_id", property = "fragmentId"),
			@Result(column = "scene_id", property = "sceneId"),
			@Result(column = "fragment_uuid", property = "fragmentUUID"),
			@Result(column = "fragment_name", property = "fragmentName"),
			@Result(column = "fragment_type", property = "fragmentType"),
			@Result(column = "fragment_obj_type", property = "fragmentObjType"),
			@Result(column = "fragment_desc", property = "fragmentDesc"),
			@Result(column = "fragment_enable", property = "fragmentEnable"),
			@Result(column = "fragment_active", property = "fragmentActive"),
			@Result(column = "fragment_date", property = "fragmentDate") })
	@SelectProvider(type = FragmentMapperProvider.class, method = "findFragment")
	public List<Fragment> findFragment(Fragment fragment);

	@Results(value = { @Result(column = "share_fragment_id", property = "shareFragmentId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "share_fragment_uuid", property = "shareFragmentUUID"),
			@Result(column = "share_fragment_name", property = "shareFragmentName"),
			@Result(column = "share_fragment_type", property = "shareFragmentType"),
			@Result(column = "share_fragment_obj_type", property = "shareFragmentObjType"),
			@Result(column = "share_fragment_desc", property = "shareFragmentDesc"),
			@Result(column = "share_fragment_version", property = "shareFragmentVersion"),
			@Result(column = "share_fragment_git", property = "shareFragmentGit"),
			@Result(column = "share_fragment_date", property = "shareFragmentDate") })
	@SelectProvider(type = ShareFragmentMapperProvider.class, method = "findShareFragment")
	public List<ShareFragment> findShareFragment(ShareFragment sharefragment);

	@Insert("insert into t_fragment(scene_id,fragment_uuid,fragment_name,fragment_type,fragment_obj_type,fragment_desc,fragment_enable,fragment_active,fragment_date)"
			+ " values(#{sceneId},#{fragmentUUID},#{fragmentName},#{fragmentType},#{fragmentObjType},#{fragmentDesc},#{fragmentEnable},#{fragmentActive},now())")
	public int insertFragment(Fragment fragment);

	@Update("update t_fragment set fragment_name=#{fragmentName},fragment_type=#{fragmentType},fragment_desc=#{fragmentDesc} where fragment_uuid=#{fragmentUUID}")
	public int updateFragment(Fragment fragment);

	@Delete("delete from t_fragment where fragment_uuid=#{fragmentUUID}")
	public int deleteFragment(Fragment fragment);

	@Update("update t_fragment set fragment_enable=0 where fragment_uuid=#{fragmentUUID}")
	public int enableFragment(Fragment fragment);

	@Update("update t_fragment set fragment_enable=-1 where fragment_uuid=#{fragmentUUID}")
	public int disableFragment(Fragment fragment);

	// 共享模板fragment
	@Update("update t_share_fragment set share_fragment_enable=1 where share_fragment_uuid=#{sharefragmentUUID} and account_id=#{accountId}")
	public int enableShareFragment(Fragment fragment);

	@Update("update t_share_fragment set share_fragment_enable=0 where share_fragment_uuid=#{sharefragmentUUID} and account_id=#{accountId}")
	public int disableShareFragment(Fragment fragment);

	// 发布模板fragment
	@Insert("insert into t_share_fragment(account_id,share_fragment_uuid,share_fragment_name,share_fragment_type,share_fragment_obj_type,share_fragment_desc,share_fragment_enable,share_fragment_active,share_fragment_version,share_fragment_git,share_fragment_date)"
			+ " values(#{accountId},#{shareFragmentUUID},#{shareFragmentName},#{shareFragmentType},#{shareFragmentObjType},#{shareFragmentDesc},#{shareFragmentEnable},#{shareFragmentActive},#{shareFragmentVersion},#{shareFragmentGit},now())")
	public int releaseFragment(ShareFragment shareFragment);

	// 更新模板fragment
	@Update("update t_share_fragment set share_fragment_name=#{shareFragmentName},share_fragment_type=#{shareFragmentType},share_fragment_desc=#{shareFragmentDesc},share_fragment_version=#{shareFragmentVersion} where share_fragment_uuid=#{shareFragmentUUID} and account_id=#{accountId}")
	public int updateShareFragment(ShareFragment shareFragment);
}
