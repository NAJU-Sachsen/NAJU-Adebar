package de.naju.adebar.web.validation;

/**
 * A service that makes use of {@link NewOrExistingEntityForm} forms and automatically decides how
 * to convert an entity based on the forms state
 *
 * @param <E> the entity
 * @param <F> the form
 * @author Rico Bergmann
 */
public interface NewOrExistingValidatingEntityFormConverter<E, F extends NewOrExistingEntityForm>
    extends ValidatingEntityFormConverter<E, F> {

  /**
   * Converts the form to an existing entity
   *
   * @param form the form
   * @return the entity
   *
   * @throws IllegalStateException if the form actually contained a new entity
   */
  E toExistingEntity(F form);

  /**
   * Converts the form to a new entity
   *
   * @param form the form
   * @return the entity
   *
   * @throws IllegalStateException if the form actually contained an existing entity
   */
  E toNewEntity(F form);

  /**
   * Checks whether the data in the form describes a valid new entity
   *
   * @param form the form
   * @return whether the form is valid
   *
   * @throws IllegalStateException if the form actually contained an existing entity
   */
  boolean newEntityDataIsValid(F form);

  /**
   * Checks whether the data in the form describes a valid existing entity
   *
   * @param form the form
   * @return whether the form is valid
   *
   * @throws IllegalStateException if the form actually contained a new entity
   */
  boolean existingEntityDataIsValid(F form);

  @Override
  default E toEntity(F form) {
    switch (form.getFormStatus()) {
      case NEW_ENTITY:
        return toNewEntity(form);
      case EXISTING_ENTITY:
        return toExistingEntity(form);
      default:
        throw new AssertionError("formStatus: " + form.getFormStatus());
    }
  }

  @Override
  default boolean isValid(F form) {
    switch (form.getFormStatus()) {
      case NEW_ENTITY:
        return newEntityDataIsValid(form);
      case EXISTING_ENTITY:
        return existingEntityDataIsValid(form);
      default:
        throw new AssertionError("formStatus: " + form.getFormStatus());
    }
  }
}
