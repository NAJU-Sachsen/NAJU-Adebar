
$('#add-participant-search-btn').on('click', function() {
    const table = $('#add-participant-modal').find('.matches');
    const firstname = $('#add-participants-search-firstname').val();
    const lastname = $('#add-participants-search-lastname').val();
    const city = $('#add-participants-search-city').val();

    const csrfToken = $('#csrf').val();

    var participantsReq = {
      async: true,
      data: {
        firstname: firstname,
        lastname: lastname,
        city: city,
        _csrf: csrfToken
      },
      dataType: 'json',
      method: 'POST',
      success: function(resp) {
        $('#add-participant-modal').find('.searching').hide();
        displayMatchingParticipants(table, resp);
      },
      url: '/api/persons/simpleSearch'
    };

    table.empty();
    $('#add-participant-modal').find('.searching').show();
    $.ajax(participantsReq);
})

$('#add-counselor-search-btn').on('click', function() {
    var table = '#add-counselor-tablebody';
    var firstname = $('#add-counselor-search-firstname').val();
    var lastname = $('#add-counselor-search-lastname').val();
    var city = $('#add-counselor-search-city').val();
    var eventId = $('#event-id').val();
    var url = `/events/${eventId}/counselors/add-new`;

    const csrfToken = $('#csrf').val();

    var activistsRequest = {
        async: true,
        data: {
            firstname: firstname,
            lastname: lastname,
            city: city,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            $('#add-counselor-modal').find('.searching').hide();
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $(table).empty();
    $('#add-counselor-modal').find('.searching').show();
    $.ajax(activistsRequest);

    var modal = $('#add-counselor-modal');
    var personsRequest = {
        async: true,
        data: {
            firstName: firstname,
            lastName: lastname,
            city: city,
            activist: false,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            $('#add-counselor-modal').find('.searching-new-activists').hide();
            displayMatchingNonActivists(modal, response, url);
        },
        url: '/api/persons/search'
    };

    modal.find('.new-activists').hide();
    $('#add-counselor-modal').find('.searching-new-activists').show();
    $.ajax(personsRequest);
});

$('#add-organizer-search-btn').on('click', function() {
    var table = '#add-organizer-tablebody';
    var firstname = $('#add-organizer-search-firstname').val();
    var lastname = $('#add-organizer-search-lastname').val();
    var city = $('#add-organizer-search-city').val();
    var eventId = $('#event-id').val();
    var url = `/events/${eventId}/organizers/add-new`;

    const csrfToken = $('#csrf').val();

    var activistsRequest = {
        async: true,
        data: {
            firstname: firstname,
            lastname: lastname,
            city: city,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            $('#add-organizer-modal').find('.searching').hide();
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $(table).empty();
    $('#add-organizer-modal').find('.searching').show();
    $.ajax(activistsRequest);

    var modal = $('#add-organizer-modal');
    var personsRequest = {
        async: true,
        data: {
            firstName: firstname,
            lastName: lastname,
            city: city,
            activist: false,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            $('#add-organizer-modal').find('.searching-new-activists').hide();
            displayMatchingNonActivists(modal, response, url);
        },
        url: '/api/persons/search'
    };

    modal.find('.new-activists').hide();
    $('#add-organizer-modal').find('.searching-new-activists').show();
    $.ajax(personsRequest);
});

$('#edit-participant-modal').on('show.bs.modal', function(event) {
    var button = $(event.relatedTarget);
    var id = button.data('id');
    var feePayed = button.data('fee-payed');
    var formReceived = button.data('form-received');
    var remarks = button.data('remarks');

    $('#edit-participant-id').val(id);
    $('#edit-participant-fee-payed').prop('checked', feePayed);
    $('#edit-participant-form-received').prop('checked', formReceived);
    $('#edit-participant-remarks').val(remarks);

    $('#remove-participant-id').val(id);
});

$('#remove-counselor-modal').on('show.bs.modal', function(event) {
    const button = $(event.relatedTarget);
    const id = button.data('id');
    const name = button.data('name');
    const csrfToken = $('#csrf').val();

    $(this).find('input[disabled]').val(name);
    $(this).find('input[name="person-id"]').val(id);
    $(this).find('input[name="_csrf"]').val(csrfToken);
});

$('#remove-organizer-modal').on('show.bs.modal', function(event) {
  const button = $(event.relatedTarget);
  const id = button.data('id');
  const name = button.data('name');
  const csrfToken = $('#csrf').val();

  $(this).find('input[disabled]').val(name);
  $(this).find('input[name="person-id"]').val(id);
  $(this).find('input[name="_csrf"]').val(csrfToken);
});

$('#edit-personToContact-modal').on('show.bs.modal', function(event) {
    var row = $(event.relatedTarget);
    var id = row.data('id');
    var name = row.find('a.name').text();
    var remarks = row.find('td.remarks').text();

    $('#edit-personToContact-id').val(id);
    $('#edit-personToContact').find('input.name').val(name);
    $('#edit-personToContact').find('input.remarks').val(remarks);

    $('#remove-personToContact-id').val(id);
});

// add participant

$(document).on('click', 'input.mark-participant', function() {
  const row = $(this).closest('tr.matching-person');
  const id = $(this).val();
  const name = row.find('.name').text();
  const dob = row.find('.date-of-birth').text();
  const address = row.find('.address').text();

  addParticipant(id, name, dob, address);
  removeFromMatches(row);
})

$(document).on('click', 'button.remove-participant', function() {
  const row = $(this).closest('.list-group-item');
  row.fadeOut(200, function() { $(this).remove() });
})

function createParticipantRow(id, name, dob, address) {
  var selectColumn = '<td class="text-center"><input type="radio" name="person-id" value="' + id + '" required="required" /></td>';
  return '<tr>' + createHtmlNode('td', name) + createHtmlNode('td', dob) + createHtmlNode('td', address) + selectColumn + '</tr>';
}

function displayMatchingParticipants(table, result) {
  if (!result.length) {
    $(table).parent().parent().parent().find('.no-results').show();
  } else {
    $(table).parent().parent().parent().find('.no-results').hide();
  }

  $(table).empty();

  for (var i = 0; i < result.length; i++) {
    var person = result[i];

    var dob = person.dob;
    if (!dob) {
      dob = '---';
    }

    var row = `<tr class="matching-person">
                <td class="name">${person.name}</td>
                <td class="date-of-birth">${dob}</td>
                <td class="address">${person.address}</td>
                <td class="text-center">
                  <label><input type="checkbox" class="mark-participant" value="${person.id}" /></label>
                </td>
              </tr>`;

    $(table).append(row);
  }
}

function addParticipant(id, name, dob, address) {
  const list = $('#add-participant');
  const entry = $(`
    <div class="list-group-item">
      <h5>${name}</h5>
      <div class="clearfix">
        <div class="col-sm-9">
          <div><span class="glyphicon glyphicon-star"></span> ${dob}</div>
          <div><span class="glyphicon glyphicon-home"></span> ${address}</div>
        </div>
        <div class="col-sm-3">
          <button type="button" class="remove-participant btn btn-danger"><span class="glyphicon glyphicon-remove"></span></button>
        </div>
        <input class="participant-id" type="hidden" name="person-id" value="${id}" />
      </div>
    </div>
    `).hide();
  list.append(entry);
  entry.show(200);
}

function removeFromMatches(tr) {
  tr.fadeOut(200, function() { $(this).remove() });
}

// dynamic reservation table

function createReservationRow(description, slots, email) {
    var descriptionColumn = '<td class="col-md-6 description">' + description + '</td>';
    var slotsColumn = '<td class="col-md-2 slots">' + slots + '</td>';
    var emailColumn = '<td class="col-md-3 email"><a href="mailto:' + email + '">' + email + '</a></td>';
    var controlsColumn = '<td class="col-md-1 controls"><div class="btn-group btn-group-xs" role="group"><button type="button" class="btn btn-default hover-primary edit-reservation"><span class="glyphicon glyphicon-pencil"></span></button><button type="button" class="btn btn-default hover-danger remove-reservation" data-description="' + description + '"><span class="glyphicon glyphicon-trash"></span></button></div></td>';
    var row = '<tr class="row reservation">' + descriptionColumn + slotsColumn + emailColumn + controlsColumn + '</tr>';
    return row;
}

function displayReservationErrorMsg() {
    $('#reservation-transmission-error').show();
}

function hideReservationErrorMsg() {
    $('#reservation-transmission-error').hide();
}

function cancelEdit(ctx) {
    hideReservationErrorMsg();

    var editRow = ctx.closest('tr.reservation-edit');

    var description = ctx.data('description');
    var slots = ctx.data('slots');
    var email = ctx.data('email');
    var row = createReservationRow(description, slots, email);

    editRow.before(row);
    editRow.remove();
}

function hideNewReservation() {
    hideReservationErrorMsg();

    $('input.reservation-description').parent().slideUp(150);
    $('input.reservation-slots').parent().slideUp(150);
    $('input.reservation-email').parent().slideUp(150);
    $('.new-reservation').find('.reservation-controls').parent().slideUp(150);
}

function fetchAddReservationResponse(response, row) {
    var reservationSlots = $('tr.new-reservation').find('input.reservation-slots');
    if (response.status === 'ok') {
        var lastRow = $('table.reservations').find('tr.reservation').last();

        if (lastRow.length) {
            lastRow.after(row);
        } else {
            $('table.reservations').prepend(row);
        }

        $('input.reservation-description').parent().hide();
        $('input.reservation-slots').parent().hide();
        $('input.reservation-email').parent().hide();
        $('.new-reservation').find('.reservation-controls').parent().hide();

        $('tr.new-reservation').find('input.reservation-description').val('');
        reservationSlots.val(1);
        $('tr.new-reservation').find('input.reservation-email').val('');
    } else if (response.status === 'overbooked') {
        reservationSlots.parent().addClass('form-group has-error');
        reservationSlots.tooltip({
            title: 'Nur noch  ' + response.slotsAvailable + ' Plätze frei',
            placement: 'bottom',
            trigger: 'manual'
        });
        reservationSlots.tooltip('show');
        reservationSlots.prop('max', response.slotsAvailable);
    } else {
        displayReservationErrorMsg();
    }
}

function fetchEditReservationResponse(response, ctx, row) {
    var reservationEdit = ctx.closest('tr.reservation-edit');
    if (response.status === 'ok') {
        reservationEdit.before(row);
        reservationEdit.remove();
    } else if (response.status === 'overbooked') {
        var reservationSlots = reservationEdit.find('input.reservation-slots');
        reservationSlots.parent().addClass('form-group has-error');
        reservationSlots.tooltip({
            title: 'Nur noch  ' + response.slotsAvailable + ' Plätze frei',
            placement: 'bottom',
            trigger: 'manual'
        });
        reservationSlots.tooltip('show');
        reservationSlots.prop('max', response.slotsAvailable);
    }
}

function fetchRemoveReservationResponse(response, ctx) {
    if (response.status === 'ok') {
        ctx.remove();
    } else {
        displayReservationErrorMsg();
    }
}

var currentEdit;

$(document).on('click', 'button.edit-reservation', function() {
    hideNewReservation();
    hideReservationErrorMsg();
    if (currentEdit) {
        cancelEdit(currentEdit);
    }

    var reservation = $(this).closest('tr.reservation');
    var description = reservation.find('td.description').text();
    var slots = reservation.find('td.slots').text();
    var email = reservation.find('td.email').text();
    var maxSlots = $('#event-remaining-capactiy').val();

    var editDescription =
      `<td class="col-md-6">
        <input type="text" class="form-control input-sm reservation-description" placeholder="Beschreibung" value="${description}" required="required">
      </td>`;
    var editSlots =
      `<td class="col-md-2">
        <input type="number" class="form-control text-right input-sm reservation-slots" value="${slots}" min="1" max="${maxSlots}">
      </td>`;
    var editEmail =
      `<td class="col-md-3">
        <input type="email" class="form-control input-sm reservation-email" placeholder="Kontakt E-Mail" value="${email}">
      </td>`;

    var editControls =
      `<td class="col-md-1" style="vertical-align:middle;">
        <div class="btn-group btn-group-xs" role="group">
          <button type="button" class="btn btn-default hover-success save-reservation">
            <span class="glyphicon glyphicon-ok"></span>
          </button>
          <button type="button" class="btn btn-default hover-danger cancel-edit" data-description="${description}" data-slots="${slots}" data-email="${email}">
            <span class="glyphicon glyphicon-remove"></span>
          </button>
        </div>
      </td>`;

    var editRow = `<tr class="row reservation-edit" data-current-description="${description}">${editDescription}${editSlots}${editEmail}${editControls}</tr>`;

    reservation.before(editRow);
    reservation.remove();

    currentEdit = $('table.reservations').find('tr.reservation-edit').find('button.cancel-edit');
});

$(document).on('click', 'button.save-reservation', function() {
    hideReservationErrorMsg();

    var eventId = $('#event-id').val();
    var reservationEdit = $(this).closest('tr.reservation-edit');
    var oldDescription = reservationEdit.data('current-description');
    var description = reservationEdit.find('input.reservation-description').val();
    var slots = reservationEdit.find('input.reservation-slots').val();
    var email = reservationEdit.find('input.reservation-email').val();

    var row = createReservationRow(description, slots, email);

    const csrfToken = $('#csrf').val();

    var request = {
        async: true,
        data: {
            'event': eventId,
            oldDescription: oldDescription,
            description: description,
            slots: slots,
            email: email,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            fetchEditReservationResponse(response, reservationEdit, row);
        },
        error: displayReservationErrorMsg,
        url: '/api/events/editReservation'
    };
    $.ajax(request);
});

$(document).on('click', 'button.cancel-edit', function() {
    hideReservationErrorMsg();
    cancelEdit($(this));
});

$(document).on('click', 'button.remove-reservation', function() {
    hideNewReservation();
    hideReservationErrorMsg();
    if (currentEdit) {
        cancelEdit(currentEdit);
        currentEdit = undefined;
    }

    var eventId = $('#event-id').val();
    var reservation = $(this).closest('tr.reservation');
    var id = reservation.find('button.remove-reservation').data('description');

    const csrfToken = $('#csrf').val();

    var request = {
        async: true,
        data: {
            'event': eventId,
            id: id,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            fetchRemoveReservationResponse(response, reservation);
        },
        error: displayReservationErrorMsg,
        url: '/api/events/removeReservation'
    };
    $.ajax(request);
});

$('button.add-reservation').click(function() {
    hideReservationErrorMsg();
    if (currentEdit) {
        cancelEdit(currentEdit);
        currentEdit = undefined;
    }

    $('input.reservation-description').parent().slideDown(100);
    $('input.reservation-slots').parent().slideDown(100);
    $('input.reservation-email').parent().slideDown(100);
    $('.new-reservation').find('.reservation-controls').parent().slideDown(100);
});

$('tr.new-reservation').find('button.add').click(function() {
    hideReservationErrorMsg();

    var eventId = $('#event-id').val();
    var description = $('tr.new-reservation').find('input.reservation-description').val();
    var slots = $('tr.new-reservation').find('input.reservation-slots').val();
    var email = $('tr.new-reservation').find('input.reservation-email').val();
    var row = createReservationRow(description, slots, email);

    const csrfToken = $('#csrf').val();

    var request = {
        async: true,
        data: {
            'event': eventId,
            description: description,
            slots: slots,
            email: email,
            _csrf: csrfToken
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            fetchAddReservationResponse(response, row);
        },
        error: displayReservationErrorMsg,
        url: '/api/events/addReservation'
    }
    $.ajax(request);
});

$('tr.new-reservation').find('button.cancel').click(function() {
    hideNewReservation();
    hideReservationErrorMsg();
});

$('#waiting-list').find('.apply-entry').click(function() {
    var row = $(this).closest('tr');
    var id = row.data('id');
    var name = row.find('.name').text();

    var modal = $('#apply-waitingListEntry-modal');
    var form = $('#apply-waitingListEntry');
    form.find('input[name="apply-first"]').val('false');
    form.find('input[name="person-id"]').val(id);
    $('#apply-waitingListEntry-name').val(name);

    modal.modal('show');
});

$('#apply-waitingListTop').click(function() {
    var name = $(this).data('person');
    $('#apply-waitingListEntry').find('input[name="apply-first"]').val('true');
    $('#apply-waitingListEntry-name').val(name);
    $('#apply-waitingListEntry-modal').modal('show');
});

$('#waiting-list').find('.remove-entry').click(function() {
    var row = $(this).closest('tr');
    var id = row.data('id');
    var name = row.find('.name').text();

    var modal = $('#remove-waitingListEntry-modal');
    var form = $('#remove-waitingListEntry');
    form.find('input[name="person-id"]').val(id);
    $('#remove-waitingListEntry-name').val(name);
    modal.modal('show');
})

$(function() {
    $('.no-results').hide();
    $('.searching').hide();
    $('.searching-new-activists').hide();
    $('.new-activists').hide();

    initSearch($('#add-personToContact-modal'));
    initSearch($('#add-waitingList-modal'));

    $('input.reservation-description').parent().hide();
    $('input.reservation-slots').parent().hide();
    $('input.reservation-email').parent().hide();
    $('.new-reservation').find('.reservation-controls').parent().hide();
    $('#reservation-transmission-error').hide();
})
