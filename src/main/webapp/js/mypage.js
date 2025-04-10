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

    // 폼 제출 시
    $('#mypageForm').submit(function(e) {
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

        // 여기에 서버로 데이터를 전송하는 AJAX 코드 추가
        const formData = new FormData(this);
        
        $.ajax({
            url: '/api/user/update',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                alert('정보가 성공적으로 수정되었습니다.');
                window.location.href = '/chat/rooms'; // 채팅방 목록으로 이동
            },
            error: function(xhr, status, error) {
                alert('정보 수정에 실패했습니다.');
            }
        });
    });
});