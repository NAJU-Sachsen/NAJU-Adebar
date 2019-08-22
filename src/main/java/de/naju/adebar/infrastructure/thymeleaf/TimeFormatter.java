package de.naju.adebar.infrastructure.thymeleaf;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import de.naju.adebar.model.core.TimeSpan;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;

/**
 * Custom formatter for time and time spans
 *
 * @author Rico Bergmann
 */
public class TimeFormatter {

	private static final String HTML_DATE_FORMAT = "yyyy-MM-dd";
	private static final String DAY_MONTH = "dd.MM.";
	private static final String DAY_ONLY = "dd.";
	private static final String TIME_ONLY = "HH:mm";
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
		if (isSingleDay(from, to)) {
			return formatAsSingleDay(from, to);
		} else if (shouldApplyDirectlyFollowingFormat(from, to)) {
			return formatAsDirectlyFollowing(from, to);
		} else if (shouldFormatWithoutYearRepetition(from, to)) {
			return formatWithoutYearRepetition(from, to);
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

	public String formatTimeSpan(TimeSpan timeSpan) {
		return formatTimeSpan( //
				LocalDateTime.of(timeSpan.getFrom(), LocalTime.MIDNIGHT), //
				LocalDateTime.of(timeSpan.getTo(), LocalTime.MIDNIGHT));
	}

	public String formatTimeSpan(LocalDateTime offset, ParticipationTime participationTime) {
		return formatTimeSpan(offset.plusDays(participationTime.getFirstNight() - 1),
				offset.plusDays(participationTime.getLastNight()));
	}

	public String formatNightAsHtml(LocalDateTime offset, int night) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HTML_DATE_FORMAT, Locale.GERMAN);
		return formatter.format(offset.plusDays(night - 1));
	}

	/**
	 * Formats the dates as two directly following ones, i.e. as "d₁/d₂" rather than "d₁ - d₂"
	 *
	 * @param from the first date
	 * @param to the second date
	 * @return the formatted time period
	 */
	private String formatAsDirectlyFollowing(LocalDateTime from, LocalDateTime to) {
		DateTimeFormatter dayOnlyFormatter = DateTimeFormatter.ofPattern(DAY_ONLY, Locale.GERMAN);
		return from.format(dayOnlyFormatter) + "/" + format(to);
	}

	private String formatAsSingleDay(LocalDateTime from, LocalDateTime to) {
		if (from.getHour() == to.getHour() && from.getMinute() == to.getMinute()) {
			return dateFormatter.format(from);
		} else {
			DateTimeFormatter timeOnly = DateTimeFormatter.ofPattern(TIME_ONLY, Locale.GERMAN);
			return dateFormatter.format(from) + " " + timeOnly.format(from) + " - " + timeOnly.format(to);
		}
	}

	private String formatWithoutYearRepetition(LocalDateTime from, LocalDateTime to) {
		DateTimeFormatter dayMonthFormatter = DateTimeFormatter.ofPattern(DAY_MONTH, Locale.GERMAN);
		return from.format(dayMonthFormatter) + " - " + format(to);
	}

	private boolean isSingleDay(LocalDateTime from, LocalDateTime to) {
		return from.getYear() == to.getYear() && from.getMonth() == to.getMonth()
				&& from.getDayOfMonth() == to.getDayOfMonth();
	}

	private boolean shouldFormatWithoutYearRepetition(LocalDateTime from, LocalDateTime to) {
		return from.getYear() == to.getYear() && hasOnlyDateSet(from) && hasOnlyDateSet(to);
	}

	private boolean hasOnlyDateSet(LocalDateTime dateTime) {
		return dateTime.getHour() == 0 && dateTime.getMinute() == 0;
	}

	/**
	 * Checks, if some pretty-printing is possible. If a date's time is 0:00 it should be treated as a
	 * pure date (as the way 'time is not important' is expressed within the model is through setting
	 * the time to 0:00)
	 *
	 * @param time the time to check
	 * @return {@code true} if the time should be formatted as 'date-only', {@code false} otherwise
	 */
	private boolean shouldApplyDateOnlyFormat(LocalDateTime time) {
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
	private boolean shouldApplyDirectlyFollowingFormat(LocalDateTime from, LocalDateTime to) {
		if (!shouldApplyDateOnlyFormat(from) || !shouldApplyDateOnlyFormat(to)) {
			return false;
		}
		return from.isBefore(to) && from.plusDays(2).isAfter(to);
	}

}
