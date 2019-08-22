
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
  const active = activeFilters();
  for (const filter of active) {
    const li = $(`[href="#${filter.id}"]`).closest('li');
    li.slideDown();
  }

  if (active.length) {
	  $('#filter-quick-nav-title').slideDown();
  } else {
	  $('#filter-quick-nav-title').slideUp();
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
