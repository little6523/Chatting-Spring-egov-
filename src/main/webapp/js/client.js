// 새로고침 F5 방지
window.addEventListener('beforeunload', (event) => {
	// 표준에 따라 기본 동작 방지
	event.preventDefault();

	// Chrome에서는 returnValue 설정이 필요함
	event.returnValue = '';
});

const USER = {
	name: sessionStorage.getItem('nickname'),
	roomName: document.getElementById('roomName').innerText,
}

const ROOM_INFO = {
	roomSeq: sessionStorage.getItem('roomSeq'),
	roomName: document.getElementById('roomName').innerText,
	users: {},   // 소켓은 연결되어있지 않으나 채팅방에 참여중인 유저
	currentUsers: [],   // 현재 소켓 연결된 유저 
	profileImages: {}
}

function initParticipants() {
	return new Promise((resolve, reject) => {
		data = {}
		data.roomSeq = sessionStorage.getItem('roomSeq');
		common.sendAjax('post', '/api/participants', data, function(response, xhr) {
			response.participants.forEach(participant => {
				ROOM_INFO.users[participant.seq] = participant.nickname;
			});
			resolve();
		})
	})
}

// 서버로 프로필 이미지를 요청하는 메소드
function fetchProfileImage() {
	const promises = Object.keys(ROOM_INFO.users).map(seq => {
		return new Promise((resolve, reject) => {
			const data = { userSeq: seq };
			common.sendAjax('post', '/api/profileImages', data, (response, xhr) => {
				if (!ROOM_INFO.profileImages.hasOwnProperty(seq) || ROOM_INFO.profileImages[seq] != response.image) {
					ROOM_INFO.profileImages[seq] = response.image;
				}
				resolve(); // 반드시 이 안에서 resolve!
			});
		});
	});
	return Promise.all(promises);
}

// 채팅 참가자를 업데이트하는 메소드
function updateParticipants(username, type) {
	const participantsList = document.getElementById("participantsList");
	if (type == 'in') {
		ROOM_INFO.currentUsers.push(username);

		let userList = "";

		if (username == USER.name) {
			userList = "<li class='participant'> 나: " + username + "</li>";
		} else {
			userList = "<li>" + username + "</li>";
		}
		participantsList.insertAdjacentHTML('beforeend', userList);

		if (USER.name != username) {
			fetchProfileImage(username)
				.then((username) => {})
		}
	}

	if (type == 'out') {
		ROOM_INFO.currentUsers = ROOM_INFO.currentUsers.filter(user => user != username);

		const participants = participantsList.querySelectorAll('li');

		participants.forEach(participant => {
			if (participant.textContent.includes(username)) {
				participantsList.removeChild(participant);
			}
		});
	}

	document.getElementById("userNumber").textContent = ROOM_INFO.currentUsers.length;
}

// 채팅 말풍선을 추가하는 메소드
function makeChatbox(json) {
	let chatMessages = document.getElementById("chatMessages");
	let message = "";

	let lines = json.message.split('\n');
	lines.forEach((line, index) => {
		if (line == '') return;
		let chat = line.split(':');
		let profile = "<div class='mini-profile'>";
		profile += "<img src='data:image/jpg;base64," + ROOM_INFO.profileImages[chat[0]] + "' alt='프로필' class='profile-img'>";
		profile += "<span class='username' id='username'>" + ROOM_INFO.users[chat[0]] + "</span>";
		profile += "</div>"

		if (chat[0] == sessionStorage.getItem('seq')) {
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

	chatMessages.insertAdjacentHTML('beforeend', message);
	scrollToBottom();
}

$(document).ready(function() {
	initParticipants()
		.then(() => {
			fetchProfileImage()
				.then(() => {
					imageLoadingDone = true;
					showBufferedMessages();
					const userSeq = sessionStorage.getItem('seq');
					document.getElementById("profileImage").src = "data:image/jpg;base64," + ROOM_INFO.profileImages[userSeq];
				}); // 모든 이미지 fetch 끝날 때까지 기다림
		})
		.then(() => {
			connectSocket(); // 이제 안전하게 소켓 연결
		});

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
			common.sendAjax('post', '/api/exitRoom', data, function(response, xhr) { })
			// 웹소켓 연결 종료
			SOCKET.socket.close();
			// 메인 페이지로 리다이렉트
			window.location.href = '/webview/chat';
		}
	});
})

let messageBuffer = [];
let imageLoadingDone = false;

function showBufferedMessages() {
	messageBuffer.forEach(msg => makeChatbox(msg));
	messageBuffer = [];
}

function connectSocket() {
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
					if (!imageLoadingDone) {
						messageBuffer.push(json);
						return
					}
					makeChatbox(json);
				}
			},

			() => {
				console.log('소켓 연결이 종료 되었습니다.')
				SOCKET.connect(document.getElementById('roomName').innerText);
			}
		)
	})
}

document.getElementById('messageInput').addEventListener('keydown', function(event) {
	if (event.key === 'Enter') {
		event.preventDefault();
		document.getElementById('sendButton').click();
	}
});

function sendMessage() {
	const userSeq = sessionStorage.getItem('seq')
	data = {};
	data.roomName = USER.roomName;
	data.message = userSeq + ":" + document.getElementById("messageInput").value;  // 입력된 메시지 가져오기
	SOCKET.socket.send(JSON.stringify(data));  // 메시지 전송

	let message = "<div class='messageBox sent'>"
	let profile = "<div class='mini-profile'>";
	profile += "<img src='data:image/jpg;base64," + ROOM_INFO.profileImages[userSeq] + "' alt='프로필' class='profile-img'>";
	profile += "<span class='username' id='username'>" + ROOM_INFO.users[userSeq] + "</span>";
	profile += "</div>"
	message += profile;
	let selfMessage = "<div class='message sent'>" + document.getElementById("messageInput").value + "</div>";
	message += selfMessage;
	message += "</div>"
	let chatMessages = document.getElementById("chatMessages");
	chatMessages.insertAdjacentHTML('beforeend', message);

	document.getElementById("messageInput").value = "";  // 입력창 초기화
	scrollToBottom();
}

function scrollToBottom() {
	let chatMessages = document.getElementById("chatMessages");
	chatMessages.scrollTop = chatMessages.scrollHeight;
}