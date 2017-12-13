package de.naju.adebar.app.filter;

import java.util.stream.Stream;
import com.querydsl.core.types.Predicate;

/**
 * Interface for filters. We only need them it to perform the filter
 * 
 * @author Rico Bergmann
 * 
 * @param <T> the kind of objects the filter consumes (e.g. a {@link Stream} or {@link Predicate})
 */
public interface AbstractFilter<T> {

  /**
   * That's why we call it "filter". It receives elements, filters them and gives elements again.
   * 
   * @param input the stream to filter
   * @return the filtered stream
   */
  T filter(T input);

}
