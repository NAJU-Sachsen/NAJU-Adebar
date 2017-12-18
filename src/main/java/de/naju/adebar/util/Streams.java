package de.naju.adebar.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility functions on streams. Mostly of mathematical nature. Beware that all of these functions
 * (unless stated otherwise) will waste the streams given!
 *
 * @author Rico Bergmann
 * @see Stream
 */
public class Streams {

  private Streams() {}

  /**
   * Calculates a stream c, such that {@code c := a ∩ b}
   *
   * @param a the first stream
   * @param b the second stream
   * @return c
   */
  public static <T> Stream<T> intersect(Stream<T> a, Stream<T> b) {
    List<T> listA = a.collect(Collectors.toList());
    List<T> listB = b.collect(Collectors.toList());
    List<T> result = new LinkedList<>();
    listA.forEach(elem -> {
      if (listB.contains(elem)) {
        result.add(elem);
      }
    });
    return result.stream();
  }

  /**
   * Calculates a stream c, such that {@code c := a ∪ b}
   *
   * @param a the first stream
   * @param b the second stream
   * @return c
   */
  public static <T> Stream<T> union(Stream<T> a, Stream<T> b) {
    Set<T> setA = a.collect(Collectors.toSet());
    Set<T> setB = b.collect(Collectors.toSet());
    setA.addAll(setB);
    return setA.stream();
  }

  /**
   * Calculates a stream c, such that {@code c := a \ b}
   *
   * @param a the first stream
   * @param b the second stream
   * @return c
   */
  public static <T> Stream<T> subtract(Stream<T> a, Stream<T> b) {
    List<T> listA = a.collect(Collectors.toList());
    List<T> listB = b.collect(Collectors.toList());
    listA.removeAll(listB);
    return listA.stream();
  }

  /**
   * Checks, if a stream contains a certain element. This will waste the stream!
   *
   * @param s the stream to check
   * @param e the element to check for
   * @return {@code true ⇔  elem ∈ stream}
   */
  public static <T> boolean contains(Stream<T> s, T e) {
    return s.collect(Collectors.toList()).contains(e);
  }

}
