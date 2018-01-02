package de.naju.adebar.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.Assert;

/**
 * Base class for '{@code update}' events raised by aggregates.
 * <p>
 * An entity should basically trigger such an event, whenever some of its publicly-accessible state
 * has been changed. For more details see the documentation of the respective events and entities.
 * <p>
 * Optionally, each update may contain a change set which provides more detail on the changes
 * performed.
 *
 * @author Rico Bergmann
 * @param <E> the kind of entity which was updated
 */
public abstract class EntityUpdatedEvent<E> {

  private final E entity;
  private final LocalDateTime timestamp;
  private Set<ChangeSetEntry> changeset;

  /**
   * Creates a new event with an empty change set
   *
   * @param entity the entity which has been updated
   */
  protected EntityUpdatedEvent(E entity) {
    this(entity, new HashSet<>());
  }

  /**
   * Creates a new event
   *
   * @param entity the entity which has been updated
   * @param changeset the changes that occurred. May be {@code null} if it should be left
   *        unspecified.
   */
  protected EntityUpdatedEvent(E entity, Collection<ChangeSetEntry> changeset) {
    Assert.notNull(entity, "Entity must be specified");
    this.entity = entity;
    this.timestamp = LocalDateTime.now();
    this.changeset = changeset != null //
        ? new HashSet<>(changeset) //
        : new HashSet<>();
  }

  /**
   * @return the entity which has been updated
   */
  public final E getEntity() {
    return entity;
  }

  /**
   * @return the point in time the event was raised
   */
  public LocalDateTime getTime() {
    return timestamp;
  }

  /**
   * @return whether a change set was attached to this event
   */
  public boolean hasChangeSet() {
    return !changeset.isEmpty();
  }

  /**
   * @return the change set. If none has been specified, the collection will be empty.
   */
  public Collection<ChangeSetEntry> getChangeset() {
    return Collections.unmodifiableCollection(changeset);
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
    result = prime * result + ((entity == null) ? 0 : entity.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
    if (!(obj instanceof EntityUpdatedEvent))
      return false;
    EntityUpdatedEvent<?> other = (EntityUpdatedEvent<?>) obj;
    if (entity == null) {
      if (other.entity != null)
        return false;
    } else if (!entity.equals(other.entity))
      return false;
    if (timestamp == null) {
      if (other.timestamp != null)
        return false;
    } else if (!timestamp.equals(other.timestamp))
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
    return this.getClass().getSimpleName() + " for " + entity + " at "
        + timestamp.format(DateTimeFormatter.ISO_DATE_TIME);
  }

}
