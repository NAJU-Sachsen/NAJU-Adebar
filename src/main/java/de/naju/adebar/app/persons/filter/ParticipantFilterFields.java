package de.naju.adebar.app.persons.filter;

import com.google.common.collect.Lists;
import de.naju.adebar.app.filter.BooleanField;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.DateField;
import de.naju.adebar.app.filter.EntityField;
import de.naju.adebar.app.filter.EnumeratedField.EnumField;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.ListFilter;
import de.naju.adebar.app.filter.StringField;

public class ParticipantFilterFields {

	public InvertableFilter getGenderFilter() {
		return InvertableFilter.createFor(new Gender());
	}

	public ComparingFilter getDateOfBirthFilter() {
		return ComparingFilter.createFor(new DateOfBirth());
	}

	public ContainmentFilter getEatingHabitsFilter() {
		return ContainmentFilter.createFor(new EatingHabits());
	}

	public ContainmentFilter getHealthImpairmentsFilter() {
		return ContainmentFilter.createFor(new HealthImpairments());
	}

	public EqualityFilter getNabuMembershipFilter() {
		return EqualityFilter.createFor(new NabuMembership());
	}

	public EqualityFilter getNabuMembershipNumberFilter() {
		return EqualityFilter.createFor(new NabuMembershipNumber());
	}

	public ContainmentFilter getRemarksFilter() {
		return ContainmentFilter.createFor(new Remarks());
	}

	public ListFilter getEventsFilter() {
		return ListFilter.anyMatchesOn(new Event());
	}

	public static class Gender extends EnumField<de.naju.adebar.model.persons.details.Gender> {

		private Gender() {
			super(de.naju.adebar.model.persons.details.Gender.class);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.EnumeratedField#getValues()
		 */
		@Override
		public Iterable<?> getValues() {
			return Lists.newArrayList(de.naju.adebar.model.persons.details.Gender.values());
		}

	}

	public static class DateOfBirth extends DateField {

	}

	public static class EatingHabits extends StringField {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.StringField#isLargeText()
		 */
		@Override
		public boolean isLargeText() {
			return true;
		}

	}

	public static class HealthImpairments extends StringField {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.StringField#isLargeText()
		 */
		@Override
		public boolean isLargeText() {
			return true;
		}

	}

	public static class NabuMembership extends BooleanField {

	}

	public static class NabuMembershipNumber extends StringField {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.StringField#isLargeText()
		 */
		@Override
		public boolean isLargeText() {
			return false;
		}

	}

	public static class Remarks extends StringField {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.StringField#isLargeText()
		 */
		@Override
		public boolean isLargeText() {
			return true;
		}

	}

	public static class Event extends EntityField {

	}

}
