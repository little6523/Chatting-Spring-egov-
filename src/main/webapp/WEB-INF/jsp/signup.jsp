<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>CK Talk - 회원가입</title>
<link rel="stylesheet" href="/css/signup.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="signup-container">
        <div class="signup-box">
            <div class="app-header">
                <img src="/images/ckInfo2.png" alt="채팅 앱 로고" class="app-logo">
                <h1 class="app-title">회원가입</h1>
            </div>
            <form id="signupForm" class="signup-form">
                <div class="profile-section">
                    <div class="profile-image-container">
                        <img id="currentProfile" src="/images/default-profile.png" alt="프로필 이미지" class="profile-image">
                        <input type="file" id="profileImage" name="profileImage" accept="image/*" style="display: none;">
                        <button type="button" id="changeProfileBtn" class="profile-button">프로필 이미지 선택</button>
                    </div>
                </div>
                <div class="input-group">
                    <label for="userId">아이디 *</label>
                    <div class="input-with-button">
                        <input type="text" id="userId" name="userId" required placeholder="아이디를 입력하세요">
                        <button type="button" id="checkIdBtn" class="check-button">중복확인</button>
                    </div>
                    <div id="idError" class="error-message"></div>
                </div>
                <div class="input-group">
                    <label for="password">비밀번호 *</label>
                    <input type="password" id="password" name="password" required placeholder="비밀번호를 입력하세요">
                </div>
                <div class="input-group">
                    <label for="confirmPassword">비밀번호 확인 *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="비밀번호를 다시 입력하세요">
                    <div id="passwordError" class="error-message"></div>
                </div>
                <div class="input-group">
                    <label for="nickname">닉네임</label>
                    <div class="input-with-button">
                        <input type="text" id="nickname" name="nickname" placeholder="닉네임을 입력하세요">
                        <button type="button" id="checkNicknameBtn" class="check-button">중복확인</button>
                    </div>
                    <div id="nicknameError" class="error-message"></div>
                </div>
                <button id="signupButton" class="signup-button">가입하기</button>
                <button type="button" id="returnButton" class="cancel-button">돌아가기</button>
            </form>
        </div>
    </div>
    <script src="/js/signup.js"></script>
    <script src="/js/ajax.js"></script>
</body>
</html>