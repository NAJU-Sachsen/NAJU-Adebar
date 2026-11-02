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

  $('#edit-event-arrival-opts').append(row);
  optInp.val('');
}

$(document).ready(function () {
  $(document).on('click', '.remove-arrival-opt', removeArrivalOpt);
  $('#add-arrival-opt').click(saveArrivalOpt);
});
