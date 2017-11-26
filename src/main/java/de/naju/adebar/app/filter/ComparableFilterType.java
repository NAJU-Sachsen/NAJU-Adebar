package de.naju.adebar.app.filter;

/**
 * Enum describing how to filter objects that possess a total ordering.
 *
 * <p>
 * This enumeration can be interpreted in a mathematical sense pretty well: Instances of the generic
 * type parameter have to form a total order. The different enumeration values define derived
 * modifications of this base ordering, e. g. an inversion of the order. The
 * {@link #matching(Comparable, Comparable)} method checks, if two elements conform to the relation
 * imposed by the enumeration value (i. e. are elements of the set formed by the relation).
 * </p>
 * 
 * @author Rico Bergmann
 * @see Comparable
 */
public enum ComparableFilterType {

  /**
   * Variation of the basic {@code ≤} order: {@code a < b}
   *
   * <p>
   * This may be derived from {@code ≤} like so: {@code (a < b ⇔ (a ≤ b) ⋀ (a ≠ b))}
   * </p>
   */
  LESS_THAN {
    /**
     * @param offset the objects to use as an "offset", i. e. the second object will be compared to
     *        the "offset"
     * @param toCheck the object to compare
     * @param <T> the type the parameters are of. They need to implement the {@link Comparable}
     *        interface
     * @return {@code true} ⇔ toCheck < offset
     */
    @Override
    public <T extends Comparable<T>> boolean matching(T offset, T toCheck) {
      return toCheck.compareTo(offset) < 0;
    }
  },

  /**
   * Variation of the basic {@code ≤} order: {@code a = b}
   *
   * <p>
   * This may be derived from {@code ≤} like so: {@code (a = b ⇔ (a ≤ b) ⋀ (b ≤ a))}
   * </p>
   */
  EQUAL {
    /**
     * @param offset the objects to use as an "offset", i. e. the second object will be compared to
     *        the "offset"
     * @param toCheck the object to compare
     * @param <T> the type the parameters are of. They need to implement the {@link Comparable}
     *        interface
     * @return {@code true} ⇔ toCheck = offset
     */
    @Override
    public <T extends Comparable<T>> boolean matching(T offset, T toCheck) {
      return toCheck.compareTo(offset) == 0;
    }
  },

  /**
   * Variation of the basic {@code ≤} order: {@code a > b}
   *
   * <p>
   * This may be derived from {@code ≤} like so: {@code (a > b ⇔ ¬(a ≤ b))}
   * </p>
   */
  GREATER_THAN {
    /**
     * @param offset the objects to use as an "offset", i. e. the second object will be compared to
     *        the "offset"
     * @param toCheck the object to compare
     * @param <T> the type the parameters are of. They need to implement the {@link Comparable}
     *        interface
     * @return {@code true} ⇔ toCheck > offset
     */
    @Override
    public <T extends Comparable<T>> boolean matching(T offset, T toCheck) {
      return toCheck.compareTo(offset) > 0;
    }
  },

  /**
   * The basic {@code ≤} order
   */
  MAXIMUM {
    /**
     * @param offset the objects to use as an "offset", i. e. the second object will be compared to
     *        the "offset"
     * @param toCheck the object to compare
     * @param <T> the type the parameters are of. They need to implement the {@link Comparable}
     *        interface
     * @return {@code true} ⇔ toCheck ≤ offset
     */
    @Override
    public <T extends Comparable<T>> boolean matching(T offset, T toCheck) {
      return toCheck.compareTo(offset) <= 0;
    }
  },

  /**
   * Variation of the basic {@code ≤} order: {@code a ≥ b}
   * 
   * <p>
   * This may be derived from {@code ≤} like so: {@code (a ≥ b ⇔ ¬(a ≤ b) ⋁ (a = b))}
   * </p>
   */
  MINIMUM {
    /**
     * @param offset the objects to use as an "offset", i. e. the second object will be compared to
     *        the "offset"
     * @param toCheck the object to compare
     * @param <T> the type the parameters are of. They need to implement the {@link Comparable}
     *        interface
     * @return {@code true} ⇔ toCheck ≥ offset
     */
    @Override
    public <T extends Comparable<T>> boolean matching(T offset, T toCheck) {
      return toCheck.compareTo(offset) >= 0;
    }
  };

  /**
   * Compares two objects
   * 
   * @param offset the objects to use as an "offset", one may think of this object as the "fixed
   *        part" of the comparison, <strong>everything else will be compared to this
   *        object</strong>
   * @param toCheck the object to compare, i. e. the <strong>this object will be compared to the
   *        "offset"</strong>
   * @param <T> the type the parameters are of. They need to implement the {@link Comparable}
   *        interface
   * @return {@code true} if the object toCheck applies to the demanded relation
   */
  public abstract <T extends Comparable<T>> boolean matching(T offset, T toCheck);
}
