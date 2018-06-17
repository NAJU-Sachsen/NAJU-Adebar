package de.naju.adebar.util;

import java.util.Objects;

/**
 * A 3-tuple, i.e. an ordered sequence of exactly three elements of arbitrary types.
 *
 * @author Rico Bergmann
 *
 * @param <F> the type of the tuple's first component
 * @param <S> the type of the tuple's second component
 * @param <T> the type of the tuple's third component
 */
public class Triple<F, S, T> {

  /**
   * The different elements of the triple
   *
   * @author Rico Bergmann
   */
  public enum Component {
    FIRST, SECOND, THIRD;
  }

  protected final F first;
  protected final S second;
  protected final T third;

  /**
   * Generates a new triple
   *
   * @param first the triple's first element
   * @param second the triple's second element
   * @param third the triple's third element
   * @return the triple
   */
  public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
    return new Triple<F, S, T>(first, second, third);
  }

  /**
   * Full constructor
   *
   * @param first the first element
   * @param second the second element
   * @param third the third element
   */
  protected Triple(F first, S second, T third) {
    this.first = first;
    this.second = second;
    this.third = third;
  }

  /**
   * @return the triple's first element
   */
  public final F first() {
    return first;
  }

  /**
   * @return the triple's second element
   */
  public final S second() {
    return second;
  }

  /**
   * @return the triple's third element
   */
  public final T third() {
    return third;
  }

  /**
   * Provides direct access to a specific element of the triple
   *
   * @param component the element to return
   * @return the element's value
   */
  public final Object get(Component component) {
    switch (component) {
      case FIRST:
        return first;
      case SECOND:
        return second;
      case THIRD:
        return third;
      default:
        throw new AssertionError(component);
    }
  }

  /**
   * Provides direct access to a specific element of the triple.
   * <p>
   * <strong>Note that the first element is indexed by {@code 1}!</strong> (Because mathematics
   * tells us we are in fact operating on an 3-tuple)
   *
   * @param idx the index
   * @return the element's value
   */
  public final Object get(int idx) {
    switch (idx) {
      case 1:
        return first;
      case 2:
        return second;
      case 3:
        return third;
      default:
        throw new IllegalArgumentException("A triple has no element with index " + idx);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((first == null) ? 0 : first.hashCode());
    result = prime * result + ((second == null) ? 0 : second.hashCode());
    result = prime * result + ((third == null) ? 0 : third.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  @SuppressWarnings("rawtypes")
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Triple other = (Triple) obj;
    if (first == null) {
      if (other.first != null)
        return false;
    } else if (!first.equals(other.first))
      return false;
    if (second == null) {
      if (other.second != null)
        return false;
    } else if (!second.equals(other.second))
      return false;
    if (third == null) {
      if (other.third != null)
        return false;
    } else if (!third.equals(other.third))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("(%s, %s, %s)", //
        Objects.toString(first), Objects.toString(second), Objects.toString(third));
  }

}
