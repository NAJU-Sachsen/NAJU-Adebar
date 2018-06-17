package de.naju.adebar.model.support;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

/**
 * A simple wrapper for longs used as entity identifiers.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class NumericEntityId implements EntityId<Long> {

  private static final long serialVersionUID = 1656900468936014460L;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  public NumericEntityId(long value) {
    this.id = value;
  }

  public NumericEntityId() {}

  /**
   * @return the id
   */
  @JpaOnly
  private long getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  @JpaOnly
  private void setId(long id) {
    this.id = id;
  }

  @Override
  @Transient
  public Long getIdentifier() {
    return id;
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
    result = prime * result + (int) (id ^ (id >>> 32));
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
    NumericEntityId other = (NumericEntityId) obj;
    if (id != other.id) {
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
    return String.format("%d", id);
  }

}
