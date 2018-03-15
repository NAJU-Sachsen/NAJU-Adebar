function updateInputs(category, active) {
  if (category.hasClass('toggle-category')) {
    const toggleOption = active ? 'show' : 'hide';
    category.find('.form-category-content').collapse(toggleOption);
  }
  category.find(
      'input:not([data-controlled-by]):not(.form-category-controller)').prop(
      'disabled', !active);
  category.find(
      'button:not([data-controlled-by]):not(.form-category-controller)').prop(
      'disabled', !active);
  category.find(
      'select:not([data-controlled-by]):not(.form-category-controller)').prop(
      'disabled', !active);
  category.find(
      'textarea:not([data-controlled-by]):not(.form-category-controller)').prop(
      'disabled', !active);
  category.find('.form-category-content').toggleClass('disabled', !active);
  category.find('select.selectpicker').selectpicker('refresh');
}

/*
 * form categories
 */

function CategoryController(controller, target) {
  this.controller = controller;
  this.target = target;

  this.updateCategory = function () {
    const active = isActive(controller);
    target.updateInputs(active);
  };

  function isActive(controller) {
    return controller.prop('checked') || controller.prop('selected')
        || (controller.data('active') === 'true');
  }

  this.updateCategory();
  controller.change(this.updateCategory);
}

function FormCategory(category) {
  this.category = category;
  this.updateInputs = (active) => updateInputs(category, active);
}

function initCategory(category) {
  category = $(category);
  const controllerElem = findController(category);
  const formCategory = new FormCategory(category);
  const formController = new CategoryController(controllerElem, formCategory);
}

function initToggledCategories() {
  $('.form-category.toggle-category').each((idx, category) => {
    $(category).find('.form-category-content').addClass('collapse');
  });
}

function findController(category) {
  const externController = $(category.data('controlled-by'));
  return externController.length ? externController : category.find(
      '.form-category-controller');
}

/*
 * controlled inputs
 */

function updateControlledInput(controller, input) {
  let active;
  switch (controller.prop('tagName').toLowerCase()) {
    case 'select':
      const selectBox = $(input.data('controlled-by')).closest('select');
      active = $(input.data('controlled-by')).get(0)
          === selectBox.find(':selected').get(0);
      break;
    case 'input':
      const checkbox = controller.is(':checkbox');
      active = (checkbox && controller.prop('checked')) || (!checkbox
          && controller.val());
      break;
  }

  if (input.data('invert-control')) {
    active = !active;
  }

  if (input.prop('tagName').toLowerCase() === 'input') {
    input.prop('disabled', !active);
  } else {
    updateInputs(input, active);
  }
}

function initControlledInputs() {
  $('[data-controlled-by]').each(
      function (idx, input) {
        const targetInput = $(input);
        let controller = $(input.dataset.controlledBy);
        if (controller.prop('tagName').toLowerCase() === 'option') {
          controller = controller.closest('select');
        }

        if (controller.data('handler-registered')) {
          return;
        }

        const handler = () => updateControlledInput(controller, targetInput);

        // register the handler for textual changes as well as "normal" ones
        controller.keyup(handler).change(handler);

        // initialize the controlled input according to the controller's state
        updateControlledInput(controller, targetInput);

        // mark the controller as initialized to prevent re-attaching the same
        // handler to it
        controller[0].dataset.handlerRegistered = 'true';
      });
}

(function () {
  $(document).ready(function () {
    initToggledCategories();
    $('.form-category').each(function (idx, category) {
      initCategory(category);
    });
    initControlledInputs();
  });
})();
