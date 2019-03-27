package de.naju.adebar.model.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.support.NumericEntityId;
import de.naju.adebar.util.Assert2;

// TODO rethink how the list's state may be updated on who should be responsible for doing so. (as
// it is quite easy to get into an inconsistent state here)

/**
 * The participants (and reservations) of an {@link Event} will be stored here.
 * <p>
 * The {@code ParticipantsList} should be treated as a mere list which does not really perform any
 * calculations and acts as a plain storage. The entity responsible for maintaining the list as well
 * as its integrity is the {@link ParticipationManager}.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class ParticipantsList extends AbstractEventInfo implements Iterable<Person> {

  @Embedded
  @AttributeOverrides( //
  @AttributeOverride( //
      name = "value", //
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
  @JoinTable( //
      name = "eventWaitingList", //
      joinColumns = @JoinColumn(name = "eventId"), //
      inverseJoinColumns = @JoinColumn(name = "personId"))
  @OrderColumn(name = "position")
  private List<Person> waitingList;

  @Column(name = "bookedOut")
  private boolean bookedOut;

  /**
   * Minimal constructor.
   *
   * @param event the event to which this list should belong. May never be {@code null}.
   */
  ParticipantsList(Event event) {
    this(event, null, null);
  }

  /**
   * Full constructor.
   *
   * @param event the event for which this list is created. May never be {@code null}.
   * @param participantsLimit the maximum amount of participants that may attend the event. May be
   *        {@code null} to indicate that there is no such limit.
   * @param accomodation information about the available rooms for overnight accommodation. If this
   *        is specified it will be used instead of the {@code participantsLimit} to determine
   *        whether a new participant may be accommodated. May be {@code null} to indicate the
   *        absence of such information.
   */
  ParticipantsList( //
      Event event, //
      Capacity participantsLimit, //
      ExtendedRoomSpecification accomodation) {

    provideRelatedEvent(event);
    this.participantsLimit = participantsLimit;
    this.accommodation = accomodation;
    this.participants = new HashMap<>();
    this.reservations = new ArrayList<>();
    this.waitingList = new ArrayList<>();
  }

  /**
   * Default constructor just for JPA's sake.
   */
  @JpaOnly
  private ParticipantsList() {
    // pass
  }

  /**
   * Specifies the maximum number of participants that may attend the event.
   * <p>
   * When comparing this value to the current number of attendees, all persons will be treated
   * equally - no matter of which gender they are or during which period they attend the event. Thus
   * this number is completely detached from any accommodation which should be way more relevant in
   * practice. I.e. there may be no way to accommodate all participants even though the
   * {@code participantsLimit} is not yet reached or there may be a possible accommodation even
   * though the limit is exceeded already.
   * <p>
   * Thus this value will be ignored in further calculations as soon as a "real" accommodation is
   * provided. And will not be used to determine whether a new participant may attend the event as
   * soon as the accommodation information exists. The limit may, however, remain useful as a broad
   * indicator.
   *
   * @return the participants limit or {@code null} if none was specified.
   * @see #getAccommodation()
   */
  @Nullable
  public Capacity getParticipantsLimit() {
    return participantsLimit;
  }

  /**
   * Checks, whether an participants limit was specified for {@code this} event.
   *
   * @see #getParticipantsLimit()
   */
  public boolean hasParticipantsLimit() {
    return participantsLimit != null;
  }

  /**
   * Sets a new participants limit.
   * <p>
   * This will not influence {@link #getAccommodation() accommodation} or {@link #getParticipants()
   * participants} in any way. However {@link #isBookedOut()} will be updated if necessary.
   *
   * @param limit the new limit. May be {@code null}.
   * @return {@code this} info. Just for easy method chaining.
   */
  public ParticipantsList updateParticipantsLimit(Capacity limit) {
    assertEventNotCanceled();

    // TODO this method should not be public as a decrease in capacity may lead to participants
    // which no longer may attend.

    if (this.participantsLimit == null && limit == null) {
      return this;
    } else if (limit == null) {
      this.bookedOut = hasAccommodationInfo() ? this.bookedOut : false;
    } else {
      this.bookedOut = hasAccommodationInfo() //
          ? this.bookedOut //
          : !limit.isLargerThan(Capacity.of(participants.size()));
    }

    this.participantsLimit = limit;

    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  /**
   * Just the setter for the {@code participants limit} field.
   *
   * @see #getParticipantsLimit()
   */
  void setParticipantsLimit(@Nullable Capacity participantsLimit) {
    this.participantsLimit = participantsLimit;
  }

  /**
   * Provides detailed information about the available overnight accommodation. This includes a list
   * of all rooms as well as the number of beds within these rooms.
   * <p>
   * If this field is set, it will be used to check the remaining capacity whenever a new
   * participant wants to attend the event.
   *
   * @return the accommodation or {@code null} if none was specified.
   * @see #getParticipantsLimit()
   */
  @Nullable
  public ExtendedRoomSpecification getAccommodation() {
    return accommodation;
  }

  /**
   * Attaches a new accommodation info for {@code this} event.
   * <p>
   * This will not influence the {@link #getParticipantsLimit() participants limit} or the actual
   * {@link #getParticipants() participants} in any way.
   *
   * @param accommodation the new info. May be {@code null}.
   * @return {@code this} info. Just for easy method chaining.
   */
  public ParticipantsList updateAccommodation(ExtendedRoomSpecification accommodation) {
    assertEventNotCanceled();
    this.accommodation = accommodation;
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  /**
   * Just the setter for the {@code accommodation} field.
   *
   * @see #getAccommodation()
   */
  void setAccommodation(@Nullable ExtendedRoomSpecification accommodation) {
    this.accommodation = accommodation;
  }

  /**
   * Checks, whether an accommodation info was attached to {@code this} event.
   *
   * @see #getAccommodation()
   */
  public boolean hasAccommodationInfo() {
    return accommodation != null && !accommodation.isEmpty();
  }

  /**
   * Provides all attendees of this event.
   * <p>
   * In order to attend an event, three conditions must hold:
   * <ul>
   * <li>the person has to be registered as participant (see {@link Person#isParticipant()})</li>
   * <li>the event must have at least one more slot open for participation</li>
   * <li>the person usually has to satisfy the minimum participation age (see
   * {@link ParticipationInfo#getMinimumParticipantAge()})</li>
   * </ul>
   * However the last condition may sometimes be ignored.
   *
   * @see #getParticipants()
   * @see #getParticipationDetailsFor(Person)
   */
  @NonNull
  @Transient
  public Collection<Person> getParticipantsList() {
    // TODO rename method to get()
    return participants.keySet();
  }

  /**
   * Provides all attendees of this event as well as their corresponding {@link RegistrationInfo}.
   *
   * @see #getParticipantsList()
   * @see #getParticipationDetailsFor(Person)
   */
  @NonNull
  public Map<Person, RegistrationInfo> getParticipants() {
    return participants;
  }

  /**
   * For each participant there will be some special information regarding his/her attendance of
   * this event, such as the participation time or the selected arrival option.
   *
   * @param person
   * @return the registration info that belongs to the {@code person}
   * @throws IllegalArgumentException if the given {@code person} does not participate
   * @see #getParticipants()
   * @see #getParticipantsList()
   */
  @NonNull
  public RegistrationInfo getParticipationDetailsFor(Person person) {
    if (!isParticipant(person)) {
      throw new IllegalArgumentException("Not a participant: " + person);
    }
    return participants.get(person);
  }

  /**
   * Just the setter for the participants field.
   *
   * @see #getParticipants()
   */
  @JpaOnly
  private void setParticipants(@NonNull Map<Person, RegistrationInfo> participants) {
    this.participants = participants;
  }

  /**
   * Saves a new attendee for {@code this} event, assuming that all checks on whether the person may
   * actually attend have already passed. Therefore no further checks regarding the participant's
   * age, gender, or the remaining capacity will be performed.
   *
   * @throws ExistingParticipantException if the person does already participate
   */
  void addParticipant(Person participant, RegistrationInfo registrationInfo) {
    assertEventNotCanceled();
    Assert.notNull(participant, "participant may not be null");
    Assert.notNull(registrationInfo, "registrationInfo may not be null");

    if (participants.containsKey(participant)) {
      throw new ExistingParticipantException(getRelatedEvent().orElse(null), participant);
    }

    participants.put(participant, registrationInfo);

    registerDomainEventIfPossible( //
        NewParticipantEvent.of( //
            getRelatedEvent().orElse(null), //
            participant, //
            registrationInfo));
  }

  /**
   * Deletes a participant.
   */
  void removeParticipant(Person participant) {
    assertEventNotCanceled();
    Assert.notNull(participant, "participant may not be null");
    Assert.isTrue(participants.containsKey(participant), "No such participant: " + participant);

    participants.remove(participant);

    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Updates the registration info of a participant.
   */
  void updateParticipant(Person participant, @NonNull RegistrationInfo newInfo) {
    assertEventNotCanceled();
    Assert.notNull(participant, "participant to update may not be null");
    Assert.notNull(newInfo, "newInfo may not be null");
    Assert.isTrue(participants.containsKey(participant), "No such participant: " + participant);

    participants.put(participant, newInfo);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Calculates the current number of participants. The calculation will not take the participation
   * times into account, i.e. if two participants attend the event during disjunct time spans, they
   * will both be counted.
   */
  public int getParticipantsCount() {
    return participants.size();
  }

  /**
   * Checks, whether there are any participants, yet.
   *
   * @see #getParticipants()
   */
  public boolean hasParticipants() {
    return !participants.keySet().isEmpty();
  }

  /**
   * Checks, whether some person attends {@code this} event.
   */
  public boolean isParticipant(@NonNull Person participant) {
    return participants.containsKey(participant);
  }

  /**
   * Checks, whether the number of participants matches the overnight capacity available.
   * <p>
   * Beware that {@code this} list is mostly not responsible for actually calculating whether it is
   * booked out or not, as this process depends on a lot of business knowledge which does not belong
   * here. Think of the participants list as just a list of persons which happen to attend the
   * event. There still needs to be an instance (the {@link ParticipationManager} in this case)
   * responsible for actually putting people on {@code this} list. Thus it is up to the manager to
   * determine whether there is enough capacity available or not.
   * <p>
   * However in some cases {@code this} list will update itself nevertheless. That is, whenever it
   * knows that there definitely is enough capacity available, the flag will be set accordingly.
   * These cases are:
   * <ul>
   * <li>a new participants limit was set which is larger than the old one</li>
   * <li>a participant was removed from {@code this} list</li>
   * <li>the participants limit is dropped and there is no accommodation info</li>
   * </ul>
   */
  public boolean isBookedOut() {
    return bookedOut;
  }

  /**
   * This field will be set by the {@link ParticipationManager}.
   *
   * @see #isBookedOut()
   * @see #setBookedOut(boolean)
   */
  void internal_setBookedOut(boolean bookedOut) {
    assertEventNotCanceled();
    this.bookedOut = bookedOut;
  }

  /**
   * Just the setter for the {@code booked out} field.
   *
   * @see #isBookedOut()
   */
  @JpaOnly
  private void setBookedOut(boolean bookedOut) {
    this.bookedOut = bookedOut;
  }

  /**
   * If new persons want to attend an event but their participation information is not yet known, or
   * if some capacity should be put aside for yet unknown persons, a reservation may be placed. It
   * will contain the number of slots that should be put aside along other information.
   * <strong>Beware that reservations will not be accounted when the remaining capacity is
   * calculated</strong>. This is due to not knowing how to accommodate the persons as e.g. their
   * gender is not specified.
   */
  @NonNull
  public Collection<Reservation> getReservations() {
    return Collections.unmodifiableCollection(reservations);
  }

  /**
   * Queries for a reservation.
   */
  @Transient
  @NonNull
  public Optional<Reservation> getReservationWithId(@NonNull NumericEntityId id) {
    return reservations.stream().filter(reservation -> reservation.getId().equals(id)).findFirst();
  }

  /**
   * Checks, whether there is at least one reservation for {@code this} event.
   *
   * @see #getReservations()
   */
  public boolean hasReservations() {
    return !reservations.isEmpty();
  }

  /**
   * Places a new reservation.
   */
  public void addReservation(@NonNull Reservation reservation) {
    assertEventNotCanceled();
    Assert.notNull(reservation, "reservation may not be null");
    Assert2.isFalse(reservations.contains(reservation),
        "Reservation was already placed: " + reservation);

    reservations.add(reservation);

    registerDomainEventIfPossible( //
        NewReservationEvent.of( //
            this.getRelatedEvent().orElse(null), //
            reservation));
  }

  /**
   * Drops a reservation.
   */
  public void removeReservation(@NonNull Reservation reservation) {
    assertEventNotCanceled();
    Assert.notNull(reservation, "reservation may not be null");
    Assert.isTrue(reservations.contains(reservation), "No such reservation: " + reservation);

    reservations.remove(reservation);

    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Replaces a reservation with some updated data.
   * <p>
   * The ID of the {@code newInfo} does not have to match the current reservation's ID. If they
   * differ, the current ID will be retained and only the new data will be adopted.
   *
   * @param reservationId the ID of the reservation to update.
   * @param newInfo the new data to use.
   */
  public void updateReservation(NumericEntityId reservationId, Reservation newInfo) {
    assertEventNotCanceled();
    Assert.notNull(reservationId, "reservationId may not be null");
    Assert.notNull(newInfo, "newInfo may not be null");

    Reservation oldReservationData = reservations.stream() //
        .filter(reservation -> reservation.getId().equals(reservationId)) //
        .findFirst() //
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("No reservation with ID %s for event %s", reservationId, this)));

    reservations.remove(oldReservationData);

    // we need to retain the reservation ID but do not want to copy all data field-by-field
    // thus we will simply create a copy of the new reservation info and set the ID there.
    Reservation updated = new Reservation(newInfo);
    updated.setId(reservationId);
    reservations.add(updated);

    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Just the setter for the reservations field.
   */
  @JpaOnly
  private void setReservations(List<Reservation> reservations) {
    this.reservations = reservations;
  }

  /**
   * If an event is booked out no new participants may attend it. However, sometimes people will
   * de-register again. In order to fill the newly available slot, a waiting list may be used. It
   * will feature all persons that wanted to participate after the event was booked out. The list
   * will be sorted by registration date with the first participant as its head element.
   */
  @NonNull
  public List<Person> getWaitingList() {
    return Collections.unmodifiableList(waitingList);
  }

  /**
   * Provides the first person that is on the waiting list (if any).
   *
   * @see #getWaitingList()
   */
  @Transient
  @NonNull
  public Optional<Person> getHeadOfWaitingList() {
    return waitingList.isEmpty() //
        ? Optional.empty() //
        : Optional.of(waitingList.get(0));
  }

  /**
   * Checks, whether there is at least one person on the waiting list.
   *
   * @see #getWaitingList()
   */
  public boolean hasWaitingList() {
    return !waitingList.isEmpty();
  }

  /**
   * Enqueues a new person at the end of the waiting list.
   *
   * @return the position of the person on the waiting list, starting at one.
   * @see #getWaitingList()
   */
  public Position putOnWaitingList(@NonNull Person participant) {
    assertEventNotCanceled();
    Assert.notNull(participant, "participant may not be null");
    Assert2.isFalse(participants.containsKey(participant),
        "Person is already on participants list: " + participant);
    Assert2.isFalse(waitingList.contains(participant),
        "Person already is on waiting list: " + participant);

    waitingList.add(participant);
    registerDomainEventIfPossible( //
        NewWaitingListEntryEvent.of( //
            this.getRelatedEvent().orElse(null), //
            participant));

    return Position.of(waitingList.size());
  }

  /**
   * Drops a person from the waiting list.
   *
   * @see #getWaitingList()
   */
  public void removeFromWaitingList(@NonNull Person participant) {
    assertEventNotCanceled();
    Assert.notNull(participant, "participant may not be null");
    Assert.isTrue(waitingList.contains(participant), "Not on waiting list: " + participant);

    waitingList.remove(participant);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Drops all persons from the waiting list.
   *
   * @see #getWaitingList()
   */
  public void clearWaitingList() {
    assertEventNotCanceled();

    waitingList.clear();
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Drops a person from the waiting list and adds her/him as a participant instead.
   * <p>
   * This basically works as a shortcut to {@link #removeFromWaitingList(Person)} and
   * {@link #addParticipant(Person, RegistrationInfo)}.
   *
   * @see #getWaitingList()
   * @see #addParticipant(Person, RegistrationInfo)
   * @see #removeFromWaitingList(Person)
   */
  void movePersonFromWaitingListToParticipants(@NonNull Person person,
      @NonNull RegistrationInfo registrationInfo) {
    assertEventNotCanceled();
    Assert.notNull(person, "person may not be null");
    Assert.isTrue(waitingList.contains(person), "Not on waiting list: " + person);

    addParticipant(person, registrationInfo);
    registerDomainEventIfPossible( //
        NewParticipantEvent.of( //
            getRelatedEvent().orElse(null), //
            person, //
            registrationInfo));
  }

  /**
   * Just the setter for the {@code waiting list} field.
   *
   * @see #getWaitingList()
   */
  @JpaOnly
  private void setWaitingList(List<Person> waitingList) {
    this.waitingList = waitingList;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Iterable#iterator()
   */
  @Override
  @NonNull
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
