package de.naju.adebar.model.newsletter;

import de.naju.adebar.model.core.Email;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.util.Assert;

/**
 * Abstraction of person that subscribed to a newsletter.
 * <p>
 * Each subscriber must have a valid(!) email address and may further possess first and last name.
 * </p>
 * <p>
 * This class is necessary, as not all persons that subscribe to a newsletter have to be tracked as
 * {@link de.naju.adebar.model.persons.Person Person}, e.g. one may solely subscribe to a newsletter
 * through a website and therefore will not become part of the activist database itself.
 * </p>
 *
 * @author Rico Bergmann
 */
@Entity(name = "subscriber")
public class Subscriber implements Serializable {

  /**
   * if serialized, this id will be used
   */
  private static final long serialVersionUID = 7082774853885904589L;

  @Id
  @GeneratedValue
  @Column(name = "id")
  private long id;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Embedded
  private Email email;

  // constructors

  /**
   * Default constructor for JPA's sake Not to be used by anything else.
   */
  public Subscriber() {

  }

  /**
   * Minimalist constructor
   *
   * @param email the subscriber's email address
   * @throws IllegalArgumentException if the email is not valid
   */
  public Subscriber(Email email) {
    this("", "", email);
  }

  /**
   * Full constructor
   *
   * @param firstName the subsriber's first name
   * @param lastName the subsriber's last name
   * @param email the subsriber's email
   * @throws IllegalArgumentException if the email is not valid or any of the parameters if
   *     {@code null}
   */
  public Subscriber(String firstName, String lastName, Email email) {
    Object[] params = {firstName, lastName, email};
    Assert.noNullElements(params, "No parameter may be null!");
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
   * Updates the subscriber's id (= primary key). As this method should only be called by Spring, it
   * is {@code protected}
   *
   * @param id the new id
   */
  protected void setId(long id) {
    this.id = id;
  }

  /**
   * @return the subsriber's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the new first name
   * @throws IllegalArgumentException if the name is {@code null}
   */
  public void setFirstName(String firstName) {
    Assert.notNull(firstName, "First name may not be null!");
    this.firstName = firstName;
  }

  // default setter

  /**
   * @return the subscriber's last name
   */
  public String getLastName() {
    return lastName;
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
   * @return the subsriber's email
   */
  public Email getEmail() {
    return email;
  }

  /**
   * @param email the new email address
   * @throws IllegalArgumentException if the email is {@code null} or invalid
   */
  public void setEmail(Email email) {
    this.email = email;
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
    return hasFirstName() || hasLastName();
  }

  /**
   * @return {@code true} iff the first name is neither {@code null} nor empty
   */
  public boolean hasFirstName() {
    return firstName != null && !firstName.isEmpty();
  }

  /**
   * @return {@code true} iff the last name is neither {@code null} nor empty
   */
  public boolean hasLastName() {
    return lastName != null && !lastName.isEmpty();
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

    // if both ID's are set, only compare them
    if (other.id != 0 && this.id != 0) {
      return other.id == this.id;
    }

    // at least one subscriber was not persisted yet => compare attributes
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
