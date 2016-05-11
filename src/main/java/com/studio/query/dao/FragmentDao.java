package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.studio.query.entity.Fragment;
import com.studio.query.mapper.FragmentMapperProvider;

public interface FragmentDao {

	@Results(value = {
			@Result(column = "fragment_id", property = "fragmentId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "fragment_no", property = "fragmentNo"),
			@Result(column = "fragment_name", property = "fragmentName"),
			@Result(column = "fragment_type", property = "fragmentType"),
			@Result(column = "fragment_obj_type", property = "fragmentObjType"),
			@Result(column = "fragment_desc", property = "fragmentDesc"),
			@Result(column = "fragment_share", property = "fragmentShare"),
			@Result(column = "fragment_date", property = "fragmentDate") })
	@SelectProvider(type = FragmentMapperProvider.class, method = "findFragment")
	public List<Fragment> findFragment(Fragment fragment);
	@Results(value = {
			@Result(column = "fragment_id", property = "fragmentId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "account_name", property = "accountName"),
			@Result(column = "fragment_no", property = "fragmentNo"),
			@Result(column = "fragment_name", property = "fragmentName"),
			@Result(column = "fragment_type", property = "fragmentType"),
			@Result(column = "fragment_obj_type", property = "fragmentObjType"),
			@Result(column = "fragment_desc", property = "fragmentDesc"),
			@Result(column = "fragment_share", property = "fragmentShare"),
			@Result(column = "fragment_date", property = "fragmentDate") })
	@Select("select a.*,b.account_name from t_fragment a left join t_account b on (a.account_id=b.account_id) where a.fragment_share=1")
	public List<Fragment> shareFragments(Fragment fragment);
	
	@Insert("insert into t_fragment(account_id,fragment_no,fragment_name,fragment_type,fragment_obj_type,fragment_desc,fragment_share,fragment_date)"
			+ " values(#{accountId},#{fragmentNo},#{fragmentName},#{fragmentType},#{fragmentObjType},#{fragmentDesc},#{fragmentShare},now())")
	public int insertFragment(Fragment fragment);

	@Update("update t_fragment set fragment_desc=#{fragmentDesc} where fragment_no=#{fragmentNo} and account_id=#{accountId}")
	public int updateFragment(Fragment fragment);
	
	@Delete("delete from t_fragment where account_id=#{accountId}  and fragment_no=#{fragmentNo}")
	public int deleteFragment(Fragment fragment);
	
	@Update("update t_fragment set fragment_enable=0 where fragment_no=#{fragmentNo} and account_id=#{accountId}")
	public int enableFragment(Fragment fragment);
	
	@Update("update t_fragment set fragment_enable=-1 where fragment_no=#{fragmentNo} and account_id=#{accountId}")
	public int disableFragment(Fragment fragment);
	
	@Update("update t_fragment set fragment_share=1 where fragment_no=#{fragmentNo} and account_id=#{accountId}")
	public int enableShareFragment(Fragment fragment);
	
	@Update("update t_fragment set fragment_share=0 where fragment_no=#{fragmentNo} and account_id=#{accountId}")
	public int disableShareFragment(Fragment fragment);
}
