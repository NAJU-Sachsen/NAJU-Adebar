package de.naju.adebar.app.filter;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class JoiningFilter extends CompoundFilter {

	public enum JoinType {
		AND, OR
	}

	public static class JoinBuilder {

		private final AbstractFilter firstFilter;

		private JoinBuilder(@NonNull AbstractFilter firstFilter) {
			this.firstFilter = firstFilter;
		}

		public JoiningFilter and(@NonNull AbstractFilter secondFilter) {
			return new JoiningFilter(firstFilter, secondFilter, JoinType.AND);
		}

		public JoiningFilter or(@NonNull AbstractFilter secondFilter) {
			return new JoiningFilter(firstFilter, secondFilter, JoinType.OR);
		}

	}

	@Nullable
	private AbstractFilter firstFilter;

	@Nullable
	private AbstractFilter secondFilter;

	@NonNull
	private final JoinType joinType;

	private JoiningFilter(@NonNull AbstractFilter firstFilter, @NonNull AbstractFilter secondFilter,
			@NonNull JoinType joinType) {
		this.firstFilter = firstFilter;
		this.secondFilter = secondFilter;
		this.joinType = joinType;

		this.firstFilter.parentFilter = this;
		this.secondFilter.parentFilter = this;
	}

	public static JoinBuilder of(@Nonnull AbstractFilter firstFilter) {
		return new JoinBuilder(firstFilter);
	}

	public Optional<AbstractFilter> getFirstFilter() {
		return Optional.ofNullable(firstFilter);
	}

	public Optional<AbstractFilter> getSecondFilter() {
		return Optional.ofNullable(secondFilter);
	}

	@NonNull
	public JoinType getJoinType() {
		return joinType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.AbstractFilter#accept(de.naju.adebar.app.filter.FilterElementVisitor)
	 */
	@Override
	public void accept(@NonNull FilterElementVisitor visitor) {
		if (firstFilter != null) {
			firstFilter.accept(visitor);
		}
		if (secondFilter != null) {
			secondFilter.accept(visitor);
		}
		if (firstFilter != null && secondFilter != null) {
			visitor.visit(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#containsCriteriaFor(de.naju.adebar.app.filter.
	 * AbstractFilterableField)
	 */
	@Override
	public boolean containsCriteriaFor(@NonNull AbstractFilterableField field) {
		return firstFilter.containsCriteriaFor(field) || secondFilter.containsCriteriaFor(field);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#isTerminal()
	 */
	@Override
	public boolean isTerminal() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#getFilteredField()
	 */
	@Override
	@NonNull
	public AbstractFilterableField getFilteredField() {
		AbstractFilterableField firstField = firstFilter.getFilteredField();
		if (firstField.equals(secondFilter.getFilteredField())) {
			return firstField;
		} else {
			throw new NoCommonFieldException(firstFilter, secondFilter,
					"JoiningFilter operates upon different fields");
		}
	}

	@Override
	public void replaceFilteredFieldBy(AbstractFilterableField newField) {
		if (!firstFilter.getFilteredField().equals(secondFilter.getFilteredField())) {
			throw new NoCommonFieldException(firstFilter, secondFilter, "May only replace common fields");
		}
		firstFilter.replaceFilteredFieldBy(newField);
		secondFilter.replaceFilteredFieldBy(newField);
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
		if (child.equals(firstFilter)) {
			firstFilter = null;
		} else if (child.equals(secondFilter)) {
			secondFilter = null;
		} else {
			// throw new IllegalArgumentException("Not a child filter " + child);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.app.filter.AbstractFilter#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(firstFilter, secondFilter, joinType);
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
		JoiningFilter that = (JoiningFilter) o;
		return Objects.equals(firstFilter, that.firstFilter)
				&& Objects.equals(secondFilter, that.secondFilter) && joinType == that.joinType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (firstFilter == null && secondFilter == null) {
			return "EMPTY";
		} else if (firstFilter == null) {
			return secondFilter.toString();
		} else if (secondFilter == null) {
			return firstFilter.toString();
		}

		return String.format("(%s)", firstFilter.toString()) //
				+ (joinType == JoinType.AND ? " AND " : " OR ") //
				+ String.format("(%s)", secondFilter.toString());
	}

}
