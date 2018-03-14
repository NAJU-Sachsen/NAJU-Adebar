/*
 * lazy-fetch the tab content
 */

function eatingHabitsContainsCustomInfo(content) {
  if (!content) {
    return false;
  }

  const options = $('#edit-eating-habits-select').find(
      'option:not([value=""])');

  for (const option of options) {
    if (content === option.value) {
      return false;
    }
  }

  return true;
}

function hideEatingHabitsSelect() {
  const selectPicker = $('#edit-eating-habits-select');
  selectPicker.find('option').prop('disabled', true);
  $('#edit-eating-habits-default-display').slideUp();
  $('#eating-habits-container').slideDown();
}

function initEatingHabitsSelect() {
  const eatingHabits = $('#edit-eating-habits').val();
  const selectPicker = $('#edit-eating-habits-select');

  selectPicker.find('option').each(function () {
    if (eatingHabits.includes(this.value)) {
      this.selected = true;
    }
  });
  selectPicker.selectpicker('refresh');

  if (eatingHabitsContainsCustomInfo(eatingHabits)) {
    hideEatingHabitsSelect();
  }
}

$('#eating-habits-container').on('shown.bs.collapse', hideEatingHabitsSelect);
$('#edit-eating-habits-select').change(eatingHabitSelected);
$('#edit-eating-habits-toggle').click(hideEatingHabitsSelect);

function eatingHabitSelected(e) {
  const selectedValue = e.target.value;
  const textBox = $('#edit-eating-habits');
  textBox.val(selectedValue);
}

$(document).ready(initEatingHabitsSelect);

/**
 * Contains all the listeners that can be registered.
 *
 * Format:
 * jQuery-selector: {
 *  event: the event to listen for, defaults to "change
 *  action: the function to run
 * }
 */
const eventListeners = {
  '#edit-eating-habits-select': {
    action: function (val) {
      console.log('eating habits select', val);
    }
  }
};

/**
 * Tries to register each non-registered event listener by modifying the
 * eventListeners.
 */
function initEventListeners() {
  for (const selector in eventListeners) {
    const event = eventListeners[selector].hasOwnProperty('event')
        ? eventListeners[selector].event : 'change';

    // check if the element exists
    const elem = $(selector);
    if (elem.length) {
      elem.on(event, eventListeners[selector].action);

      // only register the event once
      delete eventListeners[selector];
    }
  }
}

/**
 * Loads the content of the tab(s) targeted by the anchor. The target has to be
 * a member of the lazy-load class and specify the url to load in the data-load
 * property
 * @param anchor the anchor
 */
function loadTabContent(anchor) {
  const target = $(anchor.getAttribute('href'));
  const requestData = {
    _csrf: $('meta[name="_csrf"]').attr('value')
  };
  initEventListeners();

  for (const lazyContainer of target.find('.lazy-load')) {
    const url = lazyContainer.dataset.load + '?' + $.param(requestData);
    $(lazyContainer).load(url, () => {
      // once the container is loaded, it should not be loaded again when its
      // tab is re-selected
      lazyContainer.classList.remove('lazy-load');
    });
  }

}

/**
 * Register the event listener to load the tab content when it is selected
 */
$('.tab-controller').on('show.bs.tab', function (e) {
  loadTabContent(e.target);
});

/**
 * Load all contents of the currently active tab when the page is loaded
 */
$(document).ready(function () {
  for (const tab of $('ul[role="tablist"] li.active')) {
    // we know that each such link contains an anchor so it is save to just
    // load it
    loadTabContent(tab.getElementsByTagName('a')[0]);
  }
});
