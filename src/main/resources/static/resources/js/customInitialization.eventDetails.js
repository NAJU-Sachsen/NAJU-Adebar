function createHtmlNode(type, text) {
    var opening = '<' + type + '>';
    var closing = '</' + type + '>';
    return opening + text + closing;
}

function createRow(id, name, dob, address) {
    var selectColumn = '<td class="text-center"><input type="radio" name="person-id" value="' + id + '" required="required" /></td>';
    return '<tr>' + createHtmlNode('td', name) + createHtmlNode('td', dob) + createHtmlNode('td', address) + selectColumn + '</tr>';
}

function displayResult(table, result) {
    $(table).empty();

    for (var i = 0; i < result.length; i++) {
        var person = result[i];

        var row = createRow(person.id, person.name, person.dob, person.address);

        $(table).append(row);
    }
}

$('#participants a').on('click', function(e){
    e.stopPropagation();
})

$('#add-participant-search-btn').on('click', function() {
    var table = '#add-participant-tablebody';
    var firstname = $('#add-participants-search-firstname').val();
    var lastname = $('#add-participants-search-lastname').val();
    var city = $('#add-participants-search-city').val();

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
            displayResult(table, response);
        },
        url: '/api/persons/simpleSearch'
    };

    $.ajax(request);
});

$('#add-counselor-search-btn').on('click', function() {
    var table = '#add-counselor-tablebody';
    var firstname = $('#add-counselor-search-firstname').val();
    var lastname = $('#add-counselor-search-lastname').val();
    var city = $('#add-counselor-search-city').val();

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
            displayResult(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $.ajax(request);
});

$('#add-organizer-search-btn').on('click', function() {
    var table = '#add-organizer-tablebody';
    var firstname = $('#add-organizer-search-firstname').val();
    var lastname = $('#add-organizer-search-lastname').val();
    var city = $('#add-organizer-search-city').val();

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
            displayResult(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $.ajax(request);
});

$('#edit-participant-modal').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget);
    var id = button.data('id');
    var feePayed = button.data('fee-payed');
    var formReceived = button.data('form-received');

    $('#edit-participant-id').val(id);
    $('#edit-participant-fee-payed').prop('checked', feePayed);
    $('#edit-participant-form-received').prop('checked', formReceived);

    $('#remove-participant-id').val(id);
});

$('#remove-counselor-modal').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget);
    var id = button.data('id');
    var name = button.data('name');

    $(this).find('input[disabled]').val(name);
    $(this).find('input[type=hidden]').val(id);

});

$('#remove-organizer-modal').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget);
    var id = button.data('id');
    var name = button.data('name');

    $(this).find('input[disabled]').val(name);
    $(this).find('input[type=hidden]').val(id);

});
