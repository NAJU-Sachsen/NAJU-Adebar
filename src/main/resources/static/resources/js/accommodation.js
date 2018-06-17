function removeRoom(e) {
  const btn = $(e.target);
  btn.closest('.room').remove();
}

function addRoom() {
  const beds = $('#edit-accommodation-add-room-beds').val();
  const type = $('#edit-accommodation-add-room-type').val();
  const typeLabel = $('#edit-accommodation-add-room-type')
  .find('option:selected').text();

  const row = `
  <tr class="row">
    <td class="col-md-4">
      <span class="glyphicon glyphicon-bed margin-right"></span>
      ${beds}
      <input type="hidden" name="roomCapacities" value="${beds}"/>
    </td>
    <td class="col-md-7">
      <span class="glyphicon glyphicon-user margin-right"></span>
      ${typeLabel}
      <input type="hidden" name="roomTypes" value="${type}"/>
    </td>
    <td class="col-md-1">
      <div class="clearfix margin-right">
        <div class="pull-right">
          <button type="button"
                  class="btn btn-danger btn-xs remove-room">
            <span class="glyphicon glyphicon-remove"></span>
          </button>
        </div>
      </div>
    </td>
  </tr>`;

  $('#edit-accommodation').find('.rooms-list').append(row);

}

function fetchParticipantDetails(e) {
  const btn = $(e.target);
  const eventId = $('meta[name="event"]').attr('value');
  const participant = btn.data('participant');
  const csrfToken = $('meta[name="_csrf"]').attr('value');

  console.log('participant', participant);

  const ajaxData = {
    _csrf: csrfToken,
    participant: participant,
    event: eventId
  };

  $.post('/api/events/participant-details', ajaxData,
      (res) => {
        btn.popover({
          content: res,
          html: true,
          placement: 'top',
          container: 'body',
          trigger: 'focus'
        }).popover('show');
        initTooltips();
      });
}

$(document).ready(() => {
  $(document).on('click', '.remove-room', removeRoom);
  $('#edit-accommodation-add-room').click(addRoom);
  $('.show-participant-details').click(fetchParticipantDetails);
});
