package de.naju.adebar.app.filter;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Rico Bergmann
 */
public class ComparingFilter extends AbstractFilter {

	protected AbstractFilterableField field;
	protected Object value;
	protected Comparison comparison;

	protected ComparingFilter(@NonNull AbstractFilterableField field) {
		this.field = field;
		this.value = null;
		this.comparison = Comparison.EQUAL;
	}

	@NonNull
	public static ComparingFilter createFor(@NonNull AbstractFilterableField field) {
		return new ComparingFilter(field);
	}

	@Nullable
	public Object getValue() {
		return value;
	}

	@NonNull
	public Comparison getComparison() {
		return comparison;
	}

	public void setComparison(@NonNull Comparison comparison) {
		this.comparison = comparison;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.AbstractFilter#accept(de.naju.adebar.app.filter.FilterElementVisitor)
	 */
	@Override
	public void accept(@NonNull FilterElementVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#containsCriteriaFor(de.naju.adebar.app.filter.
	 * AbstractFilterableField)
	 */
	@Override
	public boolean containsCriteriaFor(@NonNull AbstractFilterableField field) {
		return this.field.equals(field);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#isTerminal()
	 */
	@Override
	public boolean isTerminal() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#getFilteredField()
	 */
	@Override
	@NonNull
	public AbstractFilterableField getFilteredField() {
		return field;
	}

	@Override
	public void replaceFilteredFieldBy(AbstractFilterableField newField) {
		this.field = newField;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#provideValue()
	 */
	@Override
	public boolean provideValue(Object value) {
		this.value = value;
		return true;
	}

	@Override
	protected void removeChildFilter(AbstractFilter child) {
		this.drop();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comparison == null) ? 0 : comparison.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ComparingFilter other = (ComparingFilter) obj;
		if (comparison != other.comparison) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String comparisonSymbol = "";
		String valueString = value != null ? value.toString() : "NULL";
		switch (comparison) {
			case LESS_THAN:
				comparisonSymbol = "<";
				break;
			case EQUAL:
				comparisonSymbol = "=";
				break;
			case GREATER_THAN:
				comparisonSymbol = ">";
				break;
		}
		return field.toString() + " " + comparisonSymbol + " " + valueString;
	}

	public enum Comparison {
		LESS_THAN, EQUAL, GREATER_THAN
	}

}
