package de.naju.adebar.web.validation.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventFactory;
import de.naju.adebar.model.events.EventFactory.EventBuilder;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.core.AddressFormConverter;
import de.naju.adebar.web.validation.events.participation.AccommodationFormConverter;
import de.naju.adebar.web.validation.events.participation.ParticipationInfoFormConverter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Service
public class AddEventFormConverter implements ValidatingEntityFormConverter<Event, AddEventForm> {

	private final EventFactory eventFactory;
	private final EventFormValidator eventFormValidator;
	private final AccommodationFormConverter accommodationFormConverter;
	private final ParticipationInfoFormConverter participationInfoFormConverter;
	private final AddressFormConverter addressFormConverter;

	public AddEventFormConverter(EventFactory eventFactory, EventFormValidator eventFormValidator,
			AccommodationFormConverter accommodationFormConverter,
			ParticipationInfoFormConverter participationInfoFormConverter,
			AddressFormConverter addressFormConverter) {
		Assert2.noNullArguments("No argument may be null", eventFactory, eventFormValidator,
				accommodationFormConverter, participationInfoFormConverter, addressFormConverter);
		this.eventFactory = eventFactory;
		this.eventFormValidator = eventFormValidator;
		this.accommodationFormConverter = accommodationFormConverter;
		this.participationInfoFormConverter = participationInfoFormConverter;
		this.addressFormConverter = addressFormConverter;
	}

	@Override
	public boolean isValid(AddEventForm form) {
		if (form.isCustomAccommodation()
				&& !accommodationFormConverter.isValid(form.getAccommodation())) {
			return false;
		}

		return eventFormValidator.nameIsValid(form) //
				&& eventFormValidator.startTimeAndEndTimeAreValid(form) //
				&& eventFormValidator.belongingIsValid(form) //
				&& participationInfoFormConverter.isValid(form.getParticipationInfo()) //
				&& addressFormConverter.isValid(form.getPlace());
	}

	@Override
	public Event toEntity(AddEventForm form) {
		if (!isValid(form)) {
			throw new IllegalArgumentException("Form is invalid: " + form);
		}

		LocalTime startTime, endTime;
		if (form.isUseEventTime()) {
			startTime = form.getStartTime() != null ? form.getStartTime() : LocalTime.of(0, 0);
			endTime = form.getEndTime() != null ? form.getEndTime() : LocalTime.of(0, 0);
		} else {
			startTime = LocalTime.of(0, 0);
			endTime = LocalTime.of(0, 0);
		}
		LocalDateTime start = LocalDateTime.of(form.getStartDate(), startTime);
		LocalDateTime end = LocalDateTime.of(form.getEndDate(), endTime);

		EventBuilder builder = eventFactory.build(form.getName(), start, end);

		builder.specifyPlace(addressFormConverter.toEntity(form.getPlace()));
		participationInfoFormConverter.applyFormToEntity(form.getParticipationInfo(), builder);

		if (form.isCustomAccommodation()) {
			builder.specifyParticipantsList()
					.specifyAccomodation(accommodationFormConverter.toEntity(form.getAccommodation()));

			if (form.isFlexibleParticipationTimes()) {
				builder.specifyParticipationInfo().withFlexibleParticipationTimes();
			}

		}

		return builder.create();
	}

	@Override
	public AddEventForm toForm(Event entity) {
		return null;
	}

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return AddEventForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, @NonNull Errors errors) {
		if (!supports(target.getClass())) {
			throw new IllegalArgumentException(
					"Validation not supported for instances of " + target.getClass());
		}

		AddEventForm form = (AddEventForm) target;

		eventFormValidator.doValidate(form, errors);

		if (form.isCustomAccommodation()) {
			try {
				errors.pushNestedPath("accommodation");
				ValidationUtils.invokeValidator(accommodationFormConverter, form.getAccommodation(),
						errors);
			} finally {
				errors.popNestedPath();
			}
		}

	}

}
