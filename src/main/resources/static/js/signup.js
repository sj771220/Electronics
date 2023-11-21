let idCheck = 0;
let pwdCheck = 0;
let phoneCheck = 0;
let nameCheck = 0;
let pwCheck = 0;
let emailCheck=0;
function regMemberid(mId) { //영문자 또는 숫자 6~16자
    var regExp = /^[A-za-z0-9]{5,15}/g;
    return regExp.test(mId);
}
function regPassword(mPwd) { //8~16자 영문, 숫자 조합
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,16}$/;
    return regExp.test(mPwd);
}

function regMemberName(mName) { //이름
    var regExp = /^[가-힣]{2,4}|[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/;
    return regExp.test(mName);
}

function regPhoneNumber(mTel) { //전화번호
    var regExp = /^\d{3}-\d{3,4}-\d{4}$/;
    return regExp.test(mTel);
}

function regEmail(email) {
    var regex = /^([\w.\-_])*[a-zA-Z0-9]+([\w.\-_])*([a-zA-Z0-9])+([\w.\-_])+@([a-zA-Z0-9]+\.)+[a-zA-Z0-9]{2,8}$/i;
    return regex.test(email);
}


function checkEmail() {
    var inputed = $('#email').val();
    $.ajax({
        data : {
            mId : inputed //입력한 값을 mId라는 변수에 담음
        },
        url : "checkmail", // data를 checkid url에 보냄 (Controller에서 받아처리한다. 중복이 되면 1, 아니면 0을 반환하는 함수를 구현했다.)
        success : function(data) {
            if(data == '1') { //아이디가 중복되었을 때
                $("#failmail1").css("display", "block"); //에러메세지를 띄운다
                $("#failmail").css("display","none"); //중복에러메세지 말고 다른 에러 메세지를 지운다.
                $(".signupbtn").prop("disabled", true);
                $(".signupbtn").css("background-color", "#aaaaaa");
                $("#email").css("border-bottom", "2px solid #FFCECE");
                emailCheck = 0;
            } else if (regEmail(inputed) == false || inputed.length > 25) {
                $("#failmail").css("display","block");
                $("#failmail1").css("display","none");
                $(".signupbtn").prop("disabled", true);
                $(".signupbtn").css("background-color", "#aaaaaa");
                $("#email").css("border-bottom", "2px solid #FFCECE");
                emailCheck = 0;
            } else if( data == '0' && regEmail(inputed)) { //중복되지않고, 정규식을 통과할 때
                $("#email").css("border-bottom", "2px solid #B0F6AC");
                $("#failmail").css("display","none");
                $("#failmail1").css("display","none");
                emailCheck = 1;
            }
        }
    });
}

function checkName() {
    var inputed = $('#mName').val();
    if (regMemberName(inputed) == false || inputed.length > 18) {
        $(".signupbtn").prop("disabled", true);
        $(".signupbtn").css("background-color", "#aaaaaa");
        $("#mName").css("border-bottom", "2px solid #FFCECE"); // 밑줄 색 변경
        $("#nEmo").css("color", "#ff2020");
        nameCheck = 0;
    } else if (regMemberName(inputed) == true) {
        $("#mName").css("border-bottom", "2px solid #B0F6AC"); // 밑줄 색 변경
        $("#nEmo").css("color", "#1853ff");
        nameCheck = 1;
    }
}
function checkPwd() {
    var inputed = $('#mPwd').val();
    if(regPassword(inputed) == false || inputed.length > 16) {
        $(".signupbtn").prop("disabled", true);
        $(".signupbtn").css("background-color", "#aaaaaa");
        $("#failpwd").css("display", "block");
        $("#mPwd").css("border-bottom", "2px solid #FFCECE");
        $("#pwEmo1").css("color", "#ff2020");
        pwdCheck = 0;
    } else if(regPassword(inputed) == true) {
        $("#mPwd").css("border-bottom", "2px solid #B0F6AC");
        $("#failpwd").css("display", "none");
        $("#pwEmo1").css("color", "#1853ff");
        pwdCheck = 1;
    }
}


function checkPwd2() {
    var inputed = $('#pwCheck').val();
    var inputed1 = $('#mPwd').val();
    if(inputed != inputed1) {
        $(".signupbtn").prop("disabled", true);
        $(".signupbtn").css("background-color", "#aaaaaa");
        $("#pwCheck").css("border-bottom", "2px solid #FFCECE");
        $("#failpwdcheck").css("display","block");
        pwCheck = 0;
    } else if(inputed == inputed1) {
        $("#pwCheck") .css("border-bottom", "2px solid #B0F6AC");
        $("#failpwdcheck").css("display","none");
        pwCheck = 1;
    }
}


function checkTel() {
    var inputed = $('#mTel').val();
    $.ajax({
        data : {
            mId : inputed //입력한 값을 mId라는 변수에 담음
        },
        url : "checktel", // data를 checkid url에 보냄 (Controller에서 받아처리한다. 중복이 되면 1, 아니면 0을 반환하는 함수를 구현했다.)
        success : function(data) {
            if(data == '1') { //아이디가 중복되었을 때
                $("#failtel2").css("display", "block"); //에러메세지를 띄운다
                $("#failtel").css("display","none"); //중복에러메세지 말고 다른 에러 메세지를 지운다.
                $(".signupbtn").prop("disabled", true);
                $(".signupbtn").css("background-color", "#aaaaaa");
                $("#mTel").css("border-bottom", "2px solid #FFCECE");
                phoneCheck = 0;
            } else if(regPhoneNumber(inputed) ==  false) {
                $("#failtel").css("display","block");
                $("#failtel2").css("display","none");
                $("#mTel").css("border-bottom", "2px solid #FFCECE");
                inputed = $('#mTel').val();
                phoneCheck = 0;
            }
            else if(regPhoneNumber(inputed)== true) {
                $("#failtel").css("display","none");
                $("#failtel2").css("display","none");
                $("#mTel") .css("border-bottom", "2px solid #B0F6AC");
                phoneCheck = 1;
            }
        }, error:function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });
}

function checkId() {
    var inputed = $('#mId').val();
    $.ajax({
        data : {
            mId : inputed //입력한 값을 mId라는 변수에 담음
        },
        url : "checkid", // data를 checkid url에 보냄 (Controller에서 받아처리한다. 중복이 되면 1, 아니면 0을 반환하는 함수를 구현했다.)
        success : function(data) {
            if(data == '1') { //아이디가 중복되었을 때
                $("#fail").css("display", "block"); //에러메세지를 띄운다
                $("#failid").css("display","none"); //중복에러메세지 말고 다른 에러 메세지를 지운다.
                $(".signupbtn").prop("disabled", true);
                $(".signupbtn").css("background-color", "#aaaaaa");
                $("#mId").css("border-bottom", "2px solid #FFCECE");
                idCheck = 0;
            } else if (regMemberid(inputed) == false || inputed.length > 14) {
                $("#failid").css("display","block");
                $("#fail").css("display","none");
                $(".signupbtn").prop("disabled", true);
                $(".signupbtn").css("background-color", "#aaaaaa");
                $("#mId").css("border-bottom", "2px solid #FFCECE");
                idCheck = 0;
            } else if( data == '0' && regMemberid(inputed)) { //중복되지않고, 정규식을 통과할 때
                $("#mId") .css("border-bottom", "2px solid #B0F6AC");
                $("#failid").css("display","none");
                $("#fail").css("display","none");
                idCheck = 1;
            }
        }, error:function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
        }
    });
}
function activateSignupbtn() {
    // 각 체크박스의 상태 확인
    var ageCheck = document.getElementById("ageCheckbox").checked;
    var termsCheck = document.getElementById("termsCheckbox").checked;
    var financeTermsCheck = document.getElementById("financeTermsCheckbox").checked;
    var privacyPolicyCheck = document.getElementById("privacyPolicyCheckbox").checked;
    var thirdPartyCheck = document.getElementById("thirdPartyCheckbox").checked;



    // 모든 조건이 충족되면 버튼 활성화
    if (ageCheck && termsCheck && financeTermsCheck && privacyPolicyCheck && thirdPartyCheck&&
        idCheck === 1 && pwdCheck === 1 && nameCheck === 1 && phoneCheck === 1 && pwCheck === 1 && emailCheck === 1
    ) {
        $(".signupbtn").prop("disabled", false);
        $(".signupbtn").css("background-color", "#1abc9c");
    } else {
        $(".signupbtn").css("background-color", "#aaaaaa");
        $(".signupbtn").prop("disabled", true);
    }
}

