
function createPersonListEntry(url, csrfToken, personId, name, dob, address) {
    var btn =
    `<div class="clearfix">
        <form class="form-inline pull-right" method="POST" action="${url}">
            <input type="hidden" name="_csrf" value="${csrfToken}" />
            <input type="hidden" name="person-id" value="${personId}" />
            <button type="submit" class="btn btn-default btn-sm">Als Aktive(n) markieren und hinzufügen</button>
        </form>
     </div>`;
     address = address.trim();
     var description = '';
     if (dob || address) {
         description += '<div class="list-group-item-text">';
         if (dob) {
             description += `<span class="pers-dob">* ${dob}</span>`;
         }
         if (address) {
             description += `<span class="pers-address">${address}</span>`;
         }
         description += '</div>';
     }

     var person =
     `<div class="pull-left">
         <h5 class="list-group-item-heading">${name}</h5>
         ${description}
     </div>`;
     return '<li class="list-group-item">' + person + btn + '</li>';
}

function displayMatchingNonActivists(modal, result, eventId) {
    var panel = modal.find('.new-activists');
    var list = panel.find('ul');

    const csrfToken = $('#csrf').val();

    list.empty();

    if (!result.length) {
        panel.hide();
        return;
    }

    for (var i = 0; i < result.length; i++) {
        var person = result[i];
        var entry = createPersonListEntry(eventId, csrfToken, person.id, person.name, person.dob, person.address);
        list.append(entry);
    }

    panel.show();
}

$('#participants').find('a').on('click', function(e){
    e.stopPropagation();
});

// create an html node consisting of the given subelements
// no sanity checks are performed
function createHtmlNode(type, text) {
  var opening = '<' + type + '>';
  var closing = '</' + type + '>';
  return opening + text + closing;
}

// create a table row for persons
function createPersonRow(id, name, dob, address) {
  var selectColumn = '<td class="text-center"><input type="radio" name="person-id" value="' + id + '" required="required" /></td>';
  return '<tr>' + createHtmlNode('td', name) + createHtmlNode('td', dob) + createHtmlNode('td', address) + selectColumn + '</tr>';
}

// displays the matching persons in the 'add member' modal
function displayMatchingPersons(table, result) {
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

    var row = createPersonRow(person.id, person.name, dob, person.address);

    $(table).append(row);
  }
}
