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

	@Results(value = {
			@Result(column = "fragment_id", property = "fragmentId"),
			@Result(column = "account_id", property = "accountId"),
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
	@Results(value = {
			@Result(column = "share_fragment_id", property = "shareFragmentId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "share_fragment_uuid", property = "shareFragmentUUID"),
			@Result(column = "share_fragment_name", property = "shareFragmentName"),
			@Result(column = "share_fragment_type", property = "shareFragmentType"),
			@Result(column = "share_fragment_obj_type", property = "shareFragmentObjType"),
			@Result(column = "share_fragment_desc", property = "shareFragmentDesc"),
			@Result(column = "share_fragment_version", property = "shareFragmentVersion"),
			@Result(column = "share_fragment_date", property = "shareFragmentDate") })
	@SelectProvider(type = ShareFragmentMapperProvider.class, method = "findShareFragment")
	public List<ShareFragment> findShareFragment(ShareFragment sharefragment);
	
	@Insert("insert into t_fragment(account_id,fragment_uuid,fragment_name,fragment_type,fragment_obj_type,fragment_desc,fragment_share,fragment_date)"
			+ " values(#{accountId},#{fragmentUUID},#{fragmentName},#{fragmentType},#{fragmentObjType},#{fragmentDesc},#{fragmentShare},now())")
	public int insertFragment(Fragment fragment);

	@Update("update t_fragment set fragment_desc=#{fragmentDesc} where fragment_uuid=#{fragmentUUID} and account_id=#{accountId}")
	public int updateFragment(Fragment fragment);
	
	@Delete("delete from t_fragment where account_id=#{accountId}  and fragment_uuid=#{fragmentUUID}")
	public int deleteFragment(Fragment fragment);
	
	@Update("update t_fragment set fragment_enable=0 where fragment_uuid=#{fragmentUUID} and account_id=#{accountId}")
	public int enableFragment(Fragment fragment);
	
	@Update("update t_fragment set fragment_enable=-1 where fragment_uuid=#{fragmentUUID} and account_id=#{accountId}")
	public int disableFragment(Fragment fragment);
	
	//共享模板fragment
	@Update("update t_share_fragment set share_fragment_enable=1 where share_fragment_uuid=#{sharefragmentUUID} and account_id=#{accountId}")
	public int enableShareFragment(Fragment fragment);
	
	@Update("update t_share_fragment set share_fragment_enable=0 where share_fragment_uuid=#{sharefragmentUUID} and account_id=#{accountId}")
	public int disableShareFragment(Fragment fragment);
	
	//发布模板fragment
	@Insert("insert into t_share_fragment(account_id,share_fragment_uuid,share_fragment_name,share_fragment_type,share_fragment_obj_type,share_fragment_desc,share_fragment_enable,share_active,share_version,fragment_date)"
			+ " values(#{accountId},#{shareFragmentUUID},#{shareFragmentName},#{shareFragmentType},#{shareFragmentObjType},#{shareFragmentDesc},#{shareFragmentEnable},#{shareFragmentActive},#{shareFragmentVersion},now())")
	public int releaseFragment(ShareFragment shareFragment);
	
	@Update("update t_shareFragment set share_fragment_desc=#{shareFragmentDesc},share_fragment_version=#{shareFragmentVersion} where shareFragment_uuid=#{shareFragmentUUID} and account_id=#{accountId}")
	public int updateShareFragment(ShareFragment shareFragment);
}
