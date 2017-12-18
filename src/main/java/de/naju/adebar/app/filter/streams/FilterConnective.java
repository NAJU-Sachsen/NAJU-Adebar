package de.naju.adebar.app.filter.streams;

import java.util.stream.Stream;
import de.naju.adebar.util.Streams;

/**
 * Simple class to combine multiple {@link AbstractStreamBasedFilter} logically.
 *
 * @param <E> the type of entities to be filtered
 * @param <F> the type of filter to use, has to be a filter for the Entity
 * @author Rico Bergmann
 */
public class FilterConnective<E, F extends AbstractStreamBasedFilter<E>>
    implements AbstractStreamBasedFilter<E> {
  private AbstractConnective<E> connective;
  private AbstractStreamBasedFilter<E> firstFilter;
  private AbstractStreamBasedFilter<E> secondFilter;

  /**
   * Creates a plain connective
   *
   * @param filter the first filter to use
   * @param <E> the type of entities to be filtered
   * @param <F> the type of filter to use, has to be a filter for the Entity
   * @return a new connective to be linked with other filters
   */
  public static <E, F extends AbstractStreamBasedFilter<E>> FilterConnective<E, F> forFilter(
      AbstractStreamBasedFilter<E> filter) {
    return new FilterConnective<>(filter);
  }

  /**
   * Creates a plain connective
   *
   * @param filter the first filter to use
   */
  public FilterConnective(AbstractStreamBasedFilter<E> filter) {
    this.firstFilter = filter;
  }

  /**
   * Creates an `AND` connective between the two filters. More precisely if the result of the first
   * filter is R₁ and the result of the second filter is R₂, then {@code F₁ ∧ F₂} will contain all
   * elements {@code e}, where {@code e∈R₁ ∧ e∈R₂}.
   *
   * @param filter the second filter to use
   * @return the resulting connective
   * @throws IllegalStateException if multiple connectives where specified (i.e.
   *         {@link #and(AbstractStreamBasedFilter)} or {@link #or(AbstractStreamBasedFilter)} where
   *         called before)
   */
  public FilterConnective<E, F> and(AbstractStreamBasedFilter<E> filter) {
    assertConnectiveIsUnspecified();
    this.connective = new AndConnective();
    this.secondFilter = filter;
    return this;
  }

  /**
   * Creates an `OR` connective between the two filters. More precisely if the result of the first
   * filter F₁ is R₁ and the result of the second filter F₂ is R₂, then {@code F₁ ∨ F₂} will contain
   * all elements {@code e}, where {@code e∈R₁ ∨ e∈R₂}.
   *
   * @param filter the second filter to use
   * @return the resulting connective
   * @throws IllegalStateException if multiple connectives where specified (i.e.
   *         {@link #and(AbstractStreamBasedFilter)} or {@link #or(AbstractStreamBasedFilter)} where
   *         called before)
   */
  public FilterConnective<E, F> or(AbstractStreamBasedFilter<E> filter) {
    assertConnectiveIsUnspecified();
    this.connective = new OrConnective();
    this.secondFilter = filter;
    return this;
  }

  @Override
  public Stream<E> filter(Stream<E> input) {
    return connective.apply(input);
  }

  /**
   * @throws IllegalStateException if the connective is already specified (i.e. not {@code null})
   */
  private void assertConnectiveIsUnspecified() {
    if (connective != null) {
      throw new IllegalStateException("A logical connective has already been specified!");
    }
  }

  /**
   * Base class for the logical connectives
   *
   * @author Rico Bergmann
   */
  private interface AbstractConnective<E> {

    /**
     * Executes the connective on the given input
     *
     * @param input the input to execute the filters on
     * @return all elements which matched the specification of the logical connective
     */
    public Stream<E> apply(Stream<E> input);
  }

  /**
   * Implementation of the `AND` connective
   *
   * @author Rico Bergmann
   * @see FilterConnective#and(AbstractStreamBasedFilter)
   */
  private class AndConnective implements AbstractConnective<E> {

    @Override
    public Stream<E> apply(Stream<E> input) {
      input = firstFilter.filter(input);
      return secondFilter.filter(input);
    }

  }

  /**
   * Implementation of the `OR` connective
   *
   * @author Rico Bergmann
   * @see FilterConnective#or(AbstractStreamBasedFilter)
   */
  private class OrConnective implements AbstractConnective<E> {

    @Override
    public Stream<E> apply(Stream<E> input) {
      Stream<E> firstFiltered = firstFilter.filter(input);
      Stream<E> secondFiltered = secondFilter.filter(input);
      return Streams.union(firstFiltered, secondFiltered);
    }

  }

}
