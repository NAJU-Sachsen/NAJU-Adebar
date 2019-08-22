package de.naju.adebar.app.filter;

import org.springframework.lang.NonNull;

public class EqualityFilter extends ComparingFilter {

	@NonNull
	public static EqualityFilter createFor(@NonNull AbstractFilterableField field) {
		return new EqualityFilter(field);
	}

	private EqualityFilter(@NonNull AbstractFilterableField field) {
		super(field);
		this.comparison = Comparison.EQUAL;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.ComparingFilter#setComparison(de.naju.adebar.app.filter.
	 * ComparingFilter.Comparison)
	 */
	@Override
	public void setComparison(@NonNull Comparison comparison) {
		throw new UnsupportedOperationException(
				"An EqualityFilter may only use equality as comparison operation");
	}

}
