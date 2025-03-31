const SOCKET = {
    socket: null,
    address: null,

    connect: function(roomName) {
		SOCKET.address = 'ws://127.0.0.1:8081/chat/' + roomName;
        SOCKET.socket = new WebSocket(this.address);  // 소켓 연결
    },
    
    init: function(onopen, onmessage, onclose) {
		SOCKET.socket.onopen = onopen;
        SOCKET.socket.onmessage = onmessage;
        SOCKET.socket.onclose = onclose;
	},
}