package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.events.Reservation;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import javax.annotation.Nonnull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Service
public class ReservationFormConverter implements
    ValidatingEntityFormConverter<Reservation, ReservationForm> {

  @Override
  public boolean isValid(ReservationForm form) {
    if (form == null) {
      return false;
    }

    return form.getDescription() != null && !form.getDescription().isEmpty()
        || form.getSlots() != null;
  }

  @Override
  public Reservation toEntity(ReservationForm form) {
    if (!isValid(form)) {
      throw new IllegalArgumentException("Form is invalid: " + form);
    }

    return form.toReservation();
  }

  @Override
  public ReservationForm toForm(Reservation entity) {
    if (entity == null) {
      return null;
    }
    return new ReservationForm(entity);
  }

  @Override
  public boolean supports(@NonNull Class<?> clazz) {
    return ReservationForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, @Nonnull Errors errors) {
    if (!supports(target.getClass())) {
      throw new IllegalArgumentException(
          "Validation not supported for instances of " + target.getClass());
    }

    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "field.required");

    ReservationForm form = (ReservationForm) target;

    if (form.getSlots() == null) {
      errors.rejectValue("slots", "field.required");
    }

  }
}
