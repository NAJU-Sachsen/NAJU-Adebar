package de.naju.adebar.model.events.rooms.scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.util.Assert;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.util.Lists2;

/**
 * The {@code ExtendedRoomSpecification} not only provides a list of fixed rooms but also more
 * elaborated concepts:
 * <ul>
 * <li>Flexible rooms (or flex rooms in short) are not tied to a gender but may be assigned on
 * demand</li>
 * <li>Fallback rooms are rooms without a fixed gender as well, but in contrast to flex rooms they
 * should only be used if there is no other choice.</li>
 * </ul>
 *
 * @author Rico Bergmann
 */
public class ExtendedRoomSpecification extends RoomSpecification {

  private List<Room> flexRooms;
  private List<Room> fallbackRooms;

  /**
   * Creates a new empty specification
   */
  public ExtendedRoomSpecification() {
    this.flexRooms = new ArrayList<>();
    this.fallbackRooms = new ArrayList<>();
  }

  /**
   * Creates a new empty specification with hints on how many rooms it will likely contain. This is
   * no hard limit and may be altered later on.
   *
   * @param roomCount the initial number of rooms. This does not affect flexible rooms, nor fallback
   *        rooms
   */
  public ExtendedRoomSpecification(int roomCount) {
    super(roomCount);
    this.flexRooms = new ArrayList<>();
    this.fallbackRooms = new ArrayList<>();
  }

  /**
   * Creates a new specification using the rooms of another specification.
   *
   * @param spec the other specification
   */
  public ExtendedRoomSpecification(RoomSpecification spec) {
    super(spec.rooms);
    this.flexRooms = new ArrayList<>();
    this.fallbackRooms = new ArrayList<>();
  }

  /**
   * Creates a new specification using exactly the specified rooms
   *
   * @param rooms the normal rooms
   * @param flexRooms the flexible rooms
   * @param fallbackRooms the fallback rooms
   */
  protected ExtendedRoomSpecification(List<Room> rooms, List<Room> flexRooms,
      List<Room> fallbackRooms) {
    super(rooms);

    Assert.notNull(flexRooms, "flexRooms may not be null");
    Assert.noNullElements(flexRooms.toArray(), "No flex room may be null");
    Assert.notNull(fallbackRooms, "fallbackRooms may not be null");
    Assert.noNullElements(fallbackRooms.toArray(), "No fallback room may be null");

    this.flexRooms = new ArrayList<>(flexRooms);
    this.fallbackRooms = new ArrayList<>(fallbackRooms);
  }

  /**
   * Provides the flexible rooms which may be assigned to any gender on demand. The gender will be
   * set to {@code null} for each element. This however is a workaround because a {@link Room} may
   * actually have a {@code null}-gender if the gender does not matter for this room.
   *
   * @return the flex rooms
   */
  public Collection<Room> getFlexRooms() {
    return Collections.unmodifiableCollection(flexRooms);
  }

  /**
   * @return the flex room with the largest capacity if there is any. If there are multiple such
   *         rooms, any room may be returned.
   */
  public Optional<Room> getFlexRoomWithLargestCapacity() {
    return flexRooms.stream().sorted(new Comparator<Room>() {
      @Override
      public int compare(Room r1, Room r2) {
        return Integer.compare(r1.getBedsCount(), r2.getBedsCount());
      }
    }).findFirst();
  }

  /**
   * @return whether the specification contains flex rooms
   */
  public boolean hasFlexRooms() {
    return !flexRooms.isEmpty();
  }

  /**
   * Provides the fallback rooms which may be assigned to any gender on demand if no other rooms are
   * available. The gender will be set to {@code null} for each element. This however is a
   * workaround because a {@link Room} may actually have a {@code null}-gender if the gender does
   * not matter for this room.
   *
   * @return the fallback rooms
   */
  public Collection<Room> getFallbackRooms() {
    return Collections.unmodifiableCollection(fallbackRooms);
  }

