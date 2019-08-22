/**
 *
 */
package de.naju.adebar.app.filter;

/**
 * @author Rico Bergmann
 *
 */
public abstract class CompoundFilter extends AbstractFilter {

	/*
	 * public boolean containsFilterOfType(Class<? extends AbstractFilter> filterClass) { return
	 * getContainedFilter().getClass().equals(filterClass); }
	 * 
	 * public boolean deepContainsFilterOfType(Class<? extends AbstractFilter> filterClass) { if
	 * (containsFilterOfType(filterClass)) { return true; }
	 * 
	 * AbstractFilter containedFilterClass = getContainedFilter();
	 * 
	 * if (containedFilterClass instanceof CompoundFilter) { return ((CompoundFilter)
	 * containedFilterClass).deepContainsFilterOfType(filterClass); } else { return false; }
	 * 
	 * }
	 * 
	 * protected abstract AbstractFilter getContainedFilter();
	 */

}
