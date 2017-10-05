package de.naju.adebar.model.human;

import de.naju.adebar.util.Validation;
import org.hibernate.validator.constraints.Email;
import org.springframework.util.Assert;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstraction of a person. No matter of its concrete role (camp participant, activist, ...) some data always
 * needs to be tracked. This will be handled here.
 * @author Rico Bergmann
 * @see de.naju.adebar.model.human
 */
@Entity(name = "person")
public class Person {
	private static final int MAX_PARENT_PROFILES = 2;
	
    @EmbeddedId @Column(name = "id") private PersonId id;

    @Column(name = "firstName") private String firstName;
    @Column(name = "lastName") private String lastName;
    @Column(name = "email") @Email private String email;
    @Column(name = "phone") private String phoneNumber;
    @Embedded private Address address;

    @OneToOne(cascade = CascadeType.ALL) @PrimaryKeyJoinColumn private ParticipantProfile participantProfile;
    @OneToOne(cascade = CascadeType.ALL) @PrimaryKeyJoinColumn private ActivistProfile activistProfile;
    @OneToOne(cascade = CascadeType.ALL) @PrimaryKeyJoinColumn private ReferentProfile referentProfile;
    
    @OneToMany(cascade = CascadeType.ALL) private List<Person> parentProfiles;
    
    @Column(name = "archived") private boolean archived;

    // constructors

    /**
     * Full constructor to create new Person instances. However to create a new object from outside the package,
     * the {@link PersonFactory} should be used.
     * @param id the person's unique ID. Used as PK within the database
     * @param firstName the person's first name. May not be empty.
     * @param lastName the person's last name. May not be empty.
     * @param email the person's email address. Must be a valid email address or {@code null}
     * @throws IllegalArgumentException if any of the parameters constraints are violated.
     * @see PersonFactory
     */
    Person(PersonId id, String firstName, String lastName, String email) {
        Object[] params = {id, firstName, lastName};
        Assert.noNullElements(params, "No argument may be null: " + Arrays.toString(params));
        Assert.hasText(firstName, "First name may not be null nor empty, but was: " + firstName);
        Assert.hasText(lastName, "Last name may not be null nor empty, but was: " + lastName);

        if (email != null && !email.isEmpty()) {
        	Assert.isTrue(Validation.isEmail(email), "Not a valid email address: " + email);
        }

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        
        this.parentProfiles = new ArrayList<>(MAX_PARENT_PROFILES);
        
        this.archived = false;
    }

    /**
     * Default constructor for JPA's sake
     */
    @SuppressWarnings("unused")
    private Person() {}

    // getter and setter

    /**
     * @return the person's unique ID.
     */
    public PersonId getId() {
        return id;
    }

    /**
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the person's first name
     * @throws IllegalArgumentException if the new name is empty or {@code null}
     */
    public void setFirstName(String firstName) {
        Assert.hasText(firstName, "First name may not be null nor empty, but was: " + firstName);
        this.firstName = firstName;
    }

    /**
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the person's last name
     * @throws IllegalArgumentException if the new name is empty or {@code null}
     */
    public void setLastName(String lastName) {
        Assert.hasText(lastName, "Last name may not be null nor empty, but was: " + lastName);
        this.lastName = lastName;
    }

    /**
     * @return the person's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the person's email address, may be {@code null
     * @throws IllegalArgumentException if the email is not valid, i.e. does not match the email regex (Existence
     * of the address is not checked)
     */
    public void setEmail(String email) {
    	if (email != null) {
    		Assert.isTrue(Validation.isEmail(email), "Not a valid email address: " + email);
    	}
        this.email = email;
    }

    /**
     * @return the person's phone number. May be {@code null}.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the person's phone number. May be {@code null}.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the person's address. May be {@code null}.
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address the person's address. May be {@code null}.
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the participant profile of the person. If it is not a camp participant, this will return {@code null}.
     */
    public ParticipantProfile getParticipantProfile() {
        return participantProfile;
    }

    /**
     * @param participantProfile the participant profile of the person. May be {@code null} if the person is not a
     * camp participant.
     */
    public void setParticipantProfile(ParticipantProfile participantProfile) {
        this.participantProfile = participantProfile;
    }

    /**
     * @return activist-related information about the person. If it is not an activist, this will return {@code null}.
     */
    public ActivistProfile getActivistProfile() {
        return activistProfile;
    }

