package de.naju.adebar.app.events.search;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.events.search.interpreters.DateQueryInterpreter;
import de.naju.adebar.app.events.search.interpreters.DurationQueryInterpreter;
import de.naju.adebar.app.events.search.interpreters.NameOrPlaceQueryInterpreter;
import de.naju.adebar.app.search.QueryInterpreter;
import de.naju.adebar.app.search.QueryInterpreterHierarchy;
import de.naju.adebar.util.Assert2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class EventQueryInterpreterHierarchy implements QueryInterpreterHierarchy<BooleanBuilder> {

  private static final int INTERPRETER_COUNT = 3;

  private final List<QueryInterpreter<BooleanBuilder>> interpreters;

  public EventQueryInterpreterHierarchy(NameOrPlaceQueryInterpreter nameOrPlaceQueryInterpreter,
      DateQueryInterpreter dateQueryInterpreter,
      DurationQueryInterpreter durationQueryInterpreter) {

    Assert2.noNullArguments("No argument may be null", nameOrPlaceQueryInterpreter,
        dateQueryInterpreter, durationQueryInterpreter);

    this.interpreters = new ArrayList<>(INTERPRETER_COUNT);

    /*
     * The order in which the interpreters are added will be the order in which they should be
     * applied.
     */
    interpreters.add(durationQueryInterpreter);
    interpreters.add(dateQueryInterpreter);
    interpreters.add(nameOrPlaceQueryInterpreter);

  }

  @Override
  public Optional<EventQueryInterpreter> getInterpreterFor(@NonNull String query) {
    /*
     * Just search for the first best interpreter (which is based on the order the interpreters
     * where added to the list)
     */

    for (QueryInterpreter<BooleanBuilder> interpreter : interpreters) {
      if (interpreter.mayInterpret(query)) {
        return Optional.of((EventQueryInterpreter) interpreter);
      }
    }
    return Optional.empty();
  }

  @Override
  @NonNull
  public Iterator<QueryInterpreter<BooleanBuilder>> iterator() {
    return interpreters.iterator();
  }
}