package de.naju.adebar.app.events.search;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.QueryInterpreter;
import java.util.Optional;

public interface EventQueryInterpreter extends QueryInterpreter<BooleanBuilder> {

  @Override
  default Optional<EventQueryInterpreter> getFallback() {
    return Optional.empty();
  }

}
