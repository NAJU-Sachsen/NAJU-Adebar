package de.naju.adebar.util;

/**
 * A special triple which contains three integers.
 * <p>
 * It only provides a minimum set of operations as most other methods may be composed by them
 * easily.
 * <p>
 * E.g. to compensate for the lack of a {@code min()} method, one may simply use
 * {@code t.scale(-1).max()}
 *
 * @author Rico Bergmann
 */
public class IntTriple extends Triple<Integer, Integer, Integer> {

  /**
   * Generates a new triple
   *
   * @param first the triple's first element
   * @param second the triple's second element
   * @param third the triple's third element
   * @return the resulting triple
   */
  public static IntTriple of(Integer first, Integer second, Integer third) {
    return new IntTriple(first, second, third);
  }

  /**
   * Full constructor
   *
   * @param first the first element
   * @param second the second element
   * @param third the third element
   */
  protected IntTriple(Integer first, Integer second, Integer third) {
    super(first, second, third);
  }

  /**
   * Combines two triples by adding the corresponding elements.
   * <p>
   * That is {@code (a1, a2, a3) + (b1, b2, b3) = (a1+b1, a2+b2, a3+b3)}
   *
   * @param other the triple to add
   * @return the resulting triple
   */
  public IntTriple add(IntTriple other) {
    return new IntTriple( //
        this.first + other.first, //
        this.second + other.second, //
        this.third + other.third);
  }

  /**
   * Scales the triple by some factor.
   * <p>
   * That is {@code s * (a, b, c) = (s*a, s*b, s*c)}
   *
   * @param factor the factor
   * @return the resulting triple
   */
  public IntTriple scale(int factor) {
    return new IntTriple(factor * first, factor * second, factor * third);
  }

  /**
   * Compares the triples elements
   *
   * @return the largest element
   */
  public Component max() {
    if (first > second && first > third) {
      return Component.FIRST;
    } else if (second > third) {
      return Component.SECOND;
    } else {
      return Component.THIRD;
    }
  }

}
