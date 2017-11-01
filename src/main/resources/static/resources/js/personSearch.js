function initSearch(modal, searchType = 'simpleSearch') {
  var firstName = modal.find('.first-name');
  var lastName = modal.find('.last-name');
  var city = modal.find('.city');
  var btn = modal.find('.search');

  btn.on('click', function() {
    performSearch(modal, firstName.val(), lastName.val(), city.val(), searchType);
  });
}

function performSearch(modal, firstName, lastName, city, searchType = 'simpleSearch') {
  var table = modal.find('tbody.matches');

  const csrfToken = $('#csrf').val();

  var request = {
    async: true,
    data: {
      firstname: firstName,
      lastname: lastName,
      city: city,
      _csrf: csrfToken
    },
    dataType: 'json',
    method: 'POST',
    success: function(response) {
      modal.find('.searching').hide();
      displayMatchingPersons(table, response);
    },
    url: '/api/persons/' + searchType
  };

  $(table).empty();
  modal.find('.searching').show();
  $.ajax(request);
}
