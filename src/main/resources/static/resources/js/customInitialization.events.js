
$('#start-time-picker').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ],
});

$('#end-time-picker').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ],
});

$('#filter-start-container').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ],
});

$('#filter-end-container').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ],
});

$('#filter-start-type').on('change', function() {
    if ($('#filter-start-type option:selected').val() == 'none') {
        $('#filter-start-container').addClass('hidden');
    } else {
        $('#filter-start-container').removeClass('hidden');
    }
});

$('#filter-end-type').on('change', function() {
    if ($('#filter-end-type option:selected').val() == 'none') {
        $('#filter-end-container').addClass('hidden');
    } else {
        $('#filter-end-container').removeClass('hidden');
    }
});

$('#filter-participants-limit-type').on('change', function() {
    if ($('#filter-participants-limit-type option:selected').val() == 'none') {
        $('#filter-participants-limit-container').addClass('hidden');
    } else {
        $('#filter-participants-limit-container').removeClass('hidden');
    }
});

$('#filter-participants-age-type').on('change', function() {
    $('#filter-participants-age-container').slideToggle();
});

$('#filter-fee-type').on('change', function() {
    if ($('#filter-fee-type option:selected').val() == 'none') {
        $('#filter-fee-container').addClass('hidden');
    } else {
        $('#filter-fee-container').removeClass('hidden');
    }
});
