package de.naju.adebar.app.events.search;

import java.util.Optional;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.QueryInterpreter;
import de.naju.adebar.model.events.Event;

/**
 * A {@link QueryInterpreter} for {@link Event} instances that produces predicates for database
 * queries.
 *
 * @author Rico Bergmann
 */
public interface EventQueryInterpreter extends QueryInterpreter<BooleanBuilder> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.naju.adebar.app.search.QueryInterpreter#getFallback()
	 */
	@Override
	default Optional<EventQueryInterpreter> getFallback() {
		return Optional.empty();
	}

}
