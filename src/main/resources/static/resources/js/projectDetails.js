
// dont show modals when clicking on links
$('#contributors').find('a').on('click', function(e) {
    e.stopPropagation();
});

// init the 'contributor member' modal
$('#remove-contributor-modal').on('show.bs.modal', function(e) {
    const button = $(e.relatedTarget);
    const id = button.data('id');
    const name = button.data('name');

    const csrfToken = $('#csrf').val();

    $(this).find('input[disabled]').val(name);
    $(this).find('input[name="person-id"]').val(id);
    $(this).find('input[name="_csrf"]').val(id);
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

    $('.searching').hide();
    $('.no-results').hide();

    initSearch($('#add-contributor-modal'));
});
