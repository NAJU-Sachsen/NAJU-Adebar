package de.naju.adebar.web.model.persons.filter;

import de.naju.adebar.app.filter.EntityField;
import org.springframework.stereotype.Service;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.AbstractFilterableField;
import de.naju.adebar.app.filter.BooleanField;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.DateField;
import de.naju.adebar.app.filter.EmailField;
import de.naju.adebar.app.filter.EnumeratedField;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.StringField;
import de.naju.adebar.util.Functional;

@Service("person_filter_rendering_service")
public class FilterRenderingService {

	public String fetch(AbstractFilter filter) {

		/*
		 * when extending this match mind the underlying inheritance hierarchies!!
		 */

		return Functional.<String>match(filter) //
				.caseOf(EqualityFilter.class, fragment("plainFilter")) //
				.caseOf(ComparingFilter.class, fragment("comparingFilter"))
				.caseOf(InvertableFilter.class, fragment("invertableFilter")) //

				.defaultCase(fragment("plainFilter")) //
				.run();
	}

	public String fetch(AbstractFilterableField field) {
		return Functional.<String>match(field) //
				.caseOf(StringField.class, field("text")) //
				.caseOf(BooleanField.class, field("bool")) //
				.caseOf(DateField.class, field("date")) //
				.caseOf(EmailField.class, field("email")) //
				.caseOf(EnumeratedField.class, field("enum")) //
				.caseOf(EntityField.class, field("entity")) //

				// .defaultCase(field("text")) //
				.run();
	}

	private String fragment(String fragmentID) {
		return "filter :: " + fragmentID + " (${filter})";
	}

	private String field(String fieldId) {
		return "filter :: " + fieldId + " (${field})";
	}

}
