<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib tagdir="/WEB-INF/tags/util" prefix="iot"%>
<h2 class="my-5 text-primary">
	<i class="fas fa-users"></i> 회원 목록
</h2>
<div class="text-right">
	<a href="create?page=${pi.page}"><i class="fas fa-user-plus"></i>
		추가</a> (총 : ${pi.totalCount} 명)
</div>
<table class="table table-striped table-hover">
	<tr>
		<th>No</th>
		<th>사용자 ID</th>
		<th>이름</th>
		<th>email</th>
		<th>전화번호</th>
		<th>등록일</th>
	</tr>
	<c:forEach var="member" items="${pi.list}" varStatus="status">
		<tr>
			<td>${pi.totalCount-((pi.page-1)*10)- status.index}</td>
			<td><a href="view/${member.userId}?page=${pi.page}">
					${member.userId} <iot:newToday test="${member.regDate}" />
			</a></td>
			<td>${member.name}</td>
			<td>${member.email}</td>
			<td>${member.phone}</td>
			<td><fmt:formatDate value="${member.regDate}"
					pattern="yyyy-MM-dd" /></td>
		</tr>
	</c:forEach>
</table>
<iot:pagination pageInfo="${pi}" />