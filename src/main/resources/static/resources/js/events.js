
$('#start-time-picker').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ]
});

$('#end-time-picker').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ]
});

$('#filter-start-container').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ]
});

$('#filter-end-container').datetimepicker({
    format: 'DD.MM.YYYY HH:mm',
    showTodayButton: true,
    extraFormats: [ 'DD.MM.YYYY' ]
});

$('#filter-start-type').on('change', function() {
    if ($('#filter-start-type').find('option:selected').val().toString() === 'none') {
        $('#filter-start-container').addClass('hidden');
    } else {
        $('#filter-start-container').removeClass('hidden');
    }
});

$('#filter-end-type').on('change', function() {
    if ($('#filter-end-type').find('option:selected').val().toString() === 'none') {
        $('#filter-end-container').addClass('hidden');
    } else {
        $('#filter-end-container').removeClass('hidden');
    }
});

$('#filter-participants-limit-type').on('change', function() {
    if ($('#filter-participants-limit-type').find('option:selected').val().toString() === 'none') {
        $('#filter-participants-limit-container').addClass('hidden');
    } else {
        $('#filter-participants-limit-container').removeClass('hidden');
    }
});

$('#filter-participants-age-type').on('change', function() {
    $('#filter-participants-age-container').slideToggle();
});

$('#filter-fee-type').on('change', function() {
    if ($('#filter-fee-type').find('option:selected').val().toString() === 'none') {
        $('#filter-fee-container').addClass('hidden');
    } else {
        $('#filter-fee-container').removeClass('hidden');
    }
});

function invertProp(elem, prop) {
    elem.prop(prop, !elem.prop(prop));
}

function updateAddEventBelonging() {
    $('#add-event-belonging-chapter-select').slideToggle();
    $('#add-event-belonging-project-select').slideToggle();
    invertProp($('#add-event-chapter'), 'required');
    invertProp($('#add-event-project'), 'required');
}

$('#add-event-belonging-localGroup').on('change', function() {
    updateAddEventBelonging();
});

$('#add-event-belonging-project').on('change', function() {
    updateAddEventBelonging();
});

$('#release-notes-modal').find('.confirm').click(function() {
  const csrfToken = $('#csrf').val();
  const person = $('#principal').text();

	const confirmationPayload = {
    async: true,
    data: {
      user: person,
      _csrf: csrfToken
    },
    dataType: 'json',
    method: 'post',
    url: '/release-notes/read'
	};

	$.ajax(confirmationPayload);
});

$(function() {
  if ($('#release-notes-modal')) {
      $('#release-notes-modal').modal();
  }
})
