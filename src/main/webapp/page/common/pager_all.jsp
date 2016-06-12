<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/page/common/commonTaglibs.jsp"%>
<%@ taglib prefix="p" uri="http://jsptags.com/tags/navigation/pager"%>

<div>
	<ul>
		<p:pager url="/test" items="${count}" maxPageItems="${pager.limit}"
			maxIndexPages="15" export="currentPageNumber=pageNumber">
			<p:param name="method" value="search" />
			<p:index>
				<p:first>
					<li style="margin-left: -18px;"><a href="${pageUrl }">[首页]</a></li>
				</p:first>
				<p:prev>
					<li><a href="${pageUrl }">[上一页]</a></li>
				</p:prev>

				<p:pages>
					<c:if test="${pageNumber == currentPageNumber}">
						<li class="active"><a href="#"
							style="color: red; text-decoration: underline;">${pageNumber}</a>
						</li>
					</c:if>
					<c:if test="${pageNumber != currentPageNumber}">
						<li><a href="${pageUrl }">${pageNumber}</a></li>
					</c:if>
					haha:${currentPageNumber}
				</p:pages>
				<p:next>
					<li><a href="${pageUrl }">[下一页]</a></li>
				</p:next>
				<p:last>
					<li><a href="${pageUrl }">[尾页]</a></li>
				</p:last>
			</p:index>
		</p:pager>
		<li><a>总共 <b>${count}</b> 条记录
		</a></li>
	</ul>

	<input type="hidden" name="curPageNumber" id="curPageNumber" /> <input
		type="hidden" name="pageLimit" value="${limit}" id="pageLimit" /> <input
		type="hidden" name="offset" id="offset" />
</div>