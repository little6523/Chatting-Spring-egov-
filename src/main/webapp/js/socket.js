const SOCKET = {
    socket: null,
    address: 'ws://127.0.0.1:8081/chat',

    connect: function() {
        SOCKET.socket = new WebSocket(this.address);  // 소켓 연결
    },
    
    init: function(onopen, onmessage) {
		SOCKET.socket.onopen = onopen;
        SOCKET.socket.onmessage = onmessage;
	},
}