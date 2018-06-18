function openEditParticipantForm(e) {
  $(e.target).closest('.participant-info').find('.edit-participant').click();
}

function initEditParticipantForm(e) {
  const button = $(e.relatedTarget);
  const participantRow = button.closest('.participant-info');

  const participantId = participantRow.data('participant');
  const formSent = participantRow.find('.form-sent').data('form-sent');
  const formFilled = participantRow.find('.form-filled').data('form-filled');
  const feePayed = participantRow.find('.fee-payed').data('fee-payed');
  const arrival = participantRow.find('.arrival').data('arrival-option');
  const departure = participantRow.find('.departure').data('departure-option');
  const mayGoHomeSingly =
      participantRow.find('.go-home-singly').data('go-home-singly');
  const remarks = participantRow.find('.remarks').text();

  $('#edit-participant-id').val(participantId);
  $('#edit-participant-form-sent').prop('checked', formSent).trigger('change');
  $('#edit-participant-form-filled').prop('checked', formFilled).trigger(
      'change');
  $('#edit-participant-fee-payed').prop('checked', feePayed).trigger('change');
  $('#edit-participant-arrival').val(arrival).selectpicker('refresh');
  $('#edit-participant-departure').val(departure).selectpicker('refresh');
  $('#edit-participant-may-go-home-singly').prop('checked',
      mayGoHomeSingly).trigger('change');
  $('#edit-participant-remarks').val(remarks);

  const participationTime = participantRow.find('.participation-time');
  if (participationTime.length) {
    const firstNight = participationTime.data('first-night');
    const lastNight = participationTime.data('last-night');

    $('#edit-participant-first-night').val(firstNight).trigger('change');
    $('#edit-participant-last-night').val(lastNight).trigger('change');
  }

}

function initEditReservationForm(e) {
  const button = $(e.relatedTarget);
  const reservationId = button.data('reservation');
  const reservationRow = button.closest('.reservation');
  const description = reservationRow.find('.description').text();
  const slots = reservationRow.find('.slots').text();
  const contact = reservationRow.find('.contact').text();

  $('#edit-reservation-id').val(reservationId);
  $('#edit-reservation-description').val(description);
  $('#edit-reservation-slots').val(slots);
  $('#edit-reservation-contact-email').val(contact);
}

function initApplyWaitingListEntryForm(e) {
  const button = $(e.relatedTarget);
  const personOnWaitingList = button.data('person');

  console.log('person', personOnWaitingList);

  $('#apply-waiting-list-person').val(personOnWaitingList);
}

function removeNewParticipant(ev) {
  $(ev.target).closest('.new-participant').fadeOut(300, function () {
    $(this).remove();
  });
}

$(document).ready(function () {
  $('.new-participant button.new-participant-remove').click(
      removeNewParticipant);
  $('#edit-participant-dialog').on('show.bs.modal', initEditParticipantForm);
  $('#edit-reservation-dialog').on('show.bs.modal', initEditReservationForm);
  $('#apply-waiting-list-dialog').on('show.bs.modal',
      initApplyWaitingListEntryForm);
  $('.remarks').click(openEditParticipantForm);
});
