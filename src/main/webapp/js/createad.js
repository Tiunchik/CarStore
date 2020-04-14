$(document).ready(function () {
    $.ajax({
        url: getContextPath() + "/post",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "getcompany"}),
        dataType: 'json'
    }).done(function (data) {
        var company = $('#company');
        company.empty();
        company.append(" <option></option>");
        $(data).each(function (index, el) {
            company.append(" <option> " + el.name + " </option>");
        });
        company.val("");
    })
});

function models() {
    $.ajax({
        url: getContextPath() + "/post",
        method: 'post',
        contentType: 'json',
        data: JSON.stringify({"action": "getmodels", "company": $('#company').val()}),
        dataType: 'json'
    }).done(function (data) {
        var models = $('#model');
        models.empty();
        models.append(" <option></option>");
        $(data).each(function (index, el) {
            models.append(" <option> " + el + " </option>");
        });
        models.val("");
    })
}

function createAd() {
    var vin = $('#vin').val();
    var company = $('#company').val();
    var model = $('#model').val();
    var engineType = $('#engineType').val();
    var horse = $('#horse').val();
    var made = $('#made').val();
    var transmition = $('#transmition').val();
    var driverType = $('#driverTpe').val();
    var odometer = $('#Odometer').val();
    var price = $('#Price').val();
    var hidden = $('#hidden').val();
    var newDate = new Date(made).getTime();
    var comment = $('#comment').val();
    var body = $('#body').val();
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({
            "action": "createad",
            "vin": vin,
            "company": company,
            "model": model,
            "engineType": engineType,
            "horse": horse,
            "made": newDate,
            "transmition": transmition,
            "drivertype": driverType,
            "odometer": odometer,
            "price": price,
            "photo": hidden,
            "comment": comment,
            "body": body
        })
    }).done(function () {
        location.href = getContextPath() + "/userpage";
    })
}

function prepareImage() {
    var data = new FormData();
    data.append('datafile', $("#Photo").get(0).files[0]);
    $.ajax({
        type: "POST",
        url: getContextPath() + "/images",
        data: data,
        cache: false,
        processData: false,
        contentType: false
    }).done(function () {
        $('#hidden').val("yes");
    })
}

function getContextPath() {
    return location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}


