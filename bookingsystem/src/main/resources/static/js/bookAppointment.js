var treatmentId = -1;
var treatments = [];
var appointmentComment = "";

function selectTreatment(treatment) {
    treatmentId = treatment;
}

function addComment(comment) {
    appointmentComment = comment;
}

function addAppointmentForGuest(dateAndTime, firstName, lastName, phoneNumber, birthday) {
    var url = "http://localhost:8080/appointments/add-guest?";
    url += "datetime=" + dateAndTime + "&firstName=" + firstName + "&lastName=" + lastName + "&phoneNumber=" + phoneNumber;
    url += "&treatment=" + treatmentId;
    if (birthday != "") {
        url += "&birthday=" + birthday;
    }
    if (appointmentComment != "") {
        url += "&comment=" + appointmentComment;
    }
    var result = "Error";
    $.ajax({
        url: url,
        method: "PUT",
        dataType: "string",
        success: function (data) {
            result = data;
        }
    });
    return result;
}

function addAppointment(dateAndTime, email, password) {
    var url = "http://localhost:8080/appointments/add?";
    url += "datetime=" + dateAndTime + "&treatment=" + treatmentId + "&email=" + email + "&password=" + password;
    if (appointmentComment != "") {
        url += "&comment=" + appointmentComment;
    }
    var result = "Error";
    $.ajax({
        url: url,
        method: "PUT",
        dataType: "string",
        success: function (data) {
            result = data;
        }
    });
    return result;
}