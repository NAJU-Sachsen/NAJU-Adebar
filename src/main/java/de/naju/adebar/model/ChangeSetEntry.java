package de.naju.adebar.model;

import org.springframework.util.Assert;

/**
 * Specification of a change which was performed as part of an '{@code update}' event
 *
 * @author Rico Bergmann
 * @see EntityUpdatedEvent
 */
public class ChangeSetEntry {

  private final String field;
  private final Object oldValue;
  private final Object newValue;

  /**
   * Creates a new entry
   *
   * @param field the updated field
   * @param oldValue the field's value before the update
   * @param newValue the field's value after the update
   * @return the entry
   */
  public static ChangeSetEntry forField(String field, Object oldValue, Object newValue) {
    return new ChangeSetEntry(field, oldValue, newValue);
  }

  /**
   * Full constructor
   * 
   * @param field the updated field
   * @param oldValue the field's value before the update
   * @param newValue the field's value after the update
   */
  private ChangeSetEntry(String field, Object oldValue, Object newValue) {
    Assert.hasText(field, "The field must be specified");
    this.field = field;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  /**
   * @return the field
   */
  public final String getField() {
    return field;
  }

  /**
   * @return the field's value before the update
   */
  public final Object getOldValue() {
    return oldValue;
  }

  /**
   * @return the field's value after the update
   */
  public final Object getNewValue() {
    return newValue;
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
    result = prime * result + ((field == null) ? 0 : field.hashCode());
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
    if (getClass() != obj.getClass())
      return false;
    ChangeSetEntry other = (ChangeSetEntry) obj;
    if (field == null) {
      if (other.field != null)
        return false;
    } else if (!field.equals(other.field))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format("%s: %s -> %s", field, oldValue, newValue);
  }
}
