package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.web.validation.ValidatingEntityFormConverter;
import de.naju.adebar.web.validation.events.accommodation.RoomType;
import de.naju.adebar.web.validation.events.participation.AccommodationForm.SimpleRoomForm;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class AccommodationFormConverter
		implements ValidatingEntityFormConverter<ExtendedRoomSpecification, AccommodationForm> {

	@Override
	public boolean isValid(AccommodationForm form) {

		for (SimpleRoomForm srf : form.getRooms()) {
			if (srf == null) {
				return false;
			} else if (!srf.hasType() || !srf.hasCapacity()) {
				return false;
			}
		}

		return form.getRoomCapacities().stream().allMatch(Objects::nonNull) //
				&& form.getRoomTypes().stream().allMatch(Objects::nonNull) //
				&& form.getRoomCapacities().size() == form.getRoomTypes().size();
	}

	@Override
	public ExtendedRoomSpecification toEntity(AccommodationForm form) {
		if (!isValid(form)) {
			throw new IllegalArgumentException("Form is invalid: " + form);
		}

		ExtendedRoomSpecification roomSpec = new ExtendedRoomSpecification();

		for (SimpleRoomForm srf : form.getRooms()) {
			switch (srf.getRoomType()) {
				case FLEX:
					roomSpec.addFlexRoom(srf.getRoomCapacity().getValue());
					break;
				case FALLBACK:
					roomSpec.addFallbackRoom(srf.getRoomCapacity().getValue());
					break;
				default:
					roomSpec.addRoom(srf.toRoom());
			}
		}

		List<RoomType> roomTypes = form.getRoomTypes();
		List<Capacity> capacities = form.getRoomCapacities();
		for (int i = 0; i < roomTypes.size(); ++i) {
			switch (roomTypes.get(i)) {
				case FEMALE:
					roomSpec.addRoom(capacities.get(i).getValue(), Gender.FEMALE);
					break;
				case MALE:
					roomSpec.addRoom(capacities.get(i).getValue(), Gender.MALE);
					break;
				case FLEX:
					roomSpec.addFlexRoom(capacities.get(i).getValue());
					break;
				case FALLBACK:
					roomSpec.addFallbackRoom(capacities.get(i).getValue());
					break;
				default:
					throw new AssertionError(roomTypes.get(i));
			}
		}

		if (!form.isIncludeCounselors()) {
			roomSpec = roomSpec.withExtraSpaceForCounselors();
		}

		return roomSpec;
	}

	@Override
	public AccommodationForm toForm(ExtendedRoomSpecification entity) {
		AccommodationForm form = new AccommodationForm();
		List<SimpleRoomForm> rooms = form.getRooms();

		entity.getRooms()
				.forEach(room -> rooms.add(new SimpleRoomForm(RoomType.fromGender(room.getGender()),
						Capacity.of(room.getBedsCount()))));

		entity.getFlexRooms().forEach(
				room -> rooms.add(new SimpleRoomForm(RoomType.FLEX, Capacity.of(room.getBedsCount()))));

		entity.getFallbackRooms().forEach(
				room -> rooms.add(new SimpleRoomForm(RoomType.FALLBACK, Capacity.of(room.getBedsCount()))));

		return form;
	}

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return AccommodationForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, @NonNull Errors errors) {
		if (!supports(target.getClass())) {
			throw new IllegalArgumentException(
					"Validation not supported for instances of " + target.getClass());
		}

		AccommodationForm form = (AccommodationForm) target;

		for (int i = 0; i < form.getRooms().size(); ++i) {
			SimpleRoomForm srf = form.getRooms().get(i);
			String field = "rooms[" + i + "]";
			if (srf == null) {
				errors.rejectValue(field, "field.null");
			} else if (!srf.hasCapacity()) {
				errors.rejectValue(field, "room.capacity.unset");
			} else if (!srf.hasType()) {
				errors.rejectValue(field, "room.type.unset");
			}
		}

		List<RoomType> roomTypes = form.getRoomTypes();
		List<Capacity> roomCapacities = form.getRoomCapacities();
		if (roomTypes.size() < roomCapacities.size()) {
			for (int i = roomTypes.size(); i < roomCapacities.size(); ++i) {
				errors.rejectValue("roomCapacities[" + i + "]", "room.capacity.no-corresponding-type");
			}
		} else if (roomCapacities.size() < roomTypes.size()) {
			for (int i = roomCapacities.size(); i < roomTypes.size(); ++i) {
				errors.rejectValue("roomTypes[" + i + "]", "room.type.no-corresponding-capacity");
			}
		}

	}
}
