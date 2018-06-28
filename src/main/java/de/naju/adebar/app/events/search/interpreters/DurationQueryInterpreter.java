package de.naju.adebar.app.events.search.interpreters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.events.search.EventQueryInterpreter;
import de.naju.adebar.model.events.QEvent;

/**
 * Interpreter that handles time spans.
 * <p>
 * The recognized patterns look like the following: {@code from - to}, where {@code from} consists
 * of a day, and optionally month and year, separated by dots ({@code .}). {@code to} also consists
 * of day and month and may optionally feature a year as well. If {@code to} contains no year, the
 * current year will be assumed.
 * <p>
 * Beware that the recognized pattern is the way dates are noted in Germany.
 *
 * @author Rico Bergmann
 */
@Service
public class DurationQueryInterpreter implements EventQueryInterpreter {

  private static final String DATE_REGEX =
      "^(?<fromDay>\\d?\\d).\\s?((?<fromMonth>\\d?\\d).\\s?(?<fromYear>\\d{2,4})?)?\\s?-\\s?(?<toDay>\\d?\\d).\\s?(?<toMonth>\\d?\\d).\\s?(?<toYear>\\d{2,4})?$";
  private static final QEvent event = QEvent.event;

  private final DateQueryInterpreter fallbackInterpreter;

  public DurationQueryInterpreter(DateQueryInterpreter fallbackInterpreter) {
    Assert.notNull(fallbackInterpreter, "Fallback interpreter may not be null");
    this.fallbackInterpreter = fallbackInterpreter;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.app.search.QueryInterpreter#mayInterpret(java.lang.String)
   */
  @Override
  public boolean mayInterpret(@NonNull String query) {
    return query.matches(DATE_REGEX);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.app.search.QueryInterpreter#interpret(java.lang.String)
   */
  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    Pattern datePattern = Pattern.compile(DATE_REGEX);
    Matcher queryMatcher = datePattern.matcher(query);

    // we need to attempt to get a match before accessing matched groups
    if (!queryMatcher.matches()) {
      throw new IllegalArgumentException("Not a date query: " + query);
    }

    // fetch the required fields
    final int fromDay = Integer.parseInt(queryMatcher.group("fromDay"));
    final int toDay = Integer.parseInt(queryMatcher.group("toDay"));
    final int toMonth = Integer.parseInt(queryMatcher.group("toMonth"));

    // try to fetch the optional fields
    String fromMonthStr = queryMatcher.group("fromMonth");
    String fromYearStr = queryMatcher.group("fromYear");
    String toYearStr = queryMatcher.group("toYear");

    // generate the values for the optional fields
    final int fromMonth = fromMonthStr != null ? Integer.parseInt(fromMonthStr) : toMonth;
    int toYear = toYearStr != null ? Integer.parseInt(toYearStr) : LocalDate.now().getYear();

    // only parse the year, if to also contains a year
    int fromYear =
        (toYearStr != null && fromYearStr != null) ? Integer.parseInt(fromYearStr) : toYear;

    fromYear = adjustYearIfNecessary(fromYear);
    toYear = adjustYearIfNecessary(toYear);

    // generate the dates
    LocalDate fromDate = LocalDate.of(fromYear, fromMonth, fromDay);
    LocalDate toDate = LocalDate.of(toYear, toMonth, toDay);

    // generate the full date time instances
    LocalDateTime from = LocalDateTime.of(fromDate, LocalTime.MIDNIGHT);
    LocalDateTime to = LocalDateTime.of(toDate, LocalTime.MIDNIGHT);

    // finally generate the predicate
    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(event.startTime.after(from).or(event.startTime.eq(from)));
    predicate.and(event.endTime.before(to).or(event.endTime.eq(to)));

    return predicate;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.app.events.search.EventQueryInterpreter#getFallback()
   */
  @Override
  public Optional<EventQueryInterpreter> getFallback() {
    return Optional.of(fallbackInterpreter);
  }

  /**
   * Converts a year from short notation (i.e. {@code yy}) to the written-out version (i.e.
   * {@code yyyy}).
   *
   * @param year the year
   * @return the converted year
   */
  private int adjustYearIfNecessary(int year) {
    if (year < 1900) {
      return year + 2000;
    }
    return year;
  }

}
