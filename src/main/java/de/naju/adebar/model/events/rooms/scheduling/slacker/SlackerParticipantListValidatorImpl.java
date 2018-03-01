package de.naju.adebar.model.events.rooms.scheduling.slacker;

import java.util.List;
import com.google.common.collect.Lists;
import de.naju.adebar.model.events.rooms.scheduling.Participant;
import de.naju.adebar.model.events.rooms.scheduling.ParticipantListValidator;
import de.naju.adebar.model.events.rooms.scheduling.Room;
import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;
import de.naju.adebar.model.persons.details.Gender;

/**
 * The implementation of the {@link SlackerParticipantListValidator}
 *
 * @author Rico Bergmann
 */
class SlackerParticipantListValidatorImpl {

  private boolean calculated;

  private RoomSpecification roomSpecification;
  private List<Participant> participants;

  private boolean schedulable;
  private int scheduleReliability = ParticipantListValidator.NORMAL_RELIABILITY;

  /**
   * @param rooms the accommodation
   * @param participants the persons. Each person must be a participant.
   */
  public SlackerParticipantListValidatorImpl(RoomSpecification rooms,
      Iterable<Participant> participants) {
    this.calculated = false;

    this.roomSpecification = rooms;
    this.participants = Lists.newArrayList(participants);
  }

  /**
   * @return whether all participants may be accomodated
   */
  public boolean isSchedulable() {
    if (!calculated) {
      calculateSchedule();
    }
    return schedulable;
  }

  /**
   * @return how certain the scheduler is that all participants may be accommodated in a meaningful
   *         way. This will be either {@link ParticipantListValidator#NORMAL_RELIABILITY} or
   *         {@link ParticipantListValidator#LOW_RELIABILITY} as a slacker can never be sure.
   */
  public int assessScheduleReliablity() {
    if (!calculated) {
      calculateSchedule();
    }
    return scheduleReliability;
  }

  /**
   * Sets the {@link #schedulable} field according to {@link #roomSpecification accommodation} and
   * {@link #participants}
   */
  private void calculateSchedule() {
    for (int night = 0; night <= getLastParticipationNight(); ++night) {
      if (!hasEnoughBedsAt(night)) {
        this.schedulable = false;
        return;
      }
    }
    this.schedulable = true;
  }

  /**
   * @return the last night any of the participants stays
   */
  private int getLastParticipationNight() {
    return participants.stream() //
        .mapToInt(Participant::getLastNight) //
        .max().orElse(0);
  }

  /**
   * @param night the night to check
   * @return whether the accommodation has enough beds for all the participants
   */
  private boolean hasEnoughBedsAt(int night) {
    for (Gender gender : Gender.values()) {
      long necessaryBeds = getNecessaryBedsFor(night, gender);
      long availableBeds = getAvailableBedsFor(gender);
      updateReliabilityIfNecessary(necessaryBeds, availableBeds);
      if (necessaryBeds > availableBeds) {
        return false;
      }
    }
    return true;
  }

  /**
   * @param gender the gender to check
   * @return the amount of beds the accommodation has available
   */
  private long getAvailableBedsFor(Gender gender) {
    return roomSpecification.getRooms().stream() //
        .filter(room -> room.accepts(gender)) //
        .mapToLong(Room::getBedsCount) //
        .sum();
  }

  /**
   * @param night the night to check
   * @param gender the gender to check
   * @return the amount of beds available at the accommodation
   */
  private long getNecessaryBedsFor(int night, Gender gender) {
    return participants.stream() //
        .filter(participant -> participant.getPerson().getParticipantProfile().getGender()
            .equals(gender)) //
        .filter(participant -> participant.participantesAt(night)) //
        .count();
  }

  /**
   * Sets the {@link #scheduleReliability} to {@link ParticipantListValidator#LOW_RELIABILITY} if
   * the amount of beds equals the amount of participants of matching gender.
   *
   * @param necessaryBeds the beds needed
   * @param availableBeds the beds available
   */
  private void updateReliabilityIfNecessary(long necessaryBeds, long availableBeds) {
    if (necessaryBeds == availableBeds) {
      this.scheduleReliability = ParticipantListValidator.LOW_RELIABILITY;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((participants == null) ? 0 : participants.hashCode());
    result = prime * result + ((roomSpecification == null) ? 0 : roomSpecification.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SlackerParticipantListValidatorImpl other = (SlackerParticipantListValidatorImpl) obj;
    if (participants == null) {
      if (other.participants != null)
        return false;
    } else if (!participants.equals(other.participants))
      return false;
    if (roomSpecification == null) {
      if (other.roomSpecification != null)
        return false;
    } else if (!roomSpecification.equals(other.roomSpecification))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SlackerParticipantListValidatorImpl [calculated=" + calculated + ", roomSpecification="
        + roomSpecification + ", participants=" + participants + ", schedulable=" + schedulable
        + ", scheduleReliability=" + scheduleReliability + "]";
  }

}
