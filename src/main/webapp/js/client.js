window.addEventListener('beforeunload', (event) => {
	// 표준에 따라 기본 동작 방지
	event.preventDefault();

	// Chrome에서는 returnValue 설정이 필요함
	event.returnValue = '';
});

const USER = {
	name: sessionStorage.getItem('username'),
	roomName: document.getElementById('roomName').innerText,
}

const ROOM_INFO = {
	roomName: document.getElementById('roomName').innerText,
	users: [],
	profileImages: {}
}

function fetchProfileImage(username) {
	return new Promise((resolve, reject) => {
		const data = { name: username };
		common.sendAjax('post', '/api/profileImages', data, (response, xhr) => {
			if (!ROOM_INFO.profileImages.hasOwnProperty(username) || ROOM_INFO.profileImages[username] != response.image) {
				ROOM_INFO.profileImages[username] = response.image;
			}
			resolve(username); // 이 사용자 이미지 처리가 끝나면 resolve
		});
	})
}

function updateParticipants(username, type) {
	const participantsList = document.getElementById("participantsList");
	if (type == 'in') {
		ROOM_INFO.users.push(username);

		// 채팅 참가자 목록 업데이트
		let userList = "";

		if (username == USER.name) {
			userList = "<li class='participant'> 나: " + username + "</li>";
		} else {
			userList = "<li>" + username + "</li>";
		}
		participantsList.insertAdjacentHTML('beforeend', userList);

		// 채팅 참가자 프로필 이미지 업데이트
		if (USER.name != username) {
			fetchProfileImage(username)
				.then((username) => {
					console.log(username);
				})
		}
	}

	if (type == 'out') {
		ROOM_INFO.users = ROOM_INFO.users.filter(user => user != username);

		const participants = participantsList.querySelectorAll('li');

		participants.forEach(participant => {
			if (participant.textContent.includes(username)) {
				participantsList.removeChild(participant);
			}
		});
	}

	document.getElementById("userNumber").textContent = ROOM_INFO.users.length;
}

function makeChatbox(username, chat) {
	/*	let chatMessages = document.getElementById("chatMessages");*/
	let message = "";

	let profile = "<div class='mini-profile'>";
	profile += "<img src='data:image/jpg;base64," + ROOM_INFO.profileImages[username] + "' alt='프로필' class='profile-img'>";
	profile += "<span class='username' id='username'>" + username + "</span>";
	profile += "</div>"

	if (username == USER.name) {
		message += "<div class='messageBox sent'>";
		message += profile;
		let selfMessage = "<div class='message sent'>" + chat + "</div>";
		message += selfMessage;
	} else {
		message += "<div class='messageBox received'>";
		message += profile;
		let newMessage = "<div class='message received'>" + chat + "</div>";
		message += newMessage;
	}
	message += "</div>"

	return message;
}

$(document).ready(function() {
	ROOM_INFO.profileImages[USER.name] = sessionStorage.getItem('profileImage');
	document.getElementById("profileImage").src = "data:image/jpg;base64," + ROOM_INFO.profileImages[USER.name];
	document.getElementById('username').innerText = USER.name;

	document.getElementById('returnButton').addEventListener('click', function() {
		if (confirm('정말로 채팅방을 나가시겠습니까?')) {
			// 웹소켓 연결 종료
			SOCKET.socket.close();
			// 메인 페이지로 리다이렉트
			window.location.href = '/webview/chat';
		}
	});
	
	document.getElementById('leaveButton').addEventListener('click', function() {
		if (confirm('정말로 채팅방을 \'탈퇴\'하시겠습니까? (기존의 채팅 내용은 사라집니다.)')) {
			data = {}
			data.nickname = sessionStorage.getItem('nickname');
			data.roomName = sessionStorage.getItem('roomname');
			common.sendAjax('post', '/api/exitRoom', data, function(response, xhr) {})
			// 웹소켓 연결 종료
			SOCKET.socket.close();
			// 메인 페이지로 리다이렉트
			window.location.href = '/webview/chat';
		}
	});

	SOCKET.connect(document.getElementById('roomName').innerText).then(() => {
		SOCKET.init(
			// onopen
			() => {
				console.log("WebSocket 연결 성공");
				SOCKET.socket.send(JSON.stringify(USER));
			},

			// onmessage
			(event) => {
				const json = JSON.parse(event.data);
				if (json.hasOwnProperty("participants")) {
					json.participants.forEach(participant => {
						updateParticipants(participant.name, 'in');
					})

					return;
				}

				if (json.hasOwnProperty("newUser")) {
					updateParticipants(json.newUser.name, 'in');

					return;
				}

				if (json.hasOwnProperty("outUser")) {
					updateParticipants(json.outUser.name, 'out');

					return;
				}

				if (json.hasOwnProperty("message")) {
					let chatMessages = document.getElementById("chatMessages");
					let message = "";

					// 채팅 참여 후 채팅 한 번씩 전송받을 때
					if (json.message.length == 1) {
						message += makeChatbox(json.message.userName, json.message.message);
					}

					// 저장된 채팅 불러올 때 (저장된 채팅이 1개일 때는 위의 분기문 통해도 상관없음)
					if (json.message.length > 1) {
						let lines = json.message.split('\n');
						lines.forEach((line, index) => {
							if (line == '') return;
							let chat = line.split(':');
							message += makeChatbox(chat[0], chat[1]);
						})
					}

					chatMessages.insertAdjacentHTML('beforeend', message);
				}
			},

			() => {
				console.log('소켓 연결이 종료 되었습니다.')
				SOCKET.connect(document.getElementById('roomName').innerText);
			}
		)
	})
})

function sendMessage() {
	data = {};
	data.userName = USER.name;
	data.roomName = USER.roomName;
	data.message = USER.name + ":" + document.getElementById("messageInput").value;  // 입력된 메시지 가져오기
	SOCKET.socket.send(JSON.stringify(data));  // 메시지 전송

	let message = "<div class='messageBox sent'>"
	let profile = "<div class='mini-profile'>";
	profile += "<img src='data:image/jpg;base64," + ROOM_INFO.profileImages[data.userName] + "' alt='프로필' class='profile-img'>";
	profile += "<span class='username' id='username'>" + data.userName + "</span>";
	profile += "</div>"
	message += profile;
	let selfMessage = "<div class='message sent'>" + document.getElementById("messageInput").value + "</div>";
	message += selfMessage;
	message += "</div>"
	let chatMessages = document.getElementById("chatMessages");
	chatMessages.insertAdjacentHTML('beforeend', message);

	document.getElementById("messageInput").value = "";  // 입력창 초기화
}