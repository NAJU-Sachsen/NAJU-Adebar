package de.naju.adebar.web.validation.events.accommodation;

import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.util.validation.ErrorUtils;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditAccommodationFormConverter
		implements ValidatingEntityFormConverter<ExtendedRoomSpecification, EditAccommodationForm> {

	@Override
	public boolean isValid(EditAccommodationForm form) {
		return form.getRoomCapacities().stream().allMatch(Objects::nonNull) //
				&& form.getRoomTypes().stream().allMatch(Objects::nonNull) //
				&& form.getRoomTypes().size() == form.getRoomCapacities().size();
	}

	@Override
	public ExtendedRoomSpecification toEntity(EditAccommodationForm form) {
		if (form == null) {
			return null;
		} else if (!isValid(form)) {
			throw new IllegalArgumentException("Form is invalid: " + form);
		}

		ExtendedRoomSpecification roomSpecification = new ExtendedRoomSpecification();

		List<Capacity> roomCapacities = form.getRoomCapacities();
		List<RoomType> roomTypes = form.getRoomTypes();
		for (int i = 0; i < roomCapacities.size(); ++i) {
			switch (roomTypes.get(i)) {
				case FEMALE:
					roomSpecification.addRoom(roomCapacities.get(i).getValue(), Gender.FEMALE);
					break;
				case MALE:
					roomSpecification.addRoom(roomCapacities.get(i).getValue(), Gender.MALE);
					break;
				case FLEX:
					roomSpecification.addFlexRoom(roomCapacities.get(i).getValue());
					break;
				case FALLBACK:
					roomSpecification.addFallbackRoom(roomCapacities.get(i).getValue());
					break;
			}

		}

		if (form.isCounselorsAreSeparate()) {
			roomSpecification.withExtraSpaceForCounselors();
		}

		return roomSpecification;
	}

	@Override
	public EditAccommodationForm toForm(ExtendedRoomSpecification entity) {
		if (entity == null) {
			return null;
		}
		return new EditAccommodationForm(entity);
	}

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return EditAccommodationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, @NonNull Errors errors) {
		if (!supports(target.getClass())) {
			throw new IllegalArgumentException(
					"Validation not supported for instances of class " + target.getClass());
		}

		EditAccommodationForm form = (EditAccommodationForm) target;

		List<RoomType> roomTypes = form.getRoomTypes();
		List<Capacity> roomCapacities = form.getRoomCapacities();
		if (roomCapacities.size() < roomTypes.size()) {
			for (int i = roomCapacities.size(); i < roomTypes.size(); ++i) {
				ErrorUtils.rejectIndexedKey("roomTypes", i, "room.type.no-corresponding-capacity", errors);
			}
		} else if (roomTypes.size() < roomCapacities.size()) {
			for (int i = roomTypes.size(); i < roomCapacities.size(); ++i) {
				ErrorUtils.rejectIndexedKey("roomCapacities", i, //
						"room.capacity.no-corresponding-type", errors);
			}
		}

		for (int i = 0; i < roomCapacities.size(); ++i) {
			if (roomCapacities.get(i) == null) {
				ErrorUtils.rejectIndexedKey("roomCapacities", i, "field.required", errors);
			}
		}

		for (int i = 0; i < roomTypes.size(); ++i) {
			if (roomTypes.get(i) == null) {
				ErrorUtils.rejectIndexedKey("roomTypes", i, "field.required", errors);
			}
		}

	}
}
