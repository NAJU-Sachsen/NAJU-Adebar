package de.naju.adebar.model.events.rooms.scheduling;

/**
 * Service to check whether a number of participants may take part in an event
 *
 * @author Rico Bergmann
 */
public interface ParticipantListValidator {

  /**
   * Indicates that the scheduler is pretty confident in its solution.
   */
  int HIGH_RELIABILITY = 100;

  /**
   * Indicates that the scheduler is neither confident nor dubious about its solution.
   */
  int NORMAL_RELIABILITY = 50;

  /**
   * Indicates that the scheduler is not confident in its solution at all.
   */
  int LOW_RELIABILITY = 10;

  /**
   * Checks whether a number of persons may participate in an event with the given specification.
   *
   * @param rooms the specification of the accommodation options
   * @param participants the participating persons
   * @return whether the persons may participate with regard to the accommodation available
   */
  boolean isSchedulable(RoomSpecification rooms, RegisteredParticipants participants);

  /**
   * Provides an indicator on how much the scheduler "trusts" its latest result.
   * <p>
   * As scheduling may become pretty computational intense, some algorithms may work on an
   * approximative basis. This method can be used to get to know how close the scheduler thinks it
   * is to an actual solution.
   *
   * @return the reliability of the latest schedule
   */
  int assessScheduleReliablity();

  /**
   * Provides the index of the first person which caused the scheduling to fail (because the event
   * would be overbooked). If this method is not implemented, it will throw an
   * {@link UnsupportedOperationException}
   *
   * @return the index of the person as in {@link RegisteredParticipants#getParticipants()}
   */
  default int errorIndex() {
    throw new UnsupportedOperationException(
        this.getClass().getSimpleName() + " provides no error indexes");
  }

  /**
   * Checks whether a number of persons may participate in an event with the given accommodation.
   * <p>
   * If this method is not implemented, the additional features of an
   * {@link ExtendedRoomSpecification} will simply be ignored and the normal check as in
   * {@link #isSchedulable(RoomSpecification, RegisteredParticipants)} will be performed.
   *
   * @param rooms the accommodation available
   * @param participants the participating persons
   * @return whether the persons may participate with regard to the accommodation available
   */
  default boolean isSchedulableWithExtendedSpec(ExtendedRoomSpecification rooms,
      RegisteredParticipants participants) {
    return isSchedulable(rooms, participants);
  }

}
