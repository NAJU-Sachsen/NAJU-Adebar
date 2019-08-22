package de.naju.adebar.model.events;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

@Embeddable
public class ArrivalOption {

	@Column(name = "arrivalOption")
	private String description;

	public static ArrivalOption of(String description) {
		return new ArrivalOption(description);
	}

	protected ArrivalOption(String description) {
		Assert.hasText(description, "description may not be null nor empty");
		this.description = description;
	}

	@JpaOnly
	private ArrivalOption() {}

	public String getDescription() {
		return description;
	}

	@JpaOnly
	private void setDescription(String description) {
		this.description = description;
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
		result = prime * result + ((description == null) ? 0 : description.hashCode());
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
		ArrivalOption other = (ArrivalOption) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		return description;
	}

}
