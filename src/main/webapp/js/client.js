const USER = {
	name: sessionStorage.getItem('username'),
	roomName: document.getElementById('roomName').innerText
}

document.getElementById('username').innerText = sessionStorage.getItem('username');
const roomName = USER.roomName;

SOCKET.connect(document.getElementById('roomName').innerText);

document.getElementById('leaveButton').addEventListener('click', function() {
    if (confirm('정말로 채팅방을 나가시겠습니까?')) {
		
		data = {}
		data.close = true;
		data.roomName = roomName;
		data.user = USER;
		SOCKET.socket.send(JSON.stringify(data));
		
        // 웹소켓 연결 종료
        SOCKET.socket.close();
        // 메인 페이지로 리다이렉트
        window.location.href = '/webview/chat';
    }
});

SOCKET.init(
	// onopen
	() => {
		console.log("WebSocket 연결 성공");
		SOCKET.socket.send(JSON.stringify(USER));
	},
	
	// onmessage
	(event) => {
		const json = JSON.parse(event.data);
		if (json.hasOwnProperty("users")) {
			document.getElementById("userNumber").textContent = json.users.length;

			const participantsList = document.getElementById("participantsList");
			let userList = "";
			json.users.forEach(user => {
				if (user.name == USER.name) {
					userList += "<li class='participant'> 나: " + user.name + "</li>";
				} else {
					userList += "<li>" + user.name + "</li>";
				}
			});
			participantsList.innerHTML = userList;
			return;
		}

		if (json.hasOwnProperty("name") && json.hasOwnProperty("message")) {
			const chatMessages = document.getElementById("chatMessages");
			let newMessage = "<div class='message received'>" + json.name + ": " + json.message + "</div>";
			chatMessages.innerHTML += newMessage;
			return;
		}
	},
	
	// onclose
	() => {

	}
	
	)

function sendMessage() {
	data = {};
	data.roomName = roomName;
	data.message = document.getElementById("messageInput").value;  // 입력된 메시지 가져오기
	SOCKET.socket.send(JSON.stringify(data));  // 메시지 전송
	document.getElementById("messageInput").value = "";  // 입력창 초기화

	let selfMessage = "<div class='message sent'>" + '나: ' + data.message + "</div>";
	let chatMessages = document.getElementById("chatMessages");
	chatMessages.innerHTML += selfMessage;
}