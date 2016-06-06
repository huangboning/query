package com.studio.query.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	public String getShareFragmentHistory(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareFragmentUUID = parmJb.optString("shareFragmentUUID", "");
			if (StringUtil.isNullOrEmpty(shareFragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_HISTORY,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_HISTORY,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			shareFragment = shareFragmentList.get(0);
			JSONObject fragmentJsonObj = new JSONObject();
			fragmentJsonObj.put("shareFragmentUUID", shareFragment.getShareFragmentUUID());
			fragmentJsonObj.put("shareFragmentName", shareFragment.getShareFragmentName());
			fragmentJsonObj.put("shareFragmentDesc", shareFragment.getShareFragmentDesc());
			fragmentJsonObj.put("shareFragmentType", shareFragment.getShareFragmentType());
			fragmentJsonObj.put("shareFragmentEnable", shareFragment.getShareFragmentEnable() == 0 ? "true" : "false");
			fragmentJsonObj.put("shareFragmentActvie", shareFragment.getShareFragmentActive() == 0 ? "true" : "false");
			fragmentJsonObj.put("shareFragmentCreatedBy", shareFragment.getAccountName());
			fragmentJsonObj.put("shareFragmentCreateTime",
					DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));

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

			resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_HISTORY,
					ParameterCode.Result.RESULT_OK, "获取共享fragment共享历史版本成功", fragmentJsonObj.toString());

		}
		return resultString;
	}

	public String getShareFragmentVersion(String bodyString, Account currentAccount, Map<String, Object> session) {
		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareFragmentUUID = parmJb.optString("shareFragmentUUID", "");
			String shareFragmentVersion = parmJb.optString("shareFragmentVersion", "");
			if (StringUtil.isNullOrEmpty(shareFragmentUUID) || StringUtil.isNullOrEmpty(shareFragmentVersion)) {

				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_VERSION,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}

			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_VERSION,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			shareFragment = shareFragmentList.get(0);
			JSONObject fragmentJsonObj = new JSONObject();
			fragmentJsonObj.put("shareFragmentUUID", shareFragment.getShareFragmentUUID());
			fragmentJsonObj.put("shareFragmentName", shareFragment.getShareFragmentName());
			fragmentJsonObj.put("shareFragmentDesc", shareFragment.getShareFragmentDesc());
			fragmentJsonObj.put("shareFragmentType", shareFragment.getShareFragmentType());
			fragmentJsonObj.put("shareFragmentEnable", shareFragment.getShareFragmentEnable() == 0 ? "true" : "false");
			fragmentJsonObj.put("shareFragmentActvie", shareFragment.getShareFragmentActive() == 0 ? "true" : "false");
			fragmentJsonObj.put("shareFragmentCreatedBy", shareFragment.getAccountName());
			fragmentJsonObj.put("shareFragmentCreateTime",
					DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));

			JGitService jGitService = new JGitService();
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			// 根据版本获取共享fragment内容
			String contentString = jGitService.getContentByVersion(gitPath, shareFragmentVersion, "template.txt");
			Committer commitInfo = jGitService.getCommitterByVersion(gitPath, shareFragmentVersion);
			if (commitInfo == null) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_VERSION,
						ParameterCode.Error.QUERY_VERSION_NO_EXIST, "查询的版本号不存在", "");
				return resultString;
			}
			JSONObject committerJsonObj = new JSONObject();

			committerJsonObj.put("commitVersion", commitInfo.getCommitVersion());
			committerJsonObj.put("commitName", commitInfo.getCommitName());
			committerJsonObj.put("commitEmail", commitInfo.getCommitEmail());
			committerJsonObj.put("commitMssage", commitInfo.getCommitMssage());
			committerJsonObj.put("commitDate", commitInfo.getCommitDate());
			committerJsonObj.put("expression", contentString);

			fragmentJsonObj.put("committer", committerJsonObj.toString());

			resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_VERSION,
					ParameterCode.Result.RESULT_OK, "获取某个版本共享fragment成功", fragmentJsonObj.toString());

		}
		return resultString;
	}

	public String getShareFragments(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONArray fragmentJsonArray = new JSONArray();
		ShareFragment findShareFragment = new ShareFragment();
		List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(findShareFragment);
		for (int i = 0; i < shareFragmentList.size(); i++) {

			ShareFragment shareFragment = shareFragmentList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("shareFragmentUUID", shareFragment.getShareFragmentUUID());
			dataObj.put("shareFragmentName", shareFragment.getShareFragmentName());
			dataObj.put("shareFragmentDesc", shareFragment.getShareFragmentDesc());
			dataObj.put("shareFragmentType", shareFragment.getShareFragmentType());
			dataObj.put("shareFragmentEnable", shareFragment.getShareFragmentEnable() == 0 ? "true" : "false");
			dataObj.put("shareFragmentActvie", shareFragment.getShareFragmentActive() == 0 ? "true" : "false");
			dataObj.put("shareFragmentCreatedBy", shareFragment.getAccountName());
			dataObj.put("shareFragmentCreateTime", DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));
			dataObj.put("shareFragmentVersion", shareFragment.getShareFragmentVersion());
			// dataObj.put("expression", "");

			fragmentJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK,
				"获取共享fragment列表成功", fragmentJsonArray.toString());

		return resultString;
	}

	// 发布共享fragment版本
	public String releaseShareFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

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

				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT, ParameterCode.Error.SERVICE_PARAMETER,
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

			Fragment findFragment = new Fragment();
			findFragment.setFragmentUUID(shareFragmentUUID);
			List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
			if (fragmentList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			Fragment fromFragment = fragmentList.get(0);
			// 查询是否已经存在共享记录
			ShareFragment findShareFragment = new ShareFragment();
			findShareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(findShareFragment);
			ShareFragment shareFragment = null;
			if (shareFragmentList.size() == 1) {
				shareFragment = shareFragmentList.get(0);
			} else {
				shareFragment = new ShareFragment();
				shareFragment.setShareFragmentUUID(shareFragmentUUID);
				shareFragment.setShareFragmentGit(StringUtil.createShareFragmentGit());
			}
			shareFragment.setAccountId(currentAccount.getAccountId());
			if (!StringUtil.isNullOrEmpty(shareFragmentName)) {
				shareFragment.setShareFragmentName(shareFragmentName);
			} else {
				shareFragment.setShareFragmentName(fromFragment.getFragmentName());
			}
			if (!StringUtil.isNullOrEmpty(shareFragmentType)) {
				shareFragment.setShareFragmentType(shareFragmentType);
			} else {
				shareFragment.setShareFragmentType(fromFragment.getFragmentType());
			}
			if (!StringUtil.isNullOrEmpty(shareFragmentObjType)) {
				shareFragment.setShareFragmentObjType(shareFragmentObjType);
			} else {
				shareFragment.setShareFragmentObjType(fromFragment.getFragmentObjType());
			}
			if (!StringUtil.isNullOrEmpty(shareFragmentDesc)) {
				shareFragment.setShareFragmentDesc(shareFragmentDesc);
			} else {
				shareFragment.setShareFragmentDesc(fromFragment.getFragmentDesc());
			}

			// 提交版本库后获取版本
			String gitPath = Configure.gitRepositoryPath + "/" + currentAccount.getAccountRepository() + "/"
					+ Constants.SHARE_FRAGMENT_REPOSITORY_NAME + "/" + shareFragment.getShareFragmentGit();
			JGitService jGitService = new JGitService();
			if (shareFragmentList.size() == 1) {
			} else {
				jGitService.initShareFragmentGit(gitPath, currentAccount);
			}
			FileUtil.writeFile(gitPath + "/template.txt", shareFragmentExpression);
			jGitService.shareFragmentCommit(gitPath, currentAccount, shareFragmentComment);
			// 获取最新版本
			Committer committer = jGitService.getLastCommitter(gitPath);
			shareFragment.setShareFragmentVersion(committer.getCommitVersion());
			if (shareFragmentList.size() == 1) {
				fragmentDao.updateShareFragment(shareFragment);
			} else {
				fragmentDao.releaseFragment(shareFragment);
			}
			resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK,
					"发布共享fragment成功", "");

		}
		return resultString;
	}
}
