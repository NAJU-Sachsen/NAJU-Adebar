function initPersonData() {
    var isActivist = ($('#person-is-activist').val().toString() === 'true');
    var hasJuleica = ($('#activist-has-juleica').val().toString() === 'true');
    var isNabuMember = ($('#person-is-nabu-member').val().toString() === 'true');

    if (!isActivist) {
        $('#edit-activist-container').addClass('hide-initially');
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
    const button = $(event.relatedTarget);
    const qualification = button.data('qualification');
    const modal = $(this);

    const csrfToken = $('#csrf').val();

    modal.find('.modal-body input[name="name"]').val(qualification);
    modal.find('.modal-body input[name="_csrf"]').val(csrfToken);
});

$('#edit-activist-isActivist').click(function() {
    $('#edit-activist-container').slideToggle();
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

$(function() {
    $('#connect-parent-modal').find('.searching').hide();
    $('#connect-parent-modal').find('.no-results').hide();
    initSearch($('#connect-parent-existing'));

    initPersonData();
    initEatingHabit();
});
