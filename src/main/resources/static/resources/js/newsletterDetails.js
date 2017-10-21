function createHtmlNode(type, text) {
  var opening = '<' + type + '>';
  var closing = '</' + type + '>';
  return opening + text + closing;
}

function createRow(id, name, dob, address) {
  var selectColumn = '<td class="text-center"><input type="radio" name="person-id" value="'
      + id + '" required="required" /></td>';
  return '<tr>' + createHtmlNode('td', name) + createHtmlNode('td', dob)
      + createHtmlNode('td', address) + selectColumn + '</tr>';
}

function displayResult(table, result) {

  $(table).empty();

  for (var i = 0; i < result.length; i++) {
    var person = result[i];

    var row = createRow(person.id, person.name, person.dob, person.address);

    $(table).append(row);
  }
}

$('#add-subscriber-person-tab').on('shown.bs.tab', function () {
  $('#add-subscriber-submit').attr('form', 'add-subscriber-person-select');
});

$('#add-subscriber-new-tab').on('shown.bs.tab', function () {
  $('#add-subscriber-submit').attr('form', 'add-subscriber-new-select');
});

$('#delete-subscriber-modal').on('show.bs.modal', function (event) {
  var button = $(event.relatedTarget);
  var recipient = button.data('email');
  var modal = $(this);
  modal.find('.modal-body input').val(recipient)
});

$('#add-subscriber-search-btn').on('click', function () {
  var table = '#add-subscriber-tablebody';
  var firstname = $('#add-subscriber-search-firstname').val();
  var lastname = $('#add-subscriber-search-lastname').val();
  var city = $('#add-subscriber-search-city').val();

  var request = {
    async: true,
    data: {
      firstname: firstname,
      lastname: lastname,
      city: city
    },
    dataType: 'json',
    method: 'POST',
    success: function (response) {
      displayResult(table, response);
    },
    url: '/api/persons/simpleSearch'
  };

  $(table).empty();
  $.ajax(request);
});
