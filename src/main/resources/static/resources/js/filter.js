
/*class MatchingEntity {
	constructor(id, title, description, modal) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.modal = modal;
		this.template = modal.querySelector('.matching-entity-template');

		this.element  = this.generateEntityNode();
		this.insertEntity();
	}

	toggleSelection() {
		this.selected = !this._selected;
		this.updateSelection();
	}

	release() {
		this.element.parent.removeChild(this.element);
	}

	generateEntityNode() {
		const entity = document.importNode(this.template.content, true);
		const nodeTitle = entity.querySelector('.entity-title');
		const nodeDescription = entity.querySelector('.entity-description');
		const nodeSelect = entity.querySelector('.entity-select-value');
		nodeTitle.textContent = this.title;
		nodeDescription.textContent = this.description;
		nodeSelect.value = this.id;
		return entity;
	}

	insertEntity() {
		this.modal.querySelector('.entity-select-showcase').appendChild(this.element);
	}

	updateSelection() {
		this.element.querySelector('.entity-select-value').checked = this.selected;
	}

}

class SearchModal {
	constructor(container) {
		this.container = container;
		this.results = container.querySelector('.entity-select-showcase');
		this.searchBtn = container.querySelector('.do-search');
		this.confirmBtn = container.querySelector('.confirm-entity-selection');
		this.selected = [];
	}
}

class EntityFilter {
	constructor(entity) {
		this.entity = entity;
		this.modal = entity.querySelector('.modal');
		this.selection = entity.querySelector('.entity-container');

		this.modal.addEventListener('selected', this.updateSelectedEntities);
	}

	updateSelectedEntities() {
		empty(this.selection);

	}


}*/

function empty(node) {
	while (node.firstChild) {
		node.removeChild(node.firstChild);
	}
}

document.addEventListener('DOMContentLoaded', () => {

	document.querySelectorAll('.confirm-entity-selection').forEach(
		btn => {
			btn.addEventListener('click', event => {
				const matchesContainer = btn.closest('.entity-selection-modal').querySelector('.entity-select-showcase');
				const entityContainer = btn.closest('.entity-filter').querySelector('.entity-container');
				const entityNames = entityContainer.dataset.valueName;
				const selectedNodes = matchesContainer.querySelectorAll('input:checked');

				selectedNodes.forEach(entity => {
					const title = entity.dataset.title;
					const description = entity.dataset.description;
					const id = entity.value;

					const listEntry = document.createElement('li');
					listEntry.classList.add('list-group-item', 'entity');
					listEntry.innerHTML = `
						${title}
						<small>${description}</small>
						<input type="hidden" name="${entityNames}" value="${id}" />
						<div class="pull-right entity-selection-remove">
							<span class="btn btn-default btn-xs entity-selection-remove">
								<button type="button" class="btn btn-no-style entity-selection-remove">
									<span class="glyphicon glyphicon-remove entity-selection-remove"></span>
								</button>
							</span>
						</div>`;
					entityContainer.appendChild(listEntry);
				});

				$(btn.closest('.entity-selection-modal')).modal('hide');
			});
		});

		document.querySelectorAll('.entity-select-search-form .do-search').forEach(
			searchBtn => {
				searchBtn.addEventListener('click', event => {
					console.log('searching');
					const resultContainer = searchBtn.closest('.entity-selection-modal').querySelector('.entity-select-showcase');

					const form = searchBtn.closest('.entity-select-search-form');
					const query = form.querySelector('input.query').value;
					const csrf = document.querySelector('meta[name="_csrf"]').getAttribute('content');
					const appendResults = form.querySelector('input.append-search').checked;

					const data = new FormData();
					data.append('query', query);
					data.append('_csrf', csrf);

					const req = new Request(form.dataset.action, {
						method: 'POST',
						body: data
					});

					fetch(req)
						.then(resp => {
							if (resp.status !== 200) {
								console.log(resp);
							}
							console.log(resp);
							return resp.json();
						}).then(entities => {
							if (!appendResults) {
								empty(resultContainer);
							}

							entities.forEach(entity => {
								const listEntry = document.createElement('li');
								listEntry.classList.add('list-group-item', 'row');
								listEntry.innerHTML = `
									<div class="col-md-10">
										${entity.title} <small>${entity.description}</small>
									</div>
									<div class="col-md-2">
										<input type="checkbox" value="${entity.id}" data-title="${entity.title}" data-description="${entity.description}">
									</div>`;
								resultContainer.appendChild(listEntry);
							});
					});

				});
			});

		document.addEventListener('click', (event) => {
			const element = event.target;
			if (element.classList.contains('entity-selection-remove')) {
				const entity = element.closest('.entity');
				entity.parentNode.removeChild(entity);
			}
		});

});
