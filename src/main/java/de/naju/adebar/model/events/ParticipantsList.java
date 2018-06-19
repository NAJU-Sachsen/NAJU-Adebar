package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.support.NumericEntityId;
import de.naju.adebar.util.Assert2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Transient;
import org.springframework.util.Assert;

/**
 * The participants (and reservations) of an {@link Event} will be stored here
 *
 * @author Rico Bergmann
 */
@Embeddable
public class ParticipantsList extends AbstractEventInfo implements Iterable<Person> {

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "value",
      column = @Column(name = "participantsLimit")))
  private Capacity participantsLimit;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "accommodation")
  private ExtendedRoomSpecification accommodation;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "eventParticipants", joinColumns = @JoinColumn(name = "eventId"))
  @MapKeyJoinColumn(name = "participant")
  @OrderBy("registrationDate")
  private Map<Person, RegistrationInfo> participants;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "eventReservations", joinColumns = @JoinColumn(name = "eventId"))
  private List<Reservation> reservations;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "eventWaitingList", joinColumns = @JoinColumn(name = "eventId"),
      inverseJoinColumns = @JoinColumn(name = "personId"))
  @OrderColumn(name = "position")
  private List<Person> waitingList;

  @Column(name = "bookedOut")
  private boolean bookedOut;

  ParticipantsList(Event event) {
    this(event, null, null);
  }

  ParticipantsList(Event event, Capacity participantsLimit) {
    this(event, participantsLimit, null);
  }

  ParticipantsList(Event event, Capacity participantsLimit,
      ExtendedRoomSpecification accomodation) {
    provideRelatedEvent(event);
    this.participantsLimit = participantsLimit;
    this.accommodation = accomodation;
    this.participants = new HashMap<>();
    this.reservations = new ArrayList<>();
    this.waitingList = new ArrayList<>();
  }

  @JpaOnly
  private ParticipantsList() {}

  public Capacity getParticipantsLimit() {
    return participantsLimit;
  }

  public ExtendedRoomSpecification getAccommodation() {
    return accommodation;
  }

  @Transient
  public Collection<Person> getParticipantsList() {
    return participants.keySet();
  }

  public Map<Person, RegistrationInfo> getParticipants() {
    return participants;
  }

  public RegistrationInfo getParticipationDetailsFor(Person person) {
    if (!isParticipant(person)) {
      throw new IllegalArgumentException("Not a participant: " + person);
    }
    return participants.get(person);
  }

  @Transient
  public int getParticipantsCount() {
    return participants.size();
  }

  public Collection<Reservation> getReservations() {
    return Collections.unmodifiableCollection(reservations);
  }

  @Transient
  public Optional<Reservation> getReservationWithId(NumericEntityId id) {
    return reservations.stream().filter(reservation -> reservation.getId().equals(id)).findFirst();
  }

  public List<Person> getWaitingList() {
    return Collections.unmodifiableList(waitingList);
  }

  @Transient
  public Optional<Person> getHeadOfWaitingList() {
    return waitingList.isEmpty() //
        ? Optional.empty() //
        : Optional.of(waitingList.get(0));
  }

  public boolean hasParticipants() {
    return !participants.keySet().isEmpty();
  }

  public boolean isParticipant(Person participant) {
    return participants.containsKey(participant);
  }

  public boolean hasReservations() {
    return !reservations.isEmpty();
  }

  public boolean hasWaitingList() {
    return !waitingList.isEmpty();
  }

  public boolean hasAccommodationInfo() {
    return accommodation != null && !accommodation.isEmpty();
  }

  public boolean hasParticipantsLimit() {
    return participantsLimit != null;
  }

  public void addReservation(Reservation reservation) {
    Assert.notNull(reservation, "reservation may not be null");
    reservations.add(reservation);

    registerDomainEventIfPossible(
        NewReservationEvent.of(this.getRelatedEvent().orElse(null), reservation));
  }

  public void removeReservation(Reservation reservation) {
    Assert.notNull(reservation, "reservation may not be null");
    Assert.isTrue(reservations.contains(reservation), "No such reservation: " + reservation);

    reservations.remove(reservation);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void putOnWaitingList(Person participant) {
    Assert.notNull(participant, "participant may not be null");
    Assert2.isFalse(participants.containsKey(participant),
        "Person is already on participants list: " + participant);
    Assert2.isFalse(waitingList.contains(participant),
        "Person already is on waiting list: " + participant);

    waitingList.add(participant);
    registerDomainEventIfPossible(
        NewWaitingListEntryEvent.of(this.getRelatedEvent().orElse(null), participant));
  }

  public void removeFromWaitingList(Person participant) {
    Assert.notNull(participant, "participant may not be null");
    Assert.isTrue(waitingList.contains(participant), "Not on waiting list: " + participant);

    waitingList.remove(participant);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void clearWaitingList() {
    waitingList.clear();
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void movePersonFromWaitingListToParticipants(Person person,
      RegistrationInfo registrationInfo) {
    Assert.notNull(person, "person may not be null");
    Assert.isTrue(waitingList.contains(person), "Not on waiting list: " + person);
    addParticipant(person, registrationInfo);
  }

  public ParticipantsList updateParticipantsLimit(Capacity limit) {
    if (limit != null && this.participantsLimit.isSmallerThan(limit)) {
      this.bookedOut = false;
    }

    this.participantsLimit = limit;

    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public ParticipantsList updateAccommodation(ExtendedRoomSpecification accommodation) {
    this.accommodation = accommodation;
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public boolean isBookedOut() {
    return bookedOut;
  }

  public void updateReservation(NumericEntityId reservationId, Reservation newInfo) {
    Assert.notNull(reservationId, "reservationId may not be null");
    Assert.notNull(newInfo, "newInfo may not be null");

    Reservation oldReservationData = reservations.stream()
        .filter(reservation -> reservation.getId().equals(reservationId)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("No reservation with ID %s for event %s", reservationId, this)));

    reservations.remove(oldReservationData);
    newInfo.setId(reservationId);
    reservations.add(newInfo);

    registerGenericEventUpdatedDomainEventIfPossible();
  }

  void addParticipant(Person participant, RegistrationInfo registrationInfo) {
    Assert.notNull(participant, "participant may not be null");
    Assert.notNull(registrationInfo, "registrationInfo may not be null");
    if (participants.containsKey(participant)) {
      throw new ExistingParticipantException(getRelatedEvent().orElse(null), participant);
    }

    participants.put(participant, registrationInfo);

    registerDomainEventIfPossible( //
        NewParticipantEvent.of(getRelatedEvent().orElse(null), //
            participant, registrationInfo));
  }

  void removeParticipant(Person participant) {
    Assert.notNull(participant, "participant may not be null");
    Assert.isTrue(participants.containsKey(participant), "No such participant: " + participant);

    participants.remove(participant);

    registerGenericEventUpdatedDomainEventIfPossible();
  }

  void updateParticipant(Person participant, RegistrationInfo newInfo) {
    Assert.notNull(participant, "participant to update may not be null");
    Assert.notNull(newInfo, "newInfo may not be null");
    Assert.isTrue(participants.containsKey(participant), "No such participant: " + participant);

    participants.put(participant, newInfo);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  void setParticipantsLimit(Capacity participantsLimit) {
    this.participantsLimit = participantsLimit;
  }

  void setAccommodation(ExtendedRoomSpecification accommodation) {
    this.accommodation = accommodation;
  }

  /**
   * This field will be set by the {@link ParticipationManager}
   *
   * @param bookedOut
   */
  void setBookedOut(boolean bookedOut) {
    this.bookedOut = bookedOut;
  }

  @JpaOnly
  private void setParticipants(Map<Person, RegistrationInfo> participants) {
    this.participants = participants;
  }

  @JpaOnly
  private void setReservations(List<Reservation> reservations) {
    this.reservations = reservations;
  }

  @JpaOnly
  private void setWaitingList(List<Person> waitingList) {
    this.waitingList = waitingList;
  }

  @Override
  @Nonnull
  public Iterator<Person> iterator() {
    return participants.keySet().iterator();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.AbstractEventInfo#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((accommodation == null) ? 0 : accommodation.hashCode());
    result = prime * result + ((participants == null) ? 0 : participants.hashCode());
    result = prime * result + ((participantsLimit == null) ? 0 : participantsLimit.hashCode());
    result = prime * result + ((reservations == null) ? 0 : reservations.hashCode());
    result = prime * result + ((waitingList == null) ? 0 : waitingList.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.AbstractEventInfo#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ParticipantsList other = (ParticipantsList) obj;
    if (accommodation == null) {
      if (other.accommodation != null) {
        return false;
      }
    } else if (!accommodation.equals(other.accommodation)) {
      return false;
    }
    if (participants == null) {
      if (other.participants != null) {
        return false;
      }
    } else if (!participants.equals(other.participants)) {
      return false;
    }
    if (participantsLimit == null) {
      if (other.participantsLimit != null) {
        return false;
      }
    } else if (!participantsLimit.equals(other.participantsLimit)) {
      return false;
    }
    if (reservations == null) {
      if (other.reservations != null) {
        return false;
      }
    } else if (!reservations.equals(other.reservations)) {
      return false;
    }
    if (waitingList == null) {
      if (other.waitingList != null) {
        return false;
      }
    } else if (!waitingList.equals(other.waitingList)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.model.events.AbstractEventInfo#toString()
   */
  @Override
  public String toString() {
    return "ParticipantsList [participantsLimit=" + participantsLimit + ", accommodation="
        + accommodation + ", participants=" + participants + ", reservations=" + reservations
        + ", waitingList=" + waitingList + "]";
  }

}
