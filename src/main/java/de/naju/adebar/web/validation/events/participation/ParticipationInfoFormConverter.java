package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.events.ArrivalOption;
import de.naju.adebar.model.events.EventFactory.EventBuilder;
import de.naju.adebar.model.events.ParticipationInfo;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import java.util.HashSet;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class ParticipationInfoFormConverter implements
    ValidatingEntityFormConverter<EventBuilder, ParticipationInfoForm> {

  @Override
  public boolean isValid(ParticipationInfoForm form) {
    for (ArrivalOption arrivalOpt : form.getArrivalOptions()) {
      if (arrivalOpt == null) {
        return false;
      }
    }

    // no arrival option should be duplicated
    return form.getArrivalOptions().size() == new HashSet<>(form.getArrivalOptions()).size();
  }

  @Override
  public EventBuilder toEntity(ParticipationInfoForm form) {
    throw new UnsupportedOperationException(
        "An ParticipationInfoFormConvert may not be converted to a builder");
  }

  @Override
  public ParticipationInfoForm toForm(EventBuilder entity) {
    throw new UnsupportedOperationException(
        "An ParticipationInfoFormConvert may only be applied to existing builders");
  }

  @Override
  public void applyFormToEntity(ParticipationInfoForm form, EventBuilder entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    entity.specifyParticipationInfo()
        .specifyArrivalOptions(form.getArrivalOptions())
        .specifyExternalParticipationFee(form.getExtParticipationFee())
        .specifyInternalParticipationFee(form.getIntParticipationFee())
        .specifyMinimumParticipantAge(form.getMinParticipantAge());

    entity.specifyParticipantsList()
        .specifyParticipantsLimit(form.getParticipantsLimit());
  }

  public void applyFormToEntity(ParticipationInfoForm form, ParticipationInfo entity) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    entity.updateArrivalOptions(form.getArrivalOptions())
        .updateInternalParticipationFee(form.getIntParticipationFee())
        .updateExternalParticipationFee(form.getExtParticipationFee())
        .updateMinimumParticipantAge(form.getMinParticipantAge());
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return ParticipationInfoForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @NonNull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for instances of " + target.getClass());
    }

    ParticipationInfoForm form = (ParticipationInfoForm) target;

    List<ArrivalOption> arrivalOptions = form.getArrivalOptions();
    for (int i = 0; i < arrivalOptions.size(); ++i) {
      ArrivalOption currentOpt = arrivalOptions.get(i);
      String field = "arrivalOptions[" + i + "]";
      if (currentOpt == null) {
        errors.rejectValue(field, "field.null");
      } else if (arrivalOptions.indexOf(currentOpt) < i) {
        errors.rejectValue(field, "field.duplicate");
      }
    }
  }

}
