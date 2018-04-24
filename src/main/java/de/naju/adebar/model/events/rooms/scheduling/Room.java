package de.naju.adebar.model.events.rooms.scheduling;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.Gender;

/**
 * Value object to describe a room. Each room consists of a certain amount of beds which could maybe
 * be used by persons of the same gender.
 *
 * @author Rico Bergmann
 */
public class Room {

  private final int bedsCount;
  private final Gender gender;

  /**
   * Creates a new room for participants of any gender
   *
   * @param bedsCount the number of beds in the room
   */
  public Room(int bedsCount) {
    this(bedsCount, null);
  }

  /**
   * Creates a new room. If the {@code gender} is {@code null} the room will accept participants of
   * any gender.
   *
   * @param bedsCount the number of beds in the room
   * @param gender the gender that persons who sleep in the room must have
   */
  public Room(int bedsCount, Gender gender) {
    this.bedsCount = bedsCount;
    this.gender = gender;
  }

  /**
   * @return the number of beds in the room
   */
  public int getBedsCount() {
    return bedsCount;
  }

  /**
   * @return the gender of the persons that sleep in the room
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * @return whether the room supports persons of any gender
   */
  public boolean isUnisex() {
    return gender == null;
  }

  /**
   * @param gender the gender to check
   * @return whether a participant of the given gender may sleep in this room
   */
  public boolean accepts(Gender gender) {
    return isUnisex() || this.gender.equals(gender);
  }

  /**
   * @param participant the person to check
   * @return whether the participant may sleep in this room
   */
  public boolean accepts(Person participant) {
    return accepts(participant.getParticipantProfile().getGender());
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
    result = prime * result + bedsCount;
    result = prime * result + ((gender == null) ? 0 : gender.hashCode());
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
    Room other = (Room) obj;
    if (bedsCount != other.bedsCount)
      return false;
    if (gender != other.gender)
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
    return "Room [beds=" + bedsCount + ", gender=" + gender + "]";
  }

}
