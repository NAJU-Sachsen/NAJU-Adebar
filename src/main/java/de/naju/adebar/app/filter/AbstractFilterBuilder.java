package de.naju.adebar.app.filter;

/**
 * Builder to collect all the needed filters and finally apply them. The interface follows a
 * variation of the builder pattern
 * 
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 * @param <R> the result-type the filter should produce
 * @param <F> the {@link AbstractFilter} implementation to use
 */
public interface AbstractFilterBuilder<R, F extends AbstractFilter<? extends R>> {

  /**
   * Saves a new filter for execution. However applying a filter does not preserve order, i. e.
   * adding one filter after another does <strong>not</strong> guarantee that this filter will be
   * executed after the first one.
   * 
   * @param filter the filter to apply
   * @return the builder instance for easy chaining
   */
  AbstractFilterBuilder<R, F> applyFilter(F filter);

  /**
   * Executes the filters
   * 
   * @return the objects that matched all of the criteria
   */
  R filter();
}
