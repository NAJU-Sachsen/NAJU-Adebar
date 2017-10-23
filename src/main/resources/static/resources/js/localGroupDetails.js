
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

    var request = {
        async: true,
        data: {
            groupId: groupId
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
$('#remove-member-modal').on('show.bs.modal', function(e) {
    var button = $(e.relatedTarget);
    var id = button.data('id');
    var name = button.data('name');

    $(this).find('input[disabled]').val(name);
    $(this).find('input[type=hidden]').val(id);
});

// init all js components
$(function(){
    $('#add-member-modal').find('.no-results').hide();
    $('#add-member-modal').find('.searching').hide();
    initSearch($('#add-member-modal'));

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
