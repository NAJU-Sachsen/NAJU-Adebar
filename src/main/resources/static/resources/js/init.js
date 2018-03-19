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
/*
$(document).ready(function(){
    $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        localStorage.setItem('activeTab', $(e.target).attr('href'));
    });

    var activeTab = localStorage.getItem('activeTab');
    if(activeTab){
        $('a[href="' + activeTab + '"]').tab('show');
    }
});
*/
