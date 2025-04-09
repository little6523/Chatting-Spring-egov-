window.addEventListener('beforeunload', (event) => {
	// 표준에 따라 기본 동작 방지
	event.preventDefault();

	// Chrome에서는 returnValue 설정이 필요함
	event.returnValue = '';
});

const USER = {
	name: sessionStorage.getItem('username'),
	roomName: document.getElementById('roomName').innerText,
	users: [],
	profileImage: {}
}

// 서버로부터 같은 채팅방의 유저들의 프로필 이미지를 받는 메소드
/*function fetchProfileImage() {
	return new Promise((resolve, reject) => {
		const promises = USER.users.map((user) => {
			return new Promise((res, rej) => {
				if (USER.profileImage[user.name] == null) {
					const data = { name: user.name };
					common.sendAjax('post', '/api/profileImages', data, (response, xhr) => {
						if (USER.profileImage[user.name] != response.image) {
							USER.profileImage[user.name] = response.image;
						}
						res(); // 이 사용자 이미지 처리가 끝나면 resolve
					});
				} else {
					res(); // 이미 이미지가 있으면 바로 resolve
				}
			});
		});

		Promise.all(promises)
			.then(() => resolve())  // 모든 사용자 이미지 처리가 끝나면 최종 resolve
			.catch(err => reject(err));
	});
}*/

function fetchProfileImage(username) {
	return new Promise((resolve, reject) => {
		const data = { name: username };
		common.sendAjax('post', '/api/profileImages', data, (response, xhr) => {
			if (USER.profileImage[username] != response.image) {
				USER.profileImage[username] = response.image;
			}
			resolve(username); // 이 사용자 이미지 처리가 끝나면 resolve
		});
	})
}

function updateParticipants(username) {
	USER.users.push(username);
	document.getElementById("userNumber").textContent = USER.users.length;
	const participantsList = document.getElementById("participantsList");

	let userList = "";

	// 채팅 참가자 프로필 이미지 업데이트
	fetchProfileImage(username)
		.then((username) => {
			console.log(username);
			document.getElementById('profileImage').src = "data:image/jpg;base64," + USER.profileImage[username];
		})

	// 채팅 참가자 목록 업데이트
	if (username == USER.name) {
		userList = "<li class='participant'> 나: " + username + "</li>";
	} else {
		userList = "<li>" + username + "</li>";
	}
	participantsList.innerHTML += userList;
}


$(document).ready(function() {
	document.getElementById('username').innerText = USER.name;

	document.getElementById('leaveButton').addEventListener('click', function() {
		if (confirm('정말로 채팅방을 나가시겠습니까?')) {
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
				updateParticipants(USER.name);
				SOCKET.socket.send(JSON.stringify(USER));
			},

			// onmessage
			(event) => {
				const json = JSON.parse(event.data);
				if (json.hasOwnProperty("newUser")) {
					USER.users.push(json.newUser.name);
					updateParticipants();

					return;
				}

				if (json.hasOwnProperty("message")) {
					let chatMessages = document.getElementById("chatMessages");
					let lines = json.message.split('\n');
					let message = "";
					lines.forEach((line, index) => {
						if (line == '') return;
						let chat = line.split(':');

						let profile = "<div class='mini-profile'>";
						profile += "<img src='" + chat[0] + "' alt='프로필' class='profile-img'>";
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
					})

					if (json.hasOwnProperty("init")) {
						fetchProfileImage(message).then(() => {
							USER.users.forEach((user) => {
								message = message.replaceAll("src='" + user.name + "'", 'src="data:image/jpg;base64,' + USER.profileImage[user.name] + '"');
							});
							chatMessages.innerHTML += message;
						});
					} else {
						USER.users.forEach((user) => {
							message = message.replaceAll("src='" + user.name + "'", 'src="data:image/jpg;base64,' + USER.profileImage[user.name] + '"');
						});
					}
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
	profile += "<img src='data:image/jpg;base64," + USER.profileImage[data.userName] + "' alt='프로필' class='profile-img'>";
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