$(document).ready(function() {
  $('#loginForm').on('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 막기

    var emailValue = $('#email').val().trim();
    var passwordValue = $('#login-password').val().trim();

    // 클라이언트에서 유효성 검증
    if (!validateEmail(emailValue)) {
      alert('올바른 이메일 형식을 입력하세요.');
      return;
    }

    if (passwordValue === '') {
      alert('비밀번호를 입력하세요.');
      return;
    }

    // 서버로 AJAX 요청
    $.ajax({
      url: '/login',
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ loginId: emailValue, password: passwordValue }),
      success: function(data) {
        if (data.status === 'success') {
          // 로그인 성공 시 페이지 리다이렉트
          window.location.href = "/";
        } else if (data.status === 'invalidEmailOrPassword') {
          alert(data.message);
        } else if (data.status === 'notVerifyEmail') {
          window.location.href = "/resend-verification";
        }
      },
      error: function(xhr) {
        console.error(xhr);
      }
    });
  });

  // 이메일 유효성 검증 함수
  function validateEmail(email) {
  var emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  return emailRegex.test(email);
  }
});
