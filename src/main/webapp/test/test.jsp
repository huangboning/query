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
<body>
	<jsp:include page="/page/common/header.jsp" />
	<div id="div_center">
		<form id="form1" name="form1" method="post" action="<%=path%>/validate">
			<table id="login_table">

				<tr>
					<td>验证场景：</td>
					<td></td>
				</tr>
				<tr>
					<td>验证内容：</td>
					<td><textarea rows="30" cols="100" name="vstrJson"></textarea></td>
				</tr>
				<tr>
					<td colspan="2"><span id="userAccountValid"
						style="color: red;"></span></td>
				</tr>
				<tr>
					<td><input type="submit" value="验证" class="common_btn" /></td>
					<td><input type="reset" value="重置" class="common_btn" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>