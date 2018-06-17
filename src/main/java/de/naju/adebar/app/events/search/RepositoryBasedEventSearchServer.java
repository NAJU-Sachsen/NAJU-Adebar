package de.naju.adebar.app.events.search;

import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import de.naju.adebar.util.Assert2;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class RepositoryBasedEventSearchServer implements EventSearchServer {

  private static final Logger log = LoggerFactory.getLogger(RepositoryBasedEventSearchServer.class);

  private final ReadOnlyEventRepository eventRepo;
  private final EventQueryInterpreterHierarchy interpreterHierarchy;

  public RepositoryBasedEventSearchServer(ReadOnlyEventRepository eventRepo,
      EventQueryInterpreterHierarchy interpreterHierarchy) {
    Assert2.noNullArguments("No argument may be null", eventRepo, interpreterHierarchy);

    this.eventRepo = eventRepo;
    this.interpreterHierarchy = interpreterHierarchy;
  }

  @Override
  @NonNull
  public List<Event> runQuery(@NonNull String query) {
    Optional<EventQueryInterpreter> interpreter = interpreterHierarchy.getInterpreterFor(query);
    return interpreter //
        .map(i -> fetchResultsFor(query, i)) //
        .orElse(Collections.emptyList());
  }

  @Override
  @NonNull
  public Page<Event> runQuery(@NonNull String query, @NonNull Pageable pageable) {
    Optional<EventQueryInterpreter> interpreter = interpreterHierarchy.getInterpreterFor(query);
    return interpreter //
        .map(i -> fetchResultsFor(query, i, pageable)) //
        .orElse(Page.empty(pageable));
  }

  @NonNull
  private List<Event> fetchResultsFor(@NonNull String query,
      @NonNull EventQueryInterpreter interpreter) {
    List<Event> result = Collections.emptyList();

    while (result.isEmpty() && interpreter != null) {
      Predicate predicate = interpreter.interpret(query);
      result = eventRepo.findAll(predicate);

      if (result.isEmpty()) {
        interpreter = interpreter.getFallback().orElse(null);
      }

    }

    return result;
  }

  private Page<Event> fetchResultsFor(@NonNull String query,
      @NonNull EventQueryInterpreter interpreter, @NonNull Pageable pageable) {
    Page<Event> result = Page.empty(pageable);

    while (!result.hasContent() && interpreter != null) {
      Predicate predicate = interpreter.interpret(query);
      result = eventRepo.findAll(predicate, pageable);

      log.debug("Interpreter {} returned {} instances for query '{}'",
          interpreter.getClass().getSimpleName(),
          result.getTotalElements(), query);

      if (!result.hasContent()) {
        interpreter = interpreter.getFallback().orElse(null);
      }

    }

    return result;
  }

}
