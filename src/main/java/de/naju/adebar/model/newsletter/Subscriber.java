package de.naju.adebar.model.newsletter;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

import de.naju.adebar.util.Validation;

/**
 * Abstraction of person that subscribed to a newsletter.
 * <p>
 * Each subscriber must have a valid(!) email address and may further possess first and last name.
 * </p>
 * <p>
 * This class is necessary, as not all persons that subscribe to a newsletter have to be
 * tracked as {@link de.naju.adebar.model.human.Person Person}, e.g. one may solely subscribe to a newsletter through
 * a website and therefore will not become part of the activist database itself.
 * </p>
 * @author Rico Bergmann
 *
 */
@Entity
public class Subscriber implements Serializable {
	
	/**
	 * if serialized, this id will be used
	 */
	private final static long serialVersionUID = 7082774853885904589L;
	
	@Id @GeneratedValue private long id;
	private String firstName, lastName;
	private String email;
	
	// constructors
	
	/**
	 * Default constructor for JPA's sake
	 * Not to be used by anything else.
	 */
	public Subscriber() {
		
	}
	
	/**
	 * Minimalist constructor
	 * @param email the subscriber's email address
	 * @throws IllegalArgumentException if the email is not valid 
	 */
	public Subscriber(String email) {
		this("", "", email);
	}
	
	/**
	 * Full constructor
	 * @param firstName the subsriber's first name
	 * @param lastName the subsriber's last name
	 * @param email the subsriber's email
	 * @throws IllegalArgumentException if the email is not valid  or any of the parameters if {@code null}
	 */
	public Subscriber(String firstName, String lastName, String email) {
		Object[] params = {firstName, lastName, email};
		Assert.noNullElements(params, "No parameter may be null!");
		if (!Validation.isEmail(email)) {
			throw new IllegalArgumentException("Must provide a valid email address, but was: " + email);
		}
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	// default getter
	
	/**
	 * @return the subscriber's id (= primary key)
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the subsriber's first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the subscriber's last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the subsriber's email
	 * @throws IllegalArgumentException if the subscriber did not specify an email address
	 */
	public String getEmail() {
		if (email == null) {
			throw new IllegalStateException("No email address was specified!");
		}
		return email;
	}
	
	// default setter
	
	/**
	 * @param firstName the new first name
	 * @throws IllegalArgumentException if the name is {@code null}
	 */
	public void setFirstName(String firstName) {
		Assert.notNull(firstName, "First name may not be null!");
		this.firstName = firstName;
	}

	/**
	 * @param lastName the new last name
	 * @throws IllegalArgumentException if the name is {@code null}
	 */
	public void setLastName(String lastName) {
		Assert.notNull(lastName, "Last name may not be null!");
		this.lastName = lastName;
	}

	/**
	 * @param email the new email address
	 * @throws IllegalArgumentException if the email is {@code null} or invalid
	 */
	public void setEmail(String email) {
		Assert.notNull(email, "Email may not be null!");
		if (!Validation.isEmail(email)) {
			throw new IllegalArgumentException("Must provide a valid email address, but was: " + email);
		}
		this.email = email;
	}

    /**
     * Updates the subscriber's id (= primary key). As this method should only be called by Spring, it is
     * {@literal protected}
     * @param id the new id
     */
	protected void setId(long id) {
	    this.id = id;
    }
	
	// "advanced" getters

    /**
     * @return the subscriber's name, which basically is {@code firstName + " " + lastName}
     */
	public String getName() {
		StringBuilder nameBuilder = new StringBuilder();
		if (firstName != null && !firstName.isEmpty()) {
			nameBuilder.append(firstName);
			if (lastName != null && !lastName.isEmpty()) {
				nameBuilder.append(" ").append(lastName);
			}
		} else if (lastName != null && !lastName.isEmpty()) {
			nameBuilder.append(lastName);
		}
		return nameBuilder.toString(); 
	}
	
	// checkers

    /**
     * @return {@code true} if at least first name or last name is not empty
     */
	public boolean hasName() {
	    if (firstName == null && lastName == null) {
	        return false;
        } else if (firstName == null && lastName.isEmpty()) {
	        return false;
        } else if (lastName == null && firstName.isEmpty()) {
	        return false;
        } else {
	        return true;
        }
	}
	
	// overridden from Object

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Subscriber)) {
			return false;
		}
		Subscriber other = (Subscriber) obj;
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("NewsletterSubscriber: %s %s (%s)", firstName, lastName, getEmail());
	}
	
}
