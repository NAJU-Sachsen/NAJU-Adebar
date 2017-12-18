package de.naju.adebar.model.events;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;
import de.naju.adebar.util.Validation;

/**
 * A reservation for an event
 *
 * @author Rico Bergmann
 */
@Embeddable
public class Reservation {

  private static final int SINGLE_RESERVATION = 1;

  @Column(name = "description")
  private String description;

  @Column(name = "contactEmail")
  private String contactEmail;

  @Column(name = "slots")
  private int numberOfSlots;

  // constructors

  /**
   * @param description the reservation's description
   */
  public Reservation(String description) {
    this(description, SINGLE_RESERVATION, null);
  }

  /**
   * @param description the reservation's description
   * @param numberOfSlots the number of slots to reserve
   */
  public Reservation(String description, int numberOfSlots) {
    this(description, numberOfSlots, null);
  }

  /**
   * @param description the reservation's description
   * @param numberOfSlots the number of slots to reserve
   * @param contactEmail address to contact for further requests
   */
  public Reservation(String description, int numberOfSlots, String contactEmail) {
    Assert.hasText(description, "Description may not be null nor empty, but was " + description);
    if (contactEmail != null && !contactEmail.isEmpty()) {
      Assert.isTrue(Validation.isEmail(contactEmail), "Not a valid email address: " + contactEmail);
    } else {
      this.contactEmail = null;
    }

    this.description = description;
    this.numberOfSlots = numberOfSlots;
    this.contactEmail = contactEmail;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private Reservation() {}

  // getter and setter

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the contact email
   */
  public String getContactEmail() {
    return contactEmail;
  }

  /**
   * @param contactEmail the contact email
   */
  public void setContactEmail(String contactEmail) {
    if (contactEmail != null) {
      Assert.isTrue(Validation.isEmail(contactEmail), "Not a valid email address: " + contactEmail);
    }
    this.contactEmail = contactEmail;
  }

  /**
   * @return the number of reserved slots
   */
  public int getNumberOfSlots() {
    return numberOfSlots;
  }

  /**
   * @param numberOfSlots the number of reserved slots
   */
  public void setNumberOfSlots(int numberOfSlots) {
    this.numberOfSlots = numberOfSlots;
  }

  /**
   * @param description the description
   */
  @SuppressWarnings("unused")
  private void setDescription(String description) {
    this.description = description;
  }

  // overridden from Object

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Reservation))
      return false;
    Reservation other = (Reservation) obj;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    return true;
  }
}
