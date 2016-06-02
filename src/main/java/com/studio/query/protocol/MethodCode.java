package com.studio.query.protocol;

/**
 * 消息编码string
 * 
 * @author hbn
 * 
 */
public class MethodCode {

	/**
	 * 账号注册
	 */
	public static final String ACCOUNT_REGISTER = "register";
	/**
	 * 账号查询
	 */
	public static final String ACCOUNT_QUERY = "accountQuery";
	/**
	 * 账号登录
	 */
	public static final String ACCOUNT_LOGIN = "login";
	/**
	 * 账号注销
	 */
	public static final String ACCOUNT_LOGOUT = "logout";

	/**
	 * 创建场景
	 */
	public static final String CREATE_SCENE = "createScene";
	/**
	 * 创建场景(旧版本)
	 */
	public static final String CREATE_SCENE_OLD = "createScenario";

	/**
	 * 获取场景列表
	 */
	public static final String LIST_SCENE = "getScenarioes";
	/**
	 * 获取场景版本历史
	 */
	public static final String HISTORY_SCENE = "getScenarioHistory";
	/**
	 * 切换场景
	 */
	public static final String SWITCH_SCENE = "switchScenario";
	/**
	 * 切换场景(旧版本)
	 */
	public static final String SWITCH_SCENE_OLD = "setCurrentScenario";
	/**
	 * 切换场景版本
	 */
	public static final String SWITCH_VERSION = "switchVersion";
	/**
	 * 获取当前版本场景
	 */
	public static final String GET_CURRENT_VERSION = "getCurrentVersion";
	/**
	 * 获取当前版本场景(旧版本)
	 */
	public static final String GET_CURRENT_VERSION_OLD = "getScenario";
	/**
	 * 关闭场景版本
	 */
	public static final String CLOSE_VERSION = "closeScenario";
	/**
	 * 更新场景
	 */
	public static final String UPDATE_SCENE = "updateScenario";
	/**
	 * 创建Fragment
	 */
	public static final String CREATE_FRAGMENT = "createFragment";
	/**
	 * 更新Fragment
	 */
	public static final String UPDATE_FRAGMENT = "updateFragment";
	/**
	 * 获取Fragment
	 */
	public static final String GET_FRAGMENT = "getFragment";
	/**
	 * 删除Fragment
	 */
	public static final String DELETE_FRAGMENT = "deleteFragment";
	/**
	 * 禁用 Fragment
	 */
	public static final String DISABLE_FRAGMENT = "disableFragment";
	/**
	 * 启用 Fragment
	 */
	public static final String ENABLE_FRAGMENT = "enableFragment";
	/**
	 * 发布共享Fragment
	 */
	public static final String RELEASE_SHARE_FRAGMENT = "releaseShareFragment";
	/**
	 * 禁用共享Fragment
	 */
	public static final String DISABLE_SHARE_FRAGMENT = "disableShareFragment";
	/**
	 * 启用共享Fragment
	 */
	public static final String ENABLE_SHARE_FRAGMENT = "enableShareFragment";
	/**
	 * 获取Fragment列表
	 */
	public static final String LIST_FRAGMENT = "getFragments";
	/**
	 * 获取共享Fragment列表
	 */
	public static final String LIST_SHARE_FRAGMENT = "getShareFragments";
	/**
	 * 获取共享Fragment历史记录
	 */
	public static final String GET_SHARE_FRAGMENT_HISTORY = "getShareFragmentHistory";
	/**
	 * 获取共享Fragment某版本
	 */
	public static final String GET_SHARE_FRAGMENT_VERSION = "getShareFragmentVersion";
	/**
	 * 创建 变量
	 */
	public static final String CREATE_VARIABLE = "createVariable";
	/**
	 * 获取变量
	 */
	public static final String GET_VARIABLE = "getVariable";
	/**
	 * 更新 变量
	 */
	public static final String UPDATE_VARIABLE = "updateVariable";
	/**
	 * 删除变量
	 */
	public static final String DELETE_VARIABLE = "deleteVariable";
	/**
	 * 禁用共享变量
	 */
	public static final String DISABLE_SHARE_VARIABLE = "disableShareVariable";
	/**
	 * 启用共享变量
	 */
	public static final String ENABLE_SHARE_VARIABLE = "enableShareVariable";
	/**
	 * 获取变量列表
	 */
	public static final String LIST_VARIABLE = "getVariableList";
	/**
	 * 获取共享变量历史记录
	 */
	public static final String GET_SHARE_VARIABLE_HISTORY = "getShareVariableHistory";
	/**
	 * 获取共享变量某版本
	 */
	public static final String GET_SHARE_VARIABLE_VERSION = "getShareVariableVersion";
	/**
	 * 获取共享变量列表
	 */
	public static final String LIST_SHARE_VARIABLE = "getShareVariables";
	/**
	 * 发布共享变量
	 */
	public static final String RELEASE_SHARE_VARIABLE = "releaseShareVariable";

	/**
	 * ES接口 getIndexDocTypes 获取数据源列表
	 */
	public static final String GET_INDEX_DOC_TYPES = "getIndexDocTypes";
	/**
	 * ES接口 getTableHeadDef 获取选择数据源定义的数据表头
	 */
	public static final String GET_TABLE_HEAD_DEF = "getTableHeadDef";

	/**
	 * ES接口 setScope 设置数据源
	 */
	public static final String SET_SCOPE = "setScope";

	/**
	 * ES接口 getHelpValue 获取提示字段
	 */
	public static final String GET_HELP_VALUE = "getHelpValue";

	/**
	 * ES接口 getInputTypes 获取一个DocTypes下面所有的field列表
	 */
	public static final String GET_INPUT_TYPES = "getInputTypes";

	/**
	 * ES接口 getHintFields 获取field列表
	 */
	public static final String GET_HINT_FIELDS = "getHintFields";

	/**
	 * ES接口 getGeocoding 查询位置
	 */
	public static final String GET_GEOCODING = "getGeocoding";

	/**
	 * ES接口 executeScenario 执行场景查询
	 */
	public static final String EXECUTE_SCENE = "executeScenario";

	/**
	 * ES接口 executeScenario 执行场景查询下一页
	 */
	public static final String NEXT_PAGE = "nextPage";
}
