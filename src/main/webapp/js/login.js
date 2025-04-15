document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const userId = document.getElementById('userId').value;
    const userPassword = document.getElementById('userPassword').value;
    
    // 여기에 로그인 검증 로직을 추가하세요
    console.log('로그인 시도:', {
        userId: userId,
        password: userPassword
    });
    
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