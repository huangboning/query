package com.studio.query.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studio.query.common.Configure;
import com.studio.query.common.ConversionTools;
import com.studio.query.entity.Account;
import com.studio.query.protocol.MethodCode;
import com.studio.query.protocol.ParameterCode;
import com.studio.query.service.AccountService;
import com.studio.query.service.FragmentService;
import com.studio.query.service.QueryService;
import com.studio.query.service.SceneService;
import com.studio.query.service.VariableService;
import com.studio.query.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * android服务器请求接口
 * 
 * @author hbn
 * 
 */
@Component
public class InterfaceAction extends BaseAction {
	private static final long serialVersionUID = -9033227137921675079L;
	@Autowired
	public AccountService accountService;
	@Autowired
	public QueryService queryService;
	@Autowired
	public SceneService sceneService;
	@Autowired
	public FragmentService fragmentService;
	@Autowired
	public VariableService variableService;

	Logger loger = Logger.getLogger(InterfaceAction.class);

	public String execute() {
		try {
			String returnString = null;
			// 验证是否允许请求
			if (checkRequest()) {
				byte[] data = getRequestData();
				// 如果data长度小于1，说明必要参数没有填写完整。
				if (data.length < 1) {
					this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_INVALID,
							"无效的请求包体", "");
					return null;
				}
				String bodyString = ConversionTools.bytesToString(data, "utf-8");
				loger.info(bodyString);
				JSONObject jb = JSONObject.fromObject(bodyString);
				String methodCode = jb.optString("method", "");
				if (StringUtil.isNullOrEmpty(methodCode)) {
					this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER,
							"无效的参数", "");
					return null;
				}
				loger.info("methodCode=" + methodCode);
				// 如果是注册或者是登录、登出接口，则不需要判断session是否过期
				if (methodCode.equals(MethodCode.ACCOUNT_REGISTER) || methodCode.equals(MethodCode.ACCOUNT_LOGIN)
						|| methodCode.equals(MethodCode.ACCOUNT_LOGOUT)
						|| methodCode.equals(MethodCode.ACCOUNT_QUERY)) {
					if (methodCode.equals(MethodCode.ACCOUNT_REGISTER)) {
						returnString = accountService.doRegisterLogic(bodyString, session);
					}
					if (methodCode.equals(MethodCode.ACCOUNT_LOGIN)) {
						returnString = accountService.doLoginLogic(bodyString, session);
					}
					if (methodCode.equals(MethodCode.ACCOUNT_LOGOUT)) {
						returnString = accountService.doLogoutLogic(bodyString, session);
					}
					if (methodCode.equals(MethodCode.ACCOUNT_QUERY)) {
						returnString = accountService.doQueryLogic(bodyString);
					}
				} else {
					Account currentAccount = (Account) session.get(Configure.systemSessionAccount);
					if (currentAccount == null) {
						this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_SESSION,
								"回话已经过期，请重新登录。", "");
						return null;
					}
					if (methodCode.equals(MethodCode.GET_INDEX_DOC_TYPES)) {
						returnString = queryService.getIndexDocTypes(bodyString);
					} else if (methodCode.equals(MethodCode.CREATE_SCENE)) {
						returnString = sceneService.createScene(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.SET_SCOPE)) {
						returnString = queryService.setScope(bodyString);
					} else if (methodCode.equals(MethodCode.GET_TABLE_HEAD_DEF)) {
						returnString = queryService.getTableHeadDef(bodyString);
					} else if (methodCode.equals(MethodCode.LIST_SCENE)) {
						returnString = sceneService.getSceneList(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.HISTORY_SCENE)) {
						returnString = sceneService.getSceneHistory(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.SWITCH_SCENE)) {
						returnString = sceneService.switchScene(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_SCENE_VERSION)) {
						returnString = sceneService.getSceneVersion(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.CLOSE_VERSION)) {
						returnString = sceneService.closeSceneVersion(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.UPDATE_SCENE)) {
						returnString = sceneService.updateScene(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.CREATE_FRAGMENT)) {
						returnString = fragmentService.createFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.UPDATE_FRAGMENT)) {
						returnString = fragmentService.updateFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_FRAGMENT)) {
						returnString = fragmentService.getFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.DELETE_FRAGMENT)) {
						returnString = fragmentService.deleteFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.DISABLE_FRAGMENT)) {
						returnString = fragmentService.disableFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.ENABLE_FRAGMENT)) {
						returnString = fragmentService.enableFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.DISABLE_SHARE_FRAGMENT)) {
						returnString = fragmentService.disableShareFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.ENABLE_SHARE_FRAGMENT)) {
						returnString = fragmentService.enableShareFragment(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.LIST_FRAGMENT)) {
						returnString = fragmentService.getFragments(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.LIST_SHARE_FRAGMENT)) {
						returnString = fragmentService.getShareFragments(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.CREATE_VARIABLE)) {
						returnString = variableService.createVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.GET_VARIABLE)) {
						returnString = variableService.getVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.UPDATE_VARIABLE)) {
						returnString = variableService.updateVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.DELETE_VARIABLE)) {
						returnString = variableService.deleteVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.DISABLE_SHARE_VARIABLE)) {
						returnString = variableService.disableShareVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.ENABLE_SHARE_VARIABLE)) {
						returnString = variableService.enableShareVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.LIST_VARIABLE)) {
						returnString = variableService.getVariables(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.LIST_SHARE_VARIABLE)) {
						returnString = variableService.getShareVariables(bodyString, currentAccount);
					} else {
						this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_PARAMETER,
								"该接口未实现", "");
						return null;
					}
				}

				if (StringUtil.isNullOrEmpty(returnString)) {
					this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_RESOLVE,
							"服务器业务处理错误", "");
					return null;
				}
				response.getOutputStream().write(returnString.getBytes("utf-8"));
				return null;
			} else {
				this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_REFUSE, "请求被服务器拒绝",
						"");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
			this.thorwError("", ParameterCode.Result.RESULT_FAIL, ParameterCode.Error.SERVICE_UNKNOWN, "服务器未知错误", "");
			return null;
		}
	}

	/**
	 * 检查请求
	 * 
	 * @return
	 * @throws IOException
	 */
	private boolean checkRequest() {
		// String type = request.getHeader("Content-type");
		// if (!"application/android-stream".equals(type)) {
		// return false;
		// } else {
		return true;
		// }
	}

	private byte[] getRequestData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			InputStream in = request.getInputStream();
			byte[] data = new byte[1024];
			int index = in.read(data);
			while (index > -1) {
				out.write(data, 0, index);
				index = in.read(data);
			}

		} catch (Exception e) {
		}
		return out.toByteArray();
	}

	private void thorwError(String optCode, String statusCode, String errorCode, String message, String baseObject) {

		try {
			JSONObject o = new JSONObject();
			o.put("optCode", optCode);
			o.put("statusCode", statusCode);
			o.put("errorCode", errorCode);
			o.put("message", message);
			o.put("baseObject", baseObject);
			response.setHeader("Content-type", "text/html;charset=utf-8");
			response.getOutputStream().write(o.toString().getBytes("utf-8"));
		} catch (Exception e) {
			loger.info(e.toString());
		}
	}
}
