package de.naju.adebar.web.validation.persons.participant;

import de.naju.adebar.documentation.DesignSmell;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * Simple service to apply the data from an {@link AddParticipantForm} to {@link ParticipantProfile}
 * instances.
 *
 * @author Rico Bergmann
 */
@Service
public class AddParticipantFormConverter implements
    ValidatingEntityFormConverter<ParticipantProfile, AddParticipantForm> {

  private final EditParticipantFormConverter editParticipantFormConverter;

  /**
   * Full constructor
   *
   * @param editParticipantFormConverter service to handle most of the conversion for us
   */
  public AddParticipantFormConverter(EditParticipantFormConverter editParticipantFormConverter) {
    Assert.notNull(editParticipantFormConverter, "editParticipantFormConverter may not be null");
    this.editParticipantFormConverter = editParticipantFormConverter;
  }

  @Override
  public boolean isValid(AddParticipantForm form) {
    return editParticipantFormConverter.isValid(form);
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return AddParticipantForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    editParticipantFormConverter.validate(target, errors);
  }

  @Override
  public ParticipantProfile toEntity(AddParticipantForm form) {
    throw new UnsupportedOperationException(
        "An AddParticipantForm may only be applied to existing persons");
  }

  @DesignSmell(description =
      "The participation profile knows nothing of the events the participants attends."
          + "This knowledge is contained entirely in the Person class")
  @Override
  public AddParticipantForm toForm(ParticipantProfile entity) {
    return new AddParticipantForm(
        entity.getGender(), // 
        entity.getDateOfBirth(), //
        entity.getEatingHabits(), //
        entity.getEatingHabits(), //
        entity.getNabuMembership(), //
        entity.getRemarks());
  }

  /**
   * Converts an {@link ParticipantProfile} to an {@link AddParticipantForm}, whilst retaining the
   * events the person participated in.
   *
   * @param entity the entity to convert
   * @param participatingEvents the events the participant attends
   * @return the form
   */
  public AddParticipantForm toForm(ParticipantProfile entity, List<Event> participatingEvents) {
    AddParticipantForm form = toForm(entity);
    form.setEvents(participatingEvents);
    return form;
  }

  @Override
  public void applyFormToEntity(AddParticipantForm form, ParticipantProfile entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    editParticipantFormConverter.applyFormToEntity(form, entity);
  }
}
