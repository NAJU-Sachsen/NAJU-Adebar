package de.naju.adebar.app.filter.streams;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.Assert;
import de.naju.adebar.app.filter.AbstractFilterBuilder;

/**
 * An {@link AbstractFilterBuilder} which consumes and produces a {@link Stream} of objects
 * 
 * @author Rico Bergmann
 *
 * @param <T> the kind of ojects in the stream
 */
public class StreamBasedFilterBuilder<T>
    implements AbstractFilterBuilder<Stream<T>, AbstractStreamBasedFilter<T>> {
  protected Stream<T> inputStream;
  protected Set<AbstractStreamBasedFilter<T>> filters;

  /**
   * @param inputStream the objects to be filtered
   */
  public StreamBasedFilterBuilder(Stream<T> inputStream) {
    Assert.notNull(inputStream, "Input stream may not be null");
    this.inputStream = inputStream;
    this.filters = new HashSet<>();
  }

  @Override
  public StreamBasedFilterBuilder<T> applyFilter(AbstractStreamBasedFilter<T> filter) {
    Assert.notNull(filter, "Filter may not be null!");
    filters.add(filter);
    return this;
  }

  /**
   * Executes the filters
   * 
   * @return the objects that matched all of the criteria
   */
  @Override
  public Stream<T> filter() {
    filters.forEach(filter -> inputStream = filter.filter(inputStream));
    return inputStream;
  }

  /**
   * Executes the filters
   * 
   * @return the obejcts that matched all of the filters
   */
  public Iterable<T> filterAndCollect() {
    return filter().collect(Collectors.toList());
  }

}
