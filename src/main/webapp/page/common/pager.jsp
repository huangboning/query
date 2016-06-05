<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ include file="/page/common/commonTaglibs.jsp"%>
<%@ taglib uri="/WEB-INF/jsptags.tld" prefix="p"%>
<script type="text/javascript">
	function goPager(p) {
		var searchForm = $('#search_form');
		var offsetNum = (parseInt(p) - 1) * parseInt($('#pageLimit').val());
		var offset = $('#offset');
		offset.val(offsetNum);
		$(searchForm).append(offset);
		searchForm.submit();
	}
</script>

<div>
	<ul>
		<p:pager url="${url}" items="${count}" maxPageItems="${limit}"
			export="currentPageNumber=pageNumber">
			<p:param name="keywords" />
			<p:index>
				<p:first>
					<li style="margin-left: -18px;"><a
						href="javascript:goPager(${pageNumber})">[首页]</a></li>
				</p:first>
				<p:prev>
					<li><a href="javascript:goPager(${pageNumber})">[上一页]</a></li>
				</p:prev>

				<p:pages>
					<c:if test="${pageNumber == currentPageNumber}">
						<li class="active"><a href="#"
							style="color: red; text-decoration: underline;">${pageNumber}</a>
						</li>
					</c:if>
					<c:if test="${pageNumber != currentPageNumber}">
						<li><a href="javascript:goPager(${pageNumber})">${pageNumber}</a>
						</li>
					</c:if>
				</p:pages>
				<p:next>
					<li><a href="javascript:goPager(${pageNumber})">[下一页]</a></li>
				</p:next>
				<p:last>
					<li><a href="javascript:goPager(${pageNumber})">[尾页]</a></li>
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