package de.naju.adebar.app.filter;

public class NoCommonFieldException extends RuntimeException {

	private static final long serialVersionUID = -416136155049838061L;

	private final AbstractFilter firstFilter;
	private final AbstractFilter secondFilter;

	/**
	 * @param firstFilter
	 * @param secondFilter
	 */
	public NoCommonFieldException(AbstractFilter firstFilter, AbstractFilter secondFilter) {
		this.firstFilter = firstFilter;
		this.secondFilter = secondFilter;
	}

	/**
	 * @param firstFilter
	 * @param secondFilter
	 */
	public NoCommonFieldException(AbstractFilter firstFilter, AbstractFilter secondFilter,
			String message) {
		super(message);
		this.firstFilter = firstFilter;
		this.secondFilter = secondFilter;
	}

	public AbstractFilter getFirstFilter() {
		return firstFilter;
	}

	public AbstractFilter getSecondFilter() {
		return secondFilter;
	}

}
