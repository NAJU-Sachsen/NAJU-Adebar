package de.naju.adebar.model.human;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Representation of referents. They may or may not be activists (e.g. referents from other organisations will
 * usually not be). As all referents nevertheless will still be tracked as {@link Person}, we only need to keep track
 * of their qualifications
 * @author Rico Bergmann
 * @see Person
 * @see Qualification
 */
@Entity
public class Referent implements Serializable {
	@EmbeddedId private PersonId associatedPerson;
	@ManyToMany private List<Qualification> qualifications;

	// constructors

    /**
     * Full constructor
     * @param person the person to turn into a referent
     */
	Referent(PersonId person) {
        Assert.notNull(person, "Associated person may not be null!");
        this.associatedPerson = person;
        this.qualifications = new ArrayList<>();
    }

    /**
     * Default constructor, just for JPA's sake
     */
    protected Referent() {

    }

    // getters

    /**
     * @return the person the referent is associated (= is the referent) with
     */
    public PersonId getAssociatedPerson() {
        return associatedPerson;
    }

    /**
     * @return the referent's qualifications
     */
    public Iterable<Qualification> getQualifications() {
        return qualifications;
    }

    // setters

    /**
     * Update's the person the referent is assoicated with. Should not be called from "outside", hence package-local
     * @param person
     */
    void setAssociatedPerson(PersonId person) {
        Assert.notNull(person, "Associated person may not be null!");
        this.associatedPerson = associatedPerson;
    }

    /**
     * Updates a the referent's qualifications. Should not be called from "outside", hence package-local
     * @param qualifications the qualifications to set
     */
    void setQualifications(List<Qualification> qualifications) {
        Assert.noNullElements(qualifications.toArray(), "At least one qualification was null");
	    this.qualifications = qualifications;
    }

    // "normal" methods

    /**
     * @param qualification the qualification to add to the referent
     * @throws IllegalArgumentException if the referent already has this qualification
     */
    public void addQualification(Qualification qualification) {
        Assert.notNull(qualification, "Qualification may not be null!");
        if (qualifications.contains(qualification)) {
            throw new IllegalArgumentException("Referent " + this + " already has qualification " + qualification);
        }
        qualifications.add(qualification);
    }

    /**
     * @param qualification the qualification to remove from the referent
     * @throws IllegalArgumentException it the referent does not have this qualification
     */
    public void removeQualification(Qualification qualification) {
	    if (!qualifications.contains(qualification)) {
	        throw new IllegalArgumentException("Referent " + this + " has no qualification " + qualification);
        }
        qualifications.remove(qualification);
    }

    /**
     * @param qualification the qualification to check
     * @return {@code true} if the referent has the qualification
     */
    public boolean hasQualification(Qualification qualification) {
	    return qualifications.contains(qualification);
    }

    // overridden from Object

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Referent referent = (Referent) o;

        if (!associatedPerson.equals(referent.associatedPerson)) return false;
        return qualifications.equals(referent.qualifications);
    }

    @Override
    public int hashCode() {
        int result = associatedPerson.hashCode();
        result = 31 * result + qualifications.hashCode();
        return result;
    }

    @Override
    public String toString() {
	    return String.format("Referent [associatedPerson=%s, qualifications=%d]", associatedPerson, qualifications.size());
    }
}
