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
			<div style="padding-top: 5px; padding-left: 5px;">帐号信息</div>
		</div>
		<div style="height: 30px; background-color:; margin-top: 10px;">
			<a href="#">信息修改</a>&nbsp;&nbsp;

		</div>
		<table style="border: 0; width: 980px; background-color: #e6e6e6">
			<tr style="background-color: #ffffff; height: 40px;">
				<td style="background-color: #f1eeee; width: 15%;">真实姓名</td>
				<td style="width: 35%;">${userInfo.userName}</td>
				<td style="background-color: #f1eeee; width: 15%;">手机</td>
				<td style="width: 35%;"></td>
			</tr>
			<tr style="background-color: #ffffff; height: 40px;">
				<td style="background-color: #f1eeee; width: 15%;">地址</td>
				<td style="width: 35%;"></td>
				<td style="background-color: #f1eeee; width: 15%;">学校</td>
				<td style="width: 35%;"></td>
			</tr>
			<tr style="background-color: #ffffff; height: 40px;">
				<td style="background-color: #f1eeee; width: 15%;">email</td>
				<td style="width: 35%;"></td>
				<td style="background-color: #f1eeee; width: 15%;">角色</td>
				<td style="width: 35%;"></td>
			</tr>

		</table>
	</div>
</body>
</html>