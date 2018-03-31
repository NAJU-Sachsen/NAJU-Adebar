package de.naju.adebar.app.search;

import java.util.List;
import org.springframework.lang.NonNull;

/**
 * A {@code SearchServer} is a service which searches for entities of a certain class, given some
 * query.
 * <p>
 * In contrast to filtering, a query is still subject to interpretation, i.e. after the query has
 * been received, it must be understood and the search must act according to the query it received.
 * This also means that according to the meaning of a query, the actual search may look completely
 * different from a search performed for another query.
 * <p>
 * A search may be understood as some kind of dispatch on a set of possible filters, based on the
 * interpretation of a query.
 * <p>
 * This interface does not make any assumptions about the infrastructure the server will run on - it
 * may be a {@link DatabaseSearchServer database} (which probably is the most common one), but some
 * cache or the like is possible as well.
 *
 * @param <E> the type of entities to search for
 * @author Rico Bergmann
 * @see DatabaseSearchServer
 */
public interface SearchServer<E> {

  /**
   * Executes the given query
   *
   * @param query the query
   * @return all entities which matched the query
   */
  @NonNull
  List<E> runQuery(@NonNull String query);

}
