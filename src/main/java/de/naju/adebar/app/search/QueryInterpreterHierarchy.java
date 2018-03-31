package de.naju.adebar.app.search;

import java.util.Optional;
import org.springframework.lang.NonNull;

/**
 * The interpretation hierarchy defines, which interpreters to check first when a new query is
 * received.
 * <p>
 * This service is necessary, as some interpreters may be more general (and their result less
 * accurate) then others. If those were applied first, the precise interpreters may never get a
 * chance.
 * <p>
 * By using this service the best filter will be used for each query.
 *
 * @param <R>
 * @author Rico Bergmann
 */
public interface QueryInterpreterHierarchy<R> extends Iterable<QueryInterpreter<R>> {

  /**
   * Directly retrieves the first appropriate interpreter for a query.
   *
   * @param query the query
   * @return the interpreter if it exists, otherwise the Optional will be empty
   */
  Optional<? extends QueryInterpreter<R>> getInterpreterFor(@NonNull String query);

}
