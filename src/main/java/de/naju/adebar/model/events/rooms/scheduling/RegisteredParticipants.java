package de.naju.adebar.model.events.rooms.scheduling;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.util.IntTriple;

/**
 * Wrapper for the persons which should attend an event.
 * <p>
 * The wrapper mainly exists to provide some convenient methods on the participants.
 *
 * @author Rico Bergmann
 */
public class RegisteredParticipants implements Iterable<Participant> {

  private final List<Participant> participants;

  /**
   * Generates a new instance for the given participants
   *
   * @param participants the participants
   * @return the new wrapper
   */
  public static RegisteredParticipants of(List<Participant> participants) {
    return new RegisteredParticipants(participants);
  }

  /**
   * Generates a new instance for the given participants
   *
   * @param participants the participants
   * @return the new wrapper
   */
  public static RegisteredParticipants of(Participant... participants) {
    return new RegisteredParticipants(Lists.newArrayList(participants));
  }

  /**
   * Full constructor
   *
   * @param participants the participants
   */
  private RegisteredParticipants(List<Participant> participants) {
    Assert.notNull(participants, "participants may not be null");
    Assert.noNullElements(participants.toArray(), "No participant may be null");
    this.participants = participants;
  }

  /**
   * @return the participants
   */
  public List<Participant> getParticipants() {
    return Collections.unmodifiableList(participants);
  }

  /**
   * @return the first night any of the participants attends the event
   */
  public int getFirstParticipationNight() {
    return participants.stream() //
        .mapToInt(Participant::getFirstNight).min().orElse(0);
  }

  /**
   * @return the last night any of the participants attends the event
   */
  public int getLastParticipationNight() {
    return participants.stream() //
        .mapToInt(Participant::getLastNight).max().orElse(0);
  }

  /**
   * @return the number of nights between first and last participation night
   */
  public int getParticipationDuration() {
    // we need to add 1 as we operate on the nights directly
    return getLastParticipationNight() - getFirstParticipationNight() + 1;
  }

  /**
   * Filters for all participants that attend the event on a certain night
   *
   * @param night the night
   * @return all matching participants
   */
  public List<Participant> getParticipantsOn(int night) {
    return participants.stream() //
        .filter(participant -> participant.participantesAt(night)) //
        .collect(Collectors.toList());
  }

  /**
   * Filters for all participants with a certain gender which attend the event on a certain night
   *
   * @param night the night
   * @param gender the gender. May be {@code null} to filter for all participants with no gender
   * @return all matching participants
   */
  public List<Participant> getParticipantsWithGenderOn(int night, Gender gender) {
    Stream<Participant> participantStream = participants.stream();
    if (gender == null) {
      participantStream.filter(participant -> participant.getGender() == null);
    } else {
      participantStream.filter(participant -> gender.equals(participant.getGender()));
    }
    participantStream.filter(participant -> participant.participantesAt(night));
    return participantStream.collect(Collectors.toList());
  }

  /**
   * Calculates the gender of the majority of participants after the participant with the given
   * index according to {@link #getParticipants()}.
   *
   * @param index the index of the participant to start the calculation after
   * @return the gender
   */
  public Gender getGenderOfMajorityOfParticipantsAfter(int index) {
    List<Participant> remainder = participants.subList(index, participants.size() - 1);

    // we will store the number of participants with the gender in a triple (int, int, int) where
    // the first component contains the number of female participants, the second is for the male
    // ones and the last one is for all other participants - (FEMALE, MALE, OTHER)

    IntTriple combined = remainder.stream() //
        .map(Participant::getGender) //
        .reduce( //
            IntTriple.of(0, 0, 0), //
            (t, g) -> {
              switch (g) {
                case FEMALE:
                  return IntTriple.of(t.first() + 1, t.second(), t.third());
                case MALE:
                  return IntTriple.of(t.first(), t.second() + 1, t.third());
                case OTHER:
                  return IntTriple.of(t.first(), t.second(), t.third() + 1);
                default:
                  throw new AssertionError(g);
              }
            }, //
            (t1, t2) -> t1.add(t2));

    switch (combined.max()) {
      case FIRST:
        return Gender.FEMALE;
      case SECOND:
        return Gender.MALE;
      case THIRD:
        return Gender.OTHER;
      default:
        throw new AssertionError(combined.max());
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Participant> iterator() {
    return participants.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RegisteredParticipants [participants=" + participants + "]";
  }

}
