$('#filter-referent-qualifications').selectpicker({
    actionsBox: true,
    width: 'auto',
    liveSearch: true,
    deselectAllText: 'Keins',
    selectAllText: 'Alle',
});


// initialize the date of birth datepicker
$('#dob-picker').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true,

});

// initialize the juleica expiry date picker
$('#add-person-juleica').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true,
});

// initialize the date of birth datepicker for filters
$('#filter-dob-container').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true,
});

// initialize the juleica expiry date picker for filters
$('#filter-juleica-expiry-container').datetimepicker({
    format: 'DD.MM.YYYY',
    showTodayButton: true,
});

// toggle juleica-checkbox depending on whether the person is an activist or not
$('#add-person-isActivist').click(function() {
    $('#add-person-hasJuleica-container').slideToggle();
});

// toggle juleica expiry date input depending on whether the person has a
// juleica or not
$('#add-person-hasJuleica').click(function() {
    $('#add-person-juleica-container').slideToggle();

    // toggle the required property of the expiry date
    $('#add-person-juleica input').prop('required', function(i,v) {return !v;});
});


$('#add-person-isReferent').click(function() {
    $('#add-person-referent-qualification-container').slideToggle();
});

// disable nabu membership number if checkbox is not selected
$('#add-person-isNabuMember').click(function() {
    $('#add-person-nabuNr').prop('disabled', function(i,v){return !v;});
});

// show gender select only if gender should be filtered
$('#filter-gender-type').on('change', function() {
    if ($('#filter-gender-type option:selected').val() == 'none') {
        $('#filter-gender-selection').addClass('hidden');
    } else {
        $('#filter-gender-selection').removeClass('hidden');
    }
});

// show dob select only if dob should be filtered
$('#filter-dob-type').on('change', function() {
    if ($('#filter-dob-type option:selected').val() == 'none') {
        $('#filter-dob-container').addClass('hidden');
    } else {
        $('#filter-dob-container').removeClass('hidden');
    }
});

var filterNabuMembershipType = ($('#filter-nabu-type option:selected').val() == 'ENFORCE');

// show nabu membership number input only if only for nabu members should be filtered
$('#filter-nabu-type').on('change', function() {
    if ($('#filter-nabu-type option:selected').val() == 'ENFORCE' && !filterNabuMembershipType) {
        $('#filter-nabu-membership-number').slideToggle();
        filterNabuMembershipType = !filterNabuMembershipType;
    } else if (filterNabuMembershipType){
        $('#filter-nabu-membership-number').slideToggle();
        filterNabuMembershipType = !filterNabuMembershipType;
    }
});

$('#filter-activist-type').on('change', function() {
    if ($('#filter-activist-type option:selected').val() != 'ENFORCE') {
        $('#filter-activist-hasJuleica-container').slideUp();
    } else {
        $('#filter-activist-hasJuleica-container').slideDown();
    }
});

$('#filter-activist-juleica-type').on('change', function() {
    if ($('#filter-activist-juleica-type option:selected').val() != 'ENFORCE') {
        $('#filter-activist-juleicaDate-container').slideUp();
    } else {
        $('#filter-activist-juleicaDate-container').slideDown();
    }
});

$('#filter-activist-juleicaDate-type').on('change', function() {
    if ($('#filter-activist-juleicaDate-type option:selected').val() == 'none') {
        $('#filter-juleica-expiry-container').slideUp();
    } else {
        $('#filter-juleica-expiry-container').slideDown()
    }
});

$('#filter-referent-type').on('changed.bs.select', function (e) {
    if ($('#filter-referent-type').val() != 'ENFORCE') {
        $('#filter-referents-qualification-container').slideUp();
    } else {
        $('#filter-referents-qualification-container').slideDown();
    }
});