package de.naju.adebar.web.model.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.Gender;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.Assert;

public class AccommodationReport {

  private final Event event;

  public static AccommodationReport generateFor(Event event) {
    return new AccommodationReport(event);
  }

  private AccommodationReport(Event event) {
    Assert.notNull(event, "event may not be null");
    this.event = event;
  }

  public List<LocalDateTime> getEventDays() {
    List<LocalDateTime> eventDays = new ArrayList<>(
        Period.between(
            event.getStartTime().toLocalDate(),
            event.getEndTime().toLocalDate())
            .getDays());

    for (LocalDateTime currDate = event.getStartTime();
        currDate.compareTo(event.getEndTime()) <= 0;
        currDate = currDate.plusDays(1L)) {
      eventDays.add(currDate);
    }

    return eventDays;
  }

  public int getNumberOfDays() {
    return getEventDays().size();
  }

  public int getTotalParticipantsOn(LocalDateTime day) {
    if (!event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      return event.getParticipantsList().getParticipantsCount();
    }

    return event.getParticipantsList().getParticipants().entrySet().stream() //
        .filter(participant -> participant.getValue().getParticipationTime() //
            .participatesDuring(day, event.getStartTime())) //
        .collect(Collectors.toList()).size();
  }

  public int getFemaleParticipantsOn(LocalDateTime day) {
    if (!event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      return event.getParticipantsList().getParticipantsList().stream() //
          .filter(participant -> participant.getParticipantProfile().getGender() == Gender.FEMALE)//
          .collect(Collectors.toList()).size();
    }

    return event.getParticipantsList().getParticipants().entrySet().stream() //
        .filter(participant ->
            participant.getKey().getParticipantProfile().getGender() == Gender.FEMALE) //
        .filter(participant -> participant.getValue().getParticipationTime() //
            .participatesDuring(day, event.getStartTime())) //
        .collect(Collectors.toList()).size();
  }

  public int getMaleParticipantsOn(LocalDateTime day) {
    if (!event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      return event.getParticipantsList().getParticipantsList().stream() //
          .filter(participant -> participant.getParticipantProfile().getGender() == Gender.MALE)//
          .collect(Collectors.toList()).size();
    }

    return event.getParticipantsList().getParticipants().entrySet().stream() //
        .filter(participant ->
            participant.getKey().getParticipantProfile().getGender() == Gender.MALE) //
        .filter(participant -> participant.getValue().getParticipationTime() //
            .participatesDuring(day, event.getStartTime())) //
        .collect(Collectors.toList()).size();
  }

  public int getCounselorsOn(LocalDateTime day) {
    if (!event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      return event.getOrganizationInfo().getCounselors().size();
    }

    return event.getOrganizationInfo().getCounselors().entrySet().stream() //
        .filter(counselor -> counselor.getValue().getParticipationTime() //
            .participatesDuring(day, event.getStartTime())) //
        .collect(Collectors.toList()).size();
  }

  public int getFemaleCounselorsOn(LocalDateTime day) {
    if (!event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      return event.getOrganizationInfo().getCounselorList().stream() //
          .filter(participant -> participant.getParticipantProfile().getGender() == Gender.FEMALE)//
          .collect(Collectors.toList()).size();
    }

    return event.getOrganizationInfo().getCounselors().entrySet().stream() //
        .filter(counselor ->
            counselor.getKey().getParticipantProfile().getGender() == Gender.FEMALE) //
        .filter(counselor -> counselor.getValue().getParticipationTime() //
            .participatesDuring(day, event.getStartTime())) //
        .collect(Collectors.toList()).size();
  }

