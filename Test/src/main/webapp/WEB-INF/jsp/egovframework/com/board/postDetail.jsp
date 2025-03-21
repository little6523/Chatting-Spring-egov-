<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<meta charset="UTF-8">
<title>게시글 상세보기</title>
<style>
body {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin: 0;
}

#editButton {
	width: 70%;
	display: flex;
	justify-content: right;
	margin-bottom: 2%;
}

#edit {
	width: 5%;
}

table {
	width: 70%;
	height: 50vh;
}

th {
	text-align: center;
}

table, td, th {
	border: 1px solid black;
	border-collapse: collapse;
}

.title {
	color: red;
}

.title>:first-child {
	width: 10%;
	height: 10%;
	color: blue;
}

.content {
	heigth: 30%;
}

#boardBtn {
	margin-top: 5%;
}
</style>
</head>
<body>
	<input type="hidden" value="${page}" id="currentPage" />
	<input type="hidden" value="${data.id}" id="postId" />
	<h1>게 시 글 상 세 보 기</h1>
	<div>
		<p>아래는 게시글의 내용입니다.</p>
	</div>
	<div id="editButton">
		<button type="button" id="edit">수정</button>
	</div>
	<table id="board">
		<tr class="title">
			<th>제목</th>
			<th id="title"><h2>${data.title}</h2></th>
		</tr>
		<tr class="content">
			<th>내용</th>
			<td id="content">${data.content}</td>
		</tr>
	</table>
	<button id="boardBtn">목록으로 돌아가기</button>
	<script src="../../js/egovframework/com/board/common.js"></script>
	<script>
	$('#boardBtn').click(function() {
		window.location.href = '/webview/board.do?page=' + $('#currentPage').val();
	});
	
	$('#edit').click(function() {
		window.location.href = '/webview/postWrite.do/' + $('#postId').val() + '?page=' + $('#currentPage').val();
	});
	</script>
</body>
</html>