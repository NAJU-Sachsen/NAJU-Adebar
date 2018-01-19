package de.naju.adebar.util;

/**
 * Utility to store a tuple of objects.
 * <p>
 * These tuples form a total order. Pairs are being sorted by the first component first and by the
 * second component afterwards. Therefore the tuple's elements need to implement the
 * {@link Comparable} interface.
 *
 * @author Rico Bergmann
 *
 * @param <A> the type of the first object
 * @param <B> the type of the second object
 */
/**
 * @author Rico Bergmann
 *
 * @param <A>
 * @param <B>
 */
/**
 * @author Rico Bergmann
 *
 * @param <A>
 * @param <B>
 */
public class Pair<A extends Comparable<? super A>, B extends Comparable<? super B>>
    implements Comparable<Pair<A, B>> {

  /**
   * The first value in the tuple
   */
  public final A first;

  /**
   * The second value in the tuple
   */
  public final B second;

  /**
   * Creates a new tuple of the given values
   *
   * @param a the first value
   * @param b the second value
   */
  public Pair(A a, B b) {
    this.first = a;
    this.second = b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Pair<A, B> other) {
    int cmpFirst = this.first.compareTo(other.first);
    if (cmpFirst != 0) {
      return cmpFirst;
    }
    return this.second.compareTo(other.second);
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
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Pair<A, B> other;
    try {
      other = (Pair<A, B>) obj;
    } catch (ClassCastException e) {
      return false;
    }
    if (first == null) {
      if (other.first != null) {
        return false;
      }
    } else if (!first.equals(other.first)) {
      return false;
    }
    if (second == null) {
      if (other.second != null) {
        return false;
      }
    } else if (!second.equals(other.second)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "(" + first + ", " + second + ")";
  }


}
