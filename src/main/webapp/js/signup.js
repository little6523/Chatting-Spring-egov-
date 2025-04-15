$(document).ready(function() {
	let isIdChecked = false;
	let isNicknameChecked = false;

	// ID 중복 확인
	$('#checkIdBtn').click(function() {
		const userId = $('#userId').val().trim();
		if (!userId) {
			$('#idError').text('아이디를 입력해주세요.').css('color', '#dc3545');
			return;
		}
		data = {}
		data.id = userId;
		common.sendAjax('post', '/api/checkDuplication', data, function(response, xhr) {
			if (response.check == 'success') {
				$('#idError').text(response.result).css('color', '#28a745');
			} else {
				$('#idError').text(response.result).css('color', '#dc3545');
			}
		})
	});

	// 닉네임 중복 확인
	$('#checkNicknameBtn').click(function() {
		const nickname = $('#nickname').val().trim();
		if (!nickname) {
			$('#nicknameError').text('닉네임을 입력해주세요.').css('color', '#dc3545');
			return;
		}
		data = {}
		data.nickname = nickname;
		common.sendAjax('post', '/api/checkDuplication', data, function(response, xhr) {
			if (response.check == 'success') {
				$('#nicknameError').text(response.result).css('color', '#28a745');
			} else {
				$('#nicknameError').text(response.result).css('color', '#dc3545');
			}
		})
	});

	// ID 입력 필드 변경 시 중복 확인 초기화
	$('#userId').on('input', function() {
		isIdChecked = false;
		$('#idError').text('아이디 중복 확인이 필요합니다.').css('color', '#dc3545');
	});

	// 닉네임 입력 필드 변경 시 중복 확인 초기화
	$('#nickname').on('input', function() {
		isNicknameChecked = false;
		$('#nicknameError').text('');
	});

	// 비밀번호 확인 실시간 검증
	$('#confirmPassword').on('input', function() {
		const password = $('#password').val();
		const confirmPassword = $(this).val();

		if (password !== confirmPassword) {
			$('#passwordError').text('비밀번호가 일치하지 않습니다.').css('color', '#dc3545');
		} else {
			$('#passwordError').text('비밀번호가 일치합니다.').css('color', '#28a745');
		}
	});

	let selectedFile = null;

	// 프로필 이미지 선택 버튼 클릭 이벤트
	$('#changeProfileBtn').click(function() {
		$('#profileImage').click();
	});

	// 파일 선택 시 이미지 미리보기
	$('#profileImage').change(function(e) {
		const file = e.target.files[0];
		if (file) {
			if (!file.type.startsWith('image/')) {
				alert('이미지 파일만 선택할 수 있습니다.');
				return;
			}

			selectedFile = file;
			const reader = new FileReader();
			reader.onload = function(e) {
				$('#currentProfile').attr('src', e.target.result);
			};
			reader.readAsDataURL(file);
		}
	});

	// 폼 제출
	$('#signupForm').submit(function(e) {
		e.preventDefault();

        const formData = new FormData();
        formData.append('userId', $('#userId').val().trim());
        formData.append('password', $('#password').val());
        formData.append('nickname', $('#nickname').val().trim());
        if (selectedFile) {
            formData.append('profileImage', selectedFile);
        }

		// 필수 필드 검증
		if (!userId || !password || !confirmPassword) {
			alert('필수 항목을 모두 입력해주세요.');
			return;
		}

		// ID 중복 확인 검증
		if (!isIdChecked) {
			alert('아이디 중복 확인이 필요합니다.');
			return;
		}

		// 비밀번호 일치 검증
		if (password !== confirmPassword) {
			alert('비밀번호가 일치하지 않습니다.');
			return;
		}

		// 닉네임이 입력된 경우에만 중복 확인 검증
		if (nickname && !isNicknameChecked) {
			alert('닉네임 중복 확인이 필요합니다.');
			return;
		}

		// 회원가입 요청
        $.ajax({
            url: '/api/signup',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                alert('회원가입이 완료되었습니다.');
                window.location.href = '/login';
            },
            error: function(xhr) {
                alert('회원가입 중 오류가 발생했습니다.');
            }
        });
	});

	// 돌아가기 버튼
	$('#returnButton').click(function() {
		window.location.href = '/webview/login';
	});
});