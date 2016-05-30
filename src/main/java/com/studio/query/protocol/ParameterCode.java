package com.studio.query.protocol;

/**
 * 参数代码
 * 
 * @author hbn
 * 
 */
public class ParameterCode {

	public static final class Result {

		/**
		 * 成功
		 */
		public static String RESULT_OK = "200";
	}

	public static final class Error {
		// ============接口公共错误编码==============
		/**
		 * 接口-消息编码不存在
		 */
		public static final String SERVICE_METHOD_CODE = "-1";
		/**
		 * 接口-参数错误
		 */
		public static final String SERVICE_PARAMETER = "-2";

		/**
		 * 接口-服务器解密失败
		 */
		public static final String SERVICE_DECRYPT = "-3";
		/**
		 * 接口-服务器业务处理错误
		 */
		public static final String SERVICE_RESOLVE = "-4";
		/**
		 * 接口-服务器无效的请求
		 */
		public static final String SERVICE_INVALID = "-5";
		/**
		 * 接口-服务器请求被拒绝
		 */
		public static final String SERVICE_REFUSE = "-6";
		/**
		 * 接口-服务器未知错误
		 */
		public static final String SERVICE_UNKNOWN = "-7";
		/**
		 * 接口-服务器会话过期
		 */
		public static final String SERVICE_SESSION = "-8";

		// ============接口业务错误编码==============
		/**
		 * 账号注册-账号已经存在
		 */
		public static final String ACCOUNT_REGISTER_EXIST = "-101";
		/**
		 * 账号注册-账号注册失败
		 */
		public static final String ACCOUNT_REGISTER_FAIL = "-102";
		/**
		 * 账号登录-验证失败
		 */
		public static final String ACCOUNT_LOGIN_VALIDATE = "-103";
		/**
		 * 创建场景-创建场景失败
		 */
		public static final String CREATE_SCENE_FAIL = "-104";
		/**
		 * 创建场景-场景已经存在
		 */
		public static final String CREATE_SCENE_EXIST = "-105";
		/**
		 * 创建fragment-创建fragment失败
		 */
		public static final String CREATE_FRAGMENT_FAIL = "-106";
		/**
		 * 更新fragment-更新fragment失败
		 */
		public static final String UPDATE_FRAGMENT_FAIL = "-107";
		/**
		 * 创建fragment-fragment已经存在
		 */
		public static final String CREATE_FRAGMENT_EXIST = "-108";
		/**
		 * 获取场景-查询的场景不存在
		 */
		public static final String QUERY_SCENE_NO_EXIST = "-109";
		/**
		 * 查询fragment-fragment不存在
		 */
		public static final String QUERY_FRAGMENT_NO_EXIST = "-110";
		/**
		 * 创建变量-创建变量失败
		 */
		public static final String CREATE_VARIABLE_FAIL = "-111";
		/**
		 * 更新变量-更新变量失败
		 */
		public static final String UPDATE_VARIABLE_FAIL = "-112";
		/**
		 * 创建变量-变量已经存在
		 */
		public static final String CREATE_VARIABLE_EXIST = "-113";
		/**
		 * 查询变量-变量不存在
		 */
		public static final String QUERY_VARIABLE_NO_EXIST = "-114";
		/**
		 * 更新场景-提交的场景跟会话中设置当前的场景不匹配
		 */
		public static final String UPDATE_SCENE_NO_MATCH = "-115";
		/**
		 * 查询版本-查询的版本号不存在
		 */
		public static final String QUERY_VERSION_NO_EXIST = "-116";

	}
}
