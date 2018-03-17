package de.naju.adebar.web.validation;

import org.springframework.validation.Validator;

/**
 * A service that validates and converts web forms to the entities they describe and vice-versa.
 *
 * @param <E> the entity
 * @param <F> the form
 * @author Rico Bergmann
 */
public interface ValidatingEntityFormConverter<E, F> extends EntityFormConverter<E, F>, Validator {

  /**
   * Checks whether the given form is valid.
   *
   * @param form the form
   * @return {@code true} if the form is valid or {@code false} otherwise
   */
  boolean isValid(F form);

}
