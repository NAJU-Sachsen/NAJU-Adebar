package de.naju.adebar.web.model.persons.filter;

import de.naju.adebar.app.filter.EqualityFilter;
import org.springframework.stereotype.Service;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.AbstractFilterableField;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.FilterElementVisitor;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.JoiningFilter;
import de.naju.adebar.app.filter.ListFilter;
import de.naju.adebar.util.Assert2;

@Service
public class FieldEnhancementService implements FilterElementVisitor {

	private final WebFilterFieldFactory fieldFactory;
	private final FilterLabellingService labellingService;

	public FieldEnhancementService(WebFilterFieldFactory fieldFactory,
			FilterLabellingService labellingService) {
		Assert2.noNullArguments("No parameter may be null", fieldFactory, labellingService);
		this.fieldFactory = fieldFactory;
		this.labellingService = labellingService;
	}

	@Override
	public void visit(ComparingFilter filter) {
		performEnhancementIfNecessary(filter);
	}

	@Override
	public void visit(ContainmentFilter filter) {
		performEnhancementIfNecessary(filter);
	}

	@Override
	public void visit(InvertableFilter filter) {
		// pass
	}

	@Override
	public void visit(JoiningFilter filter) {
		// pass
	}

	@Override
	public void visit(ListFilter filter) {
		performEnhancementIfNecessary(filter);
	}

	@Override
	public void visit(AbstractFilter filter) {
		// pass
	}

	private void performEnhancementIfNecessary(AbstractFilter filter) {
		final AbstractFilterableField resultingField =
				fieldFactory.enhanceIfNecessary(filter.getFilteredField());
		resultingField.supplyName(labellingService.title(filter.getFilteredField()));
		filter.replaceFilteredFieldBy(resultingField);
	}

}
