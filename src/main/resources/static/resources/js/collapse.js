
function toggleElement(e) {
  const toggler = e.target.closest('[data-toggle]');
  const elem = $(toggler.dataset.target);

  const showElement = elem.css('display') === 'none';
  const display = showElement ? 'inline-block' : 'none';

  elem.css('display', display);
  elem.toggleClass('collapsed-inline', !showElement);

  document.getElementById(`${elem.attr('id')}`).scrollIntoView();

  elem.find('.panel').removeClass('panel-default');
  elem.find('.panel').addClass('panel-info');
  setTimeout(() => {
    elem.find('.panel').removeClass('panel-info');
    elem.find('.panel').addClass('panel-default');
  }, 500);

  elem.trigger('changed.adebar.collapse');
}

$(() => {
  $('[data-toggle="collapse-inline"]').each((idx, toggler) => {
    const elem = $(toggler.dataset.target);
    const display = toggler.checked ? 'inline-block' : 'none';
    elem.css('display', display);
    elem.toggleClass('collapsed-inline', !toggler.checked);
    $(toggler).change(toggleElement);
  });
});
