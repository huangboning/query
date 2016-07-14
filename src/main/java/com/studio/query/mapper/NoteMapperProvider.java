package com.studio.query.mapper;

import com.studio.query.entity.Note;
import com.studio.query.util.StringUtil;

public class NoteMapperProvider {

	public static String findNote(Note note) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_note a where 1=1 ");

		if (!StringUtil.isNullOrEmpty(note.getNoteTitle())) {
			buffer.append("and a.noteTitle like concat('%',#{noteTitle},'%') ");
		}
		if (note.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (note.getBeginDate() != null && !note.getBeginDate().equals("")) {
			buffer.append("and a.note_date >= #{beginDate} ");
		}
		if (note.getEndDate() != null && !note.getEndDate().equals("")) {
			buffer.append("and a.note_date < #{endDate} ");
		}
		buffer.append(" order by a.note_date desc ");
		if (note.getOffset() == 0 && note.getLimit() == 0) {
		} else {
			buffer.append(" limit #{offset}, #{limit} ");
		}
		return buffer.toString();
	}

	public static String countNote(Note note) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select count(*) from t_note a where 1=1 ");

		if (!StringUtil.isNullOrEmpty(note.getNoteTitle())) {
			buffer.append("and a.noteTitle like concat('%',#{noteTitle},'%') ");
		}
		if (note.getAccountId() != 0) {
			buffer.append("and a.account_id = #{accountId} ");
		}
		if (note.getBeginDate() != null && !note.getBeginDate().equals("")) {
			buffer.append("and a.note_date >= #{beginDate} ");
		}
		if (note.getEndDate() != null && !note.getEndDate().equals("")) {
			buffer.append("and a.note_date < #{endDate} ");
		}
		return buffer.toString();
	}

	public static String updateNote(Note note) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("update t_note set note_content=#{noteContent} ");

		if (!StringUtil.isNullOrEmpty(note.getNoteTitle())) {
			buffer.append(",note_title=#{noteTitle} ");
		}
		buffer.append(" where note_id=#{noteId} and account_id=#{accountId} ");

		return buffer.toString();
	}
}
