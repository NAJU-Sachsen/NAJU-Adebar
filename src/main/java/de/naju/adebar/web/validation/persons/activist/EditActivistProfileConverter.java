package de.naju.adebar.web.validation.persons.activist;

import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Simple service to apply the data from an {@link EditActivistProfileForm} to {@link
 * ActivistProfile} objects.
 *
 * @author Rico Bergmann
 */
@Service
public class EditActivistProfileConverter implements
    ValidatingEntityFormConverter<ActivistProfile, EditActivistProfileForm> {

  @Override
  public ActivistProfile toEntity(EditActivistProfileForm form) {
    throw new UnsupportedOperationException(
        "An EditActivistForm may only be applied to existing activists");
  }

  @Override
  public EditActivistProfileForm toForm(ActivistProfile entity) {
    if (entity == null) {
      return null;
    }

    return new EditActivistProfileForm(entity.getJuleicaCard());
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return EditActivistProfileForm.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // every form is valid
  }

  @Override
  public boolean isValid(EditActivistProfileForm form) {
    // every form is valid
    return true;
  }

  @Override
  public void applyFormToEntity(EditActivistProfileForm form, ActivistProfile entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is not valid " + form);
    }

    JuleicaCard newCard = form.isJuleica() //
        ? new JuleicaCard(form.getJuleicaExpiryDate()) //
        : null;
    entity.updateJuleicaCard(newCard);
  }

}
