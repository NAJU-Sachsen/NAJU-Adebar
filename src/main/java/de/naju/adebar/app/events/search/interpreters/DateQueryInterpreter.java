package de.naju.adebar.app.events.search.interpreters;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import de.naju.adebar.app.events.search.EventQueryInterpreter;
import de.naju.adebar.model.events.QEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class DateQueryInterpreter implements EventQueryInterpreter {

  private static final String DATE_REGEX = "^(?<day>\\d?\\d).\\s?(?<month>\\d?\\d).\\s?(?<year>\\d{2,4})?$";
  private static final QEvent event = QEvent.event;

  @Override
  public boolean mayInterpret(@NonNull String query) {
    return query.matches(DATE_REGEX);
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    Pattern datePattern = Pattern.compile(DATE_REGEX);
    Matcher queryMatcher = datePattern.matcher(query);

    // we need to attempt to get a match before accessing matched groups
    if (!queryMatcher.matches()) {
      throw new IllegalArgumentException("Not a date query: " + query);
    }

    final int dayOfMonth = Integer.parseInt(queryMatcher.group("day"));
    final int month = Integer.parseInt(queryMatcher.group("month"));

    // do not parse the year immediately, as the group is optional
    String yearString = queryMatcher.group("year");

    BooleanBuilder predicate = new BooleanBuilder();

    BooleanExpression startTimeExpr = event.startTime.dayOfMonth().eq(dayOfMonth)
        .and(event.startTime.month().eq(month));
    BooleanExpression endTimeExpr = event.endTime.dayOfMonth().eq(dayOfMonth)
        .and(event.endTime.month().eq(month));

    if (yearString != null) {
      int year = Integer.parseInt(yearString);
      year = adjustYearIfNecessary(year);

      startTimeExpr = startTimeExpr.and(event.startTime.year().eq(year));
      endTimeExpr = endTimeExpr.and(event.endTime.year().eq(year));
    }

    predicate.and(startTimeExpr.or(endTimeExpr));
    return predicate;
  }

  private int adjustYearIfNecessary(int year) {
    if (year > 1900) {
      return year;
    }
    return year + 2000;
  }

}
