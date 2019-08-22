package de.naju.adebar.model.core;

import java.beans.Transient;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

/**
 * A simple wrapper for position values, i.e. positive indexes.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class Position implements Comparable<Position>, Serializable {

	private static final long serialVersionUID = 3003111513108622385L;

	/**
	 * Generates a new instance at the given {@code position}.
	 */
	public static Position at(@Min(1) int value) {
		return new Position(value);
	}

	/**
	 * Generates a new instance at the given {@code position}.
	 */
	public static Position of(@Min(1) int value) {
		return new Position(value);
	}

	@Column(name = "position")
	@Min(1)
	private int value;

	/**
	 * Full constructor.
	 *
	 * @param value the position
	 */
	private Position(@Min(1) int value) {
		Assert.isTrue(value > 0, "Positions start at 1");
		this.value = value;
	}

	/**
	 * Default constructor just for JPA's sake.
	 */
	@JpaOnly
	private Position() {
		// pass
	}

	/**
	 * Provides {@code this} position value.
	 */
	@Min(1)
	public int getValue() {
		return value;
	}

	/**
	 * Provides {@code this} position value.
	 */
	@Transient
	@Min(1)
	public int get() {
		return value;
	}

	/**
	 * Checks, whether {@code this} index value is less than the given position.
	 */
	public boolean isBefore(Position other) {
		return this.compareTo(other) < 0;
	}

	/**
	 * Checks, whether {@code this} index value is greater than the given position.
	 */
	public boolean isAfter(Position other) {
		return this.compareTo(other) > 0;
	}

	/**
	 * Just the setter.
	 */
	@JpaOnly
	private void setValue(int value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Position other) {
		return Integer.compare(this.value, other.value);
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
		result = prime * result + value;
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (value != other.value)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "" + value;
	}

}
