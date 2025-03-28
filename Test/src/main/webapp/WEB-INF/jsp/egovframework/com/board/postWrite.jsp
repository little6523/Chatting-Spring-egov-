<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/html/egovframework/com/cmm/utl/ckeditor/ckeditor.js"></script>
<meta charset="UTF-8">
<title>게시글 작성</title>
<style>
body {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin: 0;
}

table {
	width: 70%;
}

table, td, th {
	border-collapse: collapse;
	text-align: center;
}

#title {
	width: 100%;
}

#postWriteBtn {
	margin-top: 5%;
}
</style>
</head>
<body>
	<input type="hidden" id="currentPage" value="${page}" />
	<c:if test="${not empty post}">
		<input type="hidden" value="${post.id}" id="postId" />
	</c:if>
	<c:if test="${empty post}">
		<input type="hidden" value="-1" id="postId" />
	</c:if>
	<h1>게 시 글 작 성</h1>
	<p>아래의 항목들을 작성하여 주시기 바랍니다.</p>

	<table id="board">
		<tr>
			<td>제목</td>
			<td><c:choose>
					<c:when test="${not empty post}">
						<input type="text" id="title" value="${post.title}" />
					</c:when>
					<c:otherwise>
						<input type="text" id="title" />
					</c:otherwise>
				</c:choose></td>
		</tr>
		<tr>
			<td>글 내용</td>
			<td><c:choose>
					<c:when test="${not empty post}">
						<textarea id="content">${post.content}</textarea>
					</c:when>
					<c:otherwise>
						<textarea id="content"></textarea>
					</c:otherwise>
				</c:choose></td>
		</tr>
	</table>
	<div id="postWriteBtn">
		<button id="write">등록</button>
		<button id="cancel">취소</button>
	</div>
	<c:choose>
		<c:when test="${not empty post}">
			<script src="../../js/egovframework/com/board/common.js"></script>
		</c:when>
		<c:otherwise>
			<script src="../js/egovframework/com/board/common.js"></script>
		</c:otherwise>
	</c:choose>
	<script>
	
	// CKEditor 적용
	$(function(){
		CKEDITOR.replace('content', {
			filebrowserUploadUrl: '/api/board/image',
			filebrowserImageUploadUrl: '/api/board/image'
		});
	});
	
	$('#write').click(function() {
		const data = {};
		data.title = $('#title').val();
		data.content = CKEDITOR.instances['content'].getData();
		
	    let isEmpty = false;

	    if (data.title == "" || data.content == "") {
	        isEmpty = true;
	    }

	    // 값이 비어있는 input이 있을 경우 알람창을 띄우고 버튼 동작 막기
	    if (isEmpty) {
	        alert("모든 필드를 채워주세요.");
	        return;
	    }
		
		// postId가 -1이면 새로 글쓰기 or -1이외의 값이면 기존 글 수정
		if ($('#postId').val() == -1) {
			common.sendAjax('post', '/api/board', data, (res, xhr) => {
				console.log(res);
				window.location.href = 'board.do' + xhr.getResponseHeader("Location") + '?page=' + $('#currentPage').val();
			});
		} else {
			common.sendAjax('patch', '/api/board/' + $('#postId').val(), data, (res, xhr) => {
				console.log(res);
				window.location.href = '/webview/board.do/' + $('#postId').val() + '?page=' + $('#currentPage').val();
			});
		}
	});
	
	$('#cancel').click(function() {
		window.location.href = '/webview/board.do?page=' + $('#currentPage').val();
	})
	</script>
</body>
</html>