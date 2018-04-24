package de.naju.adebar.model.events.rooms.scheduling.greedy;

import java.util.Arrays;
import org.springframework.stereotype.Service;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.events.rooms.scheduling.Participant;
import de.naju.adebar.model.events.rooms.scheduling.ParticipantListValidator;
import de.naju.adebar.model.events.rooms.scheduling.RegisteredParticipants;
import de.naju.adebar.model.events.rooms.scheduling.Room;
import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;
import de.naju.adebar.model.events.rooms.scheduling.slacker.SlackerParticipantListValidator;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.util.Arrays2;

/**
 * A greedy implementation of the {@link ParticipantListValidator} capable of handling
 * {@link ExtendedRoomSpecification}
 *
 * Basically, the {@code GreedyValidator} may be seen as an extension of the idea behind the
 * {@link SlackerParticipantListValidator}. For each day, it will check whether there are at least
 * as many beds available, as participants registered for that night. If there is no schedule
 * available using the fixed rooms, the scheduler will try to use the flexible rooms step by step
 * and the fallback rooms as a last resort.
 *
 * @author Rico Bergmann
 */
@Service
public class GreedyParticipantListValidator implements ParticipantListValidator {

  private int[] femaleRooms;
  private int[] maleRooms;
  private int[] otherRooms;
  private int errIdx;

  @Override
  public boolean isSchedulable(RoomSpecification rooms, RegisteredParticipants participants) {
    return isSchedulableWithExtendedSpec(new ExtendedRoomSpecification(rooms), participants);
  }

  @Override
  public int assessScheduleReliablity() {
    if (errIdx > -1) {
      return HIGH_RELIABILITY;
    }

    for (int[] data : Arrays.asList(femaleRooms, maleRooms, otherRooms)) {
      if (!Arrays2.matchesBetween(data, 0, data.length, i -> i > 0, true)) {
        return LOW_RELIABILITY;
      }
    }
    return NORMAL_RELIABILITY;
  }

  @Override
  public int errorIndex() {
    return errIdx;
  }

  @Override
  public boolean isSchedulableWithExtendedSpec(ExtendedRoomSpecification rooms,
      RegisteredParticipants participants) {
    final int eventDuration = participants.getParticipationDuration();
    final int firstNight = participants.getFirstParticipationNight();
    femaleRooms = new int[eventDuration];
    Arrays.fill(femaleRooms, rooms.getTotalBedsFor(Gender.FEMALE));
    maleRooms = new int[eventDuration];
    Arrays.fill(maleRooms, rooms.getTotalBedsFor(Gender.MALE));
    otherRooms = new int[eventDuration];
    Arrays.fill(otherRooms, rooms.getTotalBedsFor(Gender.OTHER));

    if (fillOccupationAsFarAsPossible(participants, 0, firstNight)) {
      return true;
    }

    while (rooms.hasFlexRooms()) {
      Room newRoom = rooms.getFlexRoomWithLargestCapacity().get();
      Gender roomsToExtend = participants.getGenderOfMajorityOfParticipantsAfter(errIdx);
      rooms = rooms.createSpecificationWithFlexRoom(newRoom, roomsToExtend);
      extendRooms(newRoom.getBedsCount(), roomsToExtend);

      if (fillOccupationAsFarAsPossible(participants, errIdx, firstNight)) {
        return true;
      }

    }

    while (rooms.hasFallbackRooms()) {
      Room newRoom = rooms.getFallbackRoomWithLargestCapacity().get();
      Gender roomsToExtend = participants.getGenderOfMajorityOfParticipantsAfter(errIdx);
      rooms = rooms.createSpecificationWithFallbackRoom(newRoom, roomsToExtend);
      extendRooms(newRoom.getBedsCount(), roomsToExtend);

      if (fillOccupationAsFarAsPossible(participants, errIdx, firstNight)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Occupies as many beds as possible until all participants have been allotted or no beds are
   * available any more.
   * <p>
   * In that case {@link #errorIndex()} will contain the index of the participant from
   * {@code ParticipantsLimitFilter.getParticipants()} which could not be accommodated. The
   * algorithm guarantees that all other participants with a larger index have not been housed as
   * well.
   * <p>
   * To do its work, the algorithm will decrease the values in {@link #femaleRooms},
   * {@link #maleRooms} and {@link #otherRooms}.
   *
   * @param participants all participants
   * @param firstParticipantIdx the first participant to check, the algorithm will start with it and
   *        continue with all participants with larger index
   * @param firstNight the participation first night - this is important to readjust the
   *        participation nights to array indexes
   * @return whether all participants could be successfully housed
   */
  protected boolean fillOccupationAsFarAsPossible(RegisteredParticipants participants,
      int firstParticipantIdx, int firstNight) {
    int currParticipant = 0;

    for (Participant participant : participants.getParticipants().subList(firstParticipantIdx,
        participants.getParticipants().size() - 1)) {
      int[] roomArr;
      switch (participant.getGender()) {
        case FEMALE:
          roomArr = femaleRooms;
          break;
        case MALE:
          roomArr = maleRooms;
          break;
        case OTHER:
          roomArr = otherRooms;
          break;
        default:
          throw new AssertionError(participant.getGender());
      }

      // check, if there are enough beds available within the time slot
      if (Arrays2.matchesBetween(roomArr, //
          participant.getFirstNight() - firstNight, //
          participant.getLastNight() - firstNight, //
          i -> i > 0, //
          false)) {
        // if there are, one bed will be occupied
        Arrays2.applyFuncBetween(roomArr, //
            i -> i - 1, //
            participant.getFirstNight() - firstNight, //
            participant.getLastNight() - firstNight, //
            false);
      } else {
        errIdx = currParticipant;
        return false;
      }

      currParticipant++;
    }

    errIdx = -1;
    return true;
  }

  /**
   * Increases the capacity of the rooms allotted to some gender
   * 
   * @param capacity the additional capacity
   * @param gender the gender
   */
  protected void extendRooms(int capacity, Gender gender) {
    int arr[];
    switch (gender) {
      case FEMALE:
        arr = femaleRooms;
        break;
      case MALE:
        arr = maleRooms;
        break;
      case OTHER:
        arr = otherRooms;
        break;
      default:
        throw new AssertionError(gender);
    }
    Arrays2.applyFunc(arr, i -> i + capacity);
  }

}
