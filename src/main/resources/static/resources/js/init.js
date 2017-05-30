/**
 *
 */

/*
 * shortcut-function for document.getElementById()
 */
function $i(id) {
   return document.getElementById(id)
}

function $c(cl) {
    return document.getElementsByClassName(cl);
}

/*
 * fix autofocus on bootstrap-modals
 */
$('.modal').on('shown.bs.modal', function() {
    $(this).find('[autofocus]').focus();
});

$(function () {
  $('[data-toggle="tooltip"]').tooltip()
});


$('.btn-loading').click(function() {
    $(this).find('.glyphicon-refresh').addClass('glyphicon-load-animate');
});
