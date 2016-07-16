package com.studio.query.test;

import java.util.ArrayList;
import java.util.List;

import com.studio.query.util.FileUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {
	// public void oldParseTemplateVariableList(String jsonString) {
	// try {
	// JSONObject expJo = new JSONObject();
	// JSONArray expressArray = new JSONArray();
	// if (jsonString == null) {
	// // jsonString = FileUtil.readFile("E://query3.txt");
	// return;
	// }
	// expJo = JSONObject.fromObject(jsonString);
	// try {
	// expressArray = expJo.getJSONArray("expressions");
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// // System.out.println(expressArray.size());
	//
	// String dataType = expJo.optString("dataType", "");
	// if (!StringUtil.isNullOrEmpty(dataType) && dataType.equals("variable")) {
	// JSONObject variableJo = expJo.getJSONObject("variable");
	// String variableInstanceId = variableJo.optString("variableInstanceId",
	// "");
	// templateVariableList.add(variableInstanceId);
	// } else {
	// for (int i = 0; i < expressArray.size(); i++) {
	// expJo = expressArray.getJSONObject(i);
	// oldParseTemplateVariableList(expJo.toString());
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// // System.out.println("varList size="+varList.size());
	// }

	JSONObject testObj = new JSONObject();
	public List testList = new ArrayList<>();

	public void testTemplateVariableList(String jsonString, String pid) {
		// String str="";
		try {
			JSONObject expJo = new JSONObject();
			JSONArray expressArray = new JSONArray();
			if (jsonString == null) {
				jsonString = FileUtil.readFile("E://test.txt");
				// return;
			}
			expJo = JSONObject.fromObject(jsonString);
			try {
				expressArray = expJo.getJSONArray("user");
			} catch (Exception e) {
				// TODO: handle exception
			}
			// System.out.println(expressArray.size());

			String userName = expJo.optString("name", "");
			System.out.println(userName);
			String id = StringUtil.createVariableUUID();
			expJo.put("id", id);
			expJo.put("pid", pid);
			System.out.println("id=" + id + ",pid=" + pid);
			testList.add(expJo);
			if (expressArray.size() > 0) {
				for (int i = 0; i < expressArray.size(); i++) {
					expJo = expressArray.getJSONObject(i);
					testTemplateVariableList(expJo.toString(), id);
				}
			}
			// str=expJo.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(str);
		// return str;
		// System.out.println("varList size="+varList.size());
	}

	public void testNode(List<Dept> alist, Dept pDept) {
		// List<JSONObject> nodeList = new ArrayList<JSONObject>();
		for (Dept dept : alist) {
			if (dept.getPid().equals(pDept.getId())) {
				pDept.getSubDept().add(dept);
				testNode(alist, dept);
			}
		}
	}

	public void testVar(String jsonString, String pid) {
		try {
			JSONObject expJo = new JSONObject();
			JSONArray expressArray = new JSONArray();
			expJo = JSONObject.fromObject(jsonString);
			try {
				expressArray = expJo.getJSONArray("user");
			} catch (Exception e) {
			}
			String id = StringUtil.createVariableUUID();
			expJo.put("id", id);
			expJo.put("pid", pid);
			testList.add(expJo);
			if (expressArray.size() > 0) {
				for (int i = 0; i < expressArray.size(); i++) {
					expJo = expressArray.getJSONObject(i);
					testTemplateVariableList(expJo.toString(), id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// FragmentService t = new FragmentService();
		// String jsonString = FileUtil.readFile("E://query3.txt");
		// JSONObject expJo = JSONObject.fromObject(jsonString);
		// t.testbb(expJo);
		// System.out.println(expJo.toString());

		// t.testTemplateVariableList(jsonString, "root");
		// t.testNode(alist, dept);
		// System.out.println(JSONObject.fromObject(dept).toString());

		// for (int i = 0; i < t.testList.size(); i++) {
		// System.out.println(t.testList.get(i));
		// }
		// List<JSONObject> nodeList = t.createMenuTree(t.testList);
		// System.out.println(nodeList);
		// JSONObject originObj =
		// JSONObject.fromObject("{\"name\":\"hbn\",\"info\":{\"age\":10}}");
		// JSONObject agObj = originObj.getJSONObject("info");
		// agObj.put("age", 20);
		// originObj.put("name", "ca");
		// System.out.println(agObj.opt("age"));
		// System.out.println(originObj.toString());
		// System.out.println(originObj.size());

		// List<Dept> alist=new ArrayList<>();
		// Dept dept=new Dept();
		// dept.setName("hbn");
		// dept.setId("0");
		// dept.setPid("root");
		//
		// Dept dept1=new Dept();
		// dept1.setName("hbn1");
		// dept1.setId("1");
		// dept1.setPid("0");
		//
		// Dept dept2=new Dept();
		// dept2.setName("hbn2");
		// dept2.setId("2");
		// dept2.setPid("0");
		//
		// Dept dept3=new Dept();
		// dept3.setName("hbn1-1");
		// dept3.setId("3");
		// dept3.setPid("1");
		//
		// Dept dept4=new Dept();
		// dept4.setName("hbn1-2");
		// dept4.setId("4");
		// dept4.setPid("1");
		//
		// alist.add(dept);
		// alist.add(dept1);
		// alist.add(dept2);
		// alist.add(dept3);
		// alist.add(dept4);
		//
		// t.testNode(alist, dept);
		// System.out.println(JSONObject.fromObject(dept).toString());
	}
}
