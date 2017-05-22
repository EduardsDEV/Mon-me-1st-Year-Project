/**
 * Created by Chris on 17-May-17.
 */
// Builds the HTML Table out of myList json data from Ivy restful service.
function buildHtmlTable(table, myList, exclude) {
    var columns = addAllColumnHeaders(table, myList, exclude);

    for (var i = 0; i < myList.length; i++) {
        var row$ = $('<tr/>');
        for (var colIndex = 0; colIndex < columns.length; colIndex++) {
            var cellValue = myList[i][columns[colIndex]];

            if (cellValue == null) {
                cellValue = "";
            }

            row$.append($('<td/>').html(cellValue));
        }
        $(table).append(row$);
    }
}

// Adds a header row to the table and returns the set of columns.
// Need to do union of keys from all records as some records may not contain
// all records
function addAllColumnHeaders(table, myList, exclude) {
    var columnSet = [];
    var headerTr$ = $('<tr/>'); // creates empty table header row

    for (var i = 0; i < myList.length; i++) {
        var rowHash = myList[i];
        for (var key in rowHash) {
            if ($.inArray(key, columnSet) == -1 && $.inArray(key, exclude) != -1) {
                columnSet.push(key);
                headerTr$.append($('<th/>').html(key));
            }
        }
    }
    $(table).append(headerTr$);
    return columnSet;
}