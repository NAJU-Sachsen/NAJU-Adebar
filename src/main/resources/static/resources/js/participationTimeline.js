$(document).ready(() => {
	$('#toggle-participant').change((e) => {
		const action = e.target.checked ? 'show' : 'hide';
		$('.timeline-entry-wrapper.participant').collapse(action);
	});

	$('#toggle-counselor').change((e) => {
		const action = e.target.checked ? 'show' : 'hide';
		$('.timeline-entry-wrapper.counselor').collapse(action);
	});

	$('.timeline-entry-wrapper').collapse();
});
