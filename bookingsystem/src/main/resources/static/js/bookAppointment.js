var treatmentId = -1;
var treatments = [];
var appointmentComment = "";
var startingPoint;
var endingPoint;
var selectedTreatment = null;

function selectTreatment(treatment) {
    selectedTreatment = treatment;
}

function addComment(comment) {
    appointmentComment = comment;
}
function addTimes(start, ending) {
    startingPoint = start;
    endingPoint = ending;
}

function addAppointmentForGuest(firstName, lastName, phoneNumber, birthday, callback) {
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
        dataType: "text",
        success: function (data) {
            result = data;
            callback(result);
        }
    });
}

function addAppointment(email, password) {
    var url = "http://localhost:8080/appointments/add?";
    url += "datetime=" + startingPoint + "&treatment=" + selectedTreatment.treatmentId + "&email=" + email + "&password=" + password;
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