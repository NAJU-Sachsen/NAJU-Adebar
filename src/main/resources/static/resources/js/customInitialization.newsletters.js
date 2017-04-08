/*
 * Switch the modal displayed when the button is pressed
 */
function toggleModal(btn) {
    if (btn.getAttribute('data-target') == '#add-newsletter-form') {
        btn.setAttribute('data-target', '#add-subscriber-form');
    } else if (btn.getAttribute('data-target') == '#add-subscriber-form') {
        btn.setAttribute('data-target', '#add-newsletter-form');
    }
}

/*
 * Displays the subscriber's current data in the edit-form
 */
function initEditSubscriberModal(modal, data) {
    console.log(data);
    modal.find('#edit-id').val(data.subscriber.email);
    if (data.subscriber.firstName) {
       modal.find('#edit-firstName').val(data.subscriber.firstName);
    }
    if (data.subscriber.lastName) {
       modal.find('#edit-lastName').val(data.subscriber.lastName);
    }

    modal.find('#edit-email').val(data.subscriber.email);

    for (var nid in data.subscribedNewsletters) {
      var id = '#nid-' + data.subscribedNewsletters[nid];
      console.log(id);
      modal.find(id).attr('selected', 'selected');
   }

}

/*
 * if the newsletter tabs are switched, we would like to make the 'add'-button
 * react to this
 */
$('a[data-toggle="tab"]').on('shown.bs.tab', function(e) {
    e.target; // newly activated tab
    e.relatedTarget; // previous active tab
    toggleModal($i('add-button'));
});

/*
 * display the last active tab on data update
 */
var lastSessionTab = $i('session-tab').value;
if (lastSessionTab == 'subscribers') {
    $('#tabs a[href="#subscribers"]').tab('show');
} else if (lastSessionTab == 'newsletters') {
    $('#tabs a[href="#newsletters"]').tab('show');
}

/*
 * if a subscriber's edit form is displayed, fetch the corresponding data
 */
$('#edit-subscriber-form').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget);
    var recipient = button.data('whatever');
    var modal = $(this);

    var request = {
        async: true,
        data: {
            email: recipient,
        },
        dataType: 'json',
        method: 'GET',
        success: function (response) {
           initEditSubscriberModal(modal, response);
        },
        url: '/api/newsletter/subscriberDetails',
    };
    $.ajax(request);
});
