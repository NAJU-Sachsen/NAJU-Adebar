package de.naju.adebar.model.events;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * Identifier for an event
 * 
 * @author Rico Bergmann
 * @see Event
 */
@Embeddable
public class EventId implements Serializable {
  private static final long serialVersionUID = 1592830997438533859L;

  @Column(name = "id", unique = true)
  private final String id;

  /**
   * Just create a new identifier
   */
  EventId() {
    this.id = UUID.randomUUID().toString();
  }

  /**
   * Create an identifier using an existing one
   * 
   * @param id the existing id to use
   */
  public EventId(String id) {
    Assert.notNull(id, "Id may not be null!");
    this.id = id;
  }

  /**
   * @return the identifier. As it should not be modified under any circumstances, it is
   *         {@code final}
   */
  final String getId() {
    return id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof EventId))
      return false;
    EventId other = (EventId) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return id;
  }

}
