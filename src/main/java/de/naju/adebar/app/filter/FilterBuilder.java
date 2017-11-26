package de.naju.adebar.app.filter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.Assert;

/**
 * Builder to collect all the needed filters and finally apply them. The class follows a variation
 * of the builder pattern
 * 
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 * @param <T> the type of objects to be filtered
 */
public class FilterBuilder<T> {
  protected Stream<T> inputStream;
  protected Set<AbstractFilter<T>> filters;

  /**
   * @param inputStream the objects to be filtered
   */
  public FilterBuilder(Stream<T> inputStream) {
    Assert.notNull(inputStream, "Input stream may not be null");
    this.inputStream = inputStream;
    this.filters = new HashSet<>();
  }

  /**
   * Saves a new filter for execution. However applying a filter does not preserve order, i. e.
   * adding one filter after another does <strong>not</strong> guarantee that this filter will be
   * executed after the first one.
   * 
   * @param filter the filter to apply to the given persons
   * @return the builder instance for easy chaining
   */
  public FilterBuilder<T> applyFilter(AbstractFilter<T> filter) {
    Assert.notNull(filter, "Filter may not be null!");
    filters.add(filter);
    return this;
  }

  /**
   * Executes the filters
   * 
   * @return the objects that matched all of the criteria
   */
  public Iterable<T> filter() {
    return resultingStream().collect(Collectors.toList());
  }

  /**
   * Executes the filters but returns the result uncollected. The return type is the only difference
   * to {@link #filter()}
   * 
   * @return the objects that matched all of the criteria
   */
  public Stream<T> resultingStream() {
    filters.forEach(filter -> inputStream = filter.filter(inputStream));
    return inputStream;
  }

}
