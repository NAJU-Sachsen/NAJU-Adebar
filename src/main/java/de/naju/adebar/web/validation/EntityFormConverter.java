package de.naju.adebar.web.validation;

/**
 * A service that converts web forms to the entities they describe and vice-versa.
 *
 * @author Rico Bergmann
 *
 * @param <E> the entity
 * @param <F> the form
 */
public interface EntityFormConverter<E, F> {

  /**
   * Converts a form to a corresponding entity-instance
   *
   * @param form the form to convert
   * @return the entity
   */
  E toEntity(F form);

  /**
   * Converts an entity to a corresponding form-instance.
   *
   * @param entity the entity to convert
   * @return the form
   */
  F toForm(E entity);

  /**
   * Sets the data from a form on an existing entity instance
   *
   * @param form the form containing the data
   * @param entity the entity to update
   */
  default void applyFormToEntity(F form, E entity) {
    String msg = String.format("Form %s may not be applied to an entity of class %s",
        form.getClass().getSimpleName(), entity.getClass().getSimpleName());

    throw new UnsupportedOperationException(msg);
  }

}
