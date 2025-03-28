<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>채팅방 목록</title>
    <link rel="stylesheet" href="/css/roomlist.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="container">
        <!-- 사이드바 - 사용자 프로필 -->
        <div class="sidebar">
            <div class="user-profile">
                <img src="default-avatar.png" alt="프로필" class="profile-img">
                <span class="username" id="username">사용자 이름</span>
            </div>
        </div>

        <!-- 메인 컨텐츠 영역 -->
        <div class="main-content">
            <div class="rooms-header">
                <h2>채팅방 목록</h2>
                <button id="createRoomBtn">새 채팅방 만들기</button>
            </div>

            <!-- 채팅방 목록 -->
            <div class="room-list" id="roomList">
            </div>
        </div>
    </div>

    <!-- 새 채팅방 생성 모달 -->
    <div id="createRoomModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>새 채팅방 만들기</h2>
            <div class="input-group">
                <label for="roomNameInput">채팅방 이름</label>
                <input type="text" id="roomNameInput" placeholder="채팅방 이름을 입력하세요">
            </div>
            <div class="input-group">
                <label for="portInput">포트 주소</label>
                <input type="number" id="portInput" placeholder="포트 번호를 입력하세요" min="1024" max="65535">
            </div>
            <div class="modal-buttons">
                <button id="submitRoom">만들기</button>
                <button id="cancelRoom">취소</button>
            </div>
        </div>
    </div>

    <script src="../../js/roomlist.js"></script>
</body>
</html>