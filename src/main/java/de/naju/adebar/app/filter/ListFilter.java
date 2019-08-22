package de.naju.adebar.app.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @author Rico Bergmann
 */
public class ListFilter extends CompoundFilter implements Iterable<AbstractFilter> {

	// TODO rename template to better reflect its actual intention

	public static ListFilter allMatchOn(@NonNull AbstractFilterableField field) {
		return new ListFilter(field, ListFilterType.ALL_MATCH);
	}

	public static ListFilter anyMatchesOn(@NonNull AbstractFilterableField field) {
		return new ListFilter(field, ListFilterType.ANY_MATCHES);
	}

	@NonNull
	private final ListFilterType type;

	@NonNull
	private final List<AbstractFilter> values;

	@NonNull
	private AbstractFilterableField template;

	private ListFilter(@NonNull AbstractFilterableField template, @NonNull ListFilterType type) {
		Assert.notNull(template, "Template may not be null");
		Assert.notNull(type, "Filter type may not be null");
		this.template = template;
		this.type = type;
		this.values = new ArrayList<>();
	}

	@NonNull
	public Collection<AbstractFilter> getValues() {
		return Collections.unmodifiableCollection(values);
	}

	@NonNull
	public ListFilterType getType() {
		return type;
	}

	public void addValue(@NonNull AbstractFilter value) {
		Assert.notNull(value, "Value may not be null");

		value.parentFilter = this;
		this.values.add(value);
	}

	public int getValuesCount() {
		return this.values.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	@NonNull
	public Iterator<AbstractFilter> iterator() {
		return values.iterator();
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
		return false;
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
		return template;
	}

	@Override
	public void replaceFilteredFieldBy(AbstractFilterableField newField) {
		this.template = newField;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#provideValue()
	 */
	@Override
	public boolean provideValue(Object value) {
		return false;
	}

	@Override
	protected void removeChildFilter(AbstractFilter child) {
		values.remove(child);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((template == null) ? 0 : template.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
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
		ListFilter other = (ListFilter) obj;
		if (template == null) {
			if (other.template != null) {
				return false;
			}
		} else if (!template.equals(other.template)) {
			return false;
		}
		if (values == null) {
			if (other.values != null) {
				return false;
			}
		} else if (!values.equals(other.values)) {
			return false;
		}
		return true;
	}

	public enum ListFilterType {
		ALL_MATCH, ANY_MATCHES
	}

}
