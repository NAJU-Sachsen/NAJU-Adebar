package de.naju.adebar.web.validation;

import org.springframework.validation.Validator;

/**
 * A service that validates and converts web forms to the entities they describe and vice-versa.
 *
 * @author Rico Bergmann
 *
 * @param <E> the entity
 * @param <F> the form
 */
public interface ValidatingEntityFormConverter<E, F> extends EntityFormConverter<E, F>, Validator {

}
