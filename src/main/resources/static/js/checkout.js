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
const hypenTel = (target) => {
    target.value = target.value
        .replace(/[^0-9]/g, '')
        .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
}
