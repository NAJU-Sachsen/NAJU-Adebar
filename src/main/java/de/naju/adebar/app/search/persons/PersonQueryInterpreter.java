package de.naju.adebar.app.search.persons;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.QueryInterpreter;
import de.naju.adebar.model.persons.Person;
import java.util.Optional;

/**
 * A {@link QueryInterpreter} for {@link Person} instances that produces predicates for database
 * queries.
 *
 * @author Rico Bergmann
 */
public interface PersonQueryInterpreter extends QueryInterpreter<BooleanBuilder> {

  @Override
  default Optional<PersonQueryInterpreter> getFallback() {
    return Optional.empty();
  }

}
