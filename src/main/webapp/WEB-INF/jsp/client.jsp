<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>채팅 애플리케이션</title>
<link rel="stylesheet" href="/css/client.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div class="container">
		<!-- 사이드바 - 참가자 목록 -->
		<div class="sidebar">
			<div class="user-profile">
				<img src="default-avatar.png" alt="프로필" class="profile-img"> <span
					class="username" id="username">사용자 이름</span>
			</div>
			<button id="leaveButton" class="leave-button">나가기</button>
			<h3>참가자 목록</h3>
			<ul class="participants-list" id="participantsList">
				<!-- 참가자들이 여기에 동적으로 추가됩니다 -->
			</ul>
		</div>

		<!-- 메인 채팅 영역 -->
		<div class="main-content">
			<!-- 채팅방 정보 -->
			<div class="chat-header">
				<h2 id="roomName">${roomName}</h2>
				<div class="chat-info">
					<span class="participant-count">참가자: </span> <span
						class="participant-count" id="userNumber">0</span>
				</div>
			</div>

			<!-- 채팅 메시지 영역 -->
			<div class="chat-messages" id="chatMessages">
				<!-- 메시지들이 여기에 동적으로 추가됩니다 -->
			</div>

			<!-- 메시지 입력 영역 -->
			<div class="message-input-area">
				<input type="text" id="messageInput" placeholder="메시지를 입력하세요...">
				<button id="sendButton" onclick="sendMessage()">전송</button>
				<button id="fileButton" class="file-button">📎</button>
			</div>
		</div>
	</div>
	<script src="/js/socket.js"></script>
	<script src="/js/client.js"></script>
</body>
</html>