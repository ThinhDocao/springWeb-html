function login() {
    var username = $("#userName").val().trim();
    var password = $("#password").val().trim();

    debugger;
    $.ajax({
        type : "POST",
        url : "/login",
        contentType: "application/json",
        async : false,
        data :JSON.stringify({
            "username" : username,
            "password" : password
        }),
        success : function(data) {
            debugger;
            if (data !=null && data.accessProfileDetails !=null && data.accessProfileDetails.accessProfileId !=null) {
                console.log("login thanh cong")
                //window.location = $('#urlAjax').val()+"payu/step1";
                window.location ="/loginSuccess";

            }else{
                alert("Tài khoản mật khẩu không đúng");
            }



        },
        error : function(response) {
            swal("Thông báo", "Xin lỗi quý khách hệ thống gián đoạn");
        }
    });
}
function logindirect(){
    window.location ="/login";
}