
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

    $('.searching').hide();
    $('.no-results').hide();

    initSearch($('#add-contributor-modal'));
});
