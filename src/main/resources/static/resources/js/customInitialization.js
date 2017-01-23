/**
 *
 */

/*
 * shortcut-function for document.getElementById()
 */
function $i(id) {
   return document.getElementById(id)
}

/*
 * fix autofocus on bootstrap-modals
 */
$('.modal').on('shown.bs.modal', function() {
    $(this).find('[autofocus]').focus();
});

$("[data-toggle=tooltip]").tooltip({
	placement: $(this).data("placement") || 'top'
});


$('.btn-loading').click(function() {
    $(this).find('.glyphicon-refresh').addClass('glyphicon-load-animate');
});
