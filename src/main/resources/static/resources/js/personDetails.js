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
