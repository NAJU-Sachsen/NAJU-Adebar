package de.naju.adebar.web.validation.persons.activist;

import com.google.common.collect.Lists;
import de.naju.adebar.documentation.DesignSmell;
import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Simple service to apply the data from an {@link AddActivistForm} to {@link ActivistProfile}
 * instances
 *
 * @author Rico Bergmann
 */
@Service
public class AddActivistFormConverter implements
    ValidatingEntityFormConverter<ActivistProfile, AddActivistForm> {

  @Override
  public boolean isValid(AddActivistForm form) {
    // every form is valid
    return true;
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return AddActivistForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    // every form is valid
  }

  @DesignSmell(description = "It should actually be possible to use this method. "
      + "An ActivistProfile should just be some value object")
  @Override
  public ActivistProfile toEntity(AddActivistForm form) {
    throw new UnsupportedOperationException(
        "An AddActivistForm may only be applied to existing persons");
  }

  @Override
  public AddActivistForm toForm(ActivistProfile entity) {
    if (entity == null) {
      return null;
    }

    return new AddActivistForm(entity.getJuleicaCard(),
        Lists.newArrayList(entity.getLocalGroups()));
  }

  @Override
  public void applyFormToEntity(AddActivistForm form, ActivistProfile entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    if (form.isJuleica()) {
      entity.updateJuleicaCard( //
          new JuleicaCard(form.getJuleicaExpiryDate(), form.getJuleicaLevel()));
    } else {
      entity.updateJuleicaCard(null);
    }
  }
}