    /**
     * @param activistProfile the activist profile of the person. May be {@code null} if the person is not an activist.
     */
    public void setActivistProfile(ActivistProfile activistProfile) {
        this.activistProfile = activistProfile;
    }

    /**
     * @return referent-related information about the person. If it is not a referent, this will return {@code null}.
     */
    public ReferentProfile getReferentProfile() {
        return referentProfile;
    }

    /**
     * @param referentProfile the referent profile of the person. May be {@code null} if the person is not a referent.
     */
    public void setReferentProfile(ReferentProfile referentProfile) {
        this.referentProfile = referentProfile;
    }
    
    /**
     * @return the person's parents
     */
    public Iterable<Person> getParentProfiles() {
    	return parentProfiles;
    }
    
    /**
     * @param parentProfiles the person's parents
     */
    public void setParentProfiles(List<Person> parentProfiles) {
    	if (parentProfiles == null) {
    		this.parentProfiles = new ArrayList<>(MAX_PARENT_PROFILES);
    	} else {
    		this.parentProfiles = parentProfiles;
    	}
    }
    
    /**
     * @return whether the person is still to be used
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * @param archived whether the person is still to be used
     */
    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    /**
     * @param id the person's unique ID
     */
    @SuppressWarnings("unused")
    private void setId(PersonId id) {
        this.id = id;
    }
    
    // query methods
    
    /**
     * @return the subscriber's name, which basically is {@code firstName + " " + lastName}
     */
    @Transient
    public String getName() {
        return firstName + " " + lastName;
    }
    
    /**
     * @return {@code true} if an email address is set, {@code false} otherwise
     */
    public boolean hasEmail() {
    	return email != null;
    }
    
    /**
     * @return {@code true} if a parent is registered for this person, {@code false} otherwise
     */
    public boolean hasParents() {
    	return !parentProfiles.isEmpty();
    }
    
    /**
     * @return {@code true} if another parent profile may be connected to this person, {@code false} otherwise
     */
    public boolean parentProfileMayBeConnected() {
    	return parentProfiles.size() < MAX_PARENT_PROFILES;
    }

    // normal methods

    /**
     * Turns the person into a camp participant.
     * @return the person's new participant profile
     */
    public ParticipantProfile makeParticipant() {
        if (isParticipant()) {
            throw new IllegalStateException("Person already is a participant");
        }
        this.participantProfile = new ParticipantProfile(this);
        return participantProfile;
    }

    /**
     * @return {@code true} if the person is a camp participant, or {@code false} otherwise
     */
    public boolean isParticipant() {
        return participantProfile != null;
    }

    /**
     * Turns the person into an activist.
     * @return the new activist profile, containing all activist related data
     */
    public ActivistProfile makeActivist() {
        if (isActivist()) {
            throw new IllegalStateException("Person already is an activist");
        }
        this.activistProfile = new ActivistProfile(this);
        return activistProfile;
    }

    /**
     * @return {@code true} if the person is an activist, or {@code false} otherwise
     */
    public boolean isActivist() {
        return activistProfile != null;
    }

    /**
     * Turns the person into a referent.
     * @return the new referent profile, containing all referent related data.
     */
    public ReferentProfile makeReferent() {
        if (isReferent()) {
            throw new IllegalStateException("Person already is a referent");
        }
        this.referentProfile = new ReferentProfile(this);
        return referentProfile;
    }

    /**
     * @return {@code true} if the person is a referent, or {@code false} otherwise
     */
    public boolean isReferent() {
        return referentProfile != null;
    }
    
    /**
     * Saves a person as parent for {@code this}
     * @param parent the parent
     */
    public void connectParentProfile(Person parent) {
    	if (!parentProfileMayBeConnected()) {
    		throw new IllegalStateException("No more parent profile may be connected");
    	} else if (parentProfiles.contains(parent)) {
    		throw new ExistingParentException(String.format("Parent: %s; child: %s", parent, this));
    	} else if (this.equals(parent)) {
    		throw new ImpossibleKinshipRelationException("For person " + this);
    	}
    	parentProfiles.add(parent);
    }
    
    /**
     * Removes a parent - brutal.
     * @param parent the former parent
     */
    public void disconnectParentProfile(Person parent) {
    	if (parentProfiles.remove(parent)) {
    		throw new IllegalArgumentException("No connection with parent " + parent);
    	}
    }

    // overridden from Object


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
