package de.naju.adebar.web.validation.persons.referent;

import com.google.common.collect.Lists;
import de.naju.adebar.model.persons.ReferentProfile;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/**
 * Simple service to apply the data from an {@link AddReferentForm} to {@link ReferentProfile}
 * instances.
 *
 * @author Rico Bergmann
 */
@Service
public class AddReferentFormConverter implements
    ValidatingEntityFormConverter<ReferentProfile, AddReferentForm> {

  @Override
  public boolean isValid(AddReferentForm form) {
    // every form is valid
    return true;
  }

  @Override
  public ReferentProfile toEntity(AddReferentForm form) {
    throw new UnsupportedOperationException(
        "An AddReferentForm may only be applied to existing persons");
  }

  @Override
  public AddReferentForm toForm(ReferentProfile entity) {
    return new AddReferentForm(Lists.newArrayList(entity.getQualifications()));
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return AddReferentForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object o, Errors errors) {
    // every form is valid
  }

  @Override
  public void applyFormToEntity(AddReferentForm form, ReferentProfile entity) {
    form.getQualifications().forEach(entity::addQualification);
  }
}
