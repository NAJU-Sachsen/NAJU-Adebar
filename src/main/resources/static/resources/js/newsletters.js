/*
 * Switch the modal displayed when the button is pressed
 */
function toggleModal(btn) {
    if (btn.getAttribute('data-target').toString() === '#add-newsletter-form') {
        btn.setAttribute('data-target', '#add-subscriber-form');
    } else if (btn.getAttribute('data-target').toString() === '#add-subscriber-form') {
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
    modal.find('#edit-subscribed').val(data.subscribedNewsletters);
}

function hideAllSelects() {
    $('#belonging-chapter').addClass('hidden');
    $('#belonging-project').addClass('hidden');
    $('#belonging-event').addClass('hidden');
}

/*
 * if the newsletter tabs are switched, we would like to make the 'add'-button
 * react to this
 */
$('a[data-toggle="tab"]').on('shown.bs.tab', function() {
    toggleModal($i('add-button'));
});

/*
 * display the last active tab on data update
 */
var lastSessionTab = $i('session-tab').value.toString();
if (lastSessionTab === 'subscribers') {
    $('#tabs').find('a[href="#subscribers"]').tab('show');
} else if (lastSessionTab === 'newsletters') {
    $('#tabs').find('a[href="#newsletters"]').tab('show');
}

$('#belonging-type-select').find('input[type=radio]').change(function() {
    hideAllSelects();
    if (this.value.toString() === 'CHAPTER') {
        $('#belonging-chapter').removeClass('hidden');
    } else if (this.value.toString() === 'EVENT') {
        $('#belonging-event').removeClass('hidden');
    } else if (this.value.toString() === 'PROJECT') {
        $('#belonging-project').removeClass('hidden');
    }
});

/*
 * if a subscriber's edit form is displayed, fetch the corresponding data
 */
$('#edit-subscriber-form').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget);
    var recipient = button.data('whatever');
    var modal = $(this);

    const csrfToken = $('#csrf').val();

    var request = {
        async: true,
        data: {
            email: recipient,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'GET',
        success: function(response) {
            initEditSubscriberModal(modal, response);
        },
        url: '/api/newsletter/subscriberDetails'
    };
    $.ajax(request);
});
