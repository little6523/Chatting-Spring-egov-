<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CK Talk - 마이페이지</title>
<link rel="stylesheet" href="/css/mypage.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div class="mypage-container">
		<div class="mypage-box">
			<div class="app-header">
				<img src="/images/ckInfo2.png" alt="채팅 앱 로고" class="app-logo">
				<h1 class="app-title">마이페이지</h1>
			</div>
			<form id="mypageForm" class="mypage-form">
				<div class="profile-section">
					<div class="profile-image-container">
						<img id="currentProfile" src="" alt="현재 프로필" class="profile-image">
						<input type="file" id="profileImage" name="profileImage"
							accept="image/*" style="display: none;">
						<button type="button" id="changeProfileBtn" class="profile-button">프로필
							변경</button>
					</div>
				</div>
				<div class="input-group">
					<label for="nickname">닉네임</label>
					<input type="text" id="nickname" name="nickname" placeholder="새로운 닉네임을 입력하세요">
				</div>
				<div class="input-group">
					<label for="newPassword">새 비밀번호</label>
					<input type="password" id="newPassword" name="newPassword" placeholder="새 비밀번호를 입력하세요">
				</div>
				<div class="input-group">
					<label for="confirmPassword">비밀번호 확인</label>
					<input type="password" id="confirmPassword" name="confirmPassword" placeholder="새 비밀번호를 다시 입력하세요">
				</div>
				<div id="passwordError" class="error-message"></div>
				<button id="updateButton" class="update-button">정보 수정</button>
				<button id="returnButton" type="button" class="cancel-button">돌아가기</button>
			</form>
		</div>
	</div>
	<script src="/js/mypage.js"></script>
	<script src="/js/ajax.js"></script>
</body>
</html>