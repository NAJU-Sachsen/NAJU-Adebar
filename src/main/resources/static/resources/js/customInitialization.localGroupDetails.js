
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
    $(table).empty();

    for (var i = 0; i < result.length; i++) {
        var person = result[i];

        var row = createPersonRow(person.id, person.name, person.dob, person.address);

        $(table).append(row);
    }
}

// display event popover
function renderEvent(e) {
    if(e.events.length > 0) {
        var content = '';

        for(var i in e.events) {
            content += '<div class="event-tooltip-content">'
                            + '<div class="event-name" style="color:' + e.events[i].color + '">' + e.events[i].name + '</div>'
                            + '<div class="event-location">' + e.events[i].place + '</div>'
                        + '</div>';
        }

        $(e.element).popover({
            trigger: 'manual',
            container: 'body',
            html:true,
            content: content
        });

        $(e.element).popover('show');
    }
};

// show the 'add event' modal
function createEvent(e) {
    $('#add-event-startTime').val(moment(e.startDate).format('DD.MM.YYYY HH:mm'));
    $('#add-event-endTime').val(moment(e.endDate).format('DD.MM.YYYY HH:mm'));
    $('#add-event-modal').modal('show');
}

// converts a java local date time to a similiar js instance
function convertDate(d) {
    var year = d.year;
    var month = d.monthValue - 1;
    var day = d.dayOfMonth;
    var date = new Date(year, month, day);
    return date;
}

// converts an event as provided by the Adebar-API to a similiar js object
function convertEvent(e) {
    return {
        id: e.id,
        name: e.name,
        startDate: convertDate(e.startDate),
        endDate: convertDate(e.endDate),
        place: e.place,
    }
}

// display all events
function displayEvents(events) {

    for (var i = 0; i < events.length; ++i) {
        var e = convertEvent(events[i]);

        var dataSource = $('#event-calendar').data('calendar').getDataSource();
        dataSource.push(e);

        $('#event-calendar').data('calendar').setDataSource(dataSource);
    }
}

// fetch and display all events
function initEvents() {
    var groupId = $('#local-group-id').val();

    var request = {
        async: true,
        data: {
            groupId: groupId,
        },
        dataType: 'json',
        success: displayEvents,
        url: '/api/events/localGroup',
    }

    $.ajax(request);
}

// display the activists matching the given query
$('#add-member-search-btn').on('click', function() {
    var table = '#add-member-tablebody';
    var firstname = $('#add-member-search-firstname').val();
    var lastname = $('#add-member-search-lastname').val();
    var city = $('#add-member-search-city').val();

    var request = {
        async: true,
        data: {
            firstname: firstname,
            lastname: lastname,
            city: city,
        },
        dataType: 'json',
        method: 'POST',
        success: function(response) {
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $.ajax(request);
});

// sync selected board members
$('select#edit-board-select-newMember').on('changed.bs.select', function(e) {
    $('select#edit-board-select-newMember').selectpicker('toggle');
    $('#edit-board-members').val($('select#edit-board-select-newMember').val());
});

// sync select board members
$('#edit-board-members').on('change', function(e) {
    $('select#edit-board-select-newMember').val($('#edit-board-members').val());
    $('select#edit-board-select-newMember').selectpicker('refresh');
});

// redirect to clicked events
$('#event-calendar').on('clickDay', function(e) {
    var events = e.events;

    if (events.length > 0) {
        var ev = events[0];
        window.location.href = '/events/' + ev.id;
    }
});

// dont show modals when clicking on links
$('#members a').on('click', function(e) {
    e.stopPropagation();
})

// init the 'remove member' modal
$('#remove-member-modal').on('show.bs.modal', function(e) {
    var button = $(e.relatedTarget);
    var id = button.data('id');
    var name = button.data('name');

    $(this).find('input[disabled]').val(name);
    $(this).find('input[type=hidden]').val(id);
})

// init all js components
$(function(){
    $('#add-project-period input').datetimepicker({
        format: 'DD.MM.YYYY',
        showTodayButton: true,
    });

    $('#event-calendar').calendar({
        style: 'border',
        enableRangeSelection: true,
        mouseOnDay: renderEvent,
        mouseOutDay: function(e) {
            if(e.events.length > 0) {
                $(e.element).popover('hide');
            }
        },
        selectRange: function(e) {
            createEvent({ startDate: e.startDate, endDate: e.endDate });
        },
        dataSource: [],
    });

    $('#event-startTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true,
    });
    $('#event-endTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true,
    });

    initEvents();
});
