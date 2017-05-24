function timeToString(duration) {
    var hours = duration / 60 | 0;
    var minutes = duration % 60;
    var dur;
    if (hours == 0) {
        dur = minutes + " minutes";
    } else {
        if (hours == 1) {
            dur = hours + " hour";
        }
        if (hours > 1) {
            dur = hours + " hours";
        }
        if(minutes > 0){
            dur += " " + minutes + " minutes";
        }
    }
    return dur;
}