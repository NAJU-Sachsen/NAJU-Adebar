package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.support.NumericEntityId;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import org.springframework.util.Assert;

/**
 * A reservation for an event
 *
 * @author Rico Bergmann
 */
@Embeddable
public class Reservation {

  @Embedded
  private NumericEntityId id;

  @Column(name = "description")
  private String description;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "contactEmail")))
  private Email contactEmail;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "slots")))
  private Capacity slots;

  public static Reservation generateFor(String description, Capacity slots) {
    return new Reservation(description, null, slots);
  }

  public static Reservation generateFor(String description, Email contact, Capacity slots) {
    return new Reservation(description, contact, slots);
  }

  private Reservation(String description, Email contactEmail, Capacity slots) {
    Assert.hasText(description, "description may not be null nor empty");
    Assert.notNull(slots, "slots may not be null");
    this.id = new NumericEntityId();
    this.description = description;
    this.contactEmail = contactEmail;
    this.slots = slots;
  }

  @JpaOnly
  private Reservation() {}

  public NumericEntityId getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public Email getContactEmail() {
    return contactEmail;
  }

  public Capacity getNumberOfSlots() {
    return slots;
  }

  public boolean hasContactEmail() {
    return contactEmail != null;
  }

  void setId(NumericEntityId id) {
    this.id = id;
  }

  @JpaOnly
  private void setDescription(String description) {
    this.description = description;
  }

  @JpaOnly
  private void setContactEmail(Email contactEmail) {
    this.contactEmail = contactEmail;
  }

  @JpaOnly
  private void setSlots(Capacity slots) {
    this.slots = slots;
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    Reservation other = (Reservation) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
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
    return "Reservation [id=" + id + ", description=" + description + ", contactEmail="
        + contactEmail + ", slots=" + slots + "]";
  }

}
