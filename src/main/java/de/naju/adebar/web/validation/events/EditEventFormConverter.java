package de.naju.adebar.web.validation.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.core.AddressFormConverter;
import de.naju.adebar.web.validation.events.EditEventForm.Belonging;
import de.naju.adebar.web.validation.events.participation.ParticipationInfoForm;
import de.naju.adebar.web.validation.events.participation.ParticipationInfoFormConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditEventFormConverter implements ValidatingEntityFormConverter<Event, EditEventForm> {

  private final EventFormValidator eventFormValidator;
  private final ParticipationInfoFormConverter participationInfoFormConverter;
  private final AddressFormConverter addressFormConverter;

  public EditEventFormConverter(EventFormValidator eventFormValidator,
      ParticipationInfoFormConverter participationInfoFormConverter,
      AddressFormConverter addressFormConverter) {

    Assert2.noNullArguments("No argument may be null", eventFormValidator,
        participationInfoFormConverter, addressFormConverter);
    this.eventFormValidator = eventFormValidator;
    this.participationInfoFormConverter = participationInfoFormConverter;
    this.addressFormConverter = addressFormConverter;
  }

  @Override
  public boolean isValid(EditEventForm form) {
    return eventFormValidator.nameIsValid(form) //
        && eventFormValidator.startTimeAndEndTimeAreValid(form) //
        && eventFormValidator.belongingIsValid(form) //
        && participationInfoFormConverter.isValid(form.getParticipationInfo()) //
        && addressFormConverter.isValid(form.getPlace());
  }

  @Override
  public Event toEntity(EditEventForm form) {
    throw new UnsupportedOperationException(
        "An AddActivistForm may only be applied to existing persons");
  }

  @Override
  public EditEventForm toForm(Event entity) {
    LocalDate startDate = entity.getStartTime().toLocalDate();
    LocalDate endDate = entity.getEndTime().toLocalDate();
    LocalTime startTime = null;
    LocalTime endTime = null;
    boolean usesTime = usesTime(entity);

    if (usesTime) {
      startTime = entity.getStartTime().toLocalTime();
      endTime = entity.getEndTime().toLocalTime();
    }

    ParticipationInfoForm participationInfoForm = new ParticipationInfoForm(
        entity.getParticipantsList().getParticipantsLimit(),
        entity.getParticipationInfo().getMinimumParticipantAge(),
        entity.getParticipationInfo().getInternalParticipationFee(),
        entity.getParticipationInfo().getExternalParticipationFee(),
        entity.getParticipationInfo().getArrivalOptions());

    AddressForm addressForm = new AddressForm(entity.getPlace());

    Belonging belonging = Belonging.getBelongingFor(entity);
    long localGroupId = -1;
    long projectId = -1;

    if (belonging != null) {
      switch (belonging) {
        case LOCAL_GROUP:
          localGroupId = entity.getLocalGroup().getId();
          break;
        case PROJECT:
          projectId = entity.getProject().getId();
          break;
      }
    }

    return new EditEventForm( //
        entity.getName(), //
        startDate, endDate, //
        usesTime, startTime, endTime, //
        addressForm, //
        participationInfoForm, //
        belonging, localGroupId, projectId);
  }

  @Override
  public void applyFormToEntity(EditEventForm form, Event entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    entity.updateName(form.getName());

    LocalTime startTime = form.isUseEventTime() ? form.getStartTime() : LocalTime.MIDNIGHT;
    LocalTime endTime = form.isUseEventTime() ? form.getEndTime() : LocalTime.MIDNIGHT;
    entity.updateStartTimeAndEndTime(LocalDateTime.of(form.getStartDate(), startTime),
        LocalDateTime.of(form.getEndDate(), endTime));
    entity.updatePlace(addressFormConverter.toEntity(form.getPlace()));

    participationInfoFormConverter.applyFormToEntity(
        form.getParticipationInfo(), entity.getParticipationInfo());

    entity.getParticipantsList()
        .updateParticipantsLimit(form.getParticipationInfo().getParticipantsLimit());
  }

  @Override
  public boolean supports(@Nonnull Class<?> clazz) {
    return EditEventForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @Nonnull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for instances of " + target.getClass());
    }

    EditEventForm form = (EditEventForm) target;

    eventFormValidator.doValidate(form, errors);
  }

  private boolean usesTime(Event event) {
    LocalTime defaultTime = LocalTime.MIDNIGHT;

    return !event.getStartTime().toLocalTime().equals(defaultTime) //
        || !event.getEndTime().toLocalTime().equals(defaultTime);
  }

}
