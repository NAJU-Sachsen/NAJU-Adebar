package de.naju.adebar.app.search;

import java.util.Optional;
import org.springframework.lang.NonNull;

/**
 * Service to interpret a query. An interpreter is only applicable for certain kinds of queries,
 * e.g. queries with a special keyword.
 *
 * @param <R> the kind of result the interpreter produces
 * @author Rico Bergmann
 */
public interface QueryInterpreter<R> {

  /**
   * Checks whether the interpreter is applicable for a query.
   *
   * @param query the query to check
   * @return whether the interpreter may produce a result for the query
   */
  boolean mayInterpret(@NonNull String query);

  /**
   * Analyzes the query and produces an appropriate result.
   *
   * @param query the query to interpret
   * @return the interpretation result
   * @throws IllegalArgumentException if the interpreter is not applicable for the query
   */
  R interpret(@NonNull String query);

  /**
   * Provides another interpreter to use if this one did not produce any meaningful result.
   * <p>
   * It is guaranteed that the fallback interpreter will be applicable for the query, if this
   * interpreter was.
   * <p>
   * However the fallback interpreter does not have to provide a meaningful result as well. In that
   * case it may in turn specify a fallback, leading to a chain of interpreters.
   *
   * @return a fallback interpreter to use if this interpreter did not produce the expected result
   */
  default Optional<? extends QueryInterpreter<R>> getFallback() {
    return Optional.empty();
  }

  /**
   * Asserts that the interpreter may run on the given query
   *
   * @param query the query to check
   * @throws IllegalArgumentException if the interpreter is not applicable
   */
  default void assertMayInterpret(@NonNull String query) {
    if (!mayInterpret(query)) {
      throw new IllegalArgumentException(
          String.format("%s may not interpret query '%s'", this.getClass().getSimpleName(), query));
    }
  }

}
