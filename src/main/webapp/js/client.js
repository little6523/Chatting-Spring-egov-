const USER = {
	name: sessionStorage.getItem('username'),
	roomName: document.getElementById('roomName').innerText,
	users: {},
	profileImage: {}
}

$(document).ready(function() {
	const name = sessionStorage.getItem('username');
	data = {}
	data.name = name;

	document.getElementById('username').innerText = name;

	common.sendAjax('post', '/api/profileImages', data, function(response, xhr) {
		document.getElementById("profileImage").src = "data:image/png;base64," + response.image;
		USER.profileImage[USER.name] = response.image;
	});
})

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
			USER.users = json.users;
			document.getElementById("userNumber").textContent = json.users.length;

			const participantsList = document.getElementById("participantsList");
			let userList = "";
			json.users.forEach(user => {

				// 채팅 참가자 프로필 이미지 업데이트
				data = {}
				data.name = user.name;
				common.sendAjax('post', '/api/profileImages', data, function(response, xhr) {
					USER.profileImage[user.name] = response.image;
				});

				// 채팅 참가자 목록 업데이트
				if (user.name == USER.name) {
					userList += "<li class='participant'> 나: " + user.name + "</li>";
				} else {
					userList += "<li>" + user.name + "</li>";
				}
			});
			participantsList.innerHTML = userList;
			return;
		}

		if (json.hasOwnProperty("message")) {
			let chatMessages = document.getElementById("chatMessages");
			let lines = json.message.split('\n');
			let message = "";
			lines.forEach((line, index) => {
				let chat = line.split(':');
				let profile = "<div class='mini-profile'>";
				profile += "<img src='data:image/png;base64," + USER.profileImage[chat[0]] + "' alt='프로필' class='profile-img'>";
				profile += "<span class='username' id='username'>" + chat[0] + "</span>";
				profile += "</div>"
				if (chat[0] == USER.name) {
					message += "<div class='messageBox sent'>";
					message += profile;
					let selfMessage = "<div class='message sent'>" + chat[1] + "</div>";
					message += selfMessage;
				} else {
					message += "<div class='messageBox received'>";
					message += profile;
					let newMessage = "<div class='message received'>" + chat[1] + "</div>";
					message += newMessage;
				}
				message += "</div>"
			});

			chatMessages.innerHTML += message;
		}
	},

	() => {
		SOCKET.connect(document.getElementById('roomName').innerText);
	}
)

function sendMessage() {
	data = {};
	data.userName = USER.name;
	data.roomName = USER.roomName;
	data.message = USER.name + ":" + document.getElementById("messageInput").value;  // 입력된 메시지 가져오기
	SOCKET.socket.send(JSON.stringify(data));  // 메시지 전송

	let message = "<div class='messageBox sent'>"
	let profile = "<div class='mini-profile'>";
	profile += "<img src='data:image/png;base64," + USER.profileImage[data.userName] + "' alt='프로필' class='profile-img'>";
	profile += "<span class='username' id='username'>" + data.userName + "</span>";
	profile += "</div>"
	message += profile;
	let selfMessage = "<div class='message sent'>" + document.getElementById("messageInput").value + "</div>";
	message += selfMessage;
	message += "</div>"
	let chatMessages = document.getElementById("chatMessages");
	chatMessages.innerHTML += message;

	document.getElementById("messageInput").value = "";  // 입력창 초기화
}