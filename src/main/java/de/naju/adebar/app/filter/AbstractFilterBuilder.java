package de.naju.adebar.app.filter;

public interface AbstractFilterBuilder<Result, Filter extends AbstractFilter<? extends Result>> {

  AbstractFilterBuilder<Result, Filter> applyFilter(Filter filter);

  Result filter();
}
