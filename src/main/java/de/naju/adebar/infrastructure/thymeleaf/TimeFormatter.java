package de.naju.adebar.infrastructure.thymeleaf;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Custom formatter for time and time spans
 * @author Rico Bergmann
 */
public class TimeFormatter {
	private final static String DATE_FORMAT = "dd.MM.yy";
	private final static String DATE_TIME_FORMAT = "dd.MM.yy HH:mm";

	private DateTimeFormatter dateFormatter;
	private DateTimeFormatter dateTimeFormatter;

	public TimeFormatter() {
		this.dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.GERMAN);
		this.dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.GERMAN);
	}

	/**
	 * Formats a time span
	 * @param from start date
	 * @param to end date
	 * @return the formatted time span
	 */
	public String formatTimeSpan(LocalDateTime from, LocalDateTime to) {
		return format(from) + "-" + format(to);
	}

	/**
	 * Formats a date-time object. Basically it will ignore hour and minutes if the time is 0:00
	 * @param time a date to format
	 * @return the formatted date
	 */
	public String format(LocalDateTime time) {
		if (time.getHour() == 0 && time.getMinute() == 0) {
			return time.format(dateFormatter);
		}
		return time.format(dateTimeFormatter);
	}

}
