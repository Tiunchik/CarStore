function register() {
    var login = $('#login').val();
    var pass = $("#pass").val();
    var checkpass = $('#checkPass').val();
    var email = $('#email').val();
    var firstname = $('#firstname').val();
    var secondname = $('#secondname').val();
    var phone = $('#phone').val();
    if (pass != checkpass) {
        alert("Repeated password don't equals password");
        return;
    }
    if (!(login && pass
        && email && firstname
        && secondname && phone)) {
        alert("Please fill in all empty rows");
        return;
    }
    if (!($('#checkbox').is(':checked'))) {
        alert("Please checked agreement about personal data using");
        return;
    }
    $.ajax({
        url: getContextPath() + "/access",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({
            "action": "registration", "login": login,
            "password": pass, "email": email, "name": firstname,
            "surname": secondname, "phone": phone
        })
    }).done(function () {
        location.href = getContextPath() + '/userpage';
    }).fail(function () {
        alert("That login already exists");
    })
}

function getContextPath() {
    return location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}
