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

document.getElementById('username').innerText = sessionStorage.getItem('username');

// 채팅방 생성 제출
submitBtn.onclick = function() {
	const roomName = document.getElementById('roomNameInput').value;

	if (!roomName) {
		alert('채팅방 이름과 포트 번호를 모두 입력해주세요.');
		return;
	}

	const roomInfo = {
		name: roomName,
	}

	// 여기에 채팅방 생성 로직 추가
	console.log('채팅방 생성:', {
		name: roomName,
	});

	// 모달 닫기 및 입력 필드 초기화
	modal.style.display = "none";
	document.getElementById('roomNameInput').value = '';

	// 채팅방 목록 업데이트
	const roomList = document.getElementById('roomList');

	let roomItem = ''
	roomItem += '<div class="room-item" id="roomItem">'
	roomItem += '   <div class="room-info" id="roomInfo">'
	roomItem += '       <h3>' + roomName + '</h3>'
	roomItem += '   </div>'
	roomItem += '   <div class="room-meta">'
	roomItem += '       <span class="participant-count">5명</span>'
	roomItem += '   </div>'
	roomItem += '</div>'

	roomList.innerHTML += roomItem;
	
	data = {}
	data.roomName = roomName;
	data.name = sessionStorage.getItem('username');
	common.sendAjax('post', '/api/createRoom', data, function(response, xhr) {
		console.log(response);
	});

	document.getElementById("roomItem").addEventListener("click", function() {
		window.location.href = '/webview/chat/rooms?roomName=' + roomName;
	});
}