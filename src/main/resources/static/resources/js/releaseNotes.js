function setReadReleaseNotes() {
  const csrfToken = $('meta[name="_csrf"]').attr('content');
  const person = $('meta[name="_principal"]').attr('content');

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

  console.log('here we go', confirmationPayload);

  $.ajax(confirmationPayload);
}

$(document).ready(function () {
  const releaseNotesDialog = $('#release-notes-dialog');
  releaseNotesDialog.find('.confirm').click(setReadReleaseNotes);
  releaseNotesDialog.modal('show');
});
