package de.naju.adebar.app.filter;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ContainmentFilter extends AbstractFilter {

	@NonNull
	public static ContainmentFilter createFor(@NonNull AbstractFilterableField field) {
		return new ContainmentFilter(field);
	}

	protected AbstractFilterableField field;
	protected Object value;

	private ContainmentFilter(@NonNull AbstractFilterableField field) {
		this.field = field;
		this.value = null;
	}

	@NonNull
	public AbstractFilterableField getField() {
		return field;
	}

	@Nullable
	public Object getValue() {
		return value;
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
		// pass
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
		result = prime * result + field.hashCode();
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
		ContainmentFilter other = (ContainmentFilter) obj;
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
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
		if (value == null) {
			return "?? IN " + field.toString();
		} else {
			return value.toString() + " IN " + field.toString();
		}
	}

}
