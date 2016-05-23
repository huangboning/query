package com.studio.query.service;

import java.util.ArrayList;
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
			String fragmentName = parmJb.optString("fragmentName", "");
			String fragmentType = parmJb.optString("fragmentType", "");
			String fragmentDesc = parmJb.optString("fragmentDesc", "");
			if (StringUtil.isNullOrEmpty(fragmentName) || StringUtil.isNullOrEmpty(fragmentType)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
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
				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			Fragment insertFragment = new Fragment();
			insertFragment.setSceneId(sceneActive.getSceneId());
			insertFragment.setFragmentUUID(StringUtil.createFragmentUUID());
			insertFragment.setFragmentName(fragmentName);
			insertFragment.setFragmentType(fragmentType);
			insertFragment.setFragmentDesc(fragmentDesc);
			List<Fragment> sessionFragmentList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_ADD);
			if (sessionFragmentList == null || sessionFragmentList.size() == 0) {
				sessionFragmentList = new ArrayList<Fragment>();
			}
			sessionFragmentList.add(insertFragment);
			session.put(Constants.KEY_FRAGMENT_ADD, sessionFragmentList);

			// int insertResult = fragmentDao.insertFragment(insertFragment);
			// if (insertResult == 1) {
			JSONObject fragmentJsonObject = new JSONObject();
			fragmentJsonObject.put("fragmentUUID", insertFragment.getFragmentUUID());
			resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
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
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			String fragmentName = parmJb.optString("fragmentName", "");
			String fragmentType = parmJb.optString("fragmentType", "");
			String fragmentDesc = parmJb.optString("fragmentDesc", "");
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment findFragment = new Fragment();
			findFragment.setFragmentUUID(fragmentUUID);
			List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
			if (fragmentList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			Fragment updateFragment = fragmentList.get(0);
			if (!StringUtil.isNullOrEmpty(fragmentName)) {
				updateFragment.setFragmentName(fragmentName);
			}
			if (!StringUtil.isNullOrEmpty(fragmentType)) {
				updateFragment.setFragmentType(fragmentType);
			}
			if (!StringUtil.isNullOrEmpty(fragmentDesc)) {
				updateFragment.setFragmentDesc(fragmentDesc);
			}
			List<Fragment> sessionFragmentList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_UPDATE);
			if (sessionFragmentList == null || sessionFragmentList.size() == 0) {
				sessionFragmentList = new ArrayList<Fragment>();
			}
			sessionFragmentList.add(updateFragment);
			session.put(Constants.KEY_FRAGMENT_UPDATE, sessionFragmentList);
			// int insertResult = fragmentDao.updateFragment(updateFragment);
			// if (insertResult == 1) {
			resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
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
			String fragmentVersion = parmJb.optString("fragmentVersion", "");
			if (StringUtil.isNullOrEmpty(fragmentVersion)) {

				resultString = StringUtil.packetObject(MethodCode.GET_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			// 这里根据version解析出相对应的fragment

			JSONObject fragmentObj = new JSONObject();
			fragmentObj.put("id", "FRGMc1803bf6c3c347d3a46ae86c3be4e14a");
			fragmentObj.put("name", "testFragment");
			fragmentObj.put("desc", "");
			fragmentObj.put("type", "filter");
			fragmentObj.put("isShare", 0);
			fragmentObj.put("version", "");
			fragmentObj.put("createBy", "test2");
			fragmentObj.put("createTime", "2016-04-25 00:12:45");
			fragmentObj.put("expression", "");// expression直接从当前version解析提取

			resultString = StringUtil.packetObject(MethodCode.GET_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
					"获取某个版本fragment成功", fragmentObj.toString());

		}
		return resultString;
	}

	public String deleteFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment findFragment = new Fragment();
			findFragment.setFragmentUUID(fragmentUUID);
			List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
			if (fragmentList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}
			Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
			// 如果session中没有记录当前场景
			if (sceneActive == null) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
				return resultString;
			}
			Fragment deleteFragment = fragmentList.get(0);
			List<Fragment> sessionFragmentList = (List<Fragment>) session.get(Constants.KEY_FRAGMENT_DELETE);
			if (sessionFragmentList == null || sessionFragmentList.size() == 0) {
				sessionFragmentList = new ArrayList<Fragment>();
			}
			sessionFragmentList.add(deleteFragment);
			session.put(Constants.KEY_FRAGMENT_DELETE, sessionFragmentList);

			resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
					"删除fragment到缓存成功，请注意在切换场景前保存场景数据。", "");

		}
		return resultString;
	}

	public String disableFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment updateFragment = new Fragment();
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.disableFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
						"禁用fragment成功", "");
			}
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
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment updateFragment = new Fragment();
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.disableShareFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在",
						"");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_OK, "", "禁用共享fragment成功", "");
			}
		}
		return resultString;
	}

	public String enableFragment(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment updateFragment = new Fragment();
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.enableFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.ENABLE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
						"启用fragment成功", "");
			}
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
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment updateFragment = new Fragment();
			// updateFragment.setAccountId(currentAccount.getAccountId());
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.enableShareFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在",
						"");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK,
						"", "启用分享fragment成功", "");
			}
		}
		return resultString;
	}

	public String getFragments(String bodyString, Account currentAccount, Map<String, Object> session) {

		String resultString = null;
		Scene sceneActive = (Scene) session.get(Constants.SCENE_ACTIVE);
		// 如果session中没有记录当前场景
		if (sceneActive == null) {
			resultString = StringUtil.packetObject(MethodCode.LIST_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
					ParameterCode.Error.UPDATE_SCENE_NO_MATCH, "当前会话中场景为空，请确认是否已经调用切换场景接口，或者会话已经过期！", "");
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
			dataObj.put("fragmentEnable", fragment.getFragmentEnable() == 0 ? "true" : "false");
			dataObj.put("fragmentActive", fragment.getFragmentActive() == 0 ? "true" : "false");
			dataObj.put("fragmentCreateTime", DateUtil.dateTimeFormat(fragment.getFragmentDate()));

			fragmentJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
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
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_HISTORY,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在",
						"");
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
					ParameterCode.Result.RESULT_OK, "", "获取共享fragment共享历史版本成功", fragmentJsonObj.toString());

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
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}

			ShareFragment shareFragment = new ShareFragment();
			shareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment> shareFragmentList = fragmentDao.findShareFragment(shareFragment);
			if (shareFragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.GET_SHARE_FRAGMENT_VERSION,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在",
						"");
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
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_VERSION_NO_EXIST, "查询的版本号不存在", "");
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
					ParameterCode.Result.RESULT_OK, "", "获取某个版本共享fragment成功", fragmentJsonObj.toString());

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

		resultString = StringUtil.packetObject(MethodCode.LIST_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
				"获取共享fragment列表成功", fragmentJsonArray.toString());

		return resultString;
	}

	// 发布共享fragment版本
	public String releaseShareFragment(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareFragmentUUID = parmJb.optString("shareFragmentUUID", "");
			String shareFragmentComment = parmJb.optString("shareFragmentComment", "");
			String shareFragmentExpression = parmJb.optString("shareFragmentExpression", "");
			String shareFragmentName = parmJb.optString("shareFragmentName", "");
			String shareFragmentType = parmJb.optString("shareFragmentType", "");
			String shareFragmentObjType = parmJb.optString("shareFragmentObjType", "");
			String shareFragmentDesc = parmJb.optString("shareFragmentDesc", "");

			if (StringUtil.isNullOrEmpty(shareFragmentUUID) || StringUtil.isNullOrEmpty(shareFragmentComment)
					|| StringUtil.isNullOrEmpty(shareFragmentExpression)) {

				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment findFragment = new Fragment();
			findFragment.setFragmentUUID(shareFragmentUUID);
			List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
			if (fragmentList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在",
						"");
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
					"", "发布共享fragment成功", "");

		}
		return resultString;
	}
}
