function removeArrivalOpt(btn) {
  $(btn.target).closest('li').remove();
}

function saveArrivalOpt() {
  const optInp = $('#arrival-opt');
  const opt = optInp.val();

  if (!opt) {
    return;
  }

  const row = `
    <li class="list-group-item">
      ${opt}
      <input type="hidden" name="participationInfo.arrivalOptions" value="${opt}"/>
      <button type="button" class="btn btn-danger btn-xs pull-right remove-arrival-opt">
        <span class="glyphicon glyphicon-remove"></span>
      </button>
    </li>
  `;

  $('#arrival-options').append(row);
  optInp.val('');
}

function resolveRoomTypeName() {
  return $('#accommodation-room-type').find('option:selected').text();
}

function saveNewRoom() {
  const bedsInp = $('#accommodation-beds-count');
  const roomTypeInp = $('#accommodation-room-type');

  const beds = bedsInp.val();
  const roomType = roomTypeInp.val();
  const roomName = resolveRoomTypeName();

  if (!beds || !roomType) {
    return;
  }

  const row =
      `<tr class="row">
        <td class="col-md-2">
          <span class="glyphicon glyphicon-bed margin-right"></span>
          ${beds}
          <input type="hidden" name="accommodation.roomCapacities" value="${beds}"/>
        </td>
        <td class="col-md-8">
          <span class="glyphicon glyphicon-user margin-right"></span>
          ${roomName}
          <input type="hidden" name="accommodation.roomTypes" value="${roomType}"/>
        </td>
        <td class="col-md-2">
          <button type="button" class="btn btn-danger btn-xs pull-right remove-accommodation">
            <span class="glyphicon glyphicon-remove"></span>
          </button>
        </td>
      </tr>`;

  $('#accommodation-rooms').append(row);

  bedsInp.val(1);
  roomTypeInp.prop('selectedIndex', 0);
  roomTypeInp.selectpicker('refresh');
}

function removeRoom(btn) {
  $(btn.target).closest('tr').remove();
}

$(document).ready(function () {
  $(document).on('click', '.remove-arrival-opt', removeArrivalOpt);
  $('#add-arrival-opt').click(saveArrivalOpt);
  $(document).on('click', '.remove-accommodation', removeRoom);
  $('#accommodation-add-room').click(saveNewRoom);
});
