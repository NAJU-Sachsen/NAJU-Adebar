package de.naju.adebar.infrastructure.thymeleaf;

import java.util.StringJoiner;
import com.google.common.collect.Iterables;

/**
 * Provides useful helpers for dealing with {@link Iterable} instances
 * 
 * @author Rico Bergmann
 */
public class IterableUtility {

  private static final String DEFAULT_DELIMITER = ";";

  /**
   * Combines the elements in an {@link Iterable} into a single String, separating the values by a
   * semicolon.
   * 
   * @param it the iterable
   * @return the combined string
   */
  public String join(Iterable<?> it) {
    return join(it, DEFAULT_DELIMITER);
  }

  /**
   * Combines the elements in an {@link Iterable} into a single String by using a custom delimiter
   * 
   * @param it the iterable
   * @param delim the string used to separate the values
   * @return the combined string
   */
  public String join(Iterable<?> it, String delim) {
    StringJoiner sj = new StringJoiner(delim);
    it.forEach(elem -> sj.add(elem.toString()));
    return sj.toString();
  }

  /**
   * @param it the iterable to check
   * @return whether the iterable contains no elements
   */
  public boolean isEmpty(Iterable<?> it) {
    if (it == null) {
      return true;
    }
    return Iterables.isEmpty(it);
  }

}