function openZipSearch() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = '';
            if (data.userSelectedType === 'R') {
                addr = data.roadAddress;
            } else {
                addr = data.jibunAddress;
            }

            $("#zip_code").val(data.zonecode);
            $("#addr").val(addr);
            $("#addr_dtl").val("");
            $("#addr_dtl").focus();
        }
    }).open();
}

$(document).ready(function() {
    $("#showModalLink1").click(function(e) {
        e.preventDefault();
        $("#modal1").modal("show");
    });
});

$(document).ready(function() {
    $("#showModalLink2").click(function(e) {
        e.preventDefault();
        $("#modal2").modal("show");
    });
});

$(document).ready(function() {
    $("#showModalLink3").click(function(e) {
        e.preventDefault();
        $("#modal3").modal("show");
    });
});

$(document).ready(function() {
    $("#showModalLink4").click(function(e) {
        e.preventDefault();
        $("#modal4").modal("show");
    });
});

const hypenTel = (target) => {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
};

function activateSignupbtn2() {


    if ( pwdCheck === 1 &&  pwCheck === 1) {
        $(".signupbtn").prop("disabled", false);
        $(".signupbtn").css("background-color", "#1abc9c");
    } else {
        $(".signupbtn").css("background-color", "#aaaaaa");
        $(".signupbtn").prop("disabled", true);
    }
}
