package de.naju.adebar.model.events.rooms.scheduling;

import java.util.Map.Entry;
import java.util.TreeMap;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.Gender;

/**
 * Value object encapsulating a {@link Person} as well as its participation times.
 *
 * @author Rico Bergmann
 */
public class Participant {

  private final Person person;
  private TreeMap<Integer, ParticipationTime> participationTimes;

  /**
   * Creates a participant which participates during a single time span.
   *
   * @param person the participating person. Has to be a camp participant
   * @param firstNight the start of the participation time
   * @param lastNight the end of the participation time
   */
  public Participant(Person person, int firstNight, int lastNight) {
    this(person, new ParticipationTime(firstNight, lastNight));
  }

  /**
   * Creates a participant which participates during multiple time spans.
   *
   * @param person the participating person. Has to be a camp participant
   * @param participationTimes the participation times
   */
  public Participant(Person person, ParticipationTime... participationTimes) {
    this(person, Lists.newArrayList(participationTimes));
  }

  /**
   * Creates a participant which participates during multiple time spans.
   *
   * @param person the participating person. Has to be a camp participant
   * @param participationTimes the participation times
   */
  public Participant(Person person, Iterable<ParticipationTime> participationTimes) {
    Assert.notNull(person, "Person may not be null");
    Assert.isTrue(person.isParticipant(), "Person must be a camp participant");
    this.person = person;
    this.participationTimes = new TreeMap<>();
    participationTimes //
        .forEach(time -> this.participationTimes.put(time.getFirstNight(), time));
  }

  /**
   * @return the participant
   */
  public Person getPerson() {
    return person;
  }

  /**
   * @return the participant's gender - just for convenience
   */
  public Gender getGender() {
    return person.getParticipantProfile().getGender();
  }

  /**
   * @return the first night the participant attends the event
   */
  public int getFirstNight() {
    return participationTimes.values().stream() //
        .mapToInt(ParticipationTime::getFirstNight).min().orElse(0);
  }

  /**
   * @return the last night the person participates
   */
  public int getLastNight() {
    return participationTimes.values().stream() //
        .mapToInt(ParticipationTime::getLastNight).max().orElse(0);
  }

  /**
   * Checks whether a person participates at a certain time
   *
   * @param night the night
   * @return whether the person participates
   */
  public boolean participantesAt(int night) {
    return participatesDuring(night, night);
  }

  /**
   * Checks whether a person participates within a whole time span
   *
   * @param start the first night
   * @param end the last night
   * @return whether the person participates during the whole time
   */
  public boolean participatesDuring(int start, int end) {
    Entry<Integer, ParticipationTime> potentialMatch = participationTimes.floorEntry(start);

    if (potentialMatch == null) {
      return false;
    }

    return !potentialMatch.getValue().endsSoonerThan(end);
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
    result = prime * result + ((participationTimes == null) ? 0 : participationTimes.hashCode());
    result = prime * result + ((person == null) ? 0 : person.hashCode());
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
    Participant other = (Participant) obj;
    if (participationTimes == null) {
      if (other.participationTimes != null)
        return false;
    } else if (!participationTimes.equals(other.participationTimes))
      return false;
    if (person == null) {
      if (other.person != null)
        return false;
    } else if (!person.equals(other.person))
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
    return "Participant [person=" + person.getName() + ", gender="
        + person.getParticipantProfile().getGender() + ", participationTimes="
        + participationTimes.values() + "]";
  }

}
