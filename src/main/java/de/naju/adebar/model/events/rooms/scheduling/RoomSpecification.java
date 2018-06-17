package de.naju.adebar.model.events.rooms.scheduling;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.support.NumericEntityId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import org.springframework.util.Assert;

/**
 * A convenient description for how many rooms are available for accomodation.
 *
 * @author Rico Bergmann
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class RoomSpecification implements Iterable<Room> {

  @ElementCollection
  @CollectionTable(name = "accommodationRooms", joinColumns = @JoinColumn(name = "specId"))
  protected List<Room> rooms;

  @Column(name = "extraSpaceForCounselors")
  protected boolean extraSpaceForCounselors;

  @EmbeddedId
  private NumericEntityId id = new NumericEntityId();

  /**
   * Creates a new specification. Nothing special about it.
   */
  public RoomSpecification() {
    this.rooms = new ArrayList<>();
  }

  /**
   * Creates a new specification for a certain number of rooms. This is not a hard limit but may be
   * altered later on.
   *
   * @param roomCount the initial number of rooms
   */
  public RoomSpecification(int roomCount) {
    this.rooms = new ArrayList<>(roomCount);
  }

  /**
   * Creates a new specification based on the given rooms
   *
   * @param rooms the rooms
   */
  protected RoomSpecification(List<Room> rooms) {
    Assert.notNull(rooms, "rooms may not be null");
    Assert.noNullElements(rooms.toArray(), "No room may be null");
    this.rooms = new ArrayList<>(rooms);
  }

  /**
   * @return the rooms
   */
  public Collection<Room> getRooms() {
    return Collections.unmodifiableCollection(rooms);
  }

  public boolean hasExtraSpaceForCounselors() {
    return extraSpaceForCounselors;
  }

  @Transient
  public int getTotalBeds() {
    return rooms.stream().mapToInt(Room::getBedsCount).sum();
  }

  /**
   * Calculates the total number of beds available for a certain gender
   *
   * @param gender the gender. May be {@code null} to search for rooms where the gender does not
   *     matter.
   * @return the total capacity of all matching rooms
   */
  @Transient
  public int getTotalBedsFor(Gender gender) {
    Stream<Room> roomsStream = rooms.stream();

    if (gender == null) {
      return roomsStream //
          .filter(r -> r.getGender() == null) //
          .mapToInt(Room::getBedsCount) //
          .sum();
    } else {
      return roomsStream //
          .filter(r -> gender.equals(r.getGender())) //
          .mapToInt(Room::getBedsCount) //
          .sum();
    }

  }

  /**
   * Adds a new room to the specification
   *
   * @param bedsCount the number of beds in the room
   * @param gender the gender of the participants that may sleep in the room
   * @return the specification for easy method chaining
   */
  public RoomSpecification addRoom(int bedsCount, Gender gender) {
    this.rooms.add(new Room(bedsCount, gender));
    return this;
  }

  /**
   * Adds a new room to the specification
   *
   * @param room the room
   * @return the specification for easy method chaining
   */
  public RoomSpecification addRoom(Room room) {
    Assert.notNull(room, "Room may not be null");
    this.rooms.add(room);
    return this;
  }

  public RoomSpecification withExtraSpaceForCounselors() {
    setExtraSpaceForCounselors(true);
    return this;
  }

  protected void setExtraSpaceForCounselors(boolean extraSpace) {
    this.extraSpaceForCounselors = extraSpace;
  }

  @JpaOnly
  private NumericEntityId getId() {
    return id;
  }

  @JpaOnly
  private void setId(NumericEntityId id) {
    this.id = id;
  }

  @JpaOnly
  private boolean isExtraSpaceForCounselors() {
    return extraSpaceForCounselors;
  }

  @JpaOnly
  private void setRooms(List<Room> rooms) {
    this.rooms = rooms;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Room> iterator() {
    return rooms.iterator();
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
    result = prime * result + ((rooms == null) ? 0 : rooms.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RoomSpecification other = (RoomSpecification) obj;
    if (rooms == null) {
      if (other.rooms != null) {
        return false;
      }
    } else if (!rooms.equals(other.rooms)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RoomSpecification [rooms=" + rooms + "]";
  }

}
