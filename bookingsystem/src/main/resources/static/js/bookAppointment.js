var treatmentId = -1;

function selectTreatment(treatment) {
    treatmentId = treatment;
}

function addAppointment(dateAndTime, firstName, lastName, phoneNumber, birthday, comment) {
    var url = "http://localhost:8080/appointments/add-guest?";
    url += "datetime=" + dateAndTime + "&firstName=" + firstName + "&lastName=" + lastName + "&phoneNumber=" + phoneNumber;
    url += "&treatment=" + treatmentId;
    console.log(birthday);
    console.log(comment);
    if (birthday != "") {
        url += "&birthday=" + birthday;
    }
    if (comment != "") {
        url += "&comment=" + comment;
    }

    $.ajax({
        url: url,
        method: "PUT",
        dataType: "string",
        success: function (data) {
            console.log(data);
        }
    });
}