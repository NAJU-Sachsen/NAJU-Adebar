$('#dob-picker').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true,
});

$('#edit-activist-juleica-picker').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true,
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
});

$(function() {
    var isActivist = ($('#person-is-activist').val() == 'true');
    var hasJuleica = ($('#activist-has-juleica').val() == 'true');
    if (!isActivist) {
        $('#edit-activist-juleica-container').addClass('hide-initially');
    }
    
    if (!hasJuleica) {
        $('#edit-activist-juleica').addClass('hide-initially');
    }
});
