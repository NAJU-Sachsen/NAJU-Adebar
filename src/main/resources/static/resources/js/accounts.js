
function initEditRolesForm(event) {
  const btn = $(event.relatedTarget);
  const modal = $('#edit-roles');
  const roles = btn.closest('tr').find('.roles').data('roles');
  const account = btn.closest('tr').find('.username').text();
  modal.find('select.roles').val(roles.split(';'));
  modal.find('input[name="account"]').val(account);
}

function initResetPasswordForm(event) {
  const btn = $(event.relatedTarget);
  const modal = $('#reset-password');
  const account = btn.closest('tr').find('.username').text();
  modal.find('input[name="account"]').val(account);
}

function initDeleteAccountForm(event) {
  const btn = $(event.relatedTarget);
  const modal = $('#delete-account');
  const person = btn.closest('tr').find('.person').text();
  const account = btn.closest('tr').find('.username').text();
  modal.find('input[disabled="disabled"]').val(person);
  modal.find('input[name="account"]').val(account);
}

function displaySearchResults(matches) {
  const list = $('#new-account').find('.search-results');
  for (let person of matches) {
    const id = person.id;
    const name = person.name;
    const dob = person.dob || '';
    const city = person.address || '';
    list.append(
      `<li class="list-group-item row">
        <h5 class="list-group-item-heading">${name}</h5>
        <p class="list-group-item-text col-md-10">${dob}, ${city}</p>
        <div class="col-md-2">
          <input type="radio" name="person" value="${id}" />
        </div>
      </li>`
    );
  }
}

function searchPersons() {
  const form = $('#new-account');
  const searching = form.find('.searching');
  const noResults = form.find('no-results');

  const criteria = form.find('.person-search').val();
  const csrfToken = $('#csrf').val();

  const request = {
    async: true,
    data: {
      query: criteria,
      _csrf: csrfToken
    },
    dataType: 'json',
    method: 'POST',
    success: function(matches) {
      searching.hide();
      if (!matches.length) {
        noResults.show();
      } else {
        displaySearchResults(matches);
      }
    },
    url: '/api/persons/defaultSearch'
  };

  noResults.hide();
  searching.show();
  form.find('.search-results').empty();
  $.ajax(request);
}

$(function() {
  const fuzzyOptions = {
    searchClass: "fuzzy-search",
    location: 0,
    distance: 100,
    threshold: 0.4,
    multiSearch: true
  };
  const searchOptions = {
    listClass: 'account-list',
    valueNames: ['person', 'username', {name: 'roles', attr: 'data-roles'}],
    plugins: [ListFuzzySearch(fuzzyOptions)]
  };
  new List('main', searchOptions);

  $('#edit-roles').on('show.bs.modal', initEditRolesForm);
  $('#reset-password').on('show.bs.modal', initResetPasswordForm);
  $('#delete-account').on('show.bs.modal', initDeleteAccountForm);

  const newAccountForm = $('#new-account');
  newAccountForm.find('.searching').hide();
  newAccountForm.find('.no-results').hide();
  newAccountForm.find('.perform-search').on('click', searchPersons);
});
