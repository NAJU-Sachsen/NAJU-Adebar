package de.naju.adebar.web.validation.events.accommodation;

import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.events.rooms.scheduling.Room;
import java.util.ArrayList;
import java.util.List;

public class EditAccommodationForm {

  private List<Capacity> roomCapacities = new ArrayList<>();
  private List<RoomType> roomTypes = new ArrayList<>();
  private boolean flexibleParticipationTimes;
  private boolean counselorsAreSeparate;

  public EditAccommodationForm() {}

  public EditAccommodationForm(Event event) {
    if (event != null && event.getParticipantsList().hasAccommodationInfo()) {
      ExtendedRoomSpecification roomSpec = event.getParticipantsList().getAccommodation();

      for (Room room : roomSpec.getRooms()) {
        roomCapacities.add(Capacity.of(room.getBedsCount()));
        roomTypes.add(RoomType.fromGender(room.getGender()));
      }

      for (Room flexRoom : roomSpec.getFlexRooms()) {
        roomCapacities.add(Capacity.of(flexRoom.getBedsCount()));
        roomTypes.add(RoomType.FLEX);
      }

      for (Room fallbackRoom : roomSpec.getFallbackRooms()) {
        roomCapacities.add(Capacity.of(fallbackRoom.getBedsCount()));
        roomTypes.add(RoomType.FALLBACK);
      }

      counselorsAreSeparate = !roomSpec.hasExtraSpaceForCounselors();

      flexibleParticipationTimes = event.getParticipationInfo()
          .supportsFlexibleParticipationTimes();

    }

  }

  public EditAccommodationForm(ExtendedRoomSpecification roomSpec) {
    for (Room room : roomSpec.getRooms()) {
      roomCapacities.add(Capacity.of(room.getBedsCount()));
      roomTypes.add(RoomType.fromGender(room.getGender()));
    }

    for (Room flexRoom : roomSpec.getFlexRooms()) {
      roomCapacities.add(Capacity.of(flexRoom.getBedsCount()));
      roomTypes.add(RoomType.FLEX);
    }

    for (Room fallbackRoom : roomSpec.getFallbackRooms()) {
      roomCapacities.add(Capacity.of(fallbackRoom.getBedsCount()));
      roomTypes.add(RoomType.FALLBACK);
    }

    counselorsAreSeparate = !roomSpec.hasExtraSpaceForCounselors();
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

  public void setRoomTypes(
      List<RoomType> roomTypes) {
    this.roomTypes = roomTypes;
  }

  public boolean isFlexibleParticipationTimes() {
    return flexibleParticipationTimes;
  }

  public void setFlexibleParticipationTimes(boolean flexibleParticipationTimes) {
    this.flexibleParticipationTimes = flexibleParticipationTimes;
  }

  public boolean isCounselorsAreSeparate() {
    return counselorsAreSeparate;
  }

  public void setCounselorsAreSeparate(boolean counselorsAreSeparate) {
    this.counselorsAreSeparate = counselorsAreSeparate;
  }

  @Override
  public String toString() {
    return "EditAccommodationForm [" +
        "roomCapacities=" + roomCapacities +
        ", roomTypes=" + roomTypes +
        ", flexibleParticipationTimes=" + flexibleParticipationTimes +
        ", counselorsAreSeparate=" + counselorsAreSeparate +
        ']';
  }

}
