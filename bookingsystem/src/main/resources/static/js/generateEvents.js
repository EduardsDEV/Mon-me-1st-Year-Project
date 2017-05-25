function getData() {
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!

    var yyyy = today.getFullYear();
    today = getDate(yyyy, mm, dd);
    var url = "http://localhost:8080/appointments/" + today + "/week";
    var appointments = [];
    $.ajax({
        url: url,
        method: "GET",
        dataType: "json",
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                var appointment = data[i];
                var date = getDateTime(appointment.dateAndTime.year, appointment.dateAndTime.monthValue, appointment.dateAndTime.dayOfMonth, appointment.dateAndTime.hour, appointment.dateAndTime.minute);
                var start = new Date(date);
                console.log(date);
                console.log(start.toISOString());
                appointments.push(new Appointment(start, appointment.treatment.duration));
                // appointments.push({
                //     title: "appointment",
                //     start: start.toISOString(),
                //     end: moment(start).add(appointment.treatment.duration, 'm').toISOString(),
                //     overlap:false
                // });
                // var ev = new Event("appointment");
                // ev._start = start.toISOString();
                // ev._end = moment(start).add(appointment.treatment.duration, 'm').toISOString();
                // appointments.push(ev);
            }
        }
    });
    return appointments;
}

function Appointment(start, duration) {
    this.title = "appointment";
    this.start = start.toISOString();
    this.end = moment(start).add(duration, 'm').toISOString();
    this.overlap = false;
}

function getDate(year, month, day) {
    var dd = day, mm = month;
    if (day < 10) {
        dd = '0' + day;
    }
    if (month < 10) {
        mm = '0' + month;
    }
    return year + '-' + mm + '-' + dd;
}

function getDateTime(year, month, day, hour, minute) {
    var dd = day, MM = month, h = hour, m = minute;
    if (day < 10) {
        dd = '0' + day;
    }
    if (month < 10) {
        MM = '0' + month;
    }
    if (hour < 10) {
        h = '0' + hour;
    }
    if (minute < 10) {
        m = '0' + minute
    }
    return year + '-' + MM + '-' + dd + "T" + h + ":" + m + ":00";
}
