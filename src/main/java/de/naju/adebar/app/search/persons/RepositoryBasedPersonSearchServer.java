package de.naju.adebar.app.search.persons;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.ReadOnlyPersonRepository;
import de.naju.adebar.util.Assert2;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link PersonSearchServer} using the {@link ReadOnlyPersonRepository} to
 * retrieve the {@link Person} instances. It will only return non-archived instances.
 *
 * @author Rico Bergmann
 */
@Service
public class RepositoryBasedPersonSearchServer implements PersonSearchServer {

  private static final QPerson person = QPerson.person;
  private static final Logger log = LoggerFactory
      .getLogger(RepositoryBasedPersonSearchServer.class);

  private final ReadOnlyPersonRepository personRepo;
  private final PersonQueryInterpreterHierarchy interpreterHierarchy;

  /**
   * Full constructor
   *
   * @param personRepo repository containing all available person instances
   * @param interpreterHierarchy the interpreters to use
   */
  public RepositoryBasedPersonSearchServer(ReadOnlyPersonRepository personRepo,
      PersonQueryInterpreterHierarchy interpreterHierarchy) {
    Assert2.noNullArguments("No argument may be null", personRepo, interpreterHierarchy);
    this.personRepo = personRepo;
    this.interpreterHierarchy = interpreterHierarchy;
  }

  @Override
  @NonNull
  public List<Person> runQuery(@NonNull String query, @NonNull Person context) {
    return doRunQuery(query, context);
  }

  @Override
  @NonNull
  public List<Person> runQuery(@NonNull String query) {
    return doRunQuery(query, null);
  }

  @Override
  @NonNull
  public Page<Person> runQuery(@NonNull String query, @NonNull Pageable pageable) {
    return doRunQuery(query, null, pageable);
  }

  @Override
  @NonNull
  public Page<Person> runQuery(@NonNull String query, @NonNull Person context,
      @NonNull Pageable pageable) {
    return doRunQuery(query, context, pageable);
  }

  /**
   * Searches for all persons matching the given query.
   *
   * @param query the query to use
   * @param context the person to exclude from the results. May be {@code null} if all persons
   *     should be included.
   * @return all matching persons
   */
  @NonNull
  private List<Person> doRunQuery(@NonNull String query, @Nullable Person context) {
    Optional<PersonQueryInterpreter> interpreter = interpreterHierarchy.getInterpreterFor(query);

    return interpreter //
        .map(i -> fetchResultsFor(query, context, i)) //
        .orElse(Collections.emptyList());
  }

  /**
   * Searches for all persons matching the given query.
   *
   * @param query the query to use
   * @param context the person to exclude from the results. May be {@code null} if all persons
   *     should be included.
   * @param pageable pagination information for the resulting {@link Page}
   * @return all matching persons
   */
  @NonNull
  private Page<Person> doRunQuery(@NonNull String query, @Nullable Person context,
      @NonNull Pageable pageable) {
    Optional<PersonQueryInterpreter> interpreter = interpreterHierarchy.getInterpreterFor(query);

    return interpreter //
        .map(i -> fetchResultsFor(query, context, i, pageable)) //
        .orElse(Page.empty(pageable));
  }

  /**
   * Loads all results for the given query, using a certain interpreter and its fallbacks.
   *
   * @param query the query to use
   * @param context the person to exclude from the results. May be {@code null} if all persons *
   *     should be included.
   * @param interpreter the interpreter to use
   * @return all matching persons
   */
  @NonNull
  private List<Person> fetchResultsFor(@NonNull String query, @Nullable Person context,
      @NonNull PersonQueryInterpreter interpreter) {
    List<Person> result = Collections.emptyList();

    while (result.isEmpty() && interpreter != null) {
      Predicate predicate = prepareQuery(interpreter.interpret(query), context);
      result = personRepo.findAll(predicate);
      log.debug("Interpreter {} returned {} instances for query '{}'",
          interpreter.getClass().getSimpleName(),
          result.size(), query);

      if (result.isEmpty()) {
        interpreter = interpreter.getFallback().orElse(null);
      }

    }

    return result;
  }

  /**
   * Loads all results for the given query, using a certain interpreter and its fallbacks.
   *
   * @param query the query to use
   * @param context the person to exclude from the results. May be {@code null} if all persons *
   *     should be included.
   * @param interpreter the interpreter to use
   * @param pageable pagination information for the resulting {@link Page}
   * @return all matching persons
   */
  @NonNull
  private Page<Person> fetchResultsFor(@NonNull String query, @Nullable Person context,
      @NonNull PersonQueryInterpreter interpreter, @NonNull Pageable pageable) {
    Page<Person> result = Page.empty(pageable);

    while (!result.hasContent() && interpreter != null) {
      Predicate predicate = prepareQuery(interpreter.interpret(query), context);
      result = personRepo.findAll(predicate, pageable);
      log.debug("Interpreter {} returned {} instances for query '{}'",
          interpreter.getClass().getSimpleName(),
          result.getTotalElements(), query);

      if (!result.hasContent()) {
        interpreter = interpreter.getFallback().orElse(null);
      }

    }

    return result;
  }

  /**
   * Adds additional qualifiers to a predicate: if the person is not {@code null}, matching persons
   * must be different from the given one. Furthermore matching persons may not be archived.
   *
   * @param predicate the predicate to extend
   * @param context the person to exclude from the results. May be {@code null} if all persons *
   *     should be included.
   * @return the completed predicate
   */
  @NonNull
  private BooleanBuilder prepareQuery(@NonNull BooleanBuilder predicate, @Nullable Person context) {
    predicate.and(person.archived.isFalse());
    if (context != null) {
      predicate.andNot(person.id.eq(context.getId()));
    }
    return predicate;
  }

}
