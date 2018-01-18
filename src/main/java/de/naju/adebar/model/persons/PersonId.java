package de.naju.adebar.model.persons;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * As we want to link {@link Person} instances with their {@link ParticipantProfile},
 * {@link ActivistProfile} and {@link ReferentProfile}, we somehow need to persist these
 * associations. This is what this class is made for. It will be used as the common primary key for
 * all these classes, when they are translated to database entries.
 *
 * @author Rico Bergmann
 * @see Person
 * @see ParticipantProfile
 * @see <a href="https://en.wikipedia.org/wiki/Unique_key">Primary keys</a>
 */
@Embeddable
public class PersonId implements Serializable, Iterator<PersonId>, Comparable<PersonId> {

  private static final long serialVersionUID = -6223486850240404100L;

  @Column(name = "id", unique = true)
  private final String id;

  /**
   * Create an identifier using an existing one
   *
   * @param id the existing id to use
   */
  public PersonId(String id) {
    Assert.notNull(id, "Id may not be null!");
    this.id = id;
  }

  /**
   * Copy constructor
   *
   * @param other the person ID to copy
   */
  public PersonId(PersonId other) {
    this.id = other.id;
  }

  /**
   * Just create a new identifier
   */
  PersonId() {
    this.id = UUID.randomUUID().toString();
  }

  /**
   * @return the identifier. As it should not be modified under any circumstances, it is
   *         {@code final}
   */
  final String getId() {
    return id;
  }

  // implementation of Iterator-interface

  @Override
  public boolean hasNext() {
    return true;
  }

  @Override
  public PersonId next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return new PersonId();
  }

  // implementation of the Comparable-interface

  @Override
  public int compareTo(PersonId other) {
    return this.id.compareTo(other.id);
  }

  // overridden from Object

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    PersonId personId = (PersonId) o;

    return id.equals(personId.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return id;
  }

}
