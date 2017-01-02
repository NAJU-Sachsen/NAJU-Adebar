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
