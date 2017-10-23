
$('#add-subscriber-person-tab').on('shown.bs.tab', function() {
   $('#add-subscriber-submit').attr('form', 'add-subscriber-person-select');
});

$('#add-subscriber-new-tab').on('shown.bs.tab', function() {
   $('#add-subscriber-submit').attr('form', 'add-subscriber-new-select');
});


$('#delete-subscriber-modal').on('show.bs.modal', function (event) {
   var button = $(event.relatedTarget);
   var recipient = button.data('email');
   var modal = $(this);
   modal.find('.modal-body input').val(recipient)
});

$(function() {
    $('#add-subscriber-person').find('.searching').hide();
    $('#add-subscriber-person').find('.no-results').hide();
    initSearch($('#add-subscriber-person'));
});
