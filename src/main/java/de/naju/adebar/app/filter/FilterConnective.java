package de.naju.adebar.app.filter;

import java.util.stream.Stream;
import de.naju.adebar.util.Streams;

/**
 * Simple class to combine multiple {@link AbstractStreamBasedFilter} logically.
 * 
 * @param <Entity> the type of entities to be filtered
 * @param <Filter> the type of filter to use, has to be a filter for the Entity
 * @author Rico Bergmann
 */
public class FilterConnective<Entity, Filter extends AbstractStreamBasedFilter<Entity>>
    implements AbstractStreamBasedFilter<Entity> {
  private AbstractConnective connective;
  private AbstractStreamBasedFilter<Entity> firstFilter;
  private AbstractStreamBasedFilter<Entity> secondFilter;

  /**
   * Creates a plain connective
   * 
   * @param filter the first filter to use
   * @return a new connective to be linked with other filters
   */
  public static <Entity, Filter extends AbstractStreamBasedFilter<Entity>> FilterConnective<Entity, Filter> forFilter(
      AbstractStreamBasedFilter<Entity> filter) {
    return new FilterConnective<>(filter);
  }

  /**
   * Creates a plain connective
   * 
   * @param filter the first filter to use
   */
  public FilterConnective(AbstractStreamBasedFilter<Entity> filter) {
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
   *         {@link #and(AbstractStreamBasedFilter)} or {@link #or(AbstractStreamBasedFilter)} where called before)
   */
  public FilterConnective<Entity, Filter> and(AbstractStreamBasedFilter<Entity> filter) {
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
   *         {@link #and(AbstractStreamBasedFilter)} or {@link #or(AbstractStreamBasedFilter)} where called before)
   */
  public FilterConnective<Entity, Filter> or(AbstractStreamBasedFilter<Entity> filter) {
    assertConnectiveIsUnspecified();
    this.connective = new OrConnective();
    this.secondFilter = filter;
    return this;
  }

  @Override
  public Stream<Entity> filter(Stream<Entity> input) {
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
  private abstract class AbstractConnective {

    /**
     * Executes the connective on the given input
     *
     * @param input the input to execute the filters on
     * @return all elements which matched the specification of the logical connective
     */
    public abstract Stream<Entity> apply(Stream<Entity> input);
  }

  /**
   * Implementation of the `AND` connective
   *
   * @author Rico Bergmann
   * @see FilterConnective#and(AbstractStreamBasedFilter)
   */
  private class AndConnective extends AbstractConnective {

    @Override
    public Stream<Entity> apply(Stream<Entity> input) {
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
  private class OrConnective extends AbstractConnective {

    @Override
    public Stream<Entity> apply(Stream<Entity> input) {
      Stream<Entity> firstFiltered = firstFilter.filter(input);
      Stream<Entity> secondFiltered = secondFilter.filter(input);
      return Streams.union(firstFiltered, secondFiltered);
    }

  }

}
