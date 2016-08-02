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
import com.studio.query.service.NoteService;
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
	public SceneService sceneService;
	@Autowired
	public FragmentService fragmentService;
	@Autowired
	public VariableService variableService;
	@Autowired
	public NoteService noteService;
	@Autowired
	public QueryService queryService;

	Logger loger = Logger.getLogger(InterfaceAction.class);

	public String execute() {
		try {
			String returnString = null;
			// 验证是否允许请求
			if (checkRequest()) {
				byte[] data = getRequestData();
				// 如果data长度小于1，说明必要参数没有填写完整。
				if (data.length < 1) {
					this.thorwError("", ParameterCode.Error.SERVICE_INVALID, "无效的请求包体", "");
					return null;
				}
				String bodyString = ConversionTools.bytesToString(data, "utf-8");
				loger.info("request data=" + bodyString);
				JSONObject jb = JSONObject.fromObject(bodyString);
				String methodCode = jb.optString("method", "");
				if (StringUtil.isNullOrEmpty(methodCode)) {
					this.thorwError("", ParameterCode.Error.SERVICE_PARAMETER, "无效的参数", "");
					return null;
				}
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
						this.thorwError("", ParameterCode.Error.SERVICE_SESSION, "会话已经过期，请重新登录。", "");
						return null;
					}

					if (methodCode.equals(MethodCode.CREATE_SCENE) || methodCode.equals(MethodCode.CREATE_SCENE_OLD)) {
						returnString = sceneService.createScene(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.SET_SCOPE)) {
						returnString = accountService.setScope(bodyString, session);
					} else if (methodCode.equals(MethodCode.GET_TABLE_HEAD_DEF)) {
						returnString = queryService.getTableHeadDef(session);
					} else if (methodCode.equals(MethodCode.LIST_SCENE)) {
						returnString = sceneService.getSceneList(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.HISTORY_SCENE)) {
						returnString = sceneService.getSceneHistory(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.SWITCH_SCENE)
							|| methodCode.equals(MethodCode.SWITCH_SCENE_OLD)) {
						returnString = sceneService.switchScene(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.SWITCH_VERSION)) {
						returnString = sceneService.switchVersion(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_CURRENT_VERSION)
							|| methodCode.equals(MethodCode.GET_CURRENT_VERSION_OLD)) {
						returnString = sceneService.getCurrentVersion(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.CLOSE_SCENE)) {
						returnString = sceneService.closeScenario(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.OPEN_SCENE)) {
						returnString = sceneService.openScenario(bodyString, currentAccount, session);
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
					} else if (methodCode.equals(MethodCode.GET_TEMPLATES)) {
						returnString = fragmentService.getTemplates(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.CREATE_VARIABLE)) {
						returnString = variableService.createVariable(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_VARIABLE)) {
						returnString = variableService.getVariable(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.UPDATE_VARIABLE)) {
						returnString = variableService.updateVariable(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.DELETE_VARIABLE)) {
						returnString = variableService.deleteVariable(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.DISABLE_SHARE_VARIABLE)) {
						// 未实现
						// returnString =
						// variableService.disableShareVariable(bodyString,
						// currentAccount, session);
					} else if (methodCode.equals(MethodCode.ENABLE_SHARE_VARIABLE)) {
						// 未实现
						// returnString =
						// variableService.enableShareVariable(bodyString,
						// currentAccount, session);
					} else if (methodCode.equals(MethodCode.LIST_VARIABLE)) {
						returnString = variableService.getVariablesList(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.LIST_SHARE_VARIABLE)) {
						returnString = variableService.getShareVariables(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.RELEASE_TEMPLATE)) {
						returnString = fragmentService.releaseTemplate(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.REFERENCE_TEMPLATE)) {
						returnString = fragmentService.referenceTemplate(bodyString, session);
					} else if (methodCode.equals(MethodCode.INSTANCE_TEMPLATE)) {
						returnString = fragmentService.instanceTemplate(bodyString, session);
					} else if (methodCode.equals(MethodCode.UPDATE_TEMPLATE)) {
						returnString = fragmentService.transferTemplateInstance(bodyString, session);
					} else if (methodCode.equals(MethodCode.RELEASE_SHARE_VARIABLE)) {
						returnString = variableService.releaseShareVariable(bodyString, currentAccount);
					} else if (methodCode.equals(MethodCode.GET_TEMPLATE_HISTORY)) {
						returnString = fragmentService.getTemplateHistory(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_TEMPLATE_VERSION)) {
						returnString = fragmentService.getTemplateVersion(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_SHARE_VARIABLE_HISTORY)) {
						returnString = variableService.getShareVariableHistory(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.GET_SHARE_VARIABLE_VERSION)) {
						returnString = variableService.getShareVariableVersion(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.ACCOUNT_INFO)) {
						returnString = accountService.doAccountInfoLogic(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.ACCOUNT_PWD_UPDATE)) {
						returnString = accountService.doAccountPwdUpdateLogic(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.LIST_NOTE)) {
						returnString = noteService.doListNoteLogic(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.ADD_NOTE)) {
						returnString = noteService.doAddNoteLogic(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.UPDATE_NOTE)) {
						returnString = noteService.doUpdateNoteLogic(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.DELETE_NOTE)) {
						returnString = noteService.doDeleteNoteLogic(bodyString, currentAccount, session);
					} else if (methodCode.equals(MethodCode.PRE_VALIDATE)) {
						returnString = fragmentService.doPreValidateLogic(bodyString, session);
					} else if (methodCode.equals(MethodCode.GET_INDEX_DOC_TYPES)) {
						returnString = queryService.getIndexDocTypes(bodyString);
					} else if (methodCode.equals(MethodCode.GET_HELP_VALUE)) {
						returnString = queryService.getHelpValue(bodyString);
					} else if (methodCode.equals(MethodCode.GET_INPUT_TYPES)) {
						returnString = queryService.getInputTypes(bodyString);
					} else if (methodCode.equals(MethodCode.GET_HINT_FIELDS)) {
						returnString = queryService.getHintFields(bodyString);
					} else if (methodCode.equals(MethodCode.GET_GEOCODING)) {
						returnString = queryService.getGeocoding(bodyString);
					} else if (methodCode.equals(MethodCode.EXECUTE_SCENE)) {
						returnString = queryService.executeScenario(bodyString, session);
					} else if (methodCode.equals(MethodCode.NEXT_PAGE)) {
						returnString = queryService.nextPage(bodyString, session);
					} else {
						this.thorwError("", ParameterCode.Error.SERVICE_PARAMETER, "该接口未实现", "");
						return null;
					}
				}

				if (StringUtil.isNullOrEmpty(returnString)) {
					this.thorwError("", ParameterCode.Error.SERVICE_RESOLVE, "服务器业务处理错误", "");
					return null;
				}
				loger.info("return data=" + returnString);
				response.setHeader("Content-Type", "application/json;charset=utf-8");
				response.getOutputStream().write(returnString.getBytes("utf-8"));
				return null;
			} else {
				this.thorwError("", ParameterCode.Error.SERVICE_REFUSE, "请求被服务器拒绝", "");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			loger.info(e.toString());
			this.thorwError("", ParameterCode.Error.SERVICE_UNKNOWN, "服务器未知错误", "");
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

	private void thorwError(String optCode, String statusCode, String message, String baseObject) {

		try {
			JSONObject o = new JSONObject();
			o.put("optCode", optCode);
			o.put("statusCode", statusCode);
			o.put("message", message);
			if (StringUtil.isNullOrEmpty(baseObject)) {
				o.put("baseObject", "{}");
			} else {
				o.put("baseObject", baseObject);
			}
			response.setHeader("Content-Type", "application/json;charset=utf-8");
			response.getOutputStream().write(o.toString().getBytes("utf-8"));
			loger.info("return data=" + o.toString());
		} catch (Exception e) {
			loger.info(e.toString());
		}
	}

	public void register() {

		this.execute();
	}

	public void login() {

		this.execute();
	}

	public void accountQuery() {

		this.execute();
	}

	public void logout() {

		this.execute();
	}

	public void createScene() {

		this.execute();
	}

	public void createScenario() {

		this.execute();
	}

	public void getScenarioes() {
		this.execute();
	}

	public void getScenarioHistory() {
		this.execute();
	}

	public void switchScenario() {
		this.execute();
	}

	public void setCurrentScenario() {
		this.execute();
	}

	public void switchVersion() {
		this.execute();
	}

	public void getCurrentVersion() {
		this.execute();
	}

	public void getScenario() {
		this.execute();
	}

	public void closeScenario() {
		this.execute();
	}

	public void openScenario() {
		this.execute();
	}

	public void updateScenario() {
		this.execute();
	}

	public void createFragment() {
		this.execute();
	}

	public void updateFragment() {
		this.execute();
	}

	public void getFragment() {
		this.execute();
	}

	public void deleteFragment() {
		this.execute();
	}

	public void disableFragment() {
		this.execute();
	}

	public void enableFragment() {
		this.execute();
	}

	public void disableShareFragment() {
		this.execute();
	}

	public void enableShareFragment() {
		this.execute();
	}

	public void getFragments() {
		this.execute();
	}

	public void releaseTemplate() {
		this.execute();
	}

	public void referenceTemplate() {
		this.execute();
	}

	public void instanceTemplate() {
		this.execute();
	}

	public void transferTemplateInstance() {
		this.execute();
	}

	public void getTemplates() {
		this.execute();
	}

	public void getTemplateHistory() {
		this.execute();
	}

	public void getTemplateVersion() {
		this.execute();
	}

	public void createVariable() {
		this.execute();
	}

	public void getVariable() {
		this.execute();
	}

	public void updateVariable() {
		this.execute();
	}

	public void deleteVariable() {
		this.execute();
	}

	public void disableShareVariable() {
		this.execute();
	}

	public void enableShareVariable() {
		this.execute();
	}

	public void getVariablesList() {
		this.execute();
	}

	public void getShareVariableHistory() {
		this.execute();
	}

	public void getShareVariableVersion() {
		this.execute();
	}

	public void getShareVariables() {
		this.execute();
	}

	public void releaseShareVariable() {
		this.execute();
	}

	public void accountInfo() {
		this.execute();
	}

	public void accountPwdUpdate() {
		this.execute();
	}

	public void getPriority() {
		this.execute();
	}

	public void getNotes() {
		this.execute();
	}

	public void addNote() {
		this.execute();
	}

	public void updateNote() {
		this.execute();
	}

	public void deleteNote() {
		this.execute();
	}

	public void preValidate() {
		this.execute();
	}

	public void getIndexDocTypes() {
		this.execute();
	}

	public void getTableHeadDef() {
		this.execute();
	}

	public void setScope() {
		this.execute();
	}

	public void getHelpValue() {
		this.execute();
	}

	public void getInputTypes() {
		this.execute();
	}

	public void getHintFields() {
		this.execute();
	}

	public void getGeocoding() {
		this.execute();
	}

	public void executeScenario() {
		this.execute();
	}

	public void nextPage() {
		this.execute();
	}
}
