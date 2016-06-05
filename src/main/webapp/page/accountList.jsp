<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/page/common/commonTaglibs.jsp"%>
<%@ include file="/page/common/commonVariable.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>大数据用户管理系统</title>
<link href="<%=path%>/css/manager.css" type="text/css" rel="stylesheet" />
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
			style="width: 980px; height: 30px; color: white; background-color: #1D8004; margin-top: 20px;">
			<div style="padding-top: 5px; padding-left: 5px;">系统公告</div>
		</div>
		<div style="margin-top: 10px;">
			<form id="search_form" action="<%=path%>/smsmanager/listNotice.do"
				method="post">

				公告名称： <input type="text" name="searchNotice.noticeTitle"
					style="width: 120px; height: 19px;"
					value="${searchNotice.noticeTitle}" /> 创建时间: <input id="beginDate"
					name="searchNotice.beginDate" type="text" class="span2"
					value="${searchNotice.beginDate}"
					style="width: 148px; height: 19px;"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'default',minDate:'2013-01-01',maxDate:'2020-01-01'});$('#endDate').val('')"
					readonly="true" /> -- <input id="endDate"
					name="searchNotice.endDate" type="text" class="span2"
					value="${searchNotice.endDate}" style="width: 148px;"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'default',minDate:'2013-01-01',maxDate:'2020-01-01'});"
					readonly="true" /> <input type="submit" value="搜索" />

			</form>
		</div>
		<div style="margin-top: 10px;">
			<a href="<%=path%>/smsmanager/noticeAdd.jsp">新增公告</a>
		</div>
	</div>
	<div id="count_table_admin">
		<table class="stripe">
			<tr>
				<th>序号</th>
				<th>公告名称</th>
				<th>公告内容</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>

			<s:iterator value="noticeList" var="a" status="s">
				<tr>
					<td>${s.index + 1 + offset}</td>
					<td style="width: 25%">${a.noticeTitle}</td>

					<td style="width: 45%">${a.noticeContent}</td>
					<td><s:date format="yyyy-MM-dd HH:mm:ss"
							name="#a.noticeCreateDate"></s:date></td>
					<td><a
						href="<%=path%>/smsmanager/updateNotice.do?updateNotice.noticeId=${a.noticeId}">编辑</a>&nbsp;&nbsp;
						<a
						href="<%=path%>/smsmanager/deleteNotice.do?updateNotice.noticeId=${a.noticeId}"
						onclick="return confirm('请谨慎操作，确定删除该公告吗？')">删除</a></td>
				</tr>
			</s:iterator>
		</table>
	</div>
	<div id="div_page"><jsp:include page="/page/common/pager.jsp" /></div>
</body>
</html>