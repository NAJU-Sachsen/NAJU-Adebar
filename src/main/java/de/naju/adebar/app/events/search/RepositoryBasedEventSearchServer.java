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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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

	/**
	 * Loads all results for the given query, using a certain interpreter and its fallbacks.
	 *
	 * @param query the query to use
	 * @param interpreter the interpreter to use
	 * @return all matching persons
	 */
	@NonNull
	private List<Event> fetchResultsFor(@NonNull String query,
			@NonNull EventQueryInterpreter interpreter) {
		List<Event> result = Collections.emptyList();

		while (result.isEmpty() && interpreter != null) {
			Predicate predicate = interpreter.interpret(query);
			result = eventRepo.findAll(predicate);

			// mind the order of the parameters and their order in the comparison
			result.sort((e1, e2) -> e2.getStartTime().compareTo(e1.getStartTime()));

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
	 * @param interpreter the interpreter to use
	 * @param pageable pagination information for the resulting {@link Page}
	 * @return all matching events
	 */
	private Page<Event> fetchResultsFor(@NonNull String query,
			@NonNull EventQueryInterpreter interpreter, @NonNull Pageable pageable) {
		Page<Event> result = Page.empty(pageable);

		while (!result.hasContent() && interpreter != null) {
			Predicate predicate = interpreter.interpret(query);

			PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
					Direction.DESC, "startTime");
			result = eventRepo.findAll(predicate, pageRequest);

			log.trace("Interpreter {} returned {} instances for query '{}'",
					interpreter.getClass().getSimpleName(), result.getTotalElements(), query);

			if (!result.hasContent()) {
				interpreter = interpreter.getFallback().orElse(null);
			}

		}

		return result;
	}

}
