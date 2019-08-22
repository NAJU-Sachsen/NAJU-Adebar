package de.naju.adebar.app.persons.filter;

import de.naju.adebar.app.filter.BooleanField;
import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.EmailField;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.StringField;

public class PersonFilterFields {

	public ContainmentFilter getFirstNameFilter() {
		return ContainmentFilter.createFor(new FirstName());
	}

	public ContainmentFilter getLastNameFilter() {
		return ContainmentFilter.createFor(new LastName());
	}

	public EqualityFilter getEmailFilter() {
		return EqualityFilter.createFor(new Email());
	}

	public EqualityFilter getPhoneNumberFilter() {
		return EqualityFilter.createFor(new PhoneNumber());
	}

	public InvertableFilter getParticipantFilter() {
		return InvertableFilter.createFor(new Participant());
	}

	public InvertableFilter getActivistFilter() {
		return InvertableFilter.createFor(new Activist());
	}

	public InvertableFilter getReferentFilter() {
		return InvertableFilter.createFor(new Referent());
	}

	public static class FirstName extends StringField {

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

	public static class LastName extends StringField {

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

	public static class Email extends EmailField {

	}

	public static class PhoneNumber extends StringField {

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

	public static class Participant extends BooleanField {

	}

	public static class Activist extends BooleanField {

	}

	public static class Referent extends BooleanField {

	}

}
