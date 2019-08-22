package de.naju.adebar.app.filter;

import org.springframework.lang.NonNull;

public abstract class AbstractFilter {

	// TODO not all subclasses behave the same if removeChildFilter is supplied a filter which is not
	// a child filter

	protected AbstractFilter parentFilter;

	public abstract void accept(@NonNull FilterElementVisitor visitor);

	public abstract boolean containsCriteriaFor(@NonNull AbstractFilterableField field);

	public abstract boolean isTerminal();

	@NonNull
	public abstract AbstractFilterableField getFilteredField();

	public abstract void replaceFilteredFieldBy(AbstractFilterableField newField);

	public abstract boolean provideValue(Object value);

	public void drop() {
		if (parentFilter == null) {
			throw new IllegalStateException("Root filter may not be dropped");
		}
		parentFilter.removeChildFilter(this);
	}

	protected abstract void removeChildFilter(AbstractFilter child);

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object other);

}
