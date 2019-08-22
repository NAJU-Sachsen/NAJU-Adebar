package de.naju.adebar.model.core;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.util.Assert2;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * In distinction to {@link Period} this will store the actual dates, not just the time between
 * them.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class TimeSpan implements Serializable, Comparable<TimeSpan> {

	private static final long serialVersionUID = -6646165910793311461L;

	@Column(name = "from")
	private LocalDate from;

	@Column(name = "to")
	private LocalDate to;

	public static TimeSpanBuilder from(LocalDate date) {
		return new TimeSpanBuilder(date);
	}

	public static TimeSpan between(LocalDate start, LocalDate end) {
		return new TimeSpan(start, end);
	}

	private TimeSpan(LocalDate from, LocalDate to) {
		Assert2.noNullArguments("No argument may be null", from, to);
		Assert.isTrue(!to.isBefore(from), "The end date may not be before the start date");
		this.from = from;
		this.to = to;
	}

	@JpaOnly
	private TimeSpan() {}

	public LocalDate getFrom() {
		return from;
	}

	public LocalDate getTo() {
		return to;
	}

	public Period toPeriod() {
		return Period.between(from, to.plusDays(1L));
	}

	@JpaOnly
	private void setFrom(LocalDate from) {
		this.from = from;
	}

	@JpaOnly
	private void setTo(LocalDate to) {
		this.to = to;
	}

	@Override
	public int compareTo(TimeSpan other) {
		int cmpFrom = this.from.compareTo(other.from);
		if (cmpFrom != 0) {
			return cmpFrom;
		}

		return this.to.compareTo(other.to);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TimeSpan other = (TimeSpan) obj;
		if (from == null) {
			if (other.from != null) {
				return false;
			}
		} else if (!from.equals(other.from)) {
			return false;
		}
		if (to == null) {
			if (other.to != null) {
				return false;
			}
		} else if (!to.equals(other.to)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return from.toString() + " - " + to.toString();
	}

	public static class TimeSpanBuilder {

		private final LocalDate from;

		public TimeSpanBuilder(LocalDate from) {
			this.from = from;
		}

		public TimeSpan to(LocalDate date) {
			return new TimeSpan(from, date);
		}

	}

}
