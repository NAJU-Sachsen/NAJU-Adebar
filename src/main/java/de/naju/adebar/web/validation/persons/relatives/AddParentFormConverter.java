package de.naju.adebar.web.validation.persons.relatives;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.util.Validation;
import de.naju.adebar.web.validation.NewOrExistingEntityForm.SubmittedData;
import de.naju.adebar.web.validation.NewOrExistingValidatingEntityFormConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 * Service to convert an {@link AddParentForm} to {@link Person} instances. Note that this converter
 * does not handle setting the person as parent of somebody.
 *
 * @author Rico Bergmann
 */
@Service
public class AddParentFormConverter implements //
    NewOrExistingValidatingEntityFormConverter<Person, AddParentForm> {

  private final PersonFactory personFactory;

  /**
   * Full constructor
   *
   * @param personFactory factory to create new persons
   */
  public AddParentFormConverter(PersonFactory personFactory) {
    Assert.notNull(personFactory, "personFactory may not be null");
    this.personFactory = personFactory;
  }

  @Override
  public AddParentForm toForm(Person entity) {
    return new AddParentForm(entity);
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return AddParentForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for instances of class" + target.getClass());
    }

    AddParentForm form = (AddParentForm) target;

    if (form.getFormStatus() == SubmittedData.EXISTING_ENTITY) {
      return;
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newFirstName", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newLastName", "field.required");

    if (form.hasNewEmail() && !Validation.isEmail(form.getNewEmail())) {
      errors.rejectValue("newEmail", "email.phone");
    }

    if (form.hasNewPrivatePhone() && !Validation.isPhoneNumber(form.getNewPrivatePhone())) {
      errors.rejectValue("newPrivatePhone", "phone.invalid");
    }

    if (form.hasNewWorkPhone() && !Validation.isPhoneNumber(form.getNewWorkPhone())) {
      errors.rejectValue("newWorkPhone", "phone.invalid");
    }

    if (form.hasNewLandlinePhone() && !Validation.isPhoneNumber(form.getNewLandlinePhone())) {
      errors.rejectValue("newLandlinePhone", "phone.invalid");
    }

  }

  @Override
  public Person toExistingEntity(AddParentForm form) {
    if (!existingEntityDataIsValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }
    return form.getExistingPerson();
  }

  @Override
  public Person toNewEntity(AddParentForm form) {
    if (!newEntityDataIsValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    Email email = form.hasNewEmail() ? Email.of(form.getNewEmail()) : null;
    PhoneNumber privatePhone = form.hasNewPrivatePhone() //
        ? PhoneNumber.of(form.getNewPrivatePhone()) //
        : null;
    PhoneNumber workPhone = form.hasNewWorkPhone() ? PhoneNumber.of(form.getNewWorkPhone()) : null;
    PhoneNumber landlinePhone = form.hasNewLandlinePhone() //
        ? PhoneNumber.of(form.getNewLandlinePhone()) //
        : null;

    //@formatter:off
    return personFactory.buildNew(form.getNewFirstName(), form.getNewLastName(), email)
          .specifyPhoneNumber(privatePhone) //
        .makeParent() //
          .specifyLandlinePhone(landlinePhone) //
          .specifyWorkPhone(workPhone) //
        .create();
    //@formatter:on
  }

  @Override
  public boolean newEntityDataIsValid(AddParentForm form) {
    if (form.getFormStatus() != SubmittedData.NEW_ENTITY) {
      throw new IllegalStateException("Form should contain new entity: " + form);
    }

    if (form.getNewFirstName() == null || form.getNewFirstName().isEmpty()) {
      return false;
    }

    if (form.getNewLastName() == null || form.getNewLastName().isEmpty()) {
      return false;
    }

    if (form.getNewEmail() != null && !form.getNewEmail().isEmpty()
        && !Validation.isEmail(form.getNewEmail())) {
      return false;
    }

    if (form.hasNewPrivatePhone() && !Validation.isPhoneNumber(form.getNewPrivatePhone())) {
      return false;
    }

    if (form.hasNewWorkPhone() && !Validation.isPhoneNumber(form.getNewWorkPhone())) {
      return false;
    }

    if (form.hasNewLandlinePhone() && !Validation.isPhoneNumber(form.getNewLandlinePhone())) {
      return false;
    }

    return true;
  }

  @Override
  public boolean existingEntityDataIsValid(AddParentForm form) {
    if (form.getFormStatus() != SubmittedData.EXISTING_ENTITY) {
      throw new IllegalStateException("Form should contain an existing entity: " + form);
    }

    // due to the conversion applied we already know the person exists and therefore is valid
    return true;
  }

}
