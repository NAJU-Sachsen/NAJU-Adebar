function initSearch(modal, searchType = 'simpleSearch') {
  const firstName = modal.find('.first-name');
  const lastName = modal.find('.last-name');
  const city = modal.find('.city');
  const btn = modal.find('.search');

  btn.on('click', function () {
    performSearch(modal, firstName.val(), lastName.val(), city.val(),
        searchType);
  });
}

function performSearch(modal, firstName, lastName, city,
    searchType = 'simpleSearch') {
  const table = modal.find('tbody.matches');

  const csrfToken = $('#csrf').val();

  const request = {
    async: true,
    data: {
      firstname: firstName,
      lastname: lastName,
      city: city,
      _csrf: csrfToken
    },
    dataType: 'json',
    method: 'POST',
    success: function (response) {
      modal.find('.searching').hide();
      displayMatchingPersons(table, response);
    },
    url: '/api/persons/' + searchType
  };

  $(table).empty();
  modal.find('.searching').show();
  $.ajax(request);
}
