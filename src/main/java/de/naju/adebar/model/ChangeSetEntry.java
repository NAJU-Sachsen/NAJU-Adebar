package de.naju.adebar.model;

import org.springframework.util.Assert;

/**
 * Specification of a change which was performed as part of an '{@code update}' event
 *
 * @author Rico Bergmann
 * @see EntityUpdatedEvent
 * @param <T> the field which was updated
 */
public class ChangeSetEntry<T> {

  private final String field;
  private final T oldValue;
  private final T newValue;

  /**
   * Creates a new entry
   *
   * @param field the updated field
   * @param oldValue the field's value before the update
   * @param newValue the field's value after the update
   * @return the entry
   */
  public static <T> ChangeSetEntry<T> forField(String field, T oldValue, T newValue) {
    return new ChangeSetEntry<>(field, oldValue, newValue);
  }

  /**
   * Full constructor
   * 
   * @param field the updated field
   * @param oldValue the field's value before the update
   * @param newValue the field's value after the update
   */
  private ChangeSetEntry(String field, T oldValue, T newValue) {
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
  public final T getOldValue() {
    return oldValue;
  }

  /**
   * @return the field's value after the update
   */
  public final T getNewValue() {
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

  public boolean equals(ChangeSetEntry<T> other) {
    if (this == other)
      return true;
    if (other == null)
      return false;
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
