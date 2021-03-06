  // 회원탈퇴 버튼 클릭 리스너
  $("#btn-delete-user").click(() => {
      checkDelete();
  })

  // 한번 더 확인하는 함수
  function checkDelete() {
      if (window.confirm("정말 탈퇴하시겠습니까?")) {
          deleteUser();
      }
      return;
  }

  // 회원을 탈퇴하는 함수
  async function deleteUser() {
      let userId = $("#principalId").val();

      let response = await fetch(`/s/api/user/${userId}`, {
          method: "DELETE"
      })

      if (response.status != 200) {
          alert("경고!! 올바르지 않은 요청입니다.");
          return;
      }

      let responseParse = await response.json();

      if (responseParse === true) {
          alert("회원 탈퇴가 정상적으로 되었습니다.");
          location.href = "/logout";
      } else {
          alert("탈퇴 처리 도중 문제가 발생하였습니다.");
      }
  }