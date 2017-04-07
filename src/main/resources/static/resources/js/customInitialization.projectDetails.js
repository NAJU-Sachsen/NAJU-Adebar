
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

// displays the matching persons in the 'add contributor' modal
function displayMatchingPersons(table, result) {
    $(table).empty();

    for (var i = 0; i < result.length; i++) {
        var person = result[i];

        var row = createPersonRow(person.id, person.name, person.dob, person.address);

        $(table).append(row);
    }
}

// display the activists matching the given query
$('#add-contributor-search-btn').on('click', function() {
    var table = '#add-contributor-tablebody';
    var firstname = $('#add-contributor-search-firstname').val();
    var lastname = $('#add-contributor-search-lastname').val();
    var city = $('#add-contributor-search-city').val();

    var request = {
        async: true,
        data: {
            firstname: firstname,
            lastname: lastname,
            city: city,
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $.ajax(request);
});

// dont show modals when clicking on links
$('#contributors a').on('click', function(e) {
    e.stopPropagation();
})

// init the 'contributor member' modal
$('#remove-contributor-modal').on('show.bs.modal', function(e) {
    var button = $(e.relatedTarget);
    var id = button.data('id');
    var name = button.data('name');

    $(this).find('input[disabled]').val(name);
    $(this).find('input[type=hidden]').val(id);
})

$(function() {
    $('#edit-project-period input').datetimepicker({
        format: 'DD.MM.YYYY',
        showTodayButton: true,
    });

    $('#event-startTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true,
    });
    $('#event-endTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true,
    });
})
