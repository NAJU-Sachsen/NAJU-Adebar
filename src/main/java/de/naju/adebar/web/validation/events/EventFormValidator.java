package de.naju.adebar.web.validation.events;

import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.core.AddressFormConverter;
import de.naju.adebar.web.validation.events.participation.ParticipationInfoFormConverter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Service
class EventFormValidator {

  private final AddressFormConverter addressFormConverter;
  private final ParticipationInfoFormConverter participationInfoFormConverter;

  public EventFormValidator(AddressFormConverter addressFormConverter,
      ParticipationInfoFormConverter participationInfoFormConverter) {
    Assert2.noNullArguments("No argument may be null", addressFormConverter,
        participationInfoFormConverter);
    this.addressFormConverter = addressFormConverter;
    this.participationInfoFormConverter = participationInfoFormConverter;
  }

  void validateStartTimeAndEndTime(EditEventForm form, Errors errors) {
    if (form.getStartDate() == null) {
      errors.rejectValue("startDate", "field.required");
    } else if (form.getEndDate() == null) {
      errors.rejectValue("endDate", "field.required");
    } else if (!startTimeAndEndTimeAreValid(form)) {
      errors.rejectValue("endDate", "date.period.end-before-start");
    }

  }

  void validateBelonging(EditEventForm form, Errors errors) {
    if (form.getBelonging() == null) {
      errors.rejectValue("belonging", "field.required");
    } else if (form.getLocalGroup() == -1 && form.getProject() == -1) {
      errors.rejectValue("belonging", "options.select-one");
    }
  }

  void doValidate(EditEventForm form, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
    validateStartTimeAndEndTime(form, errors);
    validateBelonging(form, errors);

    try {
      errors.pushNestedPath("place");
      ValidationUtils.invokeValidator(addressFormConverter, form.getPlace(), errors);
    } finally {
      errors.popNestedPath();
    }

    try {
      errors.pushNestedPath("participationInfo");
      ValidationUtils
          .invokeValidator(participationInfoFormConverter, form.getParticipationInfo(), errors);
    } finally {
      errors.popNestedPath();
    }
  }

  boolean nameIsValid(EditEventForm eventForm) {
    return eventForm.getName() != null && !eventForm.getName().isEmpty();
  }

  boolean startTimeAndEndTimeAreValid(EditEventForm form) {
    if (form.getStartDate() == null || form.getEndDate() == null) {
      return false;
    } else if (form.isUseEventTime()) {
      LocalTime startTime = form.getStartTime() != null ? form.getStartTime() : LocalTime.of(0, 0);
      LocalTime endTime = form.getEndTime() != null ? form.getEndTime() : LocalTime.of(0, 0);

      LocalDateTime start = LocalDateTime.of(form.getStartDate(), startTime);
      LocalDateTime end = LocalDateTime.of(form.getEndDate(), endTime);

      return !end.isBefore(start);

    }

    return !form.getEndDate().isBefore(form.getStartDate());
  }

  boolean belongingIsValid(EditEventForm form) {
    return form.getBelonging() != null
        && (form.getLocalGroup() != -1 || form.getProject() != -1);
  }

}
