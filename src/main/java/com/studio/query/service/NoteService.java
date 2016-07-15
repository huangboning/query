package com.studio.query.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.dao.NoteDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Note;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.DateUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class NoteService {
	@Autowired
	public NoteDao noteDao;

	public List<Note> findNote(Note note) {
		return noteDao.findNote(note);
	}

	public int countNote(Note note) {
		return noteDao.countNote(note);
	}

	public int insertNote(Note note) {
		return noteDao.insertNote(note);
	}

	public int updateNote(Note note) {
		return noteDao.updateNote(note);
	}

	public int deleteNote(Note note) {
		return noteDao.deleteNote(note);
	}

	public String doListNoteLogic(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		Note findNote = new Note();
		findNote.setAccountId(currentAccount.getAccountId());
		List<Note> noteList = noteDao.findNote(findNote);
		JSONArray noteJsonArray = new JSONArray();
		for (int i = 0; i < noteList.size(); i++) {

			Note note = noteList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("id", note.getNoteId());
			dataObj.put("title", note.getNoteTitle());
			dataObj.put("content", note.getNoteContent());
			dataObj.put("createTime", DateUtil.dateTimeFormat(note.getNoteDate()));

			noteJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_NOTE, ParameterCode.Result.RESULT_OK, "获取记事板列表成功",
				noteJsonArray.toString());

		return resultString;
	}

	public String doAddNoteLogic(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String noteTitle = parmJb.optString("title", "");
			String noteContent = parmJb.optString("content", "");

			if (StringUtil.isNullOrEmpty(noteContent)) {

				resultString = StringUtil.packetObject(MethodCode.ADD_NOTE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			Note addNote = new Note();
			addNote.setAccountId(currentAccount.getAccountId());
			addNote.setNoteTitle(noteTitle);
			addNote.setNoteContent(noteContent);
			noteDao.insertNote(addNote);

			resultString = StringUtil.packetObject(MethodCode.ADD_NOTE, ParameterCode.Result.RESULT_OK, "添加记事板成功。", "");
		}
		return resultString;
	}

	public String doUpdateNoteLogic(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String noteId = parmJb.optString("id", "");
			String noteTitle = parmJb.optString("title", "");
			String noteContent = parmJb.optString("content", "");

			if (StringUtil.isNullOrEmpty(noteId) || StringUtil.isNullOrEmpty(noteContent)) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_NOTE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			Note updateNote = new Note();
			updateNote.setNoteId(Integer.valueOf(noteId));
			updateNote.setAccountId(currentAccount.getAccountId());
			if (!StringUtil.isNullOrEmpty(noteTitle)) {
				updateNote.setNoteTitle(noteTitle);
			}
			updateNote.setNoteContent(noteContent);
			int result = noteDao.updateNote(updateNote);
			if (result != 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_NOTE, ParameterCode.Error.QUERY_NOTE_NO_EXIST,
						"更新的记事板不存在！", "");
				return resultString;
			}
			Note findNote = new Note();
			findNote.setNoteId(Integer.valueOf(noteId));
			List<Note> noteList = noteDao.findNote(findNote);
			JSONObject dataObj = new JSONObject();
			if (noteList.size() == 1) {
				Note note = noteList.get(0);
				dataObj.put("id", note.getNoteId());
				dataObj.put("title", note.getNoteTitle());
				dataObj.put("content", note.getNoteContent());
				dataObj.put("createTime", DateUtil.dateTimeFormat(note.getNoteDate()));
			}

			resultString = StringUtil.packetObject(MethodCode.UPDATE_NOTE, ParameterCode.Result.RESULT_OK, "更新记事板成功。",
					dataObj.toString());
		}
		return resultString;
	}

	public String doDeleteNoteLogic(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String noteId = parmJb.optString("id", "");

			if (StringUtil.isNullOrEmpty(noteId)) {

				resultString = StringUtil.packetObject(MethodCode.DELETE_NOTE, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			Note deleteNote = new Note();
			deleteNote.setNoteId(Integer.valueOf(noteId));
			deleteNote.setAccountId(currentAccount.getAccountId());
			int result = noteDao.deleteNote(deleteNote);
			if (result != 1) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_NOTE, ParameterCode.Error.QUERY_NOTE_NO_EXIST,
						"删除的记事板不存在！", "");
				return resultString;
			}

			resultString = StringUtil.packetObject(MethodCode.DELETE_NOTE, ParameterCode.Result.RESULT_OK, "删除记事板成功。",
					"");
		}
		return resultString;
	}
}
