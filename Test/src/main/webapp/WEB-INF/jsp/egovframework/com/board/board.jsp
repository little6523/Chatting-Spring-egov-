<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<meta charset="UTF-8">
<title>게시판 만들기</title>
<style>
body {
	height: 100vh;
	display: flex;
	flex-direction: column;
	align-items: center;
	margin: 0;
}

#content {
	width: 100%;
	height: 30%;
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 3%;
}

table {
	width: 70%;
}

table, td, th {
	border: 1px solid black;
	border-collapse: collapse;
	text-align: center;
}

table th:nth-child(1) {
	width: 5%;
}

table th:nth-child(2) {
	width: 50%;
}

table th:nth-child(3) {
	width: 15%;
}

table th:nth-child(4) {
	width: 15%;
}

#writeBtn {
	width: 70%;
	margin-bottom: 2%;
	text-align: right;
}

#pageGroupBtn {
	width: 70%;
	height: 5%;
	justify-content: center;
	display: flex;
	gap: 10%;
	margin-bottom: 3%;
}

#pageGroupBtn button {
	width: 10%;
}

.currentPage {
	background-color: #4CAF50; /* 배경색 */
	color: white; /* 글자색 */
	border: none; /* 테두리 제거 */
	text-align: center; /* 텍스트 중앙 정렬 */
	font-size: 16px; /* 글자 크기 */
	cursor: pointer; /* 마우스 커서 변경 */
	border-radius: 5px; /* 둥근 테두리 */
}
</style>
</head>
<body>
	<input type="hidden" id="currentPage" value="${currentPage}" />
	<input type="hidden" id="totalPosts" value="${totalPosts}" />
	<h1>게 시 판</h1>
	<p>총 게시글 개수: ${totalPosts}개</p>
	<div id="writeBtn">
		<button>글쓰기</button>
	</div>
	<div id="content">
		<table id="board">
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>작성자</th>
				<th>작성일자</th>
				<th>수정일자</th>
			</tr>
			<c:forEach items="${posts}" var="post">
				<tr>
					<td>${post.id}</td>
					<td><a href="board.do/${post.id}?page=${currentPage}">${post.title}</a></td>
					<td>${post.writer}</td>
					<td>${post.created_at}</td>
					<td>${post.updated_at}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<div id="pageGroupBtn">
		<button type="button" id="first" data-page="">first</button>
		<button type="button" id="prev" data-page="">prev</button>
		<button type="button" id="next" data-page="">next</button>
		<button type="button" id="last" data-page="">last</button>
	</div>
	<div id="pageNumberBtn"></div>
	<script src="../js/egovframework/com/board/common.js"></script>
	<script>
	$(document).ready(function() {
		calPaging($('#currentPage').val());
	});

	$('#writeBtn').click(function() {
		window.location.href = 'postWrite.do';
	});
	
	function calPaging(currentPage) {
		const totalPosts = $('#totalPosts').val();
		let pagesInGroup = 10;
		let postsInPage = 10;
		
		// 페이지 그룹 이동버튼 계산
		currentPageGroup = Math.floor((currentPage - 1) / pagesInGroup + 1);
		let first = 1;
		let prev = (currentPageGroup - 1) * pagesInGroup;
		let next = (currentPageGroup + 1) * pagesInGroup - (pagesInGroup - 1);
		let last = Math.floor((totalPosts - 1) / postsInPage + 1);
		let totalPageGroups = Math.floor((last - 1) / pagesInGroup + 1);
		
		if (totalPageGroups > 1) {
			if (currentPageGroup == 1) {
				$('#next').attr('data-page', next);
				$('#last').attr('data-page', last);
				$('#next').prop('disabled', false);
				$('#last').prop('disabled', false);
				$("#first").prop("disabled", true);
				$("#prev").prop("disabled", true);
			} else if (currentPageGroup == totalPageGroups) {
				$('#first').attr('data-page', first);
				$('#prev').attr('data-page', prev);
				$('#first').prop('disabled', false);
				$('#prev').prop('disabled', false);
				$('#next').prop('disabled', true);
				$('#last').prop('disabled', true);
			} else {
				$('#first').attr('data-page', first);
				$('#prev').attr('data-page', prev);
				$('#next').attr('data-page', next);
				$('#last').attr('data-page', last);
				$('#first').prop('disabled', false);
				$('#prev').prop('disabled', false);
				$('#next').prop('disabled', false);
				$('#last').prop('disabled', false);
			}
		} else {
			$('#first').prop('disabled', true);
			$('#prev').prop('disabled', true);
			$('#next').prop('disabled', true);
			$('#last').prop('disabled', true);
		}

		// 페이지 넘버 계산
		let totalPages = (totalPosts - 1) / postsInPage + 1;
		let startPage = currentPageGroup * pagesInGroup - (pagesInGroup - 1);
		let endPage = currentPageGroup * pagesInGroup;
		if (currentPageGroup == totalPageGroups) {
			endPage = last;
		}
		
		// 페이지숫자버튼 그리기
		$('#pageNumberBtn').html("");
		let button= '';
		for (let i = startPage; i <= endPage; i++) {
			if (i == currentPage) {
				button += '<button class="currentPage" type="button" disabled="true">' + i + '</button>';
				continue;
			}
			button += '<button type="button" data-page="' + i +'">' + i + '</button>';
		}
		$('#pageNumberBtn').append(button);
		
		addMovingPageLink(postsInPage);
	}
	
	function addMovingPageLink(postsInPage) {
		// 페이지 그룹 이동 링크 추가
		$('#pageGroupBtn').children().off('click');
		$('#pageGroupBtn').children().on('click', function() {
			const page = $(this).attr('data-page');
			window.location.href = '/webview/board.do?page=' + page;
		});
		
		// 페이지 이동 링크 추가
		$('#pageNumberBtn').children().on('click', function() {
			const page = $(this).attr('data-page');
			window.location.href = '/webview/board.do?page=' + page;
		});
	}
	
	function addPosts(res) {
		// board라는 id를 가진 테이블의 첫 번째 자식(tr)을 제외한 나머지 요소 제거
		$('#board tr:gt(0)').remove();
		
		const posts = res.posts;
		let post = '';
		for (let i = 0; i < posts.length; i++) {
			post += '<tr>';
			post += '	<td>' + common.nvl(posts[i].id) + '</td>';
			post += '	<td><a href="board.do/' + common.nvl(posts[i].id) + '">' + common.nvl(posts[i].title) + '</a></td>';
			post += '	<td>' + common.nvl(posts[i].writer) + '</td>';
			post += '	<td>' + common.nvl(posts[i].created_at) + '</td>';
			post += '	<td>' + common.nvl(posts[i].updated_at) + '</td>';
			post += '</tr>';
		}
		$('#board').append(post);
	}
</script>
</body>
</html>