package de.naju.adebar.model.persons.details;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * A Juleica card qualifies an activist to host events with teenagers and assures a basic education
 * regarding youth protection, essential legal regulations and first-aid measures.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class JuleicaCard {

	public static final String BASIC_JULEICA_LEVEL = "G";
	public static final String EXTENDED_JULEICA_LEVEL = "L";

	@Column(name = "expiryDate")
	private LocalDate expiryDate;

	@Column(name = "level")
	private String level;

	/**
	 * Minimalistic constructor.
	 *
	 * @param expiryDate the expiry date of the Juleica card
	 */
	public JuleicaCard(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
		this.level = BASIC_JULEICA_LEVEL;
	}

	/**
	 * Full constructor
	 *
	 * @param expiryDate the expiry date of the Juleica card
	 * @param level the level of the Juleica education (G or A most likely)
	 */
	public JuleicaCard(LocalDate expiryDate, String level) {
		this.expiryDate = expiryDate;
		this.level = level;
	}

	/**
	 * Default constructor
	 */
	public JuleicaCard() {
		this.level = BASIC_JULEICA_LEVEL;
	}

	/**
	 * @return the expiry date of the card. May be {@code null}.
	 */
	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	/**
	 * @return the level of the Juleica education. May be {@code null}.
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * Checks, whether {@code this} card has a defined expiry date and that date was not reached.
	 */
	@Transient
	public boolean isValid() {
		if (expiryDate == null) {
			return false;
		}
		return expiryDate.isAfter(LocalDate.now());
	}

	/**
	 * Checks, whether {@code this} card has a defined expiry date which has passed already.
	 */
	public boolean isExpired() {
		if (expiryDate == null) {
			return false;
		}
		return expiryDate.isBefore(LocalDate.now());
	}

	/**
	 * Checks, whether an expiry date was set for {@code this} card.
	 */
	public boolean isExpiryDateDefined() {
		return expiryDate != null;
	}

	/**
	 * @param expiryDate the expiry date of the card. May be {@code null}.
	 */
	protected void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * @param level the level of the Juleica education. May be {@code null}.
	 */
	protected void setLevel(String level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		int result = expiryDate != null ? expiryDate.hashCode() : 0;
		result = 31 * result + (level != null ? level.hashCode() : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		JuleicaCard that = (JuleicaCard) o;

		if (expiryDate != null ? !expiryDate.equals(that.expiryDate) : that.expiryDate != null) {
			return false;
		}
		return level != null ? level.equals(that.level) : that.level == null;
	}

	@Override
	public String toString() {
		return "JuleicaCard{" + "expiryDate=" + expiryDate + ", level='" + level + '\'' + '}';
	}
}
