$(document).ready(function() {
	$('#currentProfile').attr('src', "data:image/jpg;base64," + sessionStorage.getItem('profileImage'));
	
    // 프로필 이미지 변경 버튼 클릭 시
    $('#changeProfileBtn').click(function() {
        $('#profileImage').click();
    });

    // 프로필 이미지 선택 시 미리보기
    $('#profileImage').change(function(e) {
        if (e.target.files && e.target.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#currentProfile').attr('src', e.target.result);
            }
            reader.readAsDataURL(e.target.files[0]);
        }
    });
    
    $('#nickname').val(sessionStorage.getItem('nickname'));

    // 폼 제출 시
    $('#updateButton').click(function(e) {
        e.preventDefault();

        // 비밀번호 일치 여부 확인
        const newPassword = $('#newPassword').val();
        const confirmPassword = $('#confirmPassword').val();

        if (newPassword !== confirmPassword) {
            $('#passwordError').text('비밀번호가 일치하지 않습니다.');
            return;
        }

        // 비밀번호가 일치하면 에러 메시지 제거
        $('#passwordError').text('');
        
        let str = $('#currentProfile').attr('src');
        
        data = {}
        data.image = str.substring(str.indexOf(',') + 1);
        data.oldNickname = sessionStorage.getItem('nickname');
        data.newNickname = $('#nickname').val();
        data.password = $('#newPassword').val();
        
        common.sendAjax('post', '/api/mypage/update', data, function(response, xhr) {
			sessionStorage.setItem('nickname', data.newNickname);
			let str = $('#currentProfile').attr('src');
			sessionStorage.setItem('profileImage', str.substring(str.indexOf(',') + 1));
			$('#newPassword').val('');
			$('#confirmPassword').val('');
		});
    });
    
    $('#returnButton').click(function(e) {
		window.location.href='/webview/chat'
	});
});