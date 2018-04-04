package de.naju.adebar.model.events.participants;

import de.naju.adebar.model.events.BookedOutException;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventId;
import de.naju.adebar.model.events.ExistingReservationException;
import de.naju.adebar.model.events.Reservation;
import de.naju.adebar.model.persons.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import org.springframework.util.Assert;

/**
 * The participants (and reservations) of an {@link Event} will be stored here
 *
 * @author Rico Bergmann
 */
@Entity(name = "participantsList")
public class ParticipantsList {

  private static final int WAITING_LIST_HEAD = 0;

  @EmbeddedId
  @Column(name = "event")
  private EventId event;

  @Column(name = "participantsLimit")
  private int participantsLimit;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "eventParticipants", joinColumns = @JoinColumn(name = "eventId"))
  @MapKeyJoinColumn(name = "participant")
  private Map<Person, ParticipationInfo> participants;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "eventReservations", joinColumns = @JoinColumn(name = "eventId"))
  private List<Reservation> reservations;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "eventWaitingList", joinColumns = @JoinColumn(name = "eventId"),
      inverseJoinColumns = @JoinColumn(name = "personId"))
  @OrderColumn(name = "position")
  private List<Person> waitingList;

  /**
   * Creates a new participants list with an unlimited number of possible participants and
   * reservations
   *
   * @param event the event to create the list for
   */
  public ParticipantsList(Event event) {
    this(event, Integer.MAX_VALUE);
  }

  /**
   * Creates a new participants list
   *
   * @param event event the event to create the list for
   * @param participantsLimit the maximum number of participants
   */
  public ParticipantsList(Event event, int participantsLimit) {
    this.event = event.getId();
    this.participantsLimit = participantsLimit;
    this.participants = new HashMap<>();
    this.reservations = new ArrayList<>();
    this.waitingList = new ArrayList<>();
  }

  /**
   * Default constructor just for JPA's sake
   */
  private ParticipantsList() {}

  // getter and setter

  /**
   * @return the event this list is created for
   */
  public EventId getEvent() {
    return event;
  }

  /**
   * @param event the event this list was created for
   */
  protected void setEvent(EventId event) {
    this.event = event;
  }

  /**
   * @return the maximum number of participants
   */
  public int getParticipantsLimit() {
    return participantsLimit;
  }

  /**
   * @param participantsLimit the maximum number of participants
   */
  public void setParticipantsLimit(int participantsLimit) {
    this.participantsLimit = participantsLimit;
  }

  /**
   * @return the participants
   */
  public Map<Person, ParticipationInfo> getParticipants() {
    return Collections.unmodifiableMap(participants);
  }

  /**
   * @param participants
   */
  protected void setParticipants(Map<Person, ParticipationInfo> participants) {
    this.participants = participants;
  }

  /**
   * @return the reservations
   */
  public Iterable<Reservation> getReservations() {
    return reservations;
  }

  /**
   * @param reservations the reservations
   */
  protected void setReservations(List<Reservation> reservations) {
    this.reservations = reservations;
  }

  /**
   * @return the waiting list
   */
  public Collection<Person> getWaitingList() {
    return waitingList;
  }

  /**
   * @param waitingList the waiting list
   */
  public void setWaitingList(List<Person> waitingList) {
    this.waitingList = waitingList;
  }

  // query methods

  /**
   * @param description the reservation to query for
   * @return the reservation
   */
  @Transient
  public Reservation getReservationFor(String description) {
    for (Reservation res : reservations) {
      if (res.getDescription().equals(description)) {
        return res;
      }
    }
    return null;
  }

  /**
   * @return the number of participants
   */
  @Transient
  public int getParticipantsCount() {
    return participants.size();
  }

  /**
   * @return the number of reservations (which is <b>not</b> the number of slots reserved!)
   *
   * @see #getReservedSlotsCount()
   */
  @Transient
  public int getReservationsCount() {
    return reservations.size();
  }

  /**
   * @return the total number of slots that were reserved throughout all reservations
   *
   * @see #getReservationsCount()
   */
  @Transient
  public int getReservedSlotsCount() {
    return reservations.stream().mapToInt(Reservation::getNumberOfSlots).sum();
  }

  /**
   * @return the persons who participate in the event but have not payed the fee yet
   */
  @Transient
  public Iterable<Person> getParticipantsWithFeeNotPayed() {
    List<Person> persons = new LinkedList<>();
    participants.forEach((person, info) -> {
      if (!info.isParticipationFeePayed()) {
        persons.add(person);
      }
    });
    return persons;
  }

  /**
   * @return the persons who participate in the event but have not sent the "real" participation
   *     form yet
   */
  @Transient
  public Iterable<Person> getParticipantsWithFormNotReceived() {
    List<Person> persons = new LinkedList<>();
    participants.forEach((person, info) -> {
      if (!info.isRegistrationFormFilled()) {
        persons.add(person);
      }
    });
    return persons;
  }

  /**
   * @param participant the participant to query for
   * @return the associated participation info
   */
  @Transient
  public ParticipationInfo getParticipationInfoFor(Person participant) {
    if (!isParticipant(participant)) {
      throw new IllegalArgumentException("Person does not participate: " + participant);
    }
    return participants.get(participant);
  }

  /**
   * @param person the person to check
   * @return {@code true} if the person participates in the event, or {@code false} otherwise
   */
  @Transient
  public boolean isParticipant(Person person) {
    return participants.containsKey(person);
  }

  /**
   * @param description the description to check
   * @return {@code true} if there is a reservation with that description, {@code false} otherwise
   */
  @Transient
  public boolean hasReservation(String description) {
    return reservations.stream().anyMatch(res -> res.getDescription().equals(description));
  }

  /**
   * @return {@code true} if an participant limit was specified, {@code false} otherwise
   */
  @Transient
  public boolean hasParticipantsLimit() {
    return participantsLimit > 0 && participantsLimit != Integer.MAX_VALUE;
  }

  /**
   * @return {@code true} if there are reservations for the event, {@code false} otherwise
   */
  @Transient
  public boolean hasReservations() {
    return !reservations.isEmpty();
  }

  /**
   * @return the number of spare participation slots
   */
  @Transient
  public int getRemainingCapacity() {
    if (!hasParticipantsLimit()) {
      return Integer.MAX_VALUE;
    }
    return participantsLimit - getOccupiedSlotsCount();
  }

  /**
   * @return the total number of slots that have a reservation or participant
   */
  @Transient
  public int getOccupiedSlotsCount() {
    return getParticipantsCount() + getReservedSlotsCount();
  }

  /**
   * @return {@code true} if no more persons may participate
   */
  @Transient
  public boolean isBookedOut() {
    return hasParticipantsLimit() && getOccupiedSlotsCount() == getParticipantsLimit();
  }

  /**
   * @param participants the number of participants
   * @return {@code true} if enough slots are available, {@code false} otherwise
   */
  public boolean hasCapactityFor(int participants) {
    if (!hasParticipantsLimit()) {
      return true;
    }
    return getRemainingCapacity() >= participants;
  }

  /**
   * @return {@code true} if the waiting list is used, {@code false} otherwise
   */
  public boolean hasWaitingList() {
    return !waitingList.isEmpty();
  }

  /**
   * @param person the person to check
   * @return {@code true} if the person is wait-listed, {@code false} otherwise
   */
  @Transient
  public boolean isOnWaitingList(Person person) {
    return waitingList.contains(person);
  }

  /**
   * @return the head of the waiting list
   */
  @Transient
  public Person getTopWaitingListSpot() {
    return waitingList.get(WAITING_LIST_HEAD);
  }

  /**
   * @param person the person to query for
   * @return the person's position on the waiting list
   */
  @Transient
  public int getWaitingListSpotFor(Person person) {
    return normalizeIndex(waitingList.indexOf(person));
  }

  // modification methods

  /**
   * Adds a new participant
   *
   * @param person the person to participate in the event
   * @throws ExistingParticipantException if the person already participates
   * @throws BookedOutException if no more persons may participate
   */
  public void addParticipant(Person person) {
    if (isParticipant(person)) {
      throw new ExistingParticipantException("Person does already participate: " + person);
    } else if (isBookedOut()) {
      throw new BookedOutException(
          "The event is booked out: " + getParticipantsCount() + " participants");
    }
    participants.put(person, new ParticipationInfo());
  }

  /**
   * @param person the person to remove from the list of participants
   * @throws IllegalArgumentException if the person did not participate
   */
  public void removeParticipant(Person participant) {
    if (!isParticipant(participant)) {
      throw new IllegalArgumentException("Person does not participate: " + participant);
    }
    participants.remove(participant);
  }

  /**
   * @param person the person to update
   * @param newInfo the new participation info
   */
  public void updateParticipationInfoFor(Person participant, ParticipationInfo newInfo) {
    participants.replace(participant, newInfo);
  }

  /**
   * Adds a new reservation
   *
   * @param reservation the reservation
   * @throws ExistingReservationException if there already is a reservation with that
   *     description
   */
  public void addReservation(Reservation reservation) {
    if (hasReservation(reservation.getDescription())) {
      throw new ExistingReservationException("There is already a reservation for " + reservation);
    } else if (!hasCapactityFor(reservation.getNumberOfSlots())) {
      String msg = String.format("Only %d slots available, but needed %d", getRemainingCapacity(),
          reservation.getNumberOfSlots());

      throw new TooFewEmptySlotsException(msg);
    }
    reservations.add(reservation);
  }

  /**
   * Deletes a reservation
   *
   * @param description the description (= ID) of the reservation
   */
  public void removeReservation(String description) {
    if (!hasReservation(description)) {
      throw new IllegalArgumentException("List has no reservation: " + description);
    }
    reservations.removeIf(reservation -> reservation.getDescription().equals(description));
  }

  /**
   * Updates a reservation
   *
   * @param description the description (= ID) of the reservation
   * @param newReservation the new data to use
   */
  public void updateReservation(String description, Reservation newReservation) {
    Reservation currentReservation = getReservationFor(description);
    if (currentReservation == null) {
      throw new IllegalStateException("No reservation registered with description " + description);
    } else if (!newReservation.equals(currentReservation)) {
      reservations.remove(currentReservation);
      reservations.add(newReservation);
    }
  }

  /**
   * Enqueues a person at the end of the waiting list
   *
   * @param person the person to add
   * @throws IllegalArgumentException if the person was {@code null}
   * @throws ExistingParticipantException if the person is already registered as a participant
   * @throws IllegalStateException if the person is already on the waiting list
   */
  public void putOnWaitingList(Person person) {
    Assert.notNull(person, "Person to wait-list may not be null!");
    if (isParticipant(person)) {
      throw new ExistingParticipantException("Person does already participate: " + person);
    } else if (isOnWaitingList(person)) {
      throw new IllegalStateException("Person is already on the waiting list: " + person);
    }
    waitingList.add(person);
  }

  /**
   * Removes a person from the waiting list
   *
   * @param person the person to remove
   * @throws IllegalArgumentException if the person is not wait-listed
   */
  public void removeFromWaitingList(Person person) {
    if (!isOnWaitingList(person)) {
      throw new IllegalArgumentException("Person is not wait-listed: " + person);
    }
    waitingList.remove(person);
  }

  /**
   * Moves the head of the waiting list to the participants list
   */
  public void applyTopWaitingListSpot() {
    addParticipant(waitingList.remove(WAITING_LIST_HEAD));
  }

  // helper methods

  /**
   * Maps an internal list index (which is ≥0) to a 'real world' index (which is ≥1)
   *
   * @param idx the index to normalize
   * @return the normalized index
   */
  private int normalizeIndex(int idx) {
    return idx + 1;
  }

  // overridden from Object

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((event == null) ? 0 : event.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ParticipantsList)) {
      return false;
    }
    ParticipantsList other = (ParticipantsList) obj;
    if (event == null) {
      if (other.event != null) {
        return false;
      }
    } else if (!event.equals(other.event)) {
      return false;
    }
    return true;
  }

}
