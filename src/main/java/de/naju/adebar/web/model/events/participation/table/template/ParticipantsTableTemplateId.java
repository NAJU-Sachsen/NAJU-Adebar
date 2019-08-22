package de.naju.adebar.web.model.events.participation.table.template;

import de.naju.adebar.app.security.user.Username;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Wrapper for the data that should form the primary key of a {@link ParticipantsTableTemplate}.
 * <p>
 * This class is only necessary because JPA does not directly support composite primary keys.
 * Instead they need to be extracted into a separate {@code @Embeddable} type and be included
 * through {@code @EmbeddedId}.
 * <p>
 * Therefore this class should remain as slim as possible and does not contain any logic, which is
 * kept inside the {@code ParticipantsTableTemplate}.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class ParticipantsTableTemplateId implements Serializable {

	private static final long serialVersionUID = 2282372270512885061L;

	@Embedded
	@AttributeOverrides(@AttributeOverride(name = "username", column = @Column(name = "createdBy")))
	private Username createdBy;

	@Column(name = "name")
	private String name;

	/**
	 * Full constructor.
	 *
	 * @param createdBy the creator of the template
	 * @param name the template's name
	 */
	ParticipantsTableTemplateId(Username createdBy, String name) {
		this.createdBy = createdBy;
		this.name = name;
	}

	/**
	 * Default constructor just for JPA's sake.
	 */
	@JpaOnly
	private ParticipantsTableTemplateId() {}

	/**
	 * Gets the creator of the template.
	 */
	Username getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the creator of the template.
	 */
	void setCreatedBy(Username createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the name of this template.
	 */
	String getName() {
		return name;
	}

	/**
	 * Sets the name of this template.
	 */
	void setName(String name) {
		this.name = name;
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
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ParticipantsTableTemplateId other = (ParticipantsTableTemplateId) obj;
		if (createdBy == null) {
			if (other.createdBy != null) {
				return false;
			}
		} else if (!createdBy.equals(other.createdBy)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
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
		return "createdBy=" + createdBy + ", name=" + name;
	}

}
