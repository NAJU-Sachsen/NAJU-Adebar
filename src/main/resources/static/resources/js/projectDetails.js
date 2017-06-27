
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
            city: city
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            $('#add-contributor-modal').find('.searching').hide();
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $('#add-contributor-modal').find('.searching').show();
    $.ajax(request);
});

// dont show modals when clicking on links
$('#contributors').find('a').on('click', function(e) {
    e.stopPropagation();
});

// init the 'contributor member' modal
$('#remove-contributor-modal').on('show.bs.modal', function(e) {
    var button = $(e.relatedTarget);
    var id = button.data('id');
    var name = button.data('name');

    $(this).find('input[disabled]').val(name);
    $(this).find('input[type=hidden]').val(id);
});

$(function() {
    $('#edit-project-period').find('input').datetimepicker({
        format: 'DD.MM.YYYY',
        showTodayButton: true
    });

    $('#event-startTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true
    });
    $('#event-endTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true
    });
});

$(function() {
    $('.searching').hide();
    $('.no-results').hide();
});
