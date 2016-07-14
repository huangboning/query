package com.studio.query.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.studio.query.entity.Note;
import com.studio.query.mapper.NoteMapperProvider;

public interface NoteDao {
	@Results(value = { @Result(column = "note_id", property = "noteId"),
			@Result(column = "account_id", property = "accountId"),
			@Result(column = "note_title", property = "noteTitle"),
			@Result(column = "note_content", property = "noteContent"),
			@Result(column = "note_date", property = "noteDate") })
	@SelectProvider(type = NoteMapperProvider.class, method = "findNote")
	public List<Note> findNote(Note note);

	@SelectProvider(type = NoteMapperProvider.class, method = "countNote")
	public int countNote(Note note);

	@Insert("insert into t_note(account_id,note_title,note_content,note_date)values(#{accountId},#{noteTitle},#{noteContent},now())")
	public int insertNote(Note note);

	@UpdateProvider(type = NoteMapperProvider.class, method = "updateNote")
	public int updateNote(Note note);

	@Delete("delete from t_note where note_id=#{noteId} and account_id=#{accountId}")
	public int deleteNote(Note note);
}
