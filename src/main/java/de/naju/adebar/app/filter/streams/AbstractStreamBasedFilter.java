package de.naju.adebar.app.filter.streams;

import java.util.stream.Stream;
import de.naju.adebar.app.filter.AbstractFilter;

/**
 * Interface for filters. We only need them to filter
 * 
 * @author Rico Bergmann
 */
public interface AbstractStreamBasedFilter<T> extends AbstractFilter<Stream<T>> {

  /**
   * That's why we call it "filter". It receives elements, filters them and gives elements again.
   * 
   * @param input the stream to filter
   * @return the filtered stream
   */
  @Override
  Stream<T> filter(Stream<T> input);

}
