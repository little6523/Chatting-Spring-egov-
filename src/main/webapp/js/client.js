const USER = {
	name: sessionStorage.getItem('nickname'),
	roomName: document.getElementById('roomName').innerText,
}

const ROOM_INFO = {
	roomSeq: sessionStorage.getItem('roomSeq'),
	roomName: document.getElementById('roomName').innerText,
	seqToUser: {},   // 소켓은 연결되어있지 않으나 채팅방에 참여중인 유저
	UserToSeq: {},
	currentUsers: [],   // 현재 소켓 연결된 유저
	profileImages: {}
}

function initParticipants() {
	return new Promise((resolve, reject) => {
		data = {}
		data.roomSeq = sessionStorage.getItem('roomSeq');
		common.sendAjax('post', '/api/participants', data, function(response, xhr) {
			response.participants.forEach(participant => {
				ROOM_INFO.seqToUser[participant.seq] = participant.nickname;
				ROOM_INFO.UserToSeq[participant.nickname] = participant.seq;
			});
			resolve();
		})
	})
}

// 개별 프로필 이미지를 서버에서 받아오는 함수
function fetchProfileImage(userSeq) {
	return new Promise((resolve, reject) => {
		const data = { userSeq };
		common.sendAjax('post', '/api/profileImages', data, (response, xhr) => {
			resolve(response.image);
		});
	});
}

// 여러 유저의 프로필 이미지를 초기화하는 함수
function initProfileImages() {
	const promises = Object.keys(ROOM_INFO.seqToUser).map(seq => {
		return fetchProfileImage(seq).then(image => {
			if (!ROOM_INFO.profileImages.hasOwnProperty(seq) || ROOM_INFO.profileImages[seq] !== image) {
				ROOM_INFO.profileImages[seq] = image;
			}
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

	for (let i = 0; i < json.message.length; i++) {
		let chat = json.message[i];
		if (chat.userSeq == sessionStorage.getItem('seq')) {
			message += "<div class='messageBox sent'>";
			message += "<div class='message-wrapper'>";
			let profile = "<div class='mini-profile'>";
			profile += "<img src='data:image/jpg;base64," + ROOM_INFO.profileImages[chat.userSeq] + "' alt='프로필' class='profile-img'>";
			profile += "<span class='username'>" + ROOM_INFO.seqToUser[chat.userSeq] + "</span>";
			profile += "</div>";
			message += profile;
			message += "<div class='message-content'>";
			message += "<div class='message sent'>" + chat.message + "</div>";
			message += "<span class='time'>" + chat.time + "</span>";
			message += "</div>";
			message += "</div>";
		} else {
			message += "<div class='messageBox received'>";
			message += "<div class='message-wrapper'>";
			let profile = "<div class='mini-profile'>";
			profile += "<img src='data:image/jpg;base64," + ROOM_INFO.profileImages[chat.userSeq] + "' alt='프로필' class='profile-img'>";
			profile += "<span class='username'>" + ROOM_INFO.seqToUser[chat.userSeq] + "</span>";
			profile += "</div>";
			message += profile;
			message += "<div class='message-content'>";
			message += "<div class='message received'>" + chat.message + "</div>";
			message += "<span class='time'>" + chat.time + "</span>";
			message += "</div>";
			message += "</div>";
		}
		message += "</div>";
	}

	chatMessages.insertAdjacentHTML('beforeend', message);
	scrollToBottom();
}

$(document).ready(function() {
	initParticipants()
		.then(() => {
			initProfileImages()
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

			sessionStorage.removeItem('roomname');
			sessionStorage.removeItem('roomSeq');
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

			sessionStorage.removeItem('roomname');
			sessionStorage.removeItem('roomSeq');
		}
	});
})

let messageBuffer = []
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
					initParticipants()
						.then(() => {
							fetchProfileImage(ROOM_INFO.UserToSeq[json.newUser.name].toString())
								.then((image) => {
									ROOM_INFO.profileImages[ROOM_INFO.UserToSeq[json.newUser.name]] = image;
									const miniProfiles = document.getElementsByClassName('mini-profile');
									const values = Object.values(ROOM_INFO.seqToUser);

									for (let i = 0; i < miniProfiles.length; i++) {
										const profile = miniProfiles[i];

										let profileImage = profile.querySelector('.profile-img');
										let nickname = profile.querySelector('.username');

										if (!values.includes(nickname.textContent)) {
											nickname.textContent = json.newUser.name;
											profileImage.src = 'data:image/jpg;base64,' + image;
										}
									}
								})
						})

					return;
				}

				if (json.hasOwnProperty("outUser")) {
					updateParticipants(json.outUser.name, 'out');
					initParticipants();

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
	let now = new Date();
	let timeStr = now.getHours().toString().padStart(2, '0') + ':' +
		now.getMinutes().toString().padStart(2, '0');

	const userSeq = sessionStorage.getItem('seq')
	data = {};
	data.userSeq = userSeq;
	data.roomName = USER.roomName;
	data.message = document.getElementById("messageInput").value;
	data.time = timeStr;
	SOCKET.socket.send(JSON.stringify(data));

	let message = "<div class='messageBox sent'>"
	message += "   <div class='message-wrapper'>";
	let profile = "      <div class='mini-profile'>";
	profile += "         <img src='data:image/jpg;base64," + ROOM_INFO.profileImages[userSeq] + "' alt='프로필' class='profile-img'>";
	profile += "         <span class='username' id='username'>" + ROOM_INFO.seqToUser[userSeq] + "</span>";
	profile += "      </div>"
	message += profile;
	message += "      <div class='message-content'>";
	message += "         <div class='message sent'>" + document.getElementById("messageInput").value + "</div>";
	message += "         <span class='time'>" + timeStr + "</span>";
	message += "      </div>"
	message += "   </div>"
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