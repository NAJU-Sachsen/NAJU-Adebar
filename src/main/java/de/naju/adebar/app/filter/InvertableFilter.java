package de.naju.adebar.app.filter;

import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

public class InvertableFilter extends CompoundFilter {

	public static InvertableFilter of(AbstractFilter filter) {
		return new InvertableFilter(filter);
	}

	public static InvertableFilter createFor(AbstractFilterableField field) {
		return new InvertableFilter(EqualityFilter.createFor(field));
	}

	static InvertableFilter newImmutableFor(AbstractFilter filter) {
		InvertableFilter invertableFilter = new InvertableFilter(filter);
		invertableFilter.mutable = false;
		return invertableFilter;
	}

	private final AbstractFilter actualFilter;
	private boolean invertResults = false;
	private boolean mutable = true;

	private InvertableFilter(AbstractFilter actualFilter) {
		Assert.notNull(actualFilter, "Actual filter may not be null");
		this.actualFilter = actualFilter;

		this.actualFilter.parentFilter = this;
	}

	public AbstractFilter getActualFilter() {
		return actualFilter;
	}

	public boolean isInvertResults() {
		return invertResults;
	}

	public void setInvertResults(boolean invertResults) {
		if (!mutable) {
			return;
		}
		this.invertResults = invertResults;
	}

	public void toggleInverResults() {
		if (!mutable) {
			return;
		}
		this.invertResults = !this.invertResults;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.AbstractFilter#accept(de.naju.adebar.app.filter.FilterElementVisitor)
	 */
	@Override
	public void accept(@NonNull FilterElementVisitor visitor) {
		actualFilter.accept(visitor);
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
		return actualFilter.containsCriteriaFor(field);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#isTerminal()
	 */
	@Override
	public boolean isTerminal() {
		return actualFilter.isTerminal();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#getFilteredField()
	 */
	@Override
	@NonNull
	public AbstractFilterableField getFilteredField() {
		return actualFilter.getFilteredField();
	}

	@Override
	public void replaceFilteredFieldBy(AbstractFilterableField newField) {
		this.actualFilter.replaceFilteredFieldBy(newField);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#provideValue()
	 */
	@Override
	public boolean provideValue(Object value) {
		return actualFilter.provideValue(value);
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
		return Objects.hash(actualFilter, invertResults, mutable);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		InvertableFilter that = (InvertableFilter) o;
		return invertResults == that.invertResults && mutable == that.mutable
				&& Objects.equals(actualFilter, that.actualFilter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return invertResults ? String.format("NOT (%s)", actualFilter) : actualFilter.toString();
	}

}
