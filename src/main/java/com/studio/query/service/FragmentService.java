package com.studio.query.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.common.Configure;
import com.studio.query.common.Constants;
import com.studio.query.dao.FragmentDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Committer;
import com.studio.query.entity.Fragment;
import com.studio.query.entity.Scene;
import com.studio.query.entity.ShareFragment;
import com.studio.query.entity.Variable;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.CacheUtil;
import com.studio.query.util.DateUtil;
import com.studio.query.util.FileUtil;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class FragmentService {
	@Autowired
	public FragmentDao fragmentDao;
	private List<String> templateVariableList = new ArrayList<String>();

	public List<Fragment> findFragment(Fragment fragment) {
		return fragmentDao.findFragment(fragment);
	}

	public int insertFragment(Fragment fragment) {
		return fragmentDao.insertFragment(fragment);
	}

	public int updateFragment(Fragment fragment) {
		return fragmentDao.updateFragment(fragment);
	}

	public String createFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentName;
			String fragmentType;
			String fragmentObjType;
			String fragmentDesc;
			if (Configure.serverVersion == 0) {
				fragmentName = parmJb.optString("name", "");
				fragmentType = parmJb.optString("type", "");
				fragmentObjType = parmJb.optString("objectType", "");
				fragmentDesc = parmJb.optString("desc", "");
			} else {
				fragmentName = parmJb.optString("fragmentName", "");
				fragmentType = parmJb.optString("fragmentType", "");
				fragmentObjType = parmJb.optString("fragmentObjType", "");
				fragmentDesc = parmJb.optString("fragmentDesc", "");
			}
			// String fragmentExpression =
			// parmJb.optString("fragmentExpression", "");

			if (StringUtil.isNullOrEmpty(fragmentName) || StringUtil.isNullOrEmpty(fragmentType)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// fragment重名验证暂时不启用
			// Fragment findFragment = new Fragment();
			// findFragment.setFragmentName(fragmentName);
			// List<Fragment> fragmentList =
			// fragmentDao.findFragment(findFragment);
			// if (fragmentList.size() >= 1) {
			// resultString =
			// StringUtil.packetObject(MethodCode.CREATE_FRAGMENT,
			// ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.CREATE_FRAGMENT_EXIST, "fragment已经存在", "");
			// return resultString;
			// }
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}

			Fragment insertFragment = new Fragment();
			insertFragment.setSceneId(sceneActive.getSceneId());
			insertFragment.setFragmentUUID(StringUtil.createFragmentUUID());
			insertFragment.setFragmentName(fragmentName);
			insertFragment.setFragmentType(fragmentType);
			insertFragment.setFragmentObjType(fragmentObjType);
			insertFragment.setFragmentEnable(true);
			insertFragment.setFragmentActive(true);
			insertFragment.setFragmentDateStr(DateUtil.dateTimeFormat(new Date()));
			insertFragment.setFragmentExpression("{\"expressions\":[],\"operator\":\"bool_and\"}");
			insertFragment.setFragmentDesc(fragmentDesc);

			// 将fragment保存到缓存中
			List<Fragment> sessionFragmentArray = (ArrayList<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (sessionFragmentArray == null) {
				sessionFragmentArray = new ArrayList<Fragment>();
			}
			sessionFragmentArray.add(insertFragment);
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, sessionFragmentArray);

			List<Fragment> sessionFragmentList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_ADD);
			if (sessionFragmentList == null || sessionFragmentList.size() == 0) {
				sessionFragmentList = new ArrayList<Fragment>();
			}
			sessionFragmentList.add(insertFragment);
			session.put(Constants.KEY_FRAGMENT_ADD, sessionFragmentList);

			// int insertResult = fragmentDao.insertFragment(insertFragment);
			// if (insertResult == 1) {
			JSONObject fragmentJsonObject = new JSONObject();
			if (Configure.serverVersion == 0) {
				fragmentJsonObject.put("id", insertFragment.getFragmentUUID());
				fragmentJsonObject.put("name", insertFragment.getFragmentName());
				fragmentJsonObject.put("type", insertFragment.getFragmentType());
				fragmentJsonObject.put("objectType", insertFragment.getFragmentObjType());
				fragmentJsonObject.put("enable", insertFragment.isFragmentEnable());
				fragmentJsonObject.put("active", insertFragment.isFragmentActive());
				fragmentJsonObject.put("desc", insertFragment.getFragmentDesc());
				fragmentJsonObject.put("version", "");
				fragmentJsonObject.put("expression", insertFragment.getFragmentExpression());
			} else {
				fragmentJsonObject.put("fragmentUUID", insertFragment.getFragmentUUID());
				fragmentJsonObject.put("fragmentName", insertFragment.getFragmentName());
				fragmentJsonObject.put("fragmentType", insertFragment.getFragmentType());
				fragmentJsonObject.put("fragmentObjType", insertFragment.getFragmentObjType());
				fragmentJsonObject.put("fragmentEnable", insertFragment.isFragmentEnable());
				fragmentJsonObject.put("fragmentActive", insertFragment.isFragmentActive());
				fragmentJsonObject.put("fragmentDesc", insertFragment.getFragmentDesc());
				fragmentJsonObject.put("fragmentCreateTime", insertFragment.getFragmentDateStr());
				fragmentJsonObject.put("fragmentExpression", insertFragment.getFragmentExpression());
			}
			resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"创建fragment到缓存成功，请注意在切换场景前保存场景数据。", fragmentJsonObject.toString());
			// } else {
			//
			// resultString =
			// StringUtil.packetObject(MethodCode.CREATE_FRAGMENT,
			// ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.CREATE_FRAGMENT_FAIL, "创建fragment失败", "");
			// }
		}
		return resultString;
	}

	public String updateFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID;
			String fragmentName;
			String fragmentType;
			String fragmentObjType;
			String fragmentDesc;
			boolean fragmentEnable;
			boolean fragmentActive;
			String fragmentExpression;
			if (Configure.serverVersion == 0) {
				fragmentUUID = parmJb.optString("id", "");
				fragmentName = parmJb.optString("name", "");
				fragmentType = parmJb.optString("type", "");
				fragmentObjType = parmJb.optString("objectType", "");
				fragmentDesc = parmJb.optString("desc", "");
				fragmentEnable = parmJb.optBoolean("enable", true);
				fragmentActive = parmJb.optBoolean("active", true);
				fragmentExpression = parmJb.optString("expression", "");
			} else {
				fragmentUUID = parmJb.optString("fragmentUUID", "");
				fragmentName = parmJb.optString("fragmentName", "");
				fragmentType = parmJb.optString("fragmentType", "");
				fragmentObjType = parmJb.optString("fragmentObjType", "");
				fragmentDesc = parmJb.optString("fragmentDesc", "");
				fragmentEnable = parmJb.optBoolean("fragmentEnable", true);
				fragmentActive = parmJb.optBoolean("fragmentActive", true);
				fragmentExpression = parmJb.optString("fragmentExpression", "");
			}
			if (StringUtil.isNullOrEmpty(fragmentUUID) || StringUtil.isNullOrEmpty(fragmentExpression)) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// Fragment findFragment = new Fragment();
			// findFragment.setFragmentUUID(fragmentUUID);
			// List<Fragment> fragmentList =
			// fragmentDao.findFragment(findFragment);
			// if (fragmentList.size() != 1) {
			// resultString =
			// StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT,
			// ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在",
			// "");
			// return resultString;
			// }
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			List<Fragment> fragmentList = (List<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();
			}
			JSONObject fragmentObj = new JSONObject();
			for (int i = 0; i < fragmentList.size(); i++) {

				Fragment fragment = fragmentList.get(i);
				if (fragment.getFragmentUUID().equals(fragmentUUID)) {
					if (!StringUtil.isNullOrEmpty(fragmentName)) {
						fragment.setFragmentName(fragmentName);
					}
					if (!StringUtil.isNullOrEmpty(fragmentType)) {
						fragment.setFragmentType(fragmentType);
					}
					if (!StringUtil.isNullOrEmpty(fragmentObjType)) {
						fragment.setFragmentObjType(fragmentObjType);
					}
					if (!StringUtil.isNullOrEmpty(fragmentDesc)) {
						fragment.setFragmentDesc(fragmentDesc);
					}
					fragment.setFragmentEnable(fragmentEnable);
					fragment.setFragmentActive(fragmentActive);
					fragment.setFragmentExpression(fragmentExpression);
					break;
				}
			}

			// 将fragment更新到缓存中
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, fragmentList);

			// Fragment updateFragment = fragmentList.get(0);
			// if (!StringUtil.isNullOrEmpty(fragmentName)) {
			// updateFragment.setFragmentName(fragmentName);
			// }
			// if (!StringUtil.isNullOrEmpty(fragmentType)) {
			// updateFragment.setFragmentType(fragmentType);
			// }
			// if (!StringUtil.isNullOrEmpty(fragmentDesc)) {
			// updateFragment.setFragmentDesc(fragmentDesc);
			// }
			// List<Fragment> sessionFragmentList = (List<Fragment>)
			// session.get(Constants.KEY_FRAGMENT_UPDATE);
			// if (sessionFragmentList == null || sessionFragmentList.size() ==
			// 0) {
			// sessionFragmentList = new ArrayList<Fragment>();
			// }
			// sessionFragmentList.add(updateFragment);
			// session.put(Constants.KEY_FRAGMENT_UPDATE, sessionFragmentList);
			// int insertResult = fragmentDao.updateFragment(updateFragment);
			// if (insertResult == 1) {
			resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"更新fragment到缓存成功，请注意在切换场景前保存场景数据。", "");
			// } else {
			//
			// resultString =
			// StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT,
			// ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.UPDATE_FRAGMENT_FAIL, "更新fragment失败", "");
			// }
		}
		return resultString;
	}

	public String getFragment(String bodyString, Account currentAccount, Map<String, Object> session) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID;
			if (Configure.serverVersion == 0) {
				fragmentUUID = parmJb.optString("fragmentId", "");
			} else {
				fragmentUUID = parmJb.optString("fragmentUUID", "");
			}
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.GET_FRAGMENT, ParameterCode.Error.SERVICE_PARAMETER,
						"必要参数不足", "");
				return resultString;
			}
			// 从缓存中解析出对应的fragment

			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.GET_FRAGMENT,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			List<Fragment> fragmentList = (List<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();
			}
			JSONObject fragmentObj = new JSONObject();
			for (int i = 0; i < fragmentList.size(); i++) {

				Fragment fragment = fragmentList.get(i);
				if (fragment.getFragmentUUID().equals(fragmentUUID)) {
					if (Configure.serverVersion == 0) {
						fragmentObj.put("id", fragment.getFragmentUUID());
						fragmentObj.put("name", fragment.getFragmentName());
						fragmentObj.put("desc", fragment.getFragmentDesc());
						fragmentObj.put("type", fragment.getFragmentType());
						fragmentObj.put("objectType", fragment.getFragmentObjType());
						fragmentObj.put("enable", fragment.isFragmentEnable());
						fragmentObj.put("active", fragment.isFragmentActive());
						fragmentObj.put("createTime", fragment.getFragmentDateStr());
						fragmentObj.put("expression", fragment.getFragmentExpression());
					} else {
						fragmentObj.put("fragmentUUID", fragment.getFragmentUUID());
						fragmentObj.put("fragmentName", fragment.getFragmentName());
						fragmentObj.put("fragmentDesc", fragment.getFragmentDesc());
						fragmentObj.put("fragmentType", fragment.getFragmentType());
						fragmentObj.put("fragmentObjType", fragment.getFragmentObjType());
						fragmentObj.put("fragmentEnable", fragment.isFragmentEnable());
						fragmentObj.put("fragmentActive", fragment.isFragmentActive());
						fragmentObj.put("fragmentCreateTime", fragment.getFragmentDateStr());
						fragmentObj.put("fragmentExpression", fragment.getFragmentExpression());
					}
					break;
				}
			}

			resultString = StringUtil.packetObject(MethodCode.GET_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"获取某个版本fragment成功", fragmentObj.toString());

		}
		return resultString;
	}

	public String deleteFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID;
			if (Configure.serverVersion == 0) {
				fragmentUUID = parmJb.optString("fragmentId", "");
			} else {
				fragmentUUID = parmJb.optString("fragmentUUID", "");
			}
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// Fragment findFragment = new Fragment();
			// findFragment.setFragmentUUID(fragmentUUID);
			// List<Fragment> fragmentList =
			// fragmentDao.findFragment(findFragment);
			// if (fragmentList.size() != 1) {
			// resultString =
			// StringUtil.packetObject(MethodCode.DELETE_FRAGMENT,
			// ParameterCode.Result.RESULT_FAIL,
			// ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在",
			// "");
			// return resultString;
			// }
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			List<Fragment> fragmentList = (List<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();
			}
			JSONObject fragmentObj = new JSONObject();
			for (int i = 0; i < fragmentList.size(); i++) {

				Fragment fragment = fragmentList.get(i);
				if (fragment.getFragmentUUID().equals(fragmentUUID)) {
					fragmentList.remove(fragment);
					break;
				}
			}
			// 将删除fragment更新到缓存中
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, fragmentList);

			// Fragment deleteFragment = fragmentList.get(0);
			// List<Fragment> sessionFragmentList = (List<Fragment>)
			// session.get(Constants.KEY_FRAGMENT_DELETE);
			// if (sessionFragmentList == null || sessionFragmentList.size() ==
			// 0) {
			// sessionFragmentList = new ArrayList<Fragment>();
			// }
			// sessionFragmentList.add(deleteFragment);
			// session.put(Constants.KEY_FRAGMENT_DELETE, sessionFragmentList);

			resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"删除fragment到缓存成功，请注意在切换场景前保存场景数据。", "");

		}
		return resultString;
	}

	public String disableFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID;
			if (Configure.serverVersion == 0) {
				fragmentUUID = parmJb.optString("fragmentId", "");
			} else {
				fragmentUUID = parmJb.optString("fragmentUUID", "");
			}
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			List<Fragment> fragmentList = (List<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();
			}
			JSONObject fragmentObj = new JSONObject();
			for (int i = 0; i < fragmentList.size(); i++) {

				Fragment fragment = fragmentList.get(i);
				if (fragment.getFragmentUUID().equals(fragmentUUID)) {
					fragment.setFragmentEnable(false);
					break;
				}
			}

			// 将fragment更新到缓存中
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, fragmentList);

			resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"禁用fragment成功", "");

		}
		return resultString;
	}

	public String disableShareFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment updateFragment = new Fragment();
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.disableShareFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_FRAGMENT,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_OK, "禁用共享fragment成功", "");
			}
		}
		return resultString;
	}

	public String enableFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID;
			if (Configure.serverVersion == 0) {
				fragmentUUID = parmJb.optString("fragmentId", "");
			} else {
				fragmentUUID = parmJb.optString("fragmentUUID", "");
			}
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.ENABLE_FRAGMENT,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			List<Fragment> fragmentList = (List<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();
			}
			JSONObject fragmentObj = new JSONObject();
			for (int i = 0; i < fragmentList.size(); i++) {

				Fragment fragment = fragmentList.get(i);
				if (fragment.getFragmentUUID().equals(fragmentUUID)) {
					fragment.setFragmentEnable(true);
					break;
				}
			}

			// 将fragment更新到缓存中
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, fragmentList);

			resultString = StringUtil.packetObject(MethodCode.ENABLE_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"启用fragment成功", "");

		}
		return resultString;
	}

	public String enableShareFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_FRAGMENT,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment updateFragment = new Fragment();
			// updateFragment.setAccountId(currentAccount.getAccountId());
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.enableShareFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_FRAGMENT,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK,
						"启用分享fragment成功", "");
			}
		}
		return resultString;
	}

	public String getFragments(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		// 如果session中没有记录当前场景
		if (sceneActive == null) {
			resultString = StringUtil.packetObject(MethodCode.LIST_FRAGMENT, ParameterCode.Error.UPDATE_SCENE_NO_MATCH,
					"当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
			return resultString;
		}
		JSONArray fragmentJsonArray = new JSONArray();
		Fragment findFragment = new Fragment();
		findFragment.setSceneId(sceneActive.getSceneId());
		List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
		for (int i = 0; i < fragmentList.size(); i++) {

			Fragment fragment = fragmentList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("fragmentUUID", fragment.getFragmentUUID());
			dataObj.put("fragmentName", fragment.getFragmentName());
			dataObj.put("fragmentDesc", fragment.getFragmentDesc());
			dataObj.put("fragmentType", fragment.getFragmentType());
			dataObj.put("fragmentEnable", fragment.isFragmentEnable());
			dataObj.put("fragmentActive", fragment.isFragmentActive());
			dataObj.put("fragmentCreateTime", DateUtil.dateTimeFormat(fragment.getFragmentDate()));

			fragmentJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_FRAGMENT, ParameterCode.Result.RESULT_OK,
				"获取fragment列表成功", fragmentJsonArray.toString());

		return resultString;
	}

	public String getTemplateHistory(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareFragmentUUID = parmJb.optString("templateId", "");
			if (StringUtil.isNullOrEmpty(shareFragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_HISTORY,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_HISTORY,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			shareFragment = shareFragmentList.get(0);
			JSONObject fragmentJsonObj = new JSONObject();
			fragmentJsonObj.put("templateId", shareFragment.getShareFragmentUUID());
			fragmentJsonObj.put("name", shareFragment.getShareFragmentName());
			fragmentJsonObj.put("desc", shareFragment.getShareFragmentDesc());
			fragmentJsonObj.put("type", shareFragment.getShareFragmentType());
			fragmentJsonObj.put("enable", shareFragment.getShareFragmentEnable() == 0 ? "true" : "false");
			fragmentJsonObj.put("actvie", shareFragment.getShareFragmentActive() == 0 ? "true" : "false");
			fragmentJsonObj.put("createdBy", shareFragment.getAccountName());
			fragmentJsonObj.put("createTime", DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			List<Committer> committerList = jGitService.getLogs(gitPath);
			JSONArray committerJsonArray = new JSONArray();
			for (Committer c : committerList) {
				JSONObject cJson = new JSONObject();

				cJson.put("commitVersion", c.getCommitVersion());
				cJson.put("commitName", c.getCommitName());
				cJson.put("commitEmail", c.getCommitEmail());
				cJson.put("commitMssage", c.getCommitMssage());
				cJson.put("commitDate", c.getCommitDate());
				committerJsonArray.add(cJson);
			}
			fragmentJsonObj.put("committers", committerJsonArray.toString());

			resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_HISTORY, ParameterCode.Result.RESULT_OK,
					"获取模板历史版本成功", fragmentJsonObj.toString());

		}
		return resultString;
	}

	public String getTemplateVersion(String bodyString, Account currentAccount, Map<String, Object> session) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareFragmentUUID = parmJb.optString("templateId", "");
			String shareFragmentVersion = parmJb.optString("version", "");
			if (StringUtil.isNullOrEmpty(shareFragmentUUID) || StringUtil.isNullOrEmpty(shareFragmentVersion)) {

				resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_VERSION,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}

			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_VERSION,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			shareFragment = shareFragmentList.get(0);
			JSONObject fragmentJsonObj = new JSONObject();
			fragmentJsonObj.put("templateId", shareFragment.getShareFragmentUUID());
			fragmentJsonObj.put("name", shareFragment.getShareFragmentName());
			fragmentJsonObj.put("desc", shareFragment.getShareFragmentDesc());
			fragmentJsonObj.put("type", shareFragment.getShareFragmentType());
			fragmentJsonObj.put("enable", shareFragment.getShareFragmentEnable() == 0 ? "true" : "false");
			fragmentJsonObj.put("actvie", shareFragment.getShareFragmentActive() == 0 ? "true" : "false");
			fragmentJsonObj.put("createdBy", shareFragment.getAccountName());
			fragmentJsonObj.put("createTime", DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			// 根据版本获取共享fragment内容
			String contentString = jGitService.getContentByVersion(gitPath, shareFragmentVersion, "template.txt");
			if (StringUtil.isNullOrEmpty(contentString)) {
				resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_VERSION,
						ParameterCode.Error.QUERY_VERSION_NO_EXIST, "查询的版本号不存在", "");
				return resultString;
			}
			JSONObject refJson = new JSONObject().fromObject(contentString);
			fragmentJsonObj.put("version", shareFragmentVersion);
			fragmentJsonObj.put("expression", refJson.optString("expression", ""));

			// Committer commitInfo = jGitService.getCommitterByVersion(gitPath,
			// shareFragmentVersion);
			// if (commitInfo == null) {
			// resultString =
			// StringUtil.packetObject(MethodCode.GET_TEMPLATE_VERSION,
			// ParameterCode.Error.QUERY_VERSION_NO_EXIST, "查询的版本号不存在", "");
			// return resultString;
			// }
			// JSONObject committerJsonObj = new JSONObject();
			//
			// committerJsonObj.put("commitVersion",
			// commitInfo.getCommitVersion());
			// committerJsonObj.put("commitName", commitInfo.getCommitName());
			// committerJsonObj.put("commitEmail", commitInfo.getCommitEmail());
			// committerJsonObj.put("commitMssage",
			// commitInfo.getCommitMssage());
			// committerJsonObj.put("commitDate", commitInfo.getCommitDate());
			//
			// fragmentJsonObj.put("committer", committerJsonObj.toString());

			resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATE_VERSION, ParameterCode.Result.RESULT_OK,
					"获取模板某个版本成功", fragmentJsonObj.toString());

		}
		return resultString;
	}

	public String getTemplates(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONArray fragmentJsonArray = new JSONArray();
		ShareFragment findShareFragment = new ShareFragment();
		List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(findShareFragment);
		for (int i = 0; i < shareFragmentList.size(); i++) {

			ShareFragment shareFragment = shareFragmentList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("templateId", shareFragment.getShareFragmentUUID());
			dataObj.put("name", shareFragment.getShareFragmentName());
			dataObj.put("desc", shareFragment.getShareFragmentDesc());
			dataObj.put("type", shareFragment.getShareFragmentType());
			dataObj.put("objType", shareFragment.getShareFragmentObjType());
			dataObj.put("enable", shareFragment.getShareFragmentEnable() == 0 ? "true" : "false");
			dataObj.put("actvie", shareFragment.getShareFragmentActive() == 0 ? "true" : "false");
			dataObj.put("createdBy", shareFragment.getAccountName());
			dataObj.put("createTime", DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));
			dataObj.put("version", shareFragment.getShareFragmentVersion());

			fragmentJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.GET_TEMPLATES, ParameterCode.Result.RESULT_OK, "获取模板列表成功",
				fragmentJsonArray.toString());

		return resultString;
	}

	// 发布共享fragment版本
	public String releaseTemplate(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = "";
			String fragmentDesc = "";
			if (Configure.serverVersion == 0) {
				fragmentUUID = parmJb.optString("fragmentId", "");
				fragmentDesc = parmJb.optString("desc", "");
			} else {
				fragmentUUID = parmJb.optString("fragmentUUID", "");
				fragmentDesc = parmJb.optString("fragmentDesc", "");
			}
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.RELEASE_TEMPLATE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_TEMPLATE,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			// 从缓存中解析出对应的fragment
			List<Fragment> fragmentList = (List<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (fragmentList == null) {
				fragmentList = new ArrayList<Fragment>();
			}
			JSONObject fragmentObj = new JSONObject();
			Fragment fromFragment = null;
			for (int i = 0; i < fragmentList.size(); i++) {

				Fragment temp = fragmentList.get(i);
				if (temp.getFragmentUUID().equals(fragmentUUID)) {
					fromFragment = fragmentList.get(i);
					if (Configure.serverVersion == 0) {
						fragmentObj.put("templateId", fromFragment.getFragmentUUID());
						fragmentObj.put("name", fromFragment.getFragmentName());
						if (StringUtil.isNullOrEmpty(fragmentDesc)) {
							fragmentObj.put("desc", fromFragment.getFragmentDesc());
						} else {
							fragmentObj.put("desc", fragmentDesc);
						}
						fragmentObj.put("type", fromFragment.getFragmentType());
						fragmentObj.put("objectType", "templateInstance");
						fragmentObj.put("enable", fromFragment.isFragmentEnable());
						fragmentObj.put("active", fromFragment.isFragmentActive());
						fragmentObj.put("createTime", fromFragment.getFragmentDateStr());
						fragmentObj.put("expression", fromFragment.getFragmentExpression());
					} else {
						fragmentObj.put("fragmentUUID", fromFragment.getFragmentUUID());
						fragmentObj.put("fragmentName", fromFragment.getFragmentName());
						if (StringUtil.isNullOrEmpty(fragmentDesc)) {
							fragmentObj.put("fragmentDesc", fromFragment.getFragmentDesc());
						} else {
							fragmentObj.put("fragmentDesc", fragmentDesc);
						}

						fragmentObj.put("fragmentType", fromFragment.getFragmentType());
						fragmentObj.put("fragmentObjType", "templateInstance");
						fragmentObj.put("fragmentEnable", fromFragment.isFragmentEnable());
						fragmentObj.put("fragmentActive", fromFragment.isFragmentActive());
						fragmentObj.put("fragmentCreateTime", fromFragment.getFragmentDateStr());
						fragmentObj.put("fragmentExpression", fromFragment.getFragmentExpression());
					}
					break;
				}
			}

			if (fromFragment == null) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_TEMPLATE,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			// 查看发布的fragment是否有引用变量(不管是否有引用变量统一将变量保存)

			// 获取缓存中变量数组
			List<Variable> sessionVariableArray = (ArrayList<Variable>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
			if (sessionVariableArray == null) {
				sessionVariableArray = new ArrayList<Variable>();
			}
			JSONArray variableJsonArray = new JSONArray();

			for (Variable variable : sessionVariableArray) {
				JSONObject dataObj = new JSONObject();
				dataObj.put("variableUUID", variable.getVariableUUID());
				dataObj.put("fragmentUUID", variable.getFragmentUUID());
				dataObj.put("scenarioUUID", variable.getSceneUUID());
				dataObj.put("variableName", variable.getVariableName());
				dataObj.put("variableType", variable.getVariableType());
				dataObj.put("variableValueType", variable.getVariableValueType());
				dataObj.put("variableFieldType", variable.getVariableFieldType());
				dataObj.put("variableValue", variable.getVariableValue());
				dataObj.put("variableScope", variable.getVariableScope());
				dataObj.put("variableInstanceId", variable.getVariableInstanceId());
				variableJsonArray.add(dataObj);
			}
			fragmentObj.put("variableList", variableJsonArray);
			// 查询是否已经存在共享记录
			ShareFragment findShareFragment = new ShareFragment();
			findShareFragment.setShareFragmentUUID(fragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(findShareFragment);
			ShareFragment shareFragment = null;
			if (shareFragmentList.size() == 1) {
				shareFragment = shareFragmentList.get(0);
			} else {
				shareFragment = new ShareFragment();
				shareFragment.setAccountId(currentAccount.getAccountId());
				shareFragment.setShareFragmentUUID(fragmentUUID);
				shareFragment.setShareFragmentName(fromFragment.getFragmentName());
				shareFragment.setShareFragmentType(fromFragment.getFragmentType());
				shareFragment.setShareFragmentObjType(fromFragment.getFragmentObjType());
				shareFragment.setShareFragmentDesc(fromFragment.getFragmentDesc());
				shareFragment.setShareFragmentDate(new Date());
				shareFragment.setShareFragmentGit(StringUtil.createShareFragmentGit());
			}

			// 模板版本库路径
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			JGitService jGitService = new JGitService();
			if (shareFragmentList.size() == 1) {
			} else {
				jGitService.initShareFragmentGit(gitPath);
			}
			FileUtil.writeFile(gitPath + "/template.txt", fragmentObj.toString());
			jGitService.shareFragmentCommit(gitPath, currentAccount, "发布fragment");
			// 获取最新版本
			Committer committer = jGitService.getLastCommitter(gitPath);
			shareFragment.setShareFragmentVersion(committer.getCommitVersion());
			if (shareFragmentList.size() == 1) {
				fragmentDao.updateShareFragment(shareFragment);
			} else {
				fragmentDao.releaseFragment(shareFragment);
			}
			resultString = StringUtil.packetObject(MethodCode.RELEASE_TEMPLATE, ParameterCode.Result.RESULT_OK,
					"发布模板成功", fragmentObj.toString());

		}
		return resultString;
	}

	// 引用共享fragment版本
	public String referenceTemplate(String bodyString, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String templateId = parmJb.optString("templateId", "");
			String fragmentVersion = parmJb.optString("version", "");
			String desc = parmJb.optString("desc", "");

			if (StringUtil.isNullOrEmpty(templateId) || StringUtil.isNullOrEmpty(fragmentVersion)) {

				resultString = StringUtil.packetObject(MethodCode.REFERENCE_TEMPLATE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_TEMPLATE,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			// 从模板库中解析出对应的fragment
			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(templateId);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.REFERENCE_TEMPLATE,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			shareFragment = shareFragmentList.get(0);

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + shareFragment.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			// 获取最新共享fragment内容
			// String contentString = jGitService.getContentLast(gitPath,
			// "template.txt");
			String contentString = jGitService.getContentByVersion(gitPath, fragmentVersion, "template.txt");
			//Committer lastCommitter = jGitService.getLastCommitter(gitPath);
			JSONObject refJson = new JSONObject().fromObject(contentString);

			Fragment insertFragment = new Fragment();
			insertFragment.setSceneId(sceneActive.getSceneId());
			insertFragment.setFragmentTemplateId(refJson.optString("templateId", ""));
			insertFragment.setFragmentUUID(StringUtil.createFragmentUUID());
			insertFragment.setFragmentName(refJson.optString("name", ""));
			insertFragment.setFragmentDesc(desc);
			insertFragment.setFragmentType(refJson.optString("type", ""));
			insertFragment.setFragmentObjType("templateInstance");
			insertFragment.setFragmentTemplateVersion(fragmentVersion);
			insertFragment.setFragmentEnable(true);
			insertFragment.setFragmentActive(true);
			insertFragment.setFragmentCreateBy(shareFragment.getAccountName());
			insertFragment.setFragmentDateStr(DateUtil.dateTimeFormat(new Date()));
			insertFragment.setFragmentExpression(refJson.optString("expression", ""));

			// 将模板fragment保存到缓存中
			List<Fragment> sessionFragmentArray = (ArrayList<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_TEMPLATE);
			if (sessionFragmentArray == null) {
				sessionFragmentArray = new ArrayList<Fragment>();
			}
			sessionFragmentArray.add(insertFragment);
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_TEMPLATE, sessionFragmentArray);

			JSONObject fragmentJsonObject = new JSONObject();

			fragmentJsonObject.put("templateId", insertFragment.getFragmentTemplateId());
			fragmentJsonObject.put("instanceId", insertFragment.getFragmentUUID());
			fragmentJsonObject.put("name", insertFragment.getFragmentName());
			fragmentJsonObject.put("type", insertFragment.getFragmentType());
			fragmentJsonObject.put("objectType", insertFragment.getFragmentObjType());
			fragmentJsonObject.put("enable", insertFragment.isFragmentEnable());
			fragmentJsonObject.put("active", insertFragment.isFragmentActive());
			fragmentJsonObject.put("desc", insertFragment.getFragmentDesc());
			fragmentJsonObject.put("createBy", insertFragment.getFragmentCreateBy());
			fragmentJsonObject.put("createTime", insertFragment.getFragmentDateStr());
			fragmentJsonObject.put("version", insertFragment.getFragmentTemplateVersion());
			fragmentJsonObject.put("expression", insertFragment.getFragmentExpression());

			// 查看实例化模板是否引用变量
			JSONArray refVariableArray = new JSONArray();
			try {
				refVariableArray = refJson.getJSONArray("variableList");
			} catch (Exception e) {
			}
			// 将变量保存到缓存中
			List<Variable> sessionVariableArray = (ArrayList<Variable>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
			if (sessionVariableArray == null) {
				sessionVariableArray = new ArrayList<Variable>();
			}
			templateVariableList = new ArrayList<String>();
			this.parseTemplateVariableList(insertFragment.getFragmentExpression());
			for (int i = 0; i < templateVariableList.size(); i++) {
				String templateVarString = templateVariableList.get(i);
				for (int j = 0; j < refVariableArray.size(); j++) {
					JSONObject reVOj = refVariableArray.getJSONObject(j);
					if (templateVarString.equals(reVOj.optString("variableUUID", ""))) {
						Variable insertVariable = new Variable();
						insertVariable.setFragmentUUID(reVOj.optString("fragmentUUID", ""));
						insertVariable.setSceneUUID(reVOj.optString("scenarioUUID", ""));
						insertVariable.setVariableUUID(reVOj.optString("variableUUID", ""));
						insertVariable.setVariableInstanceId(StringUtil.createVariableUUID());
						insertVariable.setVariableName(reVOj.optString("variableName", ""));
						insertVariable.setVariableType(reVOj.optString("variableType", ""));
						insertVariable.setVariableScope(reVOj.optString("variableScope", ""));
						insertVariable.setVariableFieldType(reVOj.optString("variableFieldType", ""));
						insertVariable.setVariableValueType(reVOj.optString("variableValueType", ""));
						insertVariable.setVariableValue(reVOj.optString("variableValue", ""));
						insertVariable.setVariableDateStr(DateUtil.dateTimeFormat(new Date()));
						sessionVariableArray.add(insertVariable);
					}
				}

			}

			resultString = StringUtil.packetObject(MethodCode.REFERENCE_TEMPLATE, ParameterCode.Result.RESULT_OK,
					"引用模板成功", fragmentJsonObject.toString());

		}
		return resultString;
	}

	// 实例化共享fragment版本
	public String instanceTemplate(String bodyString, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String templateId = parmJb.optString("templateId", "");
			String fragmentVersion = parmJb.optString("version", "");
			String desc = parmJb.optString("desc", "");

			if (StringUtil.isNullOrEmpty(templateId) || StringUtil.isNullOrEmpty(fragmentVersion)) {

				resultString = StringUtil.packetObject(MethodCode.INSTANCE_TEMPLATE,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.INSTANCE_TEMPLATE,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "会话已经过期！", "");
				return resultString;
			}
			// 从模板库中解析出对应的fragment
			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(templateId);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.INSTANCE_TEMPLATE,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			shareFragment = shareFragmentList.get(0);

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + shareFragment.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			// 获取最新共享fragment内容
			// String contentString = jGitService.getContentLast(gitPath,
			// "template.txt");
			String contentString = jGitService.getContentByVersion(gitPath, fragmentVersion, "template.txt");
			Committer lastCommitter = jGitService.getLastCommitter(gitPath);
			JSONObject refJson = new JSONObject().fromObject(contentString);

			Fragment insertFragment = new Fragment();
			insertFragment.setSceneId(sceneActive.getSceneId());
			insertFragment.setFragmentTemplateId("");// 实例化模板templateid为空
			insertFragment.setFragmentUUID(StringUtil.createFragmentUUID());
			insertFragment.setFragmentName(refJson.optString("name", ""));
			insertFragment.setFragmentDesc(desc);
			insertFragment.setFragmentType(refJson.optString("type", ""));
			insertFragment.setFragmentObjType("directInstance");
			insertFragment.setFragmentTemplateVersion(lastCommitter.getCommitVersion());
			insertFragment.setFragmentEnable(true);
			insertFragment.setFragmentActive(true);
			insertFragment.setFragmentCreateBy(shareFragment.getAccountName());
			insertFragment.setFragmentDateStr(DateUtil.dateTimeFormat(new Date()));
			insertFragment.setFragmentExpression(refJson.optString("expression", ""));

			// 将实例化后的模板保存到缓存中，此时的实例化的模板和用户自己创建的模板一样。
			List<Fragment> sessionFragmentArray = (ArrayList<Fragment>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM);
			if (sessionFragmentArray == null) {
				sessionFragmentArray = new ArrayList<Fragment>();
			}
			sessionFragmentArray.add(insertFragment);
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_FRGM, sessionFragmentArray);

			JSONObject fragmentJsonObject = new JSONObject();

			fragmentJsonObject.put("templateId", insertFragment.getFragmentTemplateId());
			fragmentJsonObject.put("instanceId", insertFragment.getFragmentUUID());
			fragmentJsonObject.put("name", insertFragment.getFragmentName());
			fragmentJsonObject.put("type", insertFragment.getFragmentType());
			fragmentJsonObject.put("objectType", insertFragment.getFragmentObjType());
			fragmentJsonObject.put("enable", insertFragment.isFragmentEnable());
			fragmentJsonObject.put("active", insertFragment.isFragmentActive());
			fragmentJsonObject.put("desc", insertFragment.getFragmentDesc());
			fragmentJsonObject.put("createBy", insertFragment.getFragmentCreateBy());
			fragmentJsonObject.put("createTime", insertFragment.getFragmentDateStr());
			fragmentJsonObject.put("version", insertFragment.getFragmentTemplateVersion());
			fragmentJsonObject.put("expression", insertFragment.getFragmentExpression());

			// 查看实例化模板是否引用变量
			JSONArray refVariableArray = new JSONArray();
			try {
				refVariableArray = refJson.getJSONArray("variableList");
			} catch (Exception e) {
			}
			// 将变量保存到缓存中
			List<Variable> sessionVariableArray = (ArrayList<Variable>) CacheUtil
					.getCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR);
			if (sessionVariableArray == null) {
				sessionVariableArray = new ArrayList<Variable>();
			}
			templateVariableList = new ArrayList<String>();
			this.parseTemplateVariableList(insertFragment.getFragmentExpression());
			for (int i = 0; i < templateVariableList.size(); i++) {
				String templateVarString = templateVariableList.get(i);
				for (int j = 0; j < refVariableArray.size(); j++) {
					JSONObject reVOj = refVariableArray.getJSONObject(j);
					if (templateVarString.equals(reVOj.optString("variableUUID", ""))) {
						Variable insertVariable = new Variable();
						insertVariable.setFragmentUUID(reVOj.optString("fragmentUUID", ""));
						insertVariable.setSceneUUID(reVOj.optString("scenarioUUID", ""));
						insertVariable.setVariableUUID(reVOj.optString("variableUUID", ""));
						insertVariable.setVariableInstanceId(StringUtil.createVariableUUID());
						insertVariable.setVariableName(reVOj.optString("variableName", ""));
						insertVariable.setVariableType(reVOj.optString("variableType", ""));
						insertVariable.setVariableScope(reVOj.optString("variableScope", ""));
						insertVariable.setVariableFieldType(reVOj.optString("variableFieldType", ""));
						insertVariable.setVariableValueType(reVOj.optString("variableValueType", ""));
						insertVariable.setVariableValue(reVOj.optString("variableValue", ""));
						insertVariable.setVariableDateStr(DateUtil.dateTimeFormat(new Date()));
						sessionVariableArray.add(insertVariable);
					}
				}

			}
			CacheUtil.putCacheObject(sceneActive.getSceneUUID() + Constants.KEY_VAR, sessionVariableArray);

			resultString = StringUtil.packetObject(MethodCode.INSTANCE_TEMPLATE, ParameterCode.Result.RESULT_OK,
					"实例化模板成功", fragmentJsonObject.toString());

		}
		return resultString;
	}

	public void parseTemplateVariableList(String jsonString) {
		try {
			JSONObject expJo = new JSONObject();
			JSONArray expressArray = new JSONArray();
			if (jsonString == null) {
				// jsonString = FileUtil.readFile("E://query3.txt");
				return;
			}
			expJo = JSONObject.fromObject(jsonString);
			try {
				expressArray = expJo.getJSONArray("expressions");
			} catch (Exception e) {
				// TODO: handle exception
			}
			// System.out.println(expressArray.size());

			String dataType = expJo.optString("dataType", "");
			if (!StringUtil.isNullOrEmpty(dataType) && dataType.equals("variable")) {
				JSONObject variableJo = expJo.getJSONObject("variable");
				String variableClassId = variableJo.optString("variableClassId", "");
				templateVariableList.add(variableClassId);
			} else {
				for (int i = 0; i < expressArray.size(); i++) {
					expJo = expressArray.getJSONObject(i);
					parseTemplateVariableList(expJo.toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("varList size="+varList.size());
	}

	// public static void main(String[] args) {
	//
	// FragmentService t = new FragmentService();
	// t.test(null);
	// }
}
