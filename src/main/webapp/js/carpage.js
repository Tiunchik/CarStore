$(document).ready(function () {
    var num = localStorage.getItem("advnumber");
    load(num);
    askStatus(num);
});

function load(num) {
    $('#mainimage').attr('src', getContextPath() + "/myauto/images?path=" + num);
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "getoneadd", "id": num}),
        dataType: 'json'
    }).done(function (data) {
        $('#adid').val(data.id);
        $('#vin').text("Car vin number " + data.car.vin);
        $('#company').text("Company " + data.car.company);
        $('#model').text("Model " + data.car.model);
        var date = new Date(data.car.made).getFullYear();
        $('#made').text("Made in " + date);
        $('#horse').text("Engine h.p. " + data.car.engine.horse);
        $('#body').text("Car body " + data.car.body);
        $('#type').text("Engine type " + data.car.engine.type);
        $('#transmition').text("Transmittion type " + data.car.transmission);
        $('#drivetype').text("Drive type " + data.car.driveType);
        $('#odometer').text("Odometer " + data.odometer);
        $('#price').text("Price " + data.price);
        if (!(data.status)) {
            $('#status').text("The car has been sold");
        } else {
            $('#status').text("The car is for sale");
        }
        if (data.comment) {
            $('#usercomment').text("Owner comments: " + data.comment);
        }
    })
}

function under() {
    alert("This function is under construction");
}

function askStatus(num) {
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "getchange", "id": num}),
        dataType: 'json'
    }).done(function (data) {
        var text = "Change the car status to: the car was sold";
        if (!(data.status)) {
            text = "Change the car status to: the car isn't sold";
        }
        var row = $('#setsell');
        row.empty();
        row.append("<button type=\"button\" class=\"btn btn-primary\" onclick='changeStatus()' value=\"" + text + "\">" + text + "</button>");
        row = $('#outbutton');
        row.empty();
        row.append("<div class=\"btn-group\">\n" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"under()\">Edit car info</button>\n" +
            "<button type=\"button\" class=\"btn btn-primary\" onclick=\"logout()\">Logout</button>\n" +
            "</div>");
    });
}

function changeStatus() {
    var id = $('#adid').val();
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "changestatus", "id": id}),
    }).done(function () {
        load(id);
        askStatus(id);
    })
}

function getContextPath() {
    return location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

function logout() {
    $.ajax({
        url: getContextPath() + "/access",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "logout"})
    }).done(function () {
        location.href = getContextPath() + "/main";
    });
}

function back() {
    location.href = getContextPath() + "/userpage";
}