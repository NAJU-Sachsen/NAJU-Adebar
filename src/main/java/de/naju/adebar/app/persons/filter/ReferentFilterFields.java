package de.naju.adebar.app.persons.filter;

import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.EntityField;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.ListFilter;
import de.naju.adebar.app.filter.StringField;
import de.naju.adebar.model.persons.qualifications.Qualification;

public class ReferentFilterFields {

	public ListFilter getQualificationFilter() {
		return ListFilter.allMatchOn(new QualificationField());
	}

	public ContainmentFilter getQualificationDescriptionFilter() {
		return ContainmentFilter.createFor(new QualificationDescription());
	}

	public EqualityFilter generateSingleQualificationFilter(Qualification qualification) {
		EqualityFilter filter = EqualityFilter.createFor(new QualificationField());
		filter.provideValue(qualification);
		return filter;
	}

	public static class QualificationField extends EntityField {

	}

	public static class QualificationDescription extends StringField {

		@Override
		public boolean isLargeText() {
			return true;
		}
	}

}
