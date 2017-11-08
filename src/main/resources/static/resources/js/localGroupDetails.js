
// display event popover
function renderEvent(e) {
    if(e.events.length > 0) {
        var content = '';

        for(var i = 0; i < e.events.length; i++) {
            if (e.hasOwnProperty('events')) {
                content += '<div class="event-tooltip-content">'
                    + '<div class="event-name" style="color:' + e.events[i].color + '">' + e.events[i].name + '</div>'
                    + '<div class="event-location">' + e.events[i].place + '</div>'
                    + '</div>';
            }
        }

        $(e.element).popover({
            trigger: 'manual',
            container: 'body',
            html:true,
            content: content
        });

        $(e.element).popover('show');
    }
}

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
    return new Date(year, month, day);
}

// converts an event as provided by the Adebar-API to a similiar js object
function convertEvent(e) {
    return {
        id: e.id,
        name: e.name,
        startDate: convertDate(e.startDate),
        endDate: convertDate(e.endDate),
        place: e.place
    }
}

// display all events
function displayEvents(events) {

    for (var i = 0; i < events.length; ++i) {
        var e = convertEvent(events[i]);

        var calendar = $('#event-calendar').data('calendar');
        var dataSource = calendar.getDataSource();
        dataSource.push(e);

        calendar.setDataSource(dataSource);
    }
}

// fetch and display all events
function initEvents() {
    var groupId = $('#local-group-id').val();

    const csrfToken = $('#csrf').val();

    var request = {
        async: true,
        data: {
            groupId: groupId,
            _csrf: csrfToken
        },
        dataType: 'json',
        success:  displayEvents,
        url: '/api/events/localGroup'
    };

    $.ajax(request);
}

// sync selected board members
$('select#edit-board-select-newMember').on('changed.bs.select', function() {
    $(this).selectpicker('toggle');
    $('#edit-board-members').val($(this).val());
});

// sync select board members
$('#edit-board-members').on('change', function() {
    var selectPicker = $('select#edit-board-select-newMember');
    selectPicker.val($('#edit-board-members').val());
    selectPicker.selectpicker('refresh');
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
$('#members').find('a').on('click', function(e) {
    e.stopPropagation();
});

// init the 'remove member' modal
$('#remove-member-modal').on('show.bs.modal', function(event) {
  const button = $(event.relatedTarget);
  const id = button.data('id');
  const name = button.data('name');
  const csrfToken = $('#csrf').val();

  $(this).find('input[disabled]').val(name);
  $(this).find('input[name="member-id"]').val(id);
  $(this).find('input[name="_csrf"]').val(csrfToken);
});

$('#add-member-search-btn').on('click', function() {
    var table = '#add-member-tablebody';
    var firstname = $('#add-member-search-firstname').val();
    var lastname = $('#add-member-search-lastname').val();
    var city = $('#add-member-search-city').val();
    var localGroupId = $('#local-group-id').val();
    var url = `/localGroups/${localGroupId}/members/add-new`;

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
            $('#add-member-modal').find('.searching').hide();
            displayMatchingPersons(table, response);
        },
        url: '/api/persons/activists/simpleSearch'
    };

    $(table).empty();
    $('#add-member-modal').find('.searching').show();
    $.ajax(activistsRequest);

    var modal = $('#add-member-modal');
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
            $('#add-member-modal').find('.searching-new-activists').hide();
            displayMatchingNonActivists(modal, response, url);
        },
        url: '/api/persons/search'
    };

    modal.find('.new-activists').hide();
    $('#add-member-modal').find('.searching-new-activists').show();
    $.ajax(personsRequest);
});

// init all js components
$(function(){
    $('#add-member-modal').find('.no-results').hide();
    $('#add-member-modal').find('.searching').hide();
    $('.searching-new-activists').hide();
    $('.new-activists').hide();

    $('#add-project-period').find('input').datetimepicker({
        format: 'DD.MM.YYYY',
        showTodayButton: true
    });

    var boardSelectPicker = $('select#edit-board-select-newMember');
    boardSelectPicker.val($('#edit-board-members').val());
    boardSelectPicker.selectpicker('refresh');

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
        dataSource: []
    });

    $('#event-startTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true
    });
    $('#event-endTime-picker').datetimepicker({
        format: 'DD.MM.YYYY HH:mm',
        showTodayButton: true
    });

    initEvents();
});
