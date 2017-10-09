function initPersonData() {
    var isActivist = ($('#person-is-activist').val().toString() === 'true');
    var hasJuleica = ($('#activist-has-juleica').val().toString() === 'true');
    var isNabuMember = ($('#person-is-nabu-member').val().toString() === 'true');

    if (!isActivist) {
        $('#edit-activist-juleica-container').addClass('hide-initially');
    }

    if (!hasJuleica) {
        $('#edit-activist-juleica').addClass('hide-initially');
    }

    $('#edit-activist-juleica-picker').find('input').prop('required', hasJuleica);
    $('#add-person-nabuNr').prop('disabled', !isNabuMember);
}

function initEatingHabit() {
    var eatingHabit = $('#eatingHabit').val();
    $('#eatingHabit-quick > option').each(function() {
        if (eatingHabit.includes(this.value)) {
            this.selected = true;
        }
    });
    $('#eatingHabit-quick').selectpicker('refresh');
}

$('#dob-picker').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true
});

// disable nabu membership number if checkbox is not selected
$('#add-person-isNabuMember').click(function() {
    $('#add-person-nabuNr').prop('disabled', function(i,v){return !v;});
});

$('#eatingHabit-quick').on('changed.bs.select', function() {
    var option = $(this).val();

    if (!option) {
        return;
    }

    var eatingHabit = $('#eatingHabit').val();
    if (eatingHabit) {
        // if a special eating habit was selected, append the one from the quick-select
        eatingHabit += ', ' + option;
    } else {
        // else just use the quick select
        eatingHabit = option;
    }

    // update the eating habit
    $('#eatingHabit').val(eatingHabit);

    $('#eatingHabit').addClass('input-pulse');
    setTimeout(function() {
        $('#eatingHabit').removeClass('input-pulse');
    }, 2000);
})

$('#edit-activist-juleica-picker').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true
});

$('#delete-qualification-modal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var qualification = button.data('qualification'); // Extract info from data-* attributes
    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
    var modal = $(this);
    modal.find('.modal-body input').val(qualification)
});

$('#edit-activist-isActivist').click(function() {
    $('#edit-activist-juleica-container').slideToggle();
});

$('#edit-activist-hasJuleica').click(function() {
    $('#edit-activist-juleica').slideToggle();
    $('#edit-activist-juleica-picker').find('input').prop('required', function(i,v) {return !v;});
});

$('#connect-parent-new-tab').on('shown.bs.tab', function() {
   $('#connect-parent-submit').attr('form', 'connect-parent-new-form');
});

$('#connect-parent-existing-tab').on('shown.bs.tab', function() {
   $('#connect-parent-submit').attr('form', 'connect-parent-person-select');
});

$('#connect-parent-search-btn').on('click', function() {
    var table = '#connect-parent-tablebody';
    var firstname = $('#connect-parent-search-firstname').val();
    var lastname = $('#connect-parent-search-lastname').val();
    var city = $('#connect-parent-search-city').val();

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
            $('#connect-parent-modal').find('.searching').hide();
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/simpleSearch'
    };

    $('#connect-parent-modal').find('.searching').show();
    $.ajax(request);
});

$(function() {
    $('#connect-parent-modal').find('.searching').hide();
    $('#connect-parent-modal').find('.no-results').hide();
    initPersonData();
    initEatingHabit();
});
