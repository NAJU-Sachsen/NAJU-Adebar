package de.naju.adebar.app.events.search.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.events.search.EventQueryInterpreter;
import de.naju.adebar.model.events.QEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class NameOrPlaceQueryInterpreter implements EventQueryInterpreter {

  private static final QEvent event = QEvent.event;

  @Override
  public boolean mayInterpret(@NonNull String query) {
    // we may interpret everything
    return true;
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    assertMayInterpret(query);

    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and( //
        event.name.containsIgnoreCase(query)
            .or(event.place.city.containsIgnoreCase(query)));
    return predicate;
  }
}
