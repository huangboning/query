<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/page/common/commonTaglibs.jsp"%>
<%@ include file="/page/common/commonVariable.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>大数据用户管理系统</title>
<link href="<%=path%>/css/main.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<%=path%>/js/jquery-1.7.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() { //这个就是传说的ready    
		$(".stripe tr").mouseover(function() {
			//如果鼠标移到class为stripe的表格的tr上时，执行函数    
			$(this).addClass("over");
		}).mouseout(function() {
			//给这行添加class值为over，并且当鼠标一出该行时执行函数    
			$(this).removeClass("over");
		});//移除该行的class    
		$(".stripe tr:even").addClass("alt");
		//给class为stripe的表格的偶数行添加class值为alt 
		//www.divcss5.com 整理特效 
	});
</script>
</head>
<body>
	<jsp:include page="/page/common/header.jsp" />
	<div id="div_center">
		<div
			style="width: 980px; height: 30px; color: white; background-color: #316294; margin-top: 20px;">
			<div style="padding-top: 5px; padding-left: 5px;">修改密码</div>
		</div>
		<form id="form1" name="form1" method="post"
			action="<%=path%>/user/updatePwd">
			<table id="login_table">
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>原始密码：</td>
					<td><input value="" type="password" name="oldPassword"
						id="oldPassword" class="input_on" /></td>
				</tr>
				<tr>
					<td>用户密码：</td>
					<td><input value="" type="password" name="newPassword"
						id="newPassword" class="input_on" /></td>
				</tr>
				<tr>
					<td>确认密码：</td>
					<td><input value="" type="password" name="confirmPassword"
						id="confirmPassword" class="input_on" /></td>
				</tr>
				<tr>
					<td colspan="2"><span id="userAccountValid"
						style="color: red;"></span></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td><input type="submit" value="修改" class="common_btn" /></td>
					<td><input type="button" onclick="history.go(-1)" value="返回"
						class="common_btn" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>