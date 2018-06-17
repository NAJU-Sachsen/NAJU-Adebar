
function activeFilters() {
  return $('.filter-group:not(.collapsed-inline)');
}

function inactiveFilters() {
  return $('.filter-group.collapsed-inline');
}

function updateNoFilterMessage() {
  if (activeFilters().length === 0) {
    $('.no-filters').show();
  } else {
    $('.no-filters').hide();
  }
}

function updateNavigation() {
  for (const filter of activeFilters()) {
    const li = $(`[href="#${filter.id}"]`).closest('li');
    li.slideDown();
  }
  for (const filter of inactiveFilters()) {
    const li = $(`[href="#${filter.id}"]`).closest('li');
    li.slideUp();
  }
}

$(() => {
  updateNavigation();
  updateNoFilterMessage();
  $(document).on('changed.adebar.collapse', () => {
    updateNavigation();
    updateNoFilterMessage();
  });
});
