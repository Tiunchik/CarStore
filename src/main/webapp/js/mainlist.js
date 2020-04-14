$(document).ready(function () {
    prepareAdvList();
    checkSessionStatus();
});

function prepareAdvList() {
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "getall"}),
        dataType: 'json'
    }).done(function (data) {
        loadAdd(data);
    });
    $.ajax({
        url: getContextPath() + "/post",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "getcompany"}),
        dataType: 'json'
    }).done(function (data) {
        var company = $('#findcompany');
        company.empty();
        company.append(" <option></option>");
        $(data).each(function (index, el) {
            company.append(" <option> " + el.name + " </option>");
        })
    })
}

function loadAdd(data) {
    var mainblock = $('#mainblock');
    mainblock.empty();
    $(data).each(function (index, el) {
        var string = "<br>" +
            "<div class=\"mycontainer\"'>" +
            "<div class=\"container\"'>\n" +
            "<input type=\"hidden\" value=\"" + el.id + "\" id=\"addnumber\"" +
            "            <div class=\"row\">\n" +
            "                <div class=\"col-sm-3\">\n" +
            "                    <img src=\"http://localhost:8090/myauto/images?path=" + el.photo + "\" class=\"img-rounded\" alt=\"None image\"\n" +
            "                         width=\"200px\" height=\"200px\">\n" +
            "                </div>\n" +
            "                <div class=\"col-sm-9\">\n" +
            "                    <div class=\"row\">\n" +
            "                        <dl>\n" +
            "                            <div class=\"col-sm-2\"><strong>Company</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.car.company + "</div>\n" +
            "                            <div class=\"col-sm-2\"><strong>Model</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.car.model + "</div>\n" +
            "                            <div class=\"col-sm-2\"><strong>Horse</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.car.engine.horse + "</div>\n" +
            "                        </dl>\n" +
            "                    </div>\n" +
            "                    <br>\n" +
            "                    <div class=\"row\">\n" +
            "                        <dl>\n" +
            "                            <div class=\"col-sm-2\"><strong>Engine type</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.car.engine.type + "</div>\n" +
            "                            <div class=\"col-sm-2\"><strong>Transmission</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.car.transmition + "</div>\n" +
            "                            <div class=\"col-sm-2\"><strong>Drive type</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.car.driveType + "</div>\n" +
            "                        </dl>\n" +
            "                    </div>\n" +
            "                    <br>\n" +
            "                    <div class=\"row\">\n" +
            "                        <dl>\n" +
            "                            <div class=\"col-sm-2\"><strong>Odometer</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.odometer + "</div>\n" +
            "                            <div class=\"col-sm-2\"><strong>Price</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.price + "</div>\n" +
            "                            <div class=\"col-sm-2\"><strong>Status</strong></div>\n" +
            "                            <div class=\"col-sm-2\">" + el.status + "</div>\n" +
            "                        </dl>\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "            </div>\n" +
            "        </div>";
        mainblock.append(string).off().on('click', '.container', function () {
            var addnum = $(this).find('#addnumber').val();
            localStorage.clear();
            localStorage.setItem("advnumber", addnum);
            location.href = getContextPath() + "/car";
        })
    })
}

function login() {
    $.ajax({
        url: getContextPath() + "/access",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "login", "login": $('#login').val(), "password": $('#password').val()}),
        dataType: 'json'
    }).done(function (data) {
        loginFrom(data);
    }).fail(function () {
        registry();
    });
}

function checkSessionStatus() {
    logoutForm();
    $.ajax({
        url: getContextPath() + "/access",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "sessioncheck"}),
        dataType: 'json'
    }).done(function (data) {
        loginFrom(data);
    })
}

function logoutForm() {
    var string = "<br>" +
        "<div class=\"input-group\">\n" +
        "<span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-user\"></i></span>\n" +
        "<input id=\"login\" type=\"text\" class=\"form-control\" name=\"login\" placeholder=\"Login\">\n" +
        "</div>\n" +
        "<div class=\"input-group\">\n" +
        "<span class=\"input-group-addon\"><i class=\"glyphicon glyphicon-lock\"></i></span>\n" +
        "<input id=\"password\" type=\"password\" class=\"form-control\" name=\"password\"\n" +
        "placeholder=\"Password\">\n" +
        "</div>\n" +
        "<br>\n" +
        "<div class=\"btn-group btn-group-justified\">\n" +
        "<a href=\"#\" class=\"btn btn-primary\" onclick=\"login()\">Login</a>\n" +
        "<a href=\"#\" class=\"btn btn-primary\" onclick='registry()'>Registry</a>\n" +
        "</div>";
    var form = $('#loginform');
    form.empty();
    form.append(string);
    string = "<br>" +
        "<div class=\"btn-group\">\n" +
        "<button type=\"button\" class=\"btn btn-primary\" onclick=\"registry()\" > Create Ad </button>\n" +
        "</div>";
    form = $('#touserpage');
    form.empty();
    form.append(string);
}

function loginFrom(data) {
    var form = $('#loginform');
    form.empty();
    var string = "<br>" +
        "<div class=\"form-group\">\n" +
        "<label for=\"log\">Greetings, " + data.firstName + " " + data.secondName + "</label>\n" +
        "<button type=\"button\" class=\"btn btn-primary\" onclick='logout()'> Logout </button>\n" +
        "</div>";
    form.append(string);
    string = "<br>" +
        "<div class=\"btn-group\">\n" +
        "<button type=\"button\" class=\"btn btn-primary\" onclick=\"createAd()\" > Create Ad </button>\n" +
        "<button type=\"button\" class=\"btn btn-primary\" onclick=\"toUserPage()\" > User Page </button>\n" +
        "</div>";
    form = $('#touserpage');
    form.empty();
    form.append(string);
}

function doFilter() {
    var photo = $('#findphoto').val();
    var date = $('#findday').val();
    var status = $('#findsale').val();
    var company = $('#findcompany').val();
    $.ajax({
        url: getContextPath() + "/post",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({
            "action": "filter",
            "car.company": company,
            "photo": photo,
            "created": date,
            "status": status
        }),
        dataType: 'json'
    }).done(function (data) {
        loadAdd(data);
    });
}

function dropFilters() {
    $('#findphoto').val();
    $('#findday').val();
    $('#findsale').val();
    $('#findcompany').val();
    doFilter();
}

function logout() {
    $.ajax({
        url: getContextPath() + "/access",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "logout"})
    }).done(function () {
        logoutForm();
    });
}

function registry() {
    location.href = getContextPath() + "/reg";
}

function toUserPage() {
    location.href = getContextPath() + "/userpage";
}

function createAd() {
    location.href = getContextPath() + "/create";
}

function getContextPath() {
    return location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

function toGitHub() {
    location.href = "https://github.com/Tiunchik/job4j_todo/tree/autoADbranch";
}