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
  static int HIGH_RELIABILITY = 100;

  /**
   * Indicates that the scheduler is neither confident nor dubious about its solution.
   */
  static int NORMAL_RELIABILITY = 50;

  /**
   * Indicates that the scheduler is not confident in its solution at all.
   */
  static int LOW_RELIABILITY = 10;

  /**
   * Checks whether a number of persons may participate in an event with the given specification.
   *
   * @param rooms the specification of the accommodation options
   * @param participants the participating persons
   * @return whether the persons may participate with regard to the accommodation available
   */
  boolean isSchedulable(RoomSpecification rooms, Iterable<Participant> participants);

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

}
