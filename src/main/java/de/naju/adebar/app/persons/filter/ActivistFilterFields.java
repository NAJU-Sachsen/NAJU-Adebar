package de.naju.adebar.app.persons.filter;

import com.google.common.collect.Lists;
import de.naju.adebar.app.filter.BooleanField;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.DateField;
import de.naju.adebar.app.filter.EnumeratedField;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.model.persons.details.JuleicaCard;

public class ActivistFilterFields {

	public static class Juleica extends BooleanField {
	}

	public static class JuleicaLevel extends EnumeratedField {

		/*
		 * (non-Javadoc)
		 *
		 * @see de.naju.adebar.app.filter.EnumeratedField#getValues()
		 */
		@Override
		public Iterable<?> getValues() {
			return Lists.newArrayList( //
					JuleicaCard.BASIC_JULEICA_LEVEL, //
					JuleicaCard.EXTENDED_JULEICA_LEVEL);
		}
	}

	public static class JuleicaExpiry extends DateField {
	}

	public ActivistFilterFields() {
		// pass
	}

	public EqualityFilter getJuleicaFilter() {
		return EqualityFilter.createFor(new Juleica());
	}

	public InvertableFilter getJuleicaLevelFilter() {
		return InvertableFilter.createFor(new JuleicaLevel());
	}

	public ComparingFilter getJuleicaExpiryDateFilter() {
		return ComparingFilter.createFor(new JuleicaExpiry());
	}

}
