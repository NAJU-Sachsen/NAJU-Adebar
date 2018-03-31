package de.naju.adebar.app.search.persons;

import de.naju.adebar.app.search.DatabaseSearchServer;
import de.naju.adebar.app.search.SearchServer;
import de.naju.adebar.model.persons.Person;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

/**
 * A {@link SearchServer} for {@link Person Persons}.
 *
 * @author Rico Bergmann
 */
public interface PersonSearchServer extends DatabaseSearchServer<Person> {

  /**
   * Runs a query for a certain person which will not be included in the results
   *
   * @param query the query to run
   * @param context the person for which the query is run
   * @return all persons which match the query
   */
  @NonNull
  List<Person> runQuery(@NonNull String query, @NonNull Person context);

  /**
   * Runs a query for a certain person which will not be included in the results. This makes use of
   * database specific functionality offered by the {@link DatabaseSearchServer}.
   *
   * @param query the query to run
   * @param context the person for which the query is run
   * @param pageable pagination information for the result
   * @return all persons which match the query
   */
  @NonNull
  Page<Person> runQuery(@NonNull String query, @NonNull Person context, Pageable pageable);

}