  public int getMaleCounselorsOn(LocalDateTime day) {
    if (!event.getParticipationInfo().supportsFlexibleParticipationTimes()) {
      return event.getOrganizationInfo().getCounselorList().stream() //
          .filter(participant -> participant.getParticipantProfile().getGender() == Gender.MALE)//
          .collect(Collectors.toList()).size();
    }

    return event.getOrganizationInfo().getCounselors().entrySet().stream() //
        .filter(counselor ->
            counselor.getKey().getParticipantProfile().getGender() == Gender.MALE) //
        .filter(counselor -> counselor.getValue().getParticipationTime() //
            .participatesDuring(day, event.getStartTime())) //
        .collect(Collectors.toList()).size();
  }

  public int getTotalAttendeesOn(LocalDateTime day) {
    return getTotalParticipantsOn(day) + getCounselorsOn(day);
  }

  public int getTotalFemaleAttendeesOn(LocalDateTime day) {
    return getFemaleParticipantsOn(day) + getFemaleCounselorsOn(day);
  }

  public int getTotalMaleAttendeesOn(LocalDateTime day) {
    return getMaleParticipantsOn(day) + getMaleCounselorsOn(day);
  }

  public int getTotalAvailableOn(LocalDateTime day) {
    int requiredBeds;
    int availableBeds = 0;

    if (event.getParticipantsList().hasAccommodationInfo() && event.getParticipantsList()
        .getAccommodation().hasExtraSpaceForCounselors()) {
      requiredBeds = getTotalParticipantsOn(day);
    } else {
      requiredBeds = getTotalAttendeesOn(day);
    }

    if (event.getParticipantsList().hasAccommodationInfo()) {
      availableBeds = event.getParticipantsList().getAccommodation().getTotalBeds();
    } else if (event.getParticipantsList().hasParticipantsLimit()) {
      availableBeds = event.getParticipantsList().getParticipantsLimit().getValue();
    }

    return availableBeds - requiredBeds;
  }

  public int getTotalAvailableForFemaleOn(LocalDateTime day) {
    int requiredBeds;
    int availableBeds = 0;

    if (event.getParticipantsList().hasAccommodationInfo() && event.getParticipantsList()
        .getAccommodation().hasExtraSpaceForCounselors()) {
      requiredBeds = getFemaleParticipantsOn(day);
    } else {
      requiredBeds = getTotalFemaleAttendeesOn(day);
    }

    if (event.getParticipantsList().hasAccommodationInfo()) {
      availableBeds = event.getParticipantsList().getAccommodation().getTotalBedsFor(Gender.FEMALE);
    } else if (event.getParticipantsList().hasParticipantsLimit()) {
      availableBeds = event.getParticipantsList().getParticipantsLimit().getValue();
    }

    return availableBeds - requiredBeds;
  }

  public int getTotalAvailableForMaleOn(LocalDateTime day) {
    int requiredBeds;
    int availableBeds = 0;

    if (event.getParticipantsList().hasAccommodationInfo() && event.getParticipantsList()
        .getAccommodation().hasExtraSpaceForCounselors()) {
      requiredBeds = getMaleParticipantsOn(day);
    } else {
      requiredBeds = getTotalMaleAttendeesOn(day);
    }

    if (event.getParticipantsList().hasAccommodationInfo()) {
      availableBeds = event.getParticipantsList().getAccommodation().getTotalBedsFor(Gender.MALE);
    } else if (event.getParticipantsList().hasParticipantsLimit()) {
      availableBeds = event.getParticipantsList().getParticipantsLimit().getValue();
    }

    return availableBeds - requiredBeds;
  }

  public boolean personParticipatesOn(LocalDateTime day, Person participant) {
    if (event.getParticipantsList().isParticipant(participant)) {
      return event.getParticipantsList() //
          .getParticipationDetailsFor(participant) //
          .getParticipationTime() //
          .participatesDuring(day, event.getStartTime());
    } else if (event.getOrganizationInfo().isCounselor(participant)) {
      return event.getOrganizationInfo() //
          .getCounselorInfoFor(participant) //
          .getParticipationTime() //
          .participatesDuring(day, event.getStartTime());
    }
    throw new IllegalArgumentException(
        "Person is neither a participant nor a counselor: " + participant);
  }

}
