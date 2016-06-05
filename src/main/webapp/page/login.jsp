<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/page/common/commonTaglibs.jsp"%>
<%@ include file="/page/common/commonVariable.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>大数据用户管理系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=path%>/css/main.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=path%>/js/jquery-1.7.min.js"></script>
<script type="text/javascript" src="<%=path%>/js/md5.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

	});
	function submitForm() {
		var userAccount = $.trim($("#userAccount").val());
		var userPassword = $.trim($("#userPassword").val());

		if (userAccount != "" && userPassword != "") {
			$("#userPassword").val(MD5(userPassword));
			$.post($("#form1").attr("action"), $("#form1").serialize(),
					function(data) {
						var result = $.parseJSON(data);
						if (result.code == 0) {
							window.location.href = result.result;
						} else {
							$("#userAccountValid").text("用户名或密码错误!");
							$("#userPassword").val("");
							return false;
						}
					});
		} else {
			$("#userAccountValid").text("请输入用户名和密码!");
			return false;

		}
	}
	function submitRegister(path) {
		//alert(getContextPath()+'/pages/register.jsp');
		//window.location.href=getContextPath()+'/pages/register.jsp';
		window.location.href = path + '/pages/register.jsp';

	}
	function getContextPath() {
		var pathName = document.location.pathname;
		var index = pathName.substr(1).indexOf("/");
		var result = pathName.substr(0, index + 1);
		return result;
	}
</script>
<body>
	<jsp:include page="/page/common/header.jsp" />
	<div id="div_center">
		<form id="form1" name="form1" method="post"
			action="<%=path%>/login">
			<table id="login_table">

				<tr>
					<td>用户帐号：</td>
					<td><input value="" type="text" name="userAccount"
						id="userAccount" class="input_on" /></td>
				</tr>
				<tr>
					<td>用户密码：</td>
					<td><input value="" type="password" name="userPassword"
						id="userPassword" class="input_on" /></td>
				</tr>
				<tr>
					<td colspan="2"><span id="userAccountValid"
						style="color: red;"></span></td>
				</tr>
				<tr>
					<td><input type="submit" value="登录" class="common_btn"
						onclick="submitForm();return false;" /></td>
					<td><input type="reset" value="取消" class="common_btn" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>