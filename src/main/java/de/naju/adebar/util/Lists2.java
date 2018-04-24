package de.naju.adebar.util;

import java.util.List;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;

/**
 * Provides additional utility operations on {@link List}s
 *
 * @author Rico Bergmann
 * @see Lists
 */
public class Lists2 {

  /**
   * Should not be instantiated
   */
  private Lists2() {}

  /**
   * Adds a new element to a list, but leaves the original list unchanged
   *
   * @param list the list to add the element to
   * @param elem the element to add
   * @return the resulting list
   */
  @Nonnull
  public static <T> List<T> addToCopy(@Nonnull List<T> list, T elem) {
    List<T> newList = Lists.newArrayList(list);
    newList.add(elem);
    return newList;
  }

  /**
   * Removes an element from a list but leaves the original list unchanged.
   * <p>
   * If the element may not be removed, nothing will happen.
   *
   * @param list the list to remove the element from
   * @param elem the element
   * @return the resulting list
   */
  @Nonnull
  public static <T> List<T> removeOnCopy(@Nonnull List<T> list, T elem) {
    List<T> newList = Lists.newArrayList(list);
    newList.remove(elem);
    return newList;
  }

}
