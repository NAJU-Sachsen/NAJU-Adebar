package de.naju.adebar.infrastructure.thymeleaf;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Custom formatter for time and time spans
 *
 * @author Rico Bergmann
 */
public class TimeFormatter {

  private static final String DAY_ONLY = "dd.";
  private static final String DATE_FORMAT = "dd.MM.yy";
  private static final String DATE_TIME_FORMAT = "dd.MM.yy HH:mm";

  private DateTimeFormatter dateFormatter;
  private DateTimeFormatter dateTimeFormatter;

  public TimeFormatter() {
    this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.GERMAN);
    this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.GERMAN);
  }

  /**
   * Formats a time span
   *
   * @param from start date
   * @param to end date
   * @return the formatted time span
   */
  public String formatTimeSpan(LocalDateTime from, LocalDateTime to) {
    if (shouldApplyDirectlyFollowingFormat(from, to)) {
      return formatAsDirectlyFollowing(from, to);
    }
    return format(from) + " - " + format(to);
  }

  /**
   * Formats a date-time object. Basically it will ignore hour and minutes if the time is 0:00
   *
   * @param time a date to format
   * @return the formatted date
   */
  public String format(LocalDateTime time) {
    if (shouldApplyDateOnlyFormat(time)) {
      return time.format(dateFormatter);
    }
    return time.format(dateTimeFormatter);
  }

  /**
   * Formats the dates as two directly following ones, i.e. as "d₁/d₂" rather than "d₁ - d₂"
   *
   * @param from the first date
   * @param to the second date
   * @return the formatted time period
   */
  public String formatAsDirectlyFollowing(LocalDateTime from, LocalDateTime to) {
    DateTimeFormatter dayOnlyFormatter = DateTimeFormatter.ofPattern(DAY_ONLY, Locale.GERMAN);
    return from.format(dayOnlyFormatter) + "/" + format(to);
  }

  /**
   * Checks, if some pretty-printing is possible. If a date's time is 0:00 it should be treated as a
   * pure date (as the way 'time is not important' is expressed within the model is through setting
   * the time to 0:00)
   *
   * @param time the time to check
   * @return {@code true} if the time should be formatted as 'date-only', {@code false} otherwise
   */
  public boolean shouldApplyDateOnlyFormat(LocalDateTime time) {
    return time.getHour() == 0 && time.getMinute() == 0;
  }

  /**
   * Checks, if some pretty printing is possible. If two dates follow directly on each other, they
   * should be printed as "d₁/d₂" rather than "d₁ - d₂"
   *
   * @param from the first date
   * @param to the second date
   * @return {@code true} if the dates should be formatted as directly following ones, {@code false}
   *         otherwise
   */
  public boolean shouldApplyDirectlyFollowingFormat(LocalDateTime from, LocalDateTime to) {
    if (!shouldApplyDateOnlyFormat(from) || !shouldApplyDateOnlyFormat(to)) {
      return false;
    }
    return from.isBefore(to) && from.plusDays(2).isAfter(to);
  }

}
