document.addEventListener('DOMContentLoaded', () => {
    const messageInput = document.getElementById('messageInput');
    const sendButton = document.getElementById('sendButton');
    const chatMessages = document.getElementById('chatMessages');
    const fileButton = document.getElementById('fileButton');

    // 메시지 전송 함수
    function sendMessage() {
        const message = messageInput.value.trim();
        if (message) {
            addMessage(message, 'sent');
            messageInput.value = '';
            // 여기에 실제 메시지 전송 로직 추가
        }
    }

    // 메시지 추가 함수
    function addMessage(message, type) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('message', type);
        messageElement.textContent = message;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // 이벤트 리스너
    sendButton.addEventListener('click', sendMessage);
    messageInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

    fileButton.addEventListener('click', () => {
        const input = document.createElement('input');
        input.type = 'file';
        input.onchange = (e) => {
            const file = e.target.files[0];
            if (file) {
                // 여기에 파일 전송 로직 추가
                addMessage(`파일 "${file.name}" 전송됨`, 'sent');
            }
        };
        input.click();
    });

    // 테스트용 참가자 목록 추가
    const participantsList = document.getElementById('participantsList');
    const testParticipants = ['김철수', '이영희', '박지민'];
    testParticipants.forEach(name => {
        const li = document.createElement('li');
        li.textContent = name;
        participantsList.appendChild(li);
    });
});