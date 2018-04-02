package de.naju.adebar.web.validation.persons.relatives;

import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.ParentProfile;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Simple service to apply the data from an {@link EditParentProfileForm} to {@link ParentProfile}
 * objects.
 *
 * @author Rico Bergmann
 */
@Service
public class EditParentProfileFormConverter implements //
    ValidatingEntityFormConverter<ParentProfile, EditParentProfileForm> {

  @Override
  public ParentProfile toEntity(EditParentProfileForm form) {
    throw new UnsupportedOperationException(
        "An EditParentForm may only be applied to existing entities");
  }

  @Override
  public EditParentProfileForm toForm(ParentProfile entity) {
    if (entity == null) {
      return null;
    }
    return new EditParentProfileForm(entity.getWorkPhone(), entity.getLandlinePhone());
  }

  @Override
  public void applyFormToEntity(EditParentProfileForm form, ParentProfile entity) {
    PhoneNumber workPhone = form.hasWorkPhone() ? PhoneNumber.of(form.getWorkPhone()) : null;
    PhoneNumber landlinePhone = form.hasLandlinePhone() //
        ? PhoneNumber.of(form.getLandlinePhone()) //
        : null;

    entity.updateWorkPhone(workPhone);
    entity.updateLandlinePhone(landlinePhone);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return EditParentProfileForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    // every form is valid
  }

  @Override
  public boolean isValid(EditParentProfileForm form) {
    // every form is valid
    return true;
  }

}
