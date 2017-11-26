
$('#add-subscriber-person-tab').on('shown.bs.tab', function() {
   $('#add-subscriber-submit').attr('form', 'add-subscriber-person-select');
});

$('#add-subscriber-new-tab').on('shown.bs.tab', function() {
   $('#add-subscriber-submit').attr('form', 'add-subscriber-new-select');
});


$('#delete-subscriber-modal').on('show.bs.modal', function (event) {
   const button = $(event.relatedTarget);
   const recipient = button.data('email');
   const modal = $(this);
   const csrfToken = $('#csrf').val();
   
   modal.find('.modal-body input[name="email"]').val(recipient);
   modal.find('.modal-body input[name="_csrf"]').val(csrfToken);
});

$(function() {
    $('#add-subscriber-person').find('.searching').hide();
    $('#add-subscriber-person').find('.no-results').hide();
    initSearch($('#add-subscriber-person'));
});
