function eatingHabitsContainsCustomInfo(content) {
  if (!content) {
    return false;
  }

  const options = $('#eating-habits-select').find(
      'option:not([value=""])');

  for (const option of options) {
    if (content === option.value) {
      return false;
    }
  }

  return true;
}

function hideEatingHabitsSelect() {
  const selectPicker = $('#eating-habits-select');
  selectPicker.find('option').prop('disabled', true);
  $('#eating-habits-default-display').slideUp();
  $('#eating-habits-container').slideDown();
}

function initEatingHabitsSelect() {
  const eatingHabits = $('#eating-habits').val();
  const selectPicker = $('#eating-habits-select');

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
$('#eating-habits-select').change(eatingHabitSelected);
$('#eating-habits-toggle').click(hideEatingHabitsSelect);

function eatingHabitSelected(e) {
  const selectedValue = e.target.value;
  const textBox = $('#eating-habits');
  textBox.val(selectedValue);
}

$(document).ready(initEatingHabitsSelect);
