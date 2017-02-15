package de.naju.adebar.model.human;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.*;

import org.springframework.util.Assert;

/**
 * Representation of activists. As all activists have an associated {@link Person}, we only need to keep track
 * of JuLeiCa cards. This is what distinguishes a normal camp participant from an activist for us.
 * @author Rico Bergmann
 * @see Person
 */
@Entity
public class Activist implements Serializable {
	private static final long serialVersionUID = -2571218258153771849L;
	
	@EmbeddedId private PersonId associatedPerson;
	private LocalDate juleicaExpiryDate;
	private boolean active;

	// constructors

	/**
	 * @param associatedPerson the id of the person which will become an activist now
	 * @param juleicaExpiryDate the date the activist's JuLeiCa card expires or {@code null} if the activist has no
     *                          juleica
     * @see PersonId
	 */
	Activist(PersonId associatedPerson, LocalDate juleicaExpiryDate) {
		Assert.notNull(associatedPerson, "The Person associated to an activist may not be null!");
		this.associatedPerson = associatedPerson;
		this.juleicaExpiryDate = juleicaExpiryDate;
		this.active = true;
	}

    /**
     * Default constructor for JPA's sake
     * Not to be used by anything else.
     */
    @SuppressWarnings("unused")
	protected Activist() {
		
	}

	// getter

	/**
	 * @return the id (=primary key) of the associated person
	 */
	public PersonId getAssociatedPerson() {
		return associatedPerson;
	}

	/**
	 * @return the expiry date of the JuLeiCa card or {@code null} if the activist has no JuLeiCa
	 */
	public LocalDate getJuleicaExpiryDate() {
		return juleicaExpiryDate;
	}

	// setter

	/**
	 * @param juleicaExpiryDate the expiry date of the JuLeiCa card. May be {@code null} if the activist has no
     *                          card
	 */
	public void setJuleicaExpiryDate(LocalDate juleicaExpiryDate) {
		this.juleicaExpiryDate = juleicaExpiryDate;
	}

	/**
	 * @param associatedPerson the id of the associated person. As this id links person and activist, it is made
     *                         package local. Not to be modified
	 */
	void setAssociatedPerson(PersonId associatedPerson) {
		Assert.notNull(associatedPerson, "The Person associated to an activist may not be null!");
		this.associatedPerson = associatedPerson;
	}

    /**
     * As activists may be in association with many other objects but a person may not be an activist for ever (i.e.
     * a person may just participate in events but withdraw from organising them) they will not be deleted as soon as
     * they withdraw but rather be deactivated and therefore not be selectable as organisers, etc. any more.
     * @return {@code true} if the activist is still active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the activist's status (active/inactive)
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    // checker

    /**
     * @return {@code true} if the activist has a JuLeiCa card, no matter if the card is still valid or not.
     */
	public boolean hasJuleica() {
	    return juleicaExpiryDate != null;
    }

    // overridden from Object

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associatedPerson == null) ? 0 : associatedPerson.hashCode());
		result = prime * result + ((juleicaExpiryDate == null) ? 0 : juleicaExpiryDate.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		if (!(obj instanceof Activist)) {
			return false;
		}
		Activist other = (Activist) obj;
		if (associatedPerson == null) {
			if (other.associatedPerson != null) {
				return false;
			}
		} else if (!associatedPerson.equals(other.associatedPerson)) {
			return false;
		}
		if (juleicaExpiryDate == null) {
			if (other.juleicaExpiryDate != null) {
				return false;
			}
		} else if (!juleicaExpiryDate.equals(other.juleicaExpiryDate)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Activist [associatedPerson=%s, juleicaExpiryDate=%s]", associatedPerson, juleicaExpiryDate);
	}
	
	
}
