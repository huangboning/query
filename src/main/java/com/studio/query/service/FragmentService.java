package com.studio.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studio.query.dao.FragmentDao;
import com.studio.query.entity.Account;
import com.studio.query.entity.Fragment;
import com.studio.query.entity.ShareFragment;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.util.DateUtil;
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

	public String createFragment(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentName = parmJb.optString("fragmentName", "");
			String fragmentType = parmJb.optString("fragmentType", "");
			String fragmentDesc = parmJb.optString("fragmentDesc", "");
			String fragmentExpression = parmJb.optString("fragmentExpression", "");// git保存expression
			if (StringUtil.isNullOrEmpty(fragmentName) || StringUtil.isNullOrEmpty(fragmentType)) {

				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment findFragment = new Fragment();
			findFragment.setFragmentName(fragmentName);
			List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
			if (fragmentList.size() >= 1) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.CREATE_FRAGMENT_EXIST, "fragment已经存在", "");
				return resultString;
			}

			Fragment insertFragment = new Fragment();
			insertFragment.setAccountId(currentAccount.getAccountId());
			insertFragment.setFragmentUUID(StringUtil.createFragmentUUID());
			insertFragment.setFragmentName(fragmentName);
			insertFragment.setFragmentType(fragmentType);
			insertFragment.setFragmentDesc(fragmentDesc);
			int insertResult = fragmentDao.insertFragment(insertFragment);
			if (insertResult == 1) {
				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
						"创建fragment成功", "");
			} else {

				resultString = StringUtil.packetObject(MethodCode.CREATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.CREATE_FRAGMENT_FAIL, "创建fragment失败", "");
			}
		}
		return resultString;
	}

	public String updateFragment(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String fragmentUUID = parmJb.optString("fragmentUUID", "");
			String fragmentType = parmJb.optString("fragmentType", "");
			String fragmentDesc = parmJb.optString("fragmentDesc", "");
			String fragmentExpression = parmJb.optString("fragmentExpression", "");// git保存expression
			if (StringUtil.isNullOrEmpty(fragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			Fragment findFragment = new Fragment();
			findFragment.setFragmentUUID(fragmentUUID);
			List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
			if (fragmentList.size() < 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "查询的fragment不存在", "");
				return resultString;
			}

			Fragment updateFragment = new Fragment();
			updateFragment.setAccountId(currentAccount.getAccountId());
			updateFragment.setFragmentUUID(fragmentUUID);
			updateFragment.setFragmentDesc(fragmentDesc);
			int insertResult = fragmentDao.updateFragment(updateFragment);
			if (insertResult == 1) {
				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
						"更新fragment成功", "");
			} else {

				resultString = StringUtil.packetObject(MethodCode.UPDATE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.UPDATE_FRAGMENT_FAIL, "更新fragment失败", "");
			}
		}
		return resultString;
	}

	public String getFragment(String bodyString, Account currentAccount) {
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

	public String deleteFragment(String bodyString, Account currentAccount) {

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
			findFragment.setAccountId(currentAccount.getAccountId());
			findFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.deleteFragment(findFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DELETE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
						"删除fragment成功", "");
			}
		}
		return resultString;
	}

	public String disableFragment(String bodyString, Account currentAccount) {

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
			updateFragment.setAccountId(currentAccount.getAccountId());
			updateFragment.setFragmentUUID(fragmentUUID);
			int result = fragmentDao.disableFragment(updateFragment);
			if (result < 1) {
				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT, ParameterCode.Result.RESULT_FAIL,
						ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "fragment不存在", "");
				return resultString;
			} else {

				resultString = StringUtil.packetObject(MethodCode.DISABLE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
						"禁用fragment成功", "");
			}
		}
		return resultString;
	}

	public String disableShareFragment(String bodyString, Account currentAccount) {

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
			updateFragment.setAccountId(currentAccount.getAccountId());
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

	public String enableFragment(String bodyString, Account currentAccount) {

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
			updateFragment.setAccountId(currentAccount.getAccountId());
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

	public String enableShareFragment(String bodyString, Account currentAccount) {

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
			updateFragment.setAccountId(currentAccount.getAccountId());
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

	public String getFragments(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONArray fragmentJsonArray = new JSONArray();
		Fragment findFragment = new Fragment();
		findFragment.setAccountId(currentAccount.getAccountId());
		List<Fragment> fragmentList = fragmentDao.findFragment(findFragment);
		for (int i = 0; i < fragmentList.size(); i++) {

			Fragment fragment = fragmentList.get(i);
			JSONObject dataObj = new JSONObject();
			dataObj.put("fragmentUUID", fragment.getFragmentUUID());
			dataObj.put("fragmentName", fragment.getFragmentName());
			dataObj.put("fragmentDesc", fragment.getFragmentDesc());
			dataObj.put("fragmentType", fragment.getFragmentType());
			dataObj.put("fragmentEnable", fragment.getFragmentEnable()==1?"true":"false");
			dataObj.put("fragmentActive", fragment.getFragmentActive()==1?"true":"false");
			dataObj.put("fragmentCreatedBy", currentAccount.getAccountName());
			dataObj.put("fragmentCreateTime", DateUtil.dateTimeFormat(fragment.getFragmentDate()));
			dataObj.put("fragmentExpression", "");

			fragmentJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
				"获取fragment列表成功", fragmentJsonArray.toString());

		return resultString;
	}

	public String getShareFragments(String bodyString, Account currentAccount) {

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
			dataObj.put("shareFragmentEnable", shareFragment.getShareFragmentEnable()==1?"true":"false");
			dataObj.put("shareFragmentActvie", shareFragment.getShareFragmentActive()==1?"true":"false");
			dataObj.put("shareFragmentCreatedBy", shareFragment.getAccountName());
			dataObj.put("shareFragmentCreateTime", DateUtil.dateTimeFormat(shareFragment.getShareFragmentDate()));
			dataObj.put("shareFragmentVersion", shareFragment.getShareFragmentVersion());
			//dataObj.put("expression", "");

			fragmentJsonArray.add(dataObj);
		}

		resultString = StringUtil.packetObject(MethodCode.LIST_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK, "",
				"获取共享fragment列表成功", fragmentJsonArray.toString());

		return resultString;
	}
	
	//发布共享版本
	public String releaseShareFragment(String bodyString, Account currentAccount) {

		String resultString = null;
		JSONObject jb = JSONObject.fromObject(bodyString);
		JSONObject parmJb = JSONObject.fromObject(jb.optString("params", ""));
		if (parmJb != null) {
			String shareFragmentUUID = parmJb.optString("shareFragmentUUID", "");
			if (StringUtil.isNullOrEmpty(shareFragmentUUID)) {

				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER, "必要参数不足", "");
				return resultString;
			}
			
			ShareFragment findShareFragment = new ShareFragment();
			findShareFragment.setAccountId(currentAccount.getAccountId());
			findShareFragment.setShareFragmentUUID(shareFragmentUUID);
			List<ShareFragment>shareFragmentList = fragmentDao.findShareFragment(findShareFragment);
			if (shareFragmentList.size() != 1) {
				resultString = StringUtil.packetObject(MethodCode.RELEASE_SHARE_FRAGMENT,
						ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.QUERY_FRAGMENT_NO_EXIST, "共享fragment不存在",
						"");
				return resultString;
			} else {
				//查看
				
                //查看分享表中是否已经有该记录
				
				
				resultString = StringUtil.packetObject(MethodCode.ENABLE_SHARE_FRAGMENT, ParameterCode.Result.RESULT_OK,
						"", "启用分享fragment成功", "");
			}
		}
		return resultString;
	}
}
