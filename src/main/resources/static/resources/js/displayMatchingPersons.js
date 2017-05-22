// create an html node consisting of the given subelements
// no sanity checks are performed
function createHtmlNode(type, text) {
    var opening = '<' + type + '>';
    var closing = '</' + type + '>';
    return opening + text + closing;
}

// create a table row for persons
function createPersonRow(id, name, dob, address) {
    var selectColumn = '<td class="text-center"><input type="radio" name="person-id" value="' + id + '" required="required" /></td>';
    return '<tr>' + createHtmlNode('td', name) + createHtmlNode('td', dob) + createHtmlNode('td', address) + selectColumn + '</tr>';
}

// displays the matching persons in the 'add member' modal
function displayMatchingPersons(table, result) {
    $(table).empty();

    for (var i = 0; i < result.length; i++) {
        var person = result[i];

        var dob = person.dob;
        if (!dob) {
            dob = '---';
        }

        var row = createPersonRow(person.id, person.name, dob, person.address);

        $(table).append(row);
    }
}
