package de.naju.adebar.model.events;

import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.Participant;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import de.naju.adebar.model.events.rooms.scheduling.RegisteredParticipants;
import de.naju.adebar.model.events.rooms.scheduling.greedy.GreedyParticipantListValidator;
import de.naju.adebar.model.persons.Person;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultParticipationManager implements ParticipationManager {

  private final GreedyParticipantListValidator validator;

  public DefaultParticipationManager(GreedyParticipantListValidator validator) {
    this.validator = validator;
  }

  @Override
  public Result addParticipant(Event event, Person participant) {
    return addParticipant(event, participant, new RegistrationInfo(), false);
  }

  @Override
  public Result addParticipant(Event event, Person participant, boolean ignoreAgeRestriction) {
    return addParticipant(event, participant, new RegistrationInfo(), ignoreAgeRestriction);
  }

  @Override
  public Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo) {
    return addParticipant(event, participant, registrationInfo, false);
  }

  @Override
  public Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo,
      boolean ignoreAgeRestrictions) {

    if (!participant.isParticipant()) {
      participant.makeParticipant();
    }

    if (!ignoreAgeRestrictions //
        && event.getParticipationInfo().hasMinimumParticipantAge() //
        && !satisfiesMinimumParticipantAge(participant, event)) {
      return Result.TOO_YOUNG;
    }

    // if the event does not support flexible participation times, or no times are given
    // the participant is assumed to attend the event for the whole duration
    ParticipationTime additionalTime =
        registrationInfo.hasParticipationTime() //
            && event.getParticipationInfo().hasFlexibleParticipationTimesEnabled() //
            ? registrationInfo.getParticipationTime() //
            : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    if (!mayAccommodateAdditionalPerson(event, participant, registrationInfo, false, false)) {
      return Result.BOOKED_OUT;
    }

    registrationInfo.setParticipationTime(additionalTime);
    event.getParticipantsList().addParticipant(participant, registrationInfo);
    event.getParticipantsList().setBookedOut(isBookedOut(event));

    return Result.OK;
  }

  @Override
  public Result addCounselor(Event event, Person counselor) {
    return addCounselor(event, counselor, new CounselorInfo());
  }

  @Override
  public Result addCounselor(Event event, Person counselor, CounselorInfo registrationInfo) {
    ParticipationTime additionalTime = registrationInfo.hasParticipationTime() //
        ? registrationInfo.getParticipationTime() //
        : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    if (!counselor.isActivist()) {
      counselor.makeActivist();
    }

    if (!mayAccommodateAdditionalPerson(event, counselor, registrationInfo, false, true)) {
      event.getParticipantsList().setBookedOut(isBookedOut(event));
      return Result.BOOKED_OUT;
    }

    registrationInfo.setParticipationTime(additionalTime);
    event.getOrganizationInfo().addCounselor(counselor, registrationInfo);
    event.getParticipantsList().setBookedOut(isBookedOut(event));

    return Result.OK;
  }

  @Override
  public Result updateParticipation(Event event, Person participant, RegistrationInfo newInfo) {
    ParticipationTime adjustedParticipationTime =
        newInfo.hasParticipationTime() //
            && event.getParticipationInfo().hasFlexibleParticipationTimesEnabled() //
            ? newInfo.getParticipationTime() //
            : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    // retain the original registration date
    newInfo.setRegistrationDate(
        event.getParticipantsList().getParticipationDetailsFor(participant).getRegistrationDate());

    newInfo.setParticipationTime(adjustedParticipationTime);

    if (mayAccommodateAdditionalPerson(event, participant, newInfo, true, false)) {
      event.getParticipantsList().setBookedOut(isBookedOut(event));
      event.getParticipantsList().updateParticipant(participant, newInfo);
      return Result.OK;
    }

    event.getParticipantsList().setBookedOut(isBookedOut(event));

    return Result.BOOKED_OUT;
  }

  @Override
  public Result updateCounselor(Event event, Person counselor, CounselorInfo newInfo) {
    ParticipationTime adjustedParticipationTime =
        newInfo.hasParticipationTime() //
            && event.getParticipationInfo().hasFlexibleParticipationTimesEnabled() //
            ? newInfo.getParticipationTime() //
            : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    newInfo.setParticipationTime(adjustedParticipationTime);
    if (mayAccommodateAdditionalPerson(event, counselor, newInfo, true, true)) {
      event.getParticipantsList().setBookedOut(isBookedOut(event));
      event.getOrganizationInfo().updateCounselor(counselor, newInfo);
      return Result.OK;
    }

    event.getParticipantsList().setBookedOut(isBookedOut(event));

    return Result.BOOKED_OUT;
  }

  @Override
  public void removeParticipant(Event event, Person participant) {
    event.getParticipantsList().removeParticipant(participant);
    event.getParticipantsList().setBookedOut(false);
  }

  @Override
  public void removeCounselor(Event event, Person counselor) {
    event.getOrganizationInfo().removeCounselor(counselor);

    if (event.getParticipantsList().hasAccommodationInfo() //
        && !event.getParticipantsList().getAccommodation().hasExtraSpaceForCounselors()) {
      event.getParticipantsList().setBookedOut(false);
    }
  }

  @Override
  public Result movePersonFromWaitingListToParticipants(Event event, Person person) {
    return movePersonFromWaitingListToParticipants(event, person, new RegistrationInfo());
  }

  @Override
  public Result movePersonFromWaitingListToParticipants(Event event, Person person,
      RegistrationInfo registrationInfo) {
    Result result = addParticipant(event, person, registrationInfo);

    if (result == Result.OK) {
      event.getParticipantsList().removeFromWaitingList(person);
    }

    return result;
  }

  private boolean mayAccommodateAdditionalPerson(Event event, Person participant,
      DynamicParticipationTime registrationInfo, boolean isUpdate, boolean isCounselor) {
    ParticipantsList pl = event.getParticipantsList();

    if (isCounselor && !pl.hasAccommodationInfo()) {
      return true;
    } else if (isCounselor && pl.hasAccommodationInfo() //
        && pl.getAccommodation().hasExtraSpaceForCounselors()) {
      return true;
    }

    if (pl.hasAccommodationInfo()) {
      return validateWithScheduler(event, participant, registrationInfo, isUpdate);
    } else if (pl.hasParticipantsLimit() && !isUpdate) {
      return validateWithParticipantsLimit(event, participant, registrationInfo);
    }

    return true;
  }

  private boolean validateWithScheduler(Event event, Person participant,
      DynamicParticipationTime registrationInfo, boolean isUpdate) {
    ParticipantsList pl = event.getParticipantsList();
    OrganizationInfo oi = event.getOrganizationInfo();
    ParticipationTime additionalTime = registrationInfo.hasParticipationTime() //
        ? registrationInfo.getParticipationTime() //
        : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    List<Participant> participants = new ArrayList<>(pl.getParticipantsList().size());
    for (Person p : pl.getParticipantsList()) {
      if (isUpdate && p.equals(participant)) {
        // if we are trying an update, do not add the updated participant here as it will be added
        // with the new registration info at the end anyways
        continue;
      }

      ParticipationTime pTime = pl.getParticipationDetailsFor(p).getParticipationTime();
      participants.add(new Participant(p, pTime));
    }

    if (!pl.getAccommodation().hasExtraSpaceForCounselors()) {
      for (Person c : oi.getCounselors().keySet()) {
        ParticipationTime pTime = oi.getCounselors().get(c).getParticipationTime();
        participants.add(new Participant(c, pTime));
      }
    }

    participants.add(new Participant(participant, additionalTime));

    RegisteredParticipants registeredParticipants = RegisteredParticipants.of(participants);

    return validator.isSchedulableWithExtendedSpec(pl.getAccommodation(), registeredParticipants);
  }

  private boolean validateWithParticipantsLimit(Event event, Person participant,
      DynamicParticipationTime registrationInfo) {
    ParticipantsList pl = event.getParticipantsList();
    return !Capacity.of(pl.getParticipantsList().size() + 1)
        .isLargerThan(pl.getParticipantsLimit());
  }

  private boolean isBookedOut(Event event) {
    ParticipantsList pl = event.getParticipantsList();
    if (pl.hasAccommodationInfo()) {
      return validator.isBookedOut();
    } else if (pl.hasParticipantsLimit()) {
      return !pl.getParticipantsLimit().isLargerThan(Capacity.of(pl.getParticipantsList().size()));
    } else {
      return false;
    }
  }

  private boolean satisfiesMinimumParticipantAge(Person participant, Event event) {
    if (!participant.getParticipantProfile().hasDateOfBirth()) {
      return false;
    }

    return !Age.forDateOfBirth(participant.getParticipantProfile().getDateOfBirth())
        .isYoungerThan(event.getParticipationInfo().getMinimumParticipantAge());
  }

}
