package de.naju.adebar.app.events.search.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.events.search.EventQueryInterpreter;
import de.naju.adebar.model.events.QEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class DurationQueryInterpreter implements EventQueryInterpreter {

  private static final String DATE_REGEX = "^(?<fromDay>\\d?\\d).\\s?((?<fromMonth>\\d?\\d).\\s?(?<fromYear>\\d{2,4})?)?\\s?-\\s?(?<toDay>\\d?\\d).\\s?(?<toMonth>\\d?\\d).\\s?(?<toYear>\\d{2,4})?$";
  private static final QEvent event = QEvent.event;

  @Override
  public boolean mayInterpret(@NonNull String query) {
    return query.matches(DATE_REGEX);
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    return null;
  }
}
