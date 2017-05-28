var treatmentId = -1;
var treatments = [];
var appointmentComment = "";
var startingPoint;
var selectedTreatment = null;

function selectTreatment(treatment) {
    selectedTreatment = treatment;
}

function addComment(comment) {
    appointmentComment = comment;
}
function addStartingPoint(start) {
    startingPoint = start;
}

function addAppointmentForGuest(firstName, lastName, phoneNumber, birthday) {
    var url = "http://localhost:8080/appointments/add-guest?";
    url += "datetime=" + startingPoint + "&firstName=" + firstName + "&lastName=" + lastName + "&phoneNumber=" + phoneNumber;
    url += "&treatment=" + selectedTreatment.treatmentId;
    if (birthday != "" && birthday != "undefined") {
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

function addAppointment(email, password) {
    var url = "http://localhost:8080/appointments/add?";
    url += "datetime=" + startingPoint + "&treatment=" + treatmentId + "&email=" + email + "&password=" + password;
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