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
<script type="text/javascript"
	src="<%=path%>/js/calendar/WdatePicker.js"></script>
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
			<div style="padding-top: 5px; padding-left: 5px;">用户管理</div>
		</div>
		<div style="margin-top: 10px;">
			<form id="search_form" action="<%=path%>/account/list" method="post">

				用户名称： <input type="text" name="searchAccount.accountName"
					style="width: 120px; height: 19px;"
					value="${searchAccount.accountName}" /> 用户状态： <select
					style="width: 120px; height: 25px;"
					name="searchAccount.accountStatus">
					<option value="1"
						<c:if test="${searchAccount.accountStatus==1 }">selected</c:if>>
						所有</option>
					<option value="0"
						<c:if test="${searchAccount.accountStatus==0 }">selected</c:if>>
						正常</option>
					<option value="-1"
						<c:if test="${searchAccount.accountStatus==-1 }">selected</c:if>>
						禁用</option>

				</select> 创建时间: <input id="beginDate" name="searchAccount.beginDate"
					type="text" class="span2" value="${searchAccount.beginDate}"
					style="width: 148px; height: 19px;"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'default',minDate:'2013-01-01',maxDate:'2020-01-01'});$('#endDate').val('')"
					readonly="true" /> -- <input id="endDate"
					name="searchAccount.endDate" type="text" class="span2"
					value="${searchAccount.endDate}" style="width: 148px;"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'default',minDate:'2013-01-01',maxDate:'2020-01-01'});"
					readonly="true" /> <input type="submit" value="搜索" />

			</form>
		</div>
	</div>
	<div id="count_table_admin">
		<table class="stripe">
			<tr>
				<th>序号</th>
				<th>用户名称</th>
				<th>用户邮件</th>
				<th>用户状态</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>

			<s:iterator value="accountList" var="a" status="s">
				<tr>
					<td>${s.index + 1 + offset}</td>
					<td style="width: 25%">${a.accountName}</td>

					<td style="width: 25%">${a.accountEmail}</td>
					<td><c:if test="${a.accountStatus==0}">
							<font color="green">正常</font>
						</c:if> <c:if test="${a.accountStatus==-1}">
							<font color="red">禁用</font>
						</c:if></td>
					<td><s:date format="yyyy-MM-dd HH:mm:ss" name="#a.accountDate"></s:date></td>
					<td><a
						href="<%=path%>/account/initPwd?searchAccount.accountId=${a.accountId}"
						onclick="return confirm('请谨慎操作，确定要初始化该用户密码？')">初始密码</a>&nbsp;|&nbsp;
						<c:if test="${a.accountStatus==-1}">
							<a
								href="<%=path%>/account/enable?searchAccount.accountId=${a.accountId}"
								onclick="return confirm('请谨慎操作，确定启用该用户吗？')">启用</a>
						</c:if> <c:if test="${a.accountStatus==0}">
							<a
								href="<%=path%>/account/disable?searchAccount.accountId=${a.accountId}"
								onclick="return confirm('请谨慎操作，确定禁用该用户吗？')">禁用</a>
						</c:if></td>
				</tr>
			</s:iterator>
		</table>
	</div>
	<div id="div_page"><jsp:include page="/page/common/pager.jsp" /></div>
</body>
</html>