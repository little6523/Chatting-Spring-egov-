const SOCKET = {
	socket: null,
	address: null,

	connect: function(roomName) {
		return new Promise((resolve, reject) => {
			SOCKET.address = 'ws://127.0.0.1:8081/chat/' + roomName;
			SOCKET.socket = new WebSocket(this.address);  // 소켓 연결
			
			if (SOCKET.socket != null) {
				resolve();
			} else {
				reject();
			}
		});
	},

	init: function(onopen, onmessage, onclose) {
		SOCKET.socket.onopen = onopen;
		SOCKET.socket.onmessage = onmessage;
		SOCKET.socket.onclose = onclose;
	},
}