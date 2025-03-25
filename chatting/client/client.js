const USER = {
    connect: '1', // 1: 새로 생성됨을 의미함
    name: "user" + Math.random(),
    message: null
}

const socket = new WebSocket('ws://localhost:12345');  // 서버 주소와 포트 설정

// 서버 - 클라이언트 소켓 연결
socket.onopen = function(event) {
    console.log("WebSocket 연결 성공");
    socket.send(JSON.stringify(USER));
};

// 서버로 메시지를 보내는 함수
function sendMessage() {
    const message = document.getElementById("messageInput").value;  // 입력된 메시지 가져오기
    USER.connect = '2';
    USER.message = message;
    socket.send(JSON.stringify(USER));  // 메시지 전송
    document.getElementById("messageInput").value = "";  // 입력창 초기화

    let selfMessage = "<div class='message sent'>" + '나: ' + message + "</div>";
    let chatMessages = document.getElementById("chatMessages");
    chatMessages.innerHTML += selfMessage;
}

// 메시지 수신 처리
socket.onmessage = function(event) {
    const chatMessages = document.getElementById("chatMessages");

    let newMessage = "<div class='message received'>" + event.data + "</div>";
    chatMessages.innerHTML += newMessage;
}

// 전송 버튼 클릭 이벤트
document.getElementById("sendButton").addEventListener("click", sendMessage);

document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('nicknameModal');
    const nicknameInput = document.getElementById('nicknameInput');
    const submitButton = document.getElementById('submitNickname');

    submitButton.addEventListener('click', function() {
        const nickname = nicknameInput.value.trim();
        if (nickname) {
            // 닉네임을 저장하고 모달 닫기
            USER.name = nickname;
            modal.style.display = 'none';
        } else {
            alert('닉네임을 입력해주세요!');
        }
    });
});
