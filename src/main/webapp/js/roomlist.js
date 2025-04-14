// 모달 관련 요소들
const modal = document.getElementById('createRoomModal');
const createRoomBtn = document.getElementById('createRoomBtn');
const closeBtn = document.querySelector('.close');
const submitBtn = document.getElementById('submitRoom');
const cancelBtn = document.getElementById('cancelRoom');

// 모달 열기
createRoomBtn.onclick = function() {
	modal.style.display = "block";
}

// 모달 닫기 (X 버튼)
closeBtn.onclick = function() {
	modal.style.display = "none";
}

// 모달 닫기 (취소 버튼)
cancelBtn.onclick = function() {
	modal.style.display = "none";
}

// 모달 외부 클릭시 닫기
window.onclick = function(event) {
	if (event.target == modal) {
		modal.style.display = "none";
	}
}

$(document).ready(function() {
	const userSeq = sessionStorage.getItem('seq');
	data = {}
	data.userSeq = userSeq;

	document.getElementById('username').innerText = sessionStorage.getItem('nickname');

	common.sendAjax('post', '/api/profileImages', data, function(response, xhr) {
		document.getElementById("profileImage").src = "data:image/jpg;base64," + response.image;
		sessionStorage.setItem('profileImage', response.image);
	});

	$('#mypageButton').click(function() {
		window.location.href = '/webview/mypage?nickname=' + sessionStorage.getItem('nickname');
	});

	document.querySelectorAll(".room-item").forEach(room => {
		room.addEventListener("click", function() {
			data.roomName = $(this).attr("data-room");
			data.nickname = sessionStorage.getItem('nickname');
			common.sendAjax('post', '/api/enterRoom', data, function(response, xhr) { })
			sessionStorage.setItem('roomSeq', $(this).find('input[type="hidden"]').attr('id'));
			sessionStorage.setItem('roomname', $(this).attr("data-room"));
			window.location.href = '/webview/chat/rooms?roomName=' + $(this).attr("data-room");
		});
	});
})

// 채팅방 생성 제출
submitBtn.onclick = function() {
	const roomName = document.getElementById('roomNameInput').value;

	if (!roomName) {
		alert('채팅방 이름을 입력해주세요.');
		return;
	}

	// 여기에 채팅방 생성 로직 추가
	console.log('채팅방 생성:', {
		name: roomName,
	});

	// 모달 닫기 및 입력 필드 초기화
	modal.style.display = "none";
	document.getElementById('roomNameInput').value = '';

	data = {}
	data.roomName = roomName;
	data.nickname = sessionStorage.getItem('nickname');
	common.sendAjax('post', '/api/createRoom', data, function(response, xhr) {
		if (!response.hasOwnProperty('error')) {
			// 채팅방 목록 업데이트
			const roomList = document.getElementById('roomList');

			let roomItem = ''
			roomItem += '<div class="room-item" data-room="' + roomName + '">'
			roomItem += '	<input type="hidden" id="' + response.roomSeq + '"/>'
			roomItem += '   <div class="room-info" id="roomInfo">'
			roomItem += '       <h3>' + roomName + '</h3>'
			roomItem += '   </div>'
			roomItem += '   <div class="room-meta">'
			roomItem += '       <span class="participant-count">5명</span>'
			roomItem += '   </div>'
			roomItem += '</div>'

			roomList.insertAdjacentHTML('beforeend', roomItem);

			const element = document.querySelector('[data-room="' + roomName + '"]');
			element.addEventListener("click", function() {
				common.sendAjax('post', '/api/enterRoom', data, function(response, xhr) { })
				sessionStorage.setItem('roomSeq', $(this).find('input[type="hidden"]').attr('id'));
				window.location.href = '/webview/chat/rooms?roomName=' + roomName;
			});
		} else {
			alert("입력하신 이름의 채팅방이 이미 존재합니다.");
		}
		console.log(response);
	});
}