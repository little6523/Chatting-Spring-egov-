document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const userId = document.getElementById('userId').value;
    const userPassword = document.getElementById('userPassword').value;
    
    data = {}
    data.id = userId;
    data.password = userPassword;
    common.sendAjax('post', '/api/login', data, function(response, xhr) {
		// 로그인 성공 시 채팅방 목록 페이지로 이동
		sessionStorage.setItem("nickname", response.nickname);
		sessionStorage.setItem("seq", response.seq);
    	window.location.href = '/webview/chat';
	})
});

document.getElementById('signupButton').addEventListener('click', function() {
	window.location.href = '/webview/signup'
})