function initEditCounselorDialog(e) {
  const btn = $(e.relatedTarget);
  const counselor = btn.closest('.counselor');

  const counselorId = counselor.data('counselor');
  const arrival = counselor.find('.arrival').data('arrival-option');
  const departure = counselor.find('.departure').data('departure-option');
  const remarks = counselor.find('.remarks').text();

  $('#edit-counselor-id').val(counselorId);
  $('#edit-counselor-arrival').val(arrival).selectpicker('refresh');
  $('#edit-counselor-departure').val(departure).selectpicker('refresh');
  $('#edit-counselor-remarks').val(remarks);

  const participationTime = counselor.find('.participation-time');
  if (participationTime.length) {
    const firstNight = participationTime.data('first-night');
    const lastNight = participationTime.data('last-night');

    $('#edit-counselor-first-night').val(firstNight).trigger('change');
    $('#edit-counselor-last-night').val(lastNight).trigger('change');
  }

}

$(document).ready(() => {
  $('#edit-counselor-dialog').on('show.bs.modal', initEditCounselorDialog);
});

