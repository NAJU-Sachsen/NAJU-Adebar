package de.naju.adebar.web.services.persons.filter;

import de.naju.adebar.app.filter.AbstractFilterableField.FieldType;
import de.naju.adebar.app.filter.BooleanField;
import de.naju.adebar.app.filter.ComparingFilter.Comparison;
import de.naju.adebar.app.filter.DateField;
import de.naju.adebar.app.filter.EmailField;
import de.naju.adebar.app.filter.EntityField;
import de.naju.adebar.app.filter.EnumeratedField;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.FloatField;
import de.naju.adebar.app.filter.IntField;
import de.naju.adebar.app.filter.StringField;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.util.Functional;
import de.naju.adebar.web.model.persons.filter.WebEntityField;
import de.naju.adebar.web.model.persons.filter.WebEnumeratedField;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.AbstractFilterableField;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.FilterElementVisitor;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.JoiningFilter;
import de.naju.adebar.app.filter.ListFilter;
import de.naju.adebar.web.model.persons.filter.WebFilter;
import de.naju.adebar.web.model.persons.filter.WebFilterForm;

@Service
public class FilterInitializer {

	private static final int FILTER_PER_GROUP_ESTIMATION = 4;

	public AbstractFilter initializeFrom(WebFilter filterToInit, MultiValueMap<String, String> data) {

		final WebFilterForm form = filterToInit.getForm();

		final List<AbstractFilter> allFilters =
				new ArrayList<>(form.getFilterGroups().size() * FILTER_PER_GROUP_ESTIMATION);
		form.getFilterGroups().forEach(filterGroup -> allFilters.addAll(filterGroup.getContent()));

		final FilterValueVisitor valueVisitor = new FilterValueVisitor(data);

		AbstractFilter combinedFilter = allFilters.stream()
				.reduce((firstFilter, secondFilter) -> JoiningFilter.of(firstFilter).and(secondFilter))
				.orElseThrow(IllegalStateException::new);
		combinedFilter.accept(valueVisitor);

		return combinedFilter;
	}

	private static class FilterValueVisitor implements FilterElementVisitor {

		private static final String FILTER_INACTIVE = "NOOP";
		private static final String COMPARISON_MIN = "min";
		private static final String COMPARISON_MAX = "max";
		private static final String INVERSION_ACTIVE = "active";
		private static final String INVERSION_INACTIVE = "inactive";

		private final MultiValueMap<String, String> data;

		private FilterValueVisitor(MultiValueMap<String, String> data) {
			this.data = data;
		}

		public void visit(EqualityFilter filter) {
			deEnhanceIfNecessary(filter);
			initTerminalFilter(filter);
		}

		@Override
		public void visit(ComparingFilter filter) {
			deEnhanceIfNecessary(filter);
			if (filter instanceof EqualityFilter) {
				initTerminalFilter(filter);
				return;
			}

			final String cmpKey = comparatorKey(filter);
			if (data.containsKey(cmpKey)) {
				final String comparisonStrategy = data.getFirst(cmpKey);

				if (comparisonStrategy == null  || comparisonStrategy.equals(FILTER_INACTIVE)) {
					filter.drop();
				} else {
					switch (comparisonStrategy) {
						case COMPARISON_MIN:
							filter.setComparison(Comparison.GREATER_THAN);
							break;
						case COMPARISON_MAX:
							filter.setComparison(Comparison.LESS_THAN);
							break;
						default:
							filter.drop();
					}
					initTerminalFilter(filter);
				}
			} else {
				filter.drop();
			}
		}

		@Override
		public void visit(ContainmentFilter filter) {
			deEnhanceIfNecessary(filter);
			initTerminalFilter(filter);
		}

		@Override
		public void visit(InvertableFilter filter) {
			deEnhanceIfNecessary(filter);
			final String invKey = invertedKey(filter);
			if (data.containsKey(invKey)) {
				final String inversionStatus = data.getFirst(invKey);

				if (inversionStatus == null || inversionStatus.equals(FILTER_INACTIVE)) {
					filter.drop();
				} else {
					switch (inversionStatus) {
						case INVERSION_ACTIVE:
							filter.setInvertResults(true);
							break;
						case INVERSION_INACTIVE:
							filter.setInvertResults(false);
							break;
						default:
							filter.drop();
					}
				}
			} else {
				filter.drop();
			}
		}

		@Override
		public void visit(JoiningFilter filter) {
			// pass
		}

		@Override
		public void visit(ListFilter filter) {
			deEnhanceIfNecessary(filter);

			if (filter.getValues().isEmpty()) {
				String entityKey = filterKey(filter);
				List<String> entities = data.get(entityKey);

				if (entities == null || entities.isEmpty()) {
					filter.drop();
				} else {
					entities.forEach(entity -> {
						final EqualityFilter entityFilter = EqualityFilter.createFor(filter.getFilteredField());
						entityFilter.provideValue(entity);
						filter.addValue(entityFilter);
					});
				}

			}
		}

		@Override
		public void visit(AbstractFilter filter) {
			// pass
		}

		private String filterKey(AbstractFilter filter) {
			final AbstractFilterableField field = filter.getFilteredField();
			return field.getDefaultName() + "-value";
		}

		private String comparatorKey(ComparingFilter filter) {
			final AbstractFilterableField field = filter.getFilteredField();
			return field.getDefaultName() + "-comp";
		}

		private String invertedKey(InvertableFilter filter) {
			final AbstractFilterableField field = filter.getFilteredField();
			return field.getDefaultName() + "-status";
		}

		private Object castData(String value, AbstractFilterableField field) {
			// we need to use lambdas everywhere to ensure parsers are only invoked if the value is
			// actually valid
			return Functional.<Object>match(field) //
					.caseOf(BooleanField.class, __ -> Boolean.valueOf(value)) //
					.caseOf(DateField.class, rawDate ->
						LocalDate.parse(value,
								DateTimeFormatter.ofPattern("yyyy-MM-dd",  LocaleContextHolder.getLocale()))
					) //
					.caseOf(EmailField.class, __ -> Email.of(value)) //
					.caseOf(EntityField.class, value) //
					.caseOf(EnumeratedField.class, enumField -> enumField.getValueFor(value)) //
					.caseOf(FloatField.class, __ -> Double.parseDouble(value)) //
					.caseOf(IntField.class, __ -> Integer.parseInt(value))
					.caseOf(StringField.class, value) //
					.runOrThrow();
		}

		private void initTerminalFilter(AbstractFilter filter) {
			final String fieldKey = filterKey(filter);

			if (data.containsKey(fieldKey)) {
				String rawValue = data.getFirst(fieldKey);
				if (rawValue == null || rawValue.isEmpty()) {
					filter.drop();
				} else {
					if (filter.getFilteredField().getType().equals(FieldType.BOOL) && rawValue.equals(FILTER_INACTIVE)) {
						filter.drop();
					} else {
						Object fieldValue = castData(rawValue, filter.getFilteredField());
						filter.provideValue(fieldValue);
					}
				}
			} else {
				filter.drop();
			}
		}

		private void deEnhanceIfNecessary(AbstractFilter filter) {
			final AbstractFilterableField updatedField = Functional.<AbstractFilterableField>match(filter.getFilteredField()) //
					.caseOf(WebEntityField.class, WebEntityField::getActualField) //
					.caseOf(WebEnumeratedField.class, WebEnumeratedField::getActualField) //
					.defaultCase(filter.getFilteredField()) //
					.runOrThrow();
			filter.replaceFilteredFieldBy(updatedField);
		}

	}

}
