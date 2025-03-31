const USER = {
	name: sessionStorage.getItem('username'),
	roomName: document.getElementById('roomName').innerText
}

document.getElementById('username').innerText = sessionStorage.getItem('username');
const roomName = USER.roomName;

SOCKET.connect(document.getElementById('roomName').innerText);

SOCKET.init(
	() => {
		console.log("WebSocket 연결 성공");
		SOCKET.socket.send(JSON.stringify(USER));
	},
	
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
	
	() => {
		data = {}
		data.close = true;
		data.roomName = roomName;
		data.user = USER;
		SOCKET.socket.send(JSON.stringify(data));
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

/*function connectWebSocket() {
	const modal = document.getElementById('nicknameModal');
	const nicknameInput = document.getElementById('nicknameInput');

	const nickname = nicknameInput.value.trim();
	if (nickname) {
		// 닉네임을 저장하고 모달 닫기
		USER.name = nickname;
		document.getElementById('username').textContent = nickname;
		modal.style.display = 'none';
	} else {
		alert('닉네임을 입력해주세요!');
	}

	SOCKET.socket.send(JSON.stringify(USER));
}*/