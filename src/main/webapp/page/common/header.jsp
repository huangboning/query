<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/page/common/commonTaglibs.jsp"%>
<%@ include file="/page/common/commonVariable.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<div id="div_header_bg">
	<div id="div_header">
		<div style="height: 65px"></div>
		<div>
			<table style="border: 0px; margin: 0px; width: 980px">
				<tr>
					<td width="300px" id="div_header_title">大数据用户管理系统</td>
					<td style="width: 50px">&nbsp;</td>
					<td width="204px" id="div_header_logout"><c:if
							test="${zq_query_user==null}">
							<a href="<%=path%>/page/login.jsp">登录</a>
						</c:if> <c:if test="${zq_query_user!=null}">
						当前登录用户：${zq_query_user.userAccount}，<a href="<%=path%>/logout">注销</a>
						</c:if></td>
				</tr>
			</table>
		</div>
	</div>
</div>
<c:if test="${zq_query_user!=null}">
	<div
		style="overflow:hidden;width: 980px; height: 50px; margin-right: auto; margin-left: auto; background-color: #f1eeee">
		<div>&nbsp;</div>
		<div>

			<div style="width: 80px; height: 25px; float: left;">
				<a href="<%=path%>/user/info">帐号管理</a>
			</div>
			
			<div style="width: 80px; height: 25px; float: left;margin-left: 10px">
				<a href="<%=path%>/user/list">用户管理</a>
			</div>
		</div>
	</div>
</c:if>
</html>