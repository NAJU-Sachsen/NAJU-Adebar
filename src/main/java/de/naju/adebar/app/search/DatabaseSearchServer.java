package de.naju.adebar.app.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

/**
 * A {@link SearchServer} which runs on a database and therefore may provide database-specific
 * results.
 *
 * @author Rico Bergmann
 */
public interface DatabaseSearchServer<E> extends SearchServer<E> {

  /**
   * Executes the given query.
   * <p>
   * As the query will be run on a database, the result may be wrapped in a {@link Page}. This
   * enables retrieving the results in slices.
   *
   * @param query the query
   * @param pageable pagination information for the result
   * @return all entities that matched the query
   */
  @NonNull
  default Page<E> runQuery(@NonNull String query, Pageable pageable) {
    return new PageImpl<>(runQuery(query));
  }

}
