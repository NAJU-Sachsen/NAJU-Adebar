package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.Room;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.web.validation.events.accommodation.RoomType;
import java.util.ArrayList;
import java.util.List;

public class AccommodationForm {

	private List<SimpleRoomForm> rooms = new ArrayList<>();

	private List<Capacity> roomCapacities = new ArrayList<>();

	private List<RoomType> roomTypes = new ArrayList<>();

	private boolean includeCounselors;

	public AccommodationForm() {}

	public List<SimpleRoomForm> getRooms() {
		return rooms;
	}

	public void setRooms(List<SimpleRoomForm> rooms) {
		this.rooms = rooms;
	}

	public List<Capacity> getRoomCapacities() {
		return roomCapacities;
	}

	public void setRoomCapacities(List<Capacity> roomCapacities) {
		this.roomCapacities = roomCapacities;
	}

	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public void setRoomTypes(List<RoomType> roomTypes) {
		this.roomTypes = roomTypes;
	}

	public boolean isIncludeCounselors() {
		return includeCounselors;
	}

	public void setIncludeCounselors(boolean includeCounselors) {
		this.includeCounselors = includeCounselors;
	}

	public void prepareRooms() {
		for (int i = 0; i < roomTypes.size(); ++i) {
			rooms.add(new SimpleRoomForm(roomTypes.get(i), roomCapacities.get(i)));
		}

		rooms.removeIf(SimpleRoomForm::isNullRoom);
		roomTypes.clear();
		roomCapacities.clear();
	}

	@Override
	public String toString() {
		return "AccommodationForm [" + "rooms=" + rooms + ", roomCapacities=" + roomCapacities
				+ ", roomTypes=" + roomTypes + ", includeCounselors=" + includeCounselors + ']';
	}

	public static class SimpleRoomForm {

		private RoomType roomType;
		private Capacity roomCapacity;

		public SimpleRoomForm() {}

		public SimpleRoomForm(RoomType roomType, Capacity roomCapacity) {
			this.roomType = roomType;
			this.roomCapacity = roomCapacity;
		}

		public RoomType getRoomType() {
			return roomType;
		}

		public void setRoomType(RoomType roomType) {
			this.roomType = roomType;
		}

		public Capacity getRoomCapacity() {
			return roomCapacity;
		}

		public void setRoomCapacity(Capacity roomCapacity) {
			this.roomCapacity = roomCapacity;
		}

		public boolean hasCapacity() {
			return roomCapacity != null;
		}

		public boolean hasType() {
			return roomCapacity != null;
		}

		public boolean isNullRoom() {
			return !hasType() && !hasCapacity();
		}

		public Room toRoom() {
			if (!hasCapacity() || !hasType()) {
				throw new IllegalStateException();
			}

			Gender roomGender;
			switch (roomType) {
				case MALE:
					roomGender = Gender.MALE;
					break;
				case FEMALE:
					roomGender = Gender.FEMALE;
					break;
				default:
					roomGender = null;
			}

			return new Room(roomCapacity.getValue(), roomGender);
		}

		@Override
		public String toString() {
			return "SimpleRoomForm [" + "roomType=" + roomType + ", roomCapacity=" + roomCapacity + ']';
		}

	}

}
