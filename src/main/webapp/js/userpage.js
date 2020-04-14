function createAd() {
    location.href = getContextPath() + "/create";
}

function getContextPath() {
    return location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}

$(document).ready(function () {
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "getuserad"}),
        dataType: 'json'
    }).done(function (data) {
        $(data).each(function (index, el) {
            var string = "<br>" +
                "<div class=\"container mycontainer\"'>" +
                "<div class=\"container\"'>\n" +
                "<input type=\"hidden\" value=\"" + el.id + "\" id=\"addnumber\"" +
                "            <div class=\"row\">\n" +
                "                <div class=\"col-sm-3\">\n" +
                "                    <img src=\"" + getContextPath() + "/images?path=" + el.id + "\" class=\"img-rounded\" alt=\"None image\">\n" +
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
                "                            <div class=\"col-sm-2 big\"><strong>Price</strong></div>\n" +
                "                            <div class=\"col-sm-2 big\">" + el.price + "</div>\n" +
                "                            <div class=\"col-sm-2 big\"><strong>Status</strong></div>\n" +
                "                            <div class=\"col-sm-2 big\">" + getStatus(el.status) + "</div>\n" +
                "                        </dl>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            </div>\n" +
                "        </div>" +
                "        <br>";
            $('#mainblock').append(string).off().on('click', '.container', function () {
                var addnum = $(this).find('#addnumber').val();
                localStorage.clear();
                localStorage.setItem("advnumber", addnum);
                location.href = getContextPath() + "/car";
            })
        })
    })
});

function getStatus(boolean) {
    if (boolean) {
        return "Unsold";
    } else {
        return "Sold";
    }
}

function toMainlist() {
    location.href = getContextPath() + "/main"
}

function under() {
    alert("This function is under construction");
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