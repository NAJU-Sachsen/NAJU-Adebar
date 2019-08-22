package de.naju.adebar.web.model.persons.filter;

public abstract class WebFilter {

	protected final FieldEnhancementService enhancementService;

	protected WebFilter(FieldEnhancementService enhancementService) {
		this.enhancementService = enhancementService;
	}

	public abstract WebFilterForm getForm();

	protected void enhance(FilterGroup... filterGroups) {
		for (FilterGroup group : filterGroups) {
			group.enhanceAllFiltersWith(enhancementService);
		}
	}

}