  /**
   * @return the fallback room with the largest capacity if there is any. If there are multiple such
   *         rooms, any room may be returned.
   */
  public Optional<Room> getFallbackRoomWithLargestCapacity() {
    return fallbackRooms.stream().sorted(new Comparator<Room>() {
      @Override
      public int compare(Room r1, Room r2) {
        return Integer.compare(r1.getBedsCount(), r2.getBedsCount());
      }
    }).findFirst();
  }

  /**
   * @return whether the specification contains fallback rooms
   */
  public boolean hasFallbackRooms() {
    return !fallbackRooms.isEmpty();
  }

  /**
   * Adds a new flex room to the specification
   *
   * @param capacity the room's capacity
   * @return the specification for easy method chaining
   */
  public ExtendedRoomSpecification addFlexRoom(int capacity) {
    this.flexRooms.add(new Room(capacity));
    return this;
  }

  /**
   * Adds a new fallback room to the specification
   *
   * @param capacity the room's capacity
   * @return the specification for easy method chaining
   */
  public ExtendedRoomSpecification addFallbackRoom(int capacity) {
    this.fallbackRooms.add(new Room(capacity));
    return this;
  }

  /**
   * Turns a flexible room into a 'real' room, i.e. a room for a certain gender
   *
   * @param flexRoom the flex room
   * @param gender the gender
   * @return the resulting specification. This will be a new {@code ExtendedRoomSpecification}
   *         instance
   */
  public ExtendedRoomSpecification createSpecificationWithFlexRoom(Room flexRoom, Gender gender) {
    Room newRoom = new Room(flexRoom.getBedsCount(), gender);
    List<Room> newRooms = Lists2.addToCopy(this.rooms, newRoom);
    List<Room> newFlexRooms = Lists2.removeOnCopy(this.flexRooms, flexRoom);
    return new ExtendedRoomSpecification(newRooms, newFlexRooms, fallbackRooms);
  }

  /**
   * Turns a fallback room into a 'real' room, i.e. a room for a certain gender
   *
   * @param fallbackRoom the fallback room
   * @param gender the gender
   * @return the resulting specification. This will be a new {@code ExtendedRoomSpecification}
   *         instance
   */
  public ExtendedRoomSpecification createSpecificationWithFallbackRoom(Room fallbackRoom,
      Gender gender) {
    Room newRoom = new Room(fallbackRoom.getBedsCount(), gender);
    List<Room> newRooms = Lists2.addToCopy(this.rooms, newRoom);
    List<Room> newFallbackRooms = Lists2.removeOnCopy(this.fallbackRooms, fallbackRoom);
    return new ExtendedRoomSpecification(newRooms, this.flexRooms, newFallbackRooms);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.model.events.rooms.scheduling.RoomSpecification#addRoom(de.naju.adebar.model.
   * events.rooms.scheduling.Room)
   */
  @Override
  public ExtendedRoomSpecification addRoom(Room room) {
    return (ExtendedRoomSpecification) super.addRoom(room);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.rooms.scheduling.RoomSpecification#addRoom(int,
   * de.naju.adebar.model.persons.details.Gender)
   */
  @Override
  public ExtendedRoomSpecification addRoom(int bedsCount, Gender gender) {
    return (ExtendedRoomSpecification) super.addRoom(bedsCount, gender);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.rooms.scheduling.RoomSpecification#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((fallbackRooms == null) ? 0 : fallbackRooms.hashCode());
    result = prime * result + ((flexRooms == null) ? 0 : flexRooms.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.rooms.scheduling.RoomSpecification#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    ExtendedRoomSpecification other = (ExtendedRoomSpecification) obj;
    if (fallbackRooms == null) {
      if (other.fallbackRooms != null)
        return false;
    } else if (!fallbackRooms.equals(other.fallbackRooms))
      return false;
    if (flexRooms == null) {
      if (other.flexRooms != null)
        return false;
    } else if (!flexRooms.equals(other.flexRooms))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.naju.adebar.model.events.rooms.scheduling.RoomSpecification#toString()
   */
  @Override
  public String toString() {
    return "ExtendedRoomSpecification [rooms=" + rooms + ", flexRooms=" + flexRooms
        + ", fallbackRooms=" + fallbackRooms + "]";
  }

}
