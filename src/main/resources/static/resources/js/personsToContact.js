function initEditPersonToContactModal(e) {
  const btn = $(e.relatedTarget);
  const contact = btn.closest('.contact');

  const person = contact.data('person');
  const description = contact.find('.description').html().replace(/<br>/g,
      '\n');

  $('#edit-person-to-contact-person').val(person);
  $('#edit-person-to-contact-description').val(description);
}

$(document).ready(() => {
  $('#edit-person-to-contact-dialog').on('show.bs.modal',
      initEditPersonToContactModal);
});
