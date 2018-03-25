package de.naju.adebar.web.validation;

/**
 * An form which may either contain an existing entity or a new one.
 *
 * <p> This may for example be a form which offers two tabs - one for searching for an entity and
 * one to create a new entity. By using a form which implements this interface, the converter may
 * actually do the tedious task of deciding what action to perform based on the input data
 * automatically.
 *
 * @author Rico Bergmann
 */
public interface NewOrExistingEntityForm {

  /**
   * Checks which kind of data was actually submitted
   *
   * @return whether the form contains a new or an existing entity
   */
  SubmittedData getFormStatus();

  /**
   * @return whether the form contains a new entity
   */
  default boolean isNewEntity() {
    return getFormStatus() == SubmittedData.NEW_ENTITY;
  }

  /**
   * @return whether the form contains an existing entity
   */
  default boolean isExistingEntity() {
    return getFormStatus() == SubmittedData.EXISTING_ENTITY;
  }

  /**
   * The kind of data that got submitted
   */
  enum SubmittedData {
    NEW_ENTITY, EXISTING_ENTITY
  }

}
