package de.naju.adebar.web.validation.persons.referent;

import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.persons.referent.AddQualificationForm.AddType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Simple service to apply the data from an {@link AddQualificationForm} to {@link Qualification}
 * objects.
 *
 * @author Rico Bergmann
 */
@Service
public class AddQualificationFormConverter implements
    ValidatingEntityFormConverter<Qualification, AddQualificationForm> {

  private final QualificationRepository qualificationRepo;

  /**
   * Full constructor
   *
   * @param qualificationRepo repository containing all qualifications which are currently
   *     available
   */
  public AddQualificationFormConverter(
      QualificationRepository qualificationRepo) {
    Assert.notNull(qualificationRepo, "qualificationRepo may not be null");
    this.qualificationRepo = qualificationRepo;
  }

  @Override
  public Qualification toEntity(AddQualificationForm form) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid " + form);
    } else if (form.getAddType() == AddType.EXISTING_QUALIFICATION) {
      return qualificationRepo.findById(form.getExistingQualificationKey()).orElse(null);
    } else {
      return new Qualification(form.getNewQualificationName(),
          form.getNewQualificationDescription());
    }
  }

  @Override
  public AddQualificationForm toForm(Qualification entity) {
    return new AddQualificationForm(entity.getName(), entity.getDescription());
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return AddQualificationForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for forms of class " + target.getClass());
    }

    /*
     * Depending on whether a new qualification should be added, or whether it already exists either
     * the key of the existing qualification or the name and description of the new qualification
     * are required.
     */
    AddQualificationForm form = (AddQualificationForm) target;
    if (form.getAddType() == AddType.EXISTING_QUALIFICATION) {
      ValidationUtils.rejectIfEmpty(errors, "existingQualificationKey", "select.required");
    } else {
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newQualificationName", "field.required");
      ValidationUtils
          .rejectIfEmptyOrWhitespace(errors, "newQualificationDescription", "field.required");
    }

  }

  @Override
  public boolean isValid(AddQualificationForm form) {
    if (form.getAddType() == AddType.EXISTING_QUALIFICATION) {
      return !form.getExistingQualificationKey().isEmpty();
    } else {
      return !form.getNewQualificationName().isEmpty() //
          && !form.getNewQualificationDescription().isEmpty();
    }
  }
}
