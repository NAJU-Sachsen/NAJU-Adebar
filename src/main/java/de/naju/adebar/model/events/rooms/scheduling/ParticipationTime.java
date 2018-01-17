package de.naju.adebar.model.events.rooms.scheduling;

import org.springframework.util.Assert;

/**
 * Value object representing the duration a person participates at an event.
 * <p>
 * As the times should be used to schedule participants, they do actually not depend on dates but
 * rather on the nights a person participates. It does not really matter how many persons take part
 * in an event during day. However it is important how many persons stay over night due to the
 * accomodation's size. Participation times will be compared by their first night first and the last
 * night second.
 *
 * @author Rico Bergmann
 */
public class ParticipationTime implements Comparable<ParticipationTime> {

  private final int firstNight;
  private final int lastNight;

  /**
   * Creates a new participation time for the given duration
   *
   * @param firstNight the night the participation starts
   * @param lastNight the final night the person participates
   */
  public ParticipationTime(int firstNight, int lastNight) {
    Assert.isTrue(firstNight >= 0, "First night may not be negative");
    Assert.isTrue(lastNight >= 0, "Last night may not be negative");
    Assert.isTrue(firstNight <= lastNight, "Last night may not be before first night");
    this.firstNight = firstNight;
    this.lastNight = lastNight;
  }

  /**
   * @return the first night
   */
  public int getFirstNight() {
    return firstNight;
  }

  /**
   * @return the last night
   */
  public int getLastNight() {
    return lastNight;
  }

  /**
   * @param other the time to compare
   * @return whether the first night is before the other's first night
   */
  public boolean startsSoonerThan(ParticipationTime other) {
    return this.firstNight < other.firstNight;
  }

  /**
   * @param other the time to compare
   * @return whether the first night is after the other's first night
   */
  public boolean startsLaterThan(ParticipationTime other) {
    return this.firstNight > other.firstNight;
  }

  /**
   * @param other the night to compare
   * @return whether the last night is before the other last night
   */
  public boolean endsSoonerThan(int other) {
    return this.lastNight < other;
  }

  /**
   * @param other the time to compare
   * @return whether the last night is before the other's last night
   */
  public boolean endsSoonerThan(ParticipationTime other) {
    return this.lastNight < other.lastNight;
  }

  /**
   * @param other the time to compare
   * @return whether the last night is after the other's last night
   */
  public boolean endsLaterThan(ParticipationTime other) {
    return this.lastNight > other.lastNight;
  }

  /**
   * @param other the time to compare
   * @return whether at least one night is occupied by both times
   */
  public boolean overlapsWith(ParticipationTime other) {
    return (this.firstNight <= other.firstNight && this.lastNight >= other.lastNight)
        || (other.firstNight <= this.firstNight && other.lastNight >= this.lastNight);
  }

  @Override
  public int compareTo(ParticipationTime other) {
    // if one participation time starts earlier than the other, it will be "less"
    if (startsSoonerThan(other)) {
      return -1;
    } else if (other.firstNight < this.firstNight) {
      return 1;
    }

    // if one participation time ends later than the other, it will be "larger"
    if (this.lastNight > other.lastNight) {
      return 1;
    } else if (other.lastNight > this.lastNight) {
      return -1;
    }

    // otherwise the participation times are equal
    return 0;
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
    result = prime * result + firstNight;
    result = prime * result + lastNight;
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
    ParticipationTime other = (ParticipationTime) obj;
    if (firstNight != other.firstNight)
      return false;
    if (lastNight != other.lastNight)
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
    return String.format("%d - %d", firstNight, lastNight);
  }

}
