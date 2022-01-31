package de.naju.adebar.model.events;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.Participant;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import de.naju.adebar.model.events.rooms.scheduling.RegisteredParticipants;
import de.naju.adebar.model.events.rooms.scheduling.greedy.GreedyParticipantListValidator;
import de.naju.adebar.model.persons.Person;

/**
 * A {@link ParticipationManager} which operates on a database.
 *
 * @author Rico Bergmann
 */
@Service
@Transactional
public class DefaultParticipationManager implements ParticipationManager {

  private final GreedyParticipantListValidator validator;

  public DefaultParticipationManager(GreedyParticipantListValidator validator) {
    this.validator = validator;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#addParticipant(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person)
   */
  @Override
  public Result addParticipant(Event event, Person participant) {
    return addParticipant(event, participant, new RegistrationInfo(), false);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#addParticipant(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person, boolean)
   */
  @Override
  public Result addParticipant(Event event, Person participant, boolean ignoreAgeRestriction) {
    return addParticipant(event, participant, new RegistrationInfo(), ignoreAgeRestriction);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#addParticipant(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person, de.naju.adebar.model.events.RegistrationInfo)
   */
  @Override
  public Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo) {
    return addParticipant(event, participant, registrationInfo, false);
  }

  @Override
  public Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo,
      boolean ignoreAgeRestrictions) {
    event.assertNotCanceled();

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
    ParticipationTime additionalTime = registrationInfo.hasParticipationTime() //
        && event.getParticipationInfo().hasFlexibleParticipationTimesEnabled() //
            ? registrationInfo.getParticipationTime() //
            : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    if (!mayAccommodateAdditionalPerson(event, participant, registrationInfo, false, false)) {
      return Result.BOOKED_OUT;
    }

    registrationInfo.setParticipationTime(additionalTime);

    // DISCUSS do we actually want this? What should happen, if a fee is introduced later on?
    // Is it actually possible to introduce a fee later on?
    // if no participation fee is set, mark the corresponding field in the registration info as
    // "payed"
    // @formatter:off
    /*if (!event.getParticipationInfo().hasParticipationFee()) {
      registrationInfo.setParticipationFeePayed(true);
    } else if (participant.getParticipantProfile().isNabuMember()
        && !event.getParticipationInfo().hasInternalParticipationFee()) {
      registrationInfo.setParticipationFeePayed(true);
    } else if (!participant.getParticipantProfile().isNabuMember()
        && !event.getParticipationInfo().hasExternalParticipationFee()) {
      registrationInfo.setParticipationFeePayed(true);
    }*/
    // @formatter:on

    event.getParticipantsList().addParticipant(participant, registrationInfo);
    event.getParticipantsList().internal_setBookedOut(isBookedOut(event));

    return Result.OK;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.ParticipationManager#addCounselor(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person)
   */
  @Override
  public Result addCounselor(Event event, Person counselor) {
    return addCounselor(event, counselor, new CounselorInfo());
  }

  @Override
  public Result addCounselor(Event event, Person counselor, CounselorInfo registrationInfo) {
    event.assertNotCanceled();

    if (event.getParticipantsList().hasAccommodationInfo()) {
      // just for safety measures we will invoke the scheduler here (s.t. there is an initial
      // configuration available)
      boolean scheduleTest = validator.isSchedulableWithExtendedSpec( //
          event.getParticipantsList().getAccommodation(), //
          RegisteredParticipants.of(event.getParticipantsList() //
              .getParticipants().entrySet().stream() //
              .map(participationEntry -> new Participant(participationEntry.getKey(), //
                  participationEntry.getValue().getParticipationTime())) //
              .collect(Collectors.toList()) //
          ));
      boolean extraSpaceForCounselors =
          event.getParticipantsList().getAccommodation().hasExtraSpaceForCounselors();
      Assert.isTrue(scheduleTest && !extraSpaceForCounselors,
          "Apparently the event is already booked out from the get go.");
    }


    ParticipationTime additionalTime = registrationInfo.hasParticipationTime() //
        ? registrationInfo.getParticipationTime() //
        : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    if (!counselor.isActivist()) {
      counselor.makeActivist();
    }

    if (mayAccommodateAdditionalPerson(event, counselor, registrationInfo, false, true)) {
      event.getParticipantsList().internal_setBookedOut(isBookedOut(event));
      return Result.BOOKED_OUT;
    }

    registrationInfo.setParticipationTime(additionalTime);
    event.getOrganizationInfo().addCounselor(counselor, registrationInfo);
    event.getParticipantsList().internal_setBookedOut(isBookedOut(event));

    return Result.OK;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.ParticipationManager#updateParticipation(de.naju.adebar.model.
   * events.Event, de.naju.adebar.model.persons.Person,
   * de.naju.adebar.model.events.RegistrationInfo)
   */
  @Override
  public Result updateParticipation(Event event, Person participant, RegistrationInfo newInfo) {
    event.assertNotCanceled();

    ParticipationTime adjustedParticipationTime = newInfo.hasParticipationTime() //
        && event.getParticipationInfo().hasFlexibleParticipationTimesEnabled() //
            ? newInfo.getParticipationTime() //
            : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    // retain the original registration date
    newInfo.setRegistrationDate(
        event.getParticipantsList().getParticipationDetailsFor(participant).getRegistrationDate());

    newInfo.setParticipationTime(adjustedParticipationTime);

    if (mayAccommodateAdditionalPerson(event, participant, newInfo, true, false)) {
      event.getParticipantsList().internal_setBookedOut(isBookedOut(event));
      event.getParticipantsList().updateParticipant(participant, newInfo);
      return Result.OK;
    }

    event.getParticipantsList().internal_setBookedOut(isBookedOut(event));

    return Result.BOOKED_OUT;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#updateCounselor(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person, de.naju.adebar.model.events.CounselorInfo)
   */
  @Override
  public Result updateCounselor(Event event, Person counselor, CounselorInfo newInfo) {
    event.assertNotCanceled();

    ParticipationTime adjustedParticipationTime = newInfo.hasParticipationTime() //
        && event.getParticipationInfo().hasFlexibleParticipationTimesEnabled() //
            ? newInfo.getParticipationTime() //
            : new ParticipationTime(event.getStartTime(), event.getEndTime(), event.getStartTime());

    newInfo.setParticipationTime(adjustedParticipationTime);
    if (mayAccommodateAdditionalPerson(event, counselor, newInfo, true, true)) {
      event.getParticipantsList().internal_setBookedOut(isBookedOut(event));
      event.getOrganizationInfo().updateCounselor(counselor, newInfo);
      return Result.OK;
    }

    event.getParticipantsList().internal_setBookedOut(isBookedOut(event));

    return Result.BOOKED_OUT;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#removeParticipant(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person)
   */
  @Override
  public void removeParticipant(Event event, Person participant) {
    event.assertNotCanceled();
    event.getParticipantsList().removeParticipant(participant);
    event.getParticipantsList().internal_setBookedOut(false);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#removeCounselor(de.naju.adebar.model.events.
   * Event, de.naju.adebar.model.persons.Person)
   */
  @Override
  public void removeCounselor(Event event, Person counselor) {
    event.assertNotCanceled();
    event.getOrganizationInfo().removeCounselor(counselor);

    if (event.getParticipantsList().hasAccommodationInfo() //
        && !event.getParticipantsList().getAccommodation().hasExtraSpaceForCounselors()) {
      event.getParticipantsList().internal_setBookedOut(false);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.ParticipationManager#movePersonFromWaitingListToParticipants(de.
   * naju.adebar.model.events.Event, de.naju.adebar.model.persons.Person)
   */
  @Override
  public Result movePersonFromWaitingListToParticipants(Event event, Person person) {
    return movePersonFromWaitingListToParticipants(event, person, new RegistrationInfo());
  }

  @Override
  public Result movePersonFromWaitingListToParticipants(Event event, Person person,
      RegistrationInfo registrationInfo) {
    event.assertNotCanceled();

    Result result = addParticipant(event, person, registrationInfo);

    if (result == Result.OK) {
      event.getParticipantsList().removeFromWaitingList(person);
    }

    return result;
  }

  /**
   * Marks the participation fee of all participants as "not payed".
   * <p>
   * <strong>This method is currently inactive</strong>
   *
   * @param feeAddedEvent the event that made the invalidation become necessary
   */
  // @EventListener
  public void invalidateParticipationFeeInfo(ParticipationFeeIncreasedEvent feeAddedEvent) {
    final Event correspondingEvent = feeAddedEvent.getEntity();

    for (Person participant : correspondingEvent.getParticipantsList().getParticipantsList()) {
      RegistrationInfo registrationInfo =
          correspondingEvent.getParticipantsList().getParticipationDetailsFor(participant);
      registrationInfo.setParticipationFeePayed(false);
      correspondingEvent.getParticipantsList().updateParticipant(participant, registrationInfo);
    }
  }

  /**
   * Checks, whether a person may attend an event. The correct strategy to decide will be inferred
   * from the parameters passed.
   *
   * @param event the event the person wants to attend
   * @param participant the person
   * @param registrationInfo information about how the person plans to attend
   * @param isUpdate whether the person already participates and only the {@code
   *     registrationInfo} is updated
   * @param isCounselor whether the person attends the event as a counselor
   * @return whether the person may attend
   */
  private boolean mayAccommodateAdditionalPerson(Event event, Person participant,
      ParticipationInfoWithDynamicTime registrationInfo, boolean isUpdate, boolean isCounselor) {
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

  /**
   * Checks, whether a person may attend an event using a scheduling algorithm.
   * <p>
   * This method supports two operation modes:
   * <ul>
   * <li>per default the person is assumed to be a new participant</li>
   * <li>alternatively the person could attend the event already, but the participation time needs
   * to change (e.g. because holiday plans changed)</li>
   * </ul>
   * The {@code isUpdate} param is used to switch between the two modes.
   *
   * @param event the event the person wants to attend
   * @param participant the person
   * @param registrationInfo information about how the person plans to attend the event
   * @param isUpdate whether the person already participates and only the {@code
   *     registrationInfo} is updated
   * @return whether the person may attend
   */
  private boolean validateWithScheduler(Event event, Person participant,
      ParticipationInfoWithDynamicTime registrationInfo, boolean isUpdate) {
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

  /**
   * Checks, whether a person may attend an event using the participants limit of the event.
   *
   * @param event the event the person wants to attend
   * @param participant the person
   * @param registrationInfo information about how the person plans to attend the event
   * @return whether the person may attend
   */
  private boolean validateWithParticipantsLimit(Event event, Person participant,
      ParticipationInfoWithDynamicTime registrationInfo) {
    ParticipantsList pl = event.getParticipantsList();
    return !Capacity.of(pl.getParticipantsList().size() + 1)
        .isLargerThan(pl.getParticipantsLimit());
  }

  /**
   * Checks, whether all available slots for an event are occupied.
   * <p>
   * <strong>If the event has custom accomodation info and the scheduler has not been invoked
   * before, an {@link IllegalStateException} will be thrown.</strong>
   * <p>
   * Therefore this method should only be called after for updating an event's state after its
   * participants or counselors have changed.
   *
   * @param event the event to check
   * @return whether no more people may participate in the event
   */
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

  /**
   * Checks, whether a participant has the age required to participate in an event. This check is
   * defensive, meaning that for participant's whose age is unknown will fail the test.
   *
   * @param participant the person to check. Assumed to be a
   *        {@link de.naju.adebar.model.persons.ParticipantProfile participant}
   * @param event the event that the participant wants to attend
   * @return whether the person may attend the event (according to age requirements)
   */
  private boolean satisfiesMinimumParticipantAge(Person participant, Event event) {
    if (!participant.getParticipantProfile().hasDateOfBirth()) {
      return false;
    }

    return !Age.forDateOfBirth(participant.getParticipantProfile().getDateOfBirth())
        .isYoungerThan(event.getParticipationInfo().getMinimumParticipantAge());
  }

}
