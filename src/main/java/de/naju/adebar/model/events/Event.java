package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.events.participants.ExistingParticipantException;
import de.naju.adebar.model.events.participants.ParticipantsList;
import de.naju.adebar.model.events.participants.ParticipationInfo;
import de.naju.adebar.model.events.participants.PersonIsTooYoungException;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoActivistException;
import de.naju.adebar.model.persons.exceptions.NoParticipantException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

/**
 * Abstraction of an event. It may be a regular camp or any other kind of event such as workshops or
 * presentations. Maybe there will be more precise classes for the different event-types one day..
 *
 * <p>
 * Events may be automatically sorted according to their start date, i.e. the event that takes place
 * earlier is considered "less".
 *
 * @author Rico Bergmann
 */
@Entity(name = "event")
public class Event implements Comparable<Event> {

  private static final String START_TIME_AFTER_END_TIME = "Start time may not be after end time";
  private static final String PERSON_NOT_IN_TO_CONTACT_LIST =
      "Person is not registered as 'should be contacted': ";

  @EmbeddedId
  @Column(name = "id")
  private EventId id;

  @Column(name = "name")
  private String name;

  @Column(name = "startTime")
  private LocalDateTime startTime;

  @Column(name = "endTime")
  private LocalDateTime endTime;

  @Column(name = "minParticipantAge")
  private Age minimumParticipantAge;

  @Column(name = "intParticipationFee")
  private Money internalParticipationFee;

  @Column(name = "extParticipationFee")
  private Money externalParticipationFee;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "street", column = @Column(name = "locationStreet")),
      @AttributeOverride(name = "zip", column = @Column(name = "locationZip")),
      @AttributeOverride(name = "city", column = @Column(name = "locationCity")),
      @AttributeOverride(name = "additionalInfo", column = @Column(name = "locationHints"))})
  private Address place;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "counselors", inverseJoinColumns = @JoinColumn(name = "counselorId"))
  private List<Person> counselors;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "eventOrganizers", inverseJoinColumns = @JoinColumn(name = "organizerId"))
  private List<Person> organizers;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "eventToContact", joinColumns = @JoinColumn(name = "eventId"))
  @MapKeyJoinColumn(name = "personId")
  @Column(name = "contactInfo")
  private Map<Person, String> personsToContact;

  @ElementCollection(fetch = FetchType.LAZY)
  private List<Lecture> lectures;

  @OneToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private ParticipantsList participantsList;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "localGroupEvents", //
      joinColumns = @JoinColumn(name = "eventId"), //
      inverseJoinColumns = @JoinColumn(name = "localGroupId"))
  private LocalGroup localGroup;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "projectEvents", //
      joinColumns = @JoinColumn(name = "eventId"), //
      inverseJoinColumns = @JoinColumn(name = "projectId"))
  private Project project;

  /**
   * Simplified constructor initializing the most important data
   *
   * @param name the event's name
   * @param startTime the event's start time
   * @param endTime the event's end time
   */
  Event(EventId id, String name, LocalDateTime startTime, LocalDateTime endTime) {
    Assert.notNull(id, "Event id may not be null!");
    Assert.hasText(name, "Name must contain text but was: " + name);
    Assert.notNull(startTime, "Start time must not be null!");
    Assert.notNull(endTime, "End time must not be null!");
    Assert.isTrue(!startTime.isAfter(endTime), "Start time must be before end time");
    this.id = id;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.place = new Address("", "", "");
    this.counselors = new LinkedList<>();
    this.organizers = new LinkedList<>();
    this.personsToContact = new HashMap<>();
    this.lectures = new LinkedList<>();
    this.participantsList = new ParticipantsList(this, Integer.MAX_VALUE);
  }

  /**
   * Default constructor just for JPA's sake. Not to be used from outside, hence {@code private}.
   */
  @JpaOnly
  private Event() {}

  /**
   * @return the event's ID (= primary key)
   */
  public EventId getId() {
    return id;
  }

  // getter and setter

  /**
   * @param id the event's id
   */
  protected void setId(EventId id) {
    this.id = id;
  }

  /**
   * @return the event's name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the event's name
   * @throws IllegalArgumentException if the name is empty or {@code null}
   */
  public void setName(String name) {
    Assert.hasText(name, "Name may not be null nor empty, but was: " + name);
    this.name = name;
  }

  /**
   * @return the time the event starts
   */
  public LocalDateTime getStartTime() {
    return startTime;
  }

  /**
   * @param startTime the time the event starts
   * @throws IllegalArgumentException if the start time is after the end time or {@code null}
   */
  public void setStartTime(LocalDateTime startTime) {
    Assert.notNull(startTime, "Start time may not be null");

    // If an instance is re-initialized from database, JPA will use it's
    // empty constructor and set all the fields
    // afterwards. Therefore end time may be null and we must check this
    // before validating the start date
    if (endTime != null) {
      Assert.isTrue(!endTime.isBefore(startTime), START_TIME_AFTER_END_TIME);
    }
    this.startTime = startTime;
  }

  /**
   * @return the time the event ends
   */
  public LocalDateTime getEndTime() {
    return endTime;
  }

  /**
   * @param endTime the time the event ends
   * @throws IllegalArgumentException if the start time is after the end time or the end time is
   *     {@code null}
   */
  public void setEndTime(LocalDateTime endTime) {
    Assert.notNull(endTime, "End time may not be null");

    // If an instance is re-initialized from database, JPA will use it's
    // empty constructor and set all the fields
    // afterwards. Therefore start time may be null and we must check this
    // before validating the end date
    if (startTime != null) {
      Assert.isTrue(!endTime.isBefore(startTime), START_TIME_AFTER_END_TIME);
    }
    this.endTime = endTime;
  }

  /**
   * @return whether the event takes place in the future
   */
  @Transient
  public boolean isProspective() {
    return startTime.isAfter(LocalDateTime.now());
  }

  /**
   * @return the number of persons that may at most participate
   */
  @Transient
  public int getParticipantsLimit() {
    return participantsList.getParticipantsLimit();
  }

  /**
   * @param participantsLimit the number of persons that may at most participate
   * @throws IllegalArgumentException if the participants limit is non-positive
   */
  public void setParticipantsLimit(int participantsLimit) {
    Assert.isTrue(participantsLimit > 0,
        "Participants limit must be positive, but was: " + participantsLimit);
    this.participantsList.setParticipantsLimit(participantsLimit);
  }

  /**
   * @return the age which new participants must be at least
   */
  public Age getMinimumParticipantAge() {
    return minimumParticipantAge;
  }

  /**
   * @param minimumParticipantAge the age which new participants must be at least
   * @throws IllegalArgumentException if the minimum participant age is negative
   */
  public void setMinimumParticipantAge(Age minimumParticipantAge) {
    this.minimumParticipantAge = minimumParticipantAge;
  }

  /**
   * The internal participation fee is used for NABU members.
   *
   * @return the fee to pay in order to participate. May be {@code null}.
   */
  public Money getInternalParticipationFee() {
    return internalParticipationFee;
  }

  /**
   * @param internalParticipationFee the fee to pay in order to participate
   * @throws IllegalArgumentException if the participation fee is negative
   */
  public void setInternalParticipationFee(Money internalParticipationFee) {
    if (internalParticipationFee != null) {
      Assert.isTrue(internalParticipationFee.isPositiveOrZero(),
          "Participation fee may not be negative, but was: " + internalParticipationFee);
    }
    this.internalParticipationFee = internalParticipationFee;
  }

  /**
   * The external participation fee is used for all participants who are not members of the NABU.
   *
   * @return the fee to pay in order to participate. May be {@code null}.
   */
  public Money getExternalParticipationFee() {
    return externalParticipationFee;
  }

  /**
   * @param externalParticipationFee the fee to pay in order to participate
   * @throws IllegalArgumentException if the participation fee is negative
   */
  public void setExternalParticipationFee(Money externalParticipationFee) {
    if (externalParticipationFee != null) {
      Assert.isTrue(externalParticipationFee.isPositiveOrZero(),
          "Participation fee may not be negative, but was: " + externalParticipationFee);
    }
    this.externalParticipationFee = externalParticipationFee;
  }

  /**
   * @return the address where the event takes place. May be {@code null}
   */
  public Address getPlace() {
    return place;
  }

  /**
   * @param place the address where the event takes place
   */
  public void setPlace(Address place) {
    this.place = place;
  }

  /**
   * @return the persons who participate in the event
   */
  @Transient
  public Iterable<Person> getParticipants() {
    return participantsList.getParticipants().keySet();
  }

  /**
   * @return the persons who participate in the event but have not payed the fee yet
   */
  @Transient
  public Iterable<Person> getParticipantsWithFeeNotPayed() {
    return participantsList.getParticipantsWithFeeNotPayed();
  }

  /**
   * @return the persons who participate in the event but have not sent the "real" participation
   *     form yet
   */
  @Transient
  public Iterable<Person> getParticipantsWithFormNotReceived() {
    return participantsList.getParticipantsWithFormNotReceived();
  }

  /**
   * @return all reservations for the event
   */
  public Iterable<Reservation> getReservations() {
    return participantsList.getReservations();
  }

  /**
   * @return the activists who take care of the event - i. e. are in attendance when the event takes
   *     place
   */
  public Iterable<Person> getCounselors() {
    return counselors;
  }

  /**
   * @param counselors the counselors of the event
   */
  protected void setCounselors(List<Person> counselors) {
    this.counselors = counselors;
  }

  /**
   * @return the activists who organize the event
   */
  public Iterable<Person> getOrganizers() {
    return organizers;
  }

  /**
   * @param organizers the event's organizers
   */
  protected void setOrganizers(List<Person> organizers) {
    this.organizers = organizers;
  }

  /**
   * @return all persons that should be contacted for the event, in a map as {@code person ->
   *     remark}
   */
  public Map<Person, String> getPersonsToContact() {
    return Collections.unmodifiableMap(personsToContact);
  }

  /**
   * @param personsToContact all persons that should be contacted for the event, in a map as
   *     {@code person -> remark}
   */
  protected void setPersonsToContact(Map<Person, String> personsToContact) {
    this.personsToContact = personsToContact;
  }

  /**
   * @return the lectures held during the event
   */
  public Iterable<Lecture> getLectures() {
    return lectures;
  }

  /**
   * @param lectures the lectures held on the event
   */
  protected void setLectures(List<Lecture> lectures) {
    this.lectures = lectures;
  }

  /**
   * A read-only map (participant -> participationInfo). Beware: all write-operations will result in
   * an {@link UnsupportedOperationException}!
   *
   * @return information about each participant
   *
   * @see ParticipationInfo
   */
  @Transient
  public Map<Person, ParticipationInfo> getParticipationInfo() {
    return participantsList.getParticipants();
  }

  /**
   * @return the waiting list
   */
  @Transient
  public Iterable<Person> getWaitingList() {
    return participantsList.getWaitingList();
  }

  protected ParticipantsList getParticipantsList() {
    return participantsList;
  }

  protected void setParticipantsList(ParticipantsList participantsList) {
    if (participantsList.getEvent() != this.id) {
      throw new IllegalArgumentException();
    }
    this.participantsList = participantsList;
  }

  /**
   * @return the number of participants
   */
  @Transient
  public int getParticipantsCount() {
    return participantsList.getParticipantsCount();
  }

  // "advanced" getter

  /**
   * @return {@code true} if an participant limit was specified, {@code false} otherwise
   */
  public boolean hasParticipantsLimit() {
    return participantsList.hasParticipantsLimit();
  }

  /**
   * @return the number of spare participation slots
   */
  @Transient
  public int getRemainingCapacity() {
    return participantsList.getRemainingCapacity();
  }

  /**
   * @return {@code true} if no more persons may participate
   */
  @Transient
  public boolean isBookedOut() {
    return participantsList.isBookedOut();
  }

  /**
   * @return {@code true} if there are reservations for this event, or {@code false} otherwise
   */
  @Transient
  public boolean hasReservations() {
    return participantsList.hasReservations();
  }

  @Transient
  public boolean hasMinimumParticipantAge() {
    return minimumParticipantAge != null;
  }

  /**
   * @param person the person to check
   * @return {@code true} if the person may participate in the event regarding to age-restrictions,
   *     {@code false} otherwise
   *
   * @throws IllegalArgumentException if the person is no camp participant
   */
  @Transient
  public boolean satisfiesAgeRestrictions(Person person) {
    if (!person.isParticipant()) {
      throw new IllegalArgumentException("Person is not a camp participant: " + person);
    } else if (!person.getParticipantProfile().hasDateOfBirth()) {
      return true;
    } else if (!hasMinimumParticipantAge()) {
      return true;
    }
    return !person.getParticipantProfile().calculateAge().isYoungerThan(minimumParticipantAge);
  }

  /**
   * Queries for specific reservation data
   *
   * @param description the description (= ID) of the reservation to query for
   * @return the reservation
   */
  @Transient
  public Reservation getReservationFor(String description) {
    return participantsList.getReservationFor(description);
  }

  /**
   * @return {@code true} if the waiting list is used, {@code false} otherwise
   */
  public boolean hasWaitingList() {
    return participantsList.hasWaitingList();
  }

  /**
   * @param person the person to check
   * @return {@code true} if the person is wait-listed, {@code false} otherwise
   */
  @Transient
  public boolean isOnWaitingList(Person person) {
    return participantsList.isOnWaitingList(person);
  }

  /**
   * @return the head of the waiting list
   */
  @Transient
  public Person getTopWaitingListSpot() {
    return participantsList.getTopWaitingListSpot();
  }

  /**
   * @param person the person to query for
   * @return the person's position on the waiting list
   */
  @Transient
  public int getWaitingListSpotFor(Person person) {
    return participantsList.getWaitingListSpotFor(person);
  }

  /**
   * Updates start and end time simultaneously. Useful to prevent contract violations that would
   * occur when doing the same through a sequential call to the related setters
   *
   * @param startTime
   * @param endTime
   * @throws IllegalArgumentException if {@code startTime < endTime} or one of the parameters is
   *     {@code null}
   */
  public void updateTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
    Assert.notNull(startTime, "Start time may not be null!");
    Assert.notNull(endTime, "End time may not be null!");
    Assert.isTrue(!endTime.isBefore(startTime), START_TIME_AFTER_END_TIME);
    this.startTime = startTime;
    this.endTime = endTime;
  }

  // modification methods

  /**
   * Adds a new participant
   *
   * @param person the person to participate in the event
   * @throws NoParticipantException if the person is not registered as a possible participant
   * @throws ExistingParticipantException if the person already participates
   * @throws PersonIsTooYoungException if the person does not have the required age
   * @throws BookedOutException if no more persons may participate
   */
  public void addParticipant(Person person) {
    Assert.notNull(person, "Participant to add may not be null!");
    if (!person.isParticipant()) {
      throw new NoParticipantException("Person is no camp participant: " + person);
    } else if (isParticipant(person)) {
      throw new ExistingParticipantException("Person does already participate: " + person);
    } else if (!satisfiesAgeRestrictions(person)) {
      throw new PersonIsTooYoungException(
          String.format("Person is too young: must be %d years old but was born on %s",
              minimumParticipantAge, person.getParticipantProfile().getDateOfBirth()));
    }
    participantsList.addParticipant(person);
  }

  /**
   * Adds a new participant to the event, regardless of eventual violations of the minimum
   * participation age
   *
   * @param person the person to participate in the event
   * @throws NoParticipantException if the person is no participant
   * @throws ExistingParticipantException if the person already participates
   * @throws BookedOutException if no more persons may participate
   */
  public void addParticipantIgnoreAge(Person person) {
    Assert.notNull(person, "Participant to add may not be null!");
    if (!person.isParticipant()) {
      throw new NoParticipantException("Person is no camp participant: " + person);
    }
    participantsList.addParticipant(person);
  }

  /**
   * @param person the person to remove from the list of participants
   * @throws IllegalArgumentException if the person did not participate
   */
  public void removeParticipant(Person person) {
    participantsList.removeParticipant(person);
  }

  /**
   * @param person the person to check
   * @return {@code true} if the person participates in the event, {@code false} otherwise
   */
  public boolean isParticipant(Person person) {
    return participantsList.isParticipant(person);
  }

  /**
   * @param person the person to get the participation info for
   * @return the participation info
   */
  public ParticipationInfo getParticipationInfo(Person person) {
    return participantsList.getParticipationInfoFor(person);
  }

  /**
   * @param person the person to update
   * @param newInfo the new participation info
   */
  public void updateParticipationInfo(Person person, ParticipationInfo newInfo) {
    if (!isParticipant(person)) {
      throw new IllegalArgumentException("Person does not participate: " + person);
    }
    Assert.notNull(newInfo, "New participation info may not be null!");
    participantsList.updateParticipationInfoFor(person, newInfo);
  }

  /**
   * Creates a new reservation
   *
   * @param description the description of the reservation
   * @return the new reservation
   */
  public Reservation addReservationFor(String description) {
    Reservation reservation = new Reservation(description);
    participantsList.addReservation(reservation);
    return reservation;
  }

  /**
   * Creates a new reservation
   *
   * @param description the description of the reservation
   * @param numberOfSlots the capacity that should be reserved
   * @return the new reservation
   */
  public Reservation addReservationFor(String description, int numberOfSlots) {
    Reservation reservation = new Reservation(description, numberOfSlots);
    participantsList.addReservation(reservation);
    return reservation;
  }

  /**
   * Creates a new reservation
   *
   * @param description the description of the reservation
   * @param numberOfSlots the capacity that should be reserved
   * @param email an email to contact for the reservation
   * @return the new reservation
   */
  public Reservation addReservationFor(String description, int numberOfSlots, String email) {
    Reservation reservation = new Reservation(description, numberOfSlots, email);
    participantsList.addReservation(reservation);
    return reservation;
  }

  /**
   * Deletes a reservation
   *
   * @param description the description (= ID) of the reservation
   */
  public void removeReservation(String description) {
    participantsList.removeReservation(description);
  }

  /**
   * Updates a reservation
   *
   * @param description the description (= ID) of the reservation
   * @param newData the new data to use
   */
  public void updateReservation(String description, Reservation newData) {
    participantsList.updateReservation(description, newData);
  }

  /**
   * @param person the activist to make counselor
   * @throws IllegalArgumentException if the activist is already a counselor
   * @throws NoActivistException if the person is no activist
   */
  public void addCounselor(Person person) {
    Assert.notNull(person, "Counselor to add may not be null!");
    if (!person.isActivist()) {
      throw new NoActivistException("Person is no activist: " + person);
    } else if (isCounselor(person)) {
      throw new IllegalArgumentException("Activist is already counselor: " + person);
    }
    counselors.add(person);
  }

  /**
   * @param activist the activist to remove as counselor
   * @throws IllegalArgumentException if the activist is currently no counselor
   */
  public void removeCounselor(Person activist) {
    if (!isCounselor(activist)) {
      throw new IllegalArgumentException("Activist is no counselor: " + activist);
    }
    counselors.remove(activist);
  }

  /**
   * @param activist the activist to check
   * @return {@code true} if the activist is a counselor or {@code false} otherwise
   */
  public boolean isCounselor(Person activist) {
    return counselors.contains(activist);
  }

  /**
   * @param person the activist to make organizer
   * @throws IllegalArgumentException if the activist already is an organizer
   * @throws NoActivistException if the person is no activist
   */
  public void addOrganizer(Person person) {
    Assert.notNull(person, "Organizer to add may not be null!");
    if (!person.isActivist()) {
      throw new NoActivistException("Person is no activist: " + person);
    } else if (isOrganizer(person)) {
      throw new IllegalArgumentException("Activist is already organizer: " + person);
    }
    organizers.add(person);
  }

  /**
   * @param activist the organizer to remove as organizer
   * @throws IllegalArgumentException if the activist was no organizer
   */
  public void removeOrganizer(Person activist) {
    if (!isOrganizer(activist)) {
      throw new IllegalArgumentException("Activist is no organizer: " + activist);
    }
    organizers.remove(activist);
  }

  /**
   * @param activist the activist to check
   * @return {@code true} if the activist is organizer or {@code false} otherwise
   */
  public boolean isOrganizer(Person activist) {
    return organizers.contains(activist);
  }

  /**
   * Enqueues a person at the end of the waiting list
   *
   * @param person the person to add
   */
  public void putOnWaitingList(Person person) {
    participantsList.putOnWaitingList(person);
  }

  /**
   * Removes a person from the waiting list
   *
   * @param person the person to remove
   */
  public void removeFromWaitingList(Person person) {
    participantsList.removeFromWaitingList(person);
  }

  /**
   * Moves the head of the waiting list to the participants list
   */
  public void applyTopWaitingListSpot() {
    participantsList.applyTopWaitingListSpot();
  }

  /**
   * Moves a person from the waiting list to the participants list
   *
   * @param person the person to move
   */
  public void applyWaitingListEntryFor(Person person) {
    if (!isOnWaitingList(person)) {
      throw new IllegalArgumentException("Person is not wait-listed: " + person);
    }
    // we have to add the participant prior to adjusting the waiting list
    // if the addition fails, the person should still be wait-listed
    addParticipant(person);
    removeFromWaitingList(person);
  }

  /**
   * Adds a new person to contact for the event
   *
   * @param person the person
   * @param remark remarks regarding the reason of contact
   */
  public void addPersonToContact(Person person, String remark) {
    if (shouldBeContacted(person)) {
      throw new IllegalStateException("Person should already be contacted: " + person);
    }
    personsToContact.put(person, remark);
  }

  /**
   * Removes a person from the list of persons to be contacted for the event
   *
   * @param person the person
   */
  public void removePersonToContact(Person person) {
    if (!shouldBeContacted(person)) {
      throw new IllegalArgumentException(PERSON_NOT_IN_TO_CONTACT_LIST + person);
    }
    personsToContact.remove(person);
  }

  /**
   * @param person the person to check
   * @return {@code true} if the person is in the list of persons to be contacted for the event
   */
  public boolean shouldBeContacted(Person person) {
    return personsToContact.containsKey(person);
  }

  /**
   * @param person the person to query for
   * @return the remarks for the person to be contacted for the event
   */
  @Transient
  public String getContactRemarkFor(Person person) {
    if (!shouldBeContacted(person)) {
      throw new IllegalArgumentException(PERSON_NOT_IN_TO_CONTACT_LIST + person);
    }
    return personsToContact.get(person);
  }

  /**
   * @param person the person to update
   * @param remark the new remarks
   */
  public void updatePersonToContact(Person person, String remark) {
    if (!shouldBeContacted(person)) {
      throw new IllegalArgumentException(PERSON_NOT_IN_TO_CONTACT_LIST + person);
    }
    personsToContact.replace(person, remark);
  }

  /**
   * @param lecture the lecture to add
   * @throws IllegalArgumentException if <strong>exactly</strong> this lecture is already being
   *     held
   * @see Lecture
   */
  public void addLecture(Lecture lecture) {
    if (lectures.contains(lecture)) {
      throw new IllegalArgumentException("Lecture is already given: " + lecture);
    }
    lectures.add(lecture);
  }

  /**
   * @param referent the referent to search for
   * @return all lectures which the referent holds within the event
   */
  public Iterable<Lecture> getLecturesForReferent(Person referent) {
    LinkedList<Lecture> referentsLectures = new LinkedList<>();
    for (Lecture lecture : lectures) {
      if (lecture.getReferent().equals(referent)) {
        referentsLectures.add(lecture);
      }
    }
    return referentsLectures;
  }

  /**
   * @param lecture the lecture to remove
   * @throws IllegalArgumentException if the lecture is not being given in the event
   */
  public void removeLecture(Lecture lecture) {
    if (!lectures.contains(lecture)) {
      throw new IllegalArgumentException("No such lecture given on event: " + lecture);
    }
    lectures.remove(lecture);
  }

  /**
   * @return the local group the event belongs to
   */
  public LocalGroup getLocalGroup() {
    return localGroup;
  }

  /**
   * @param localGroup the local group the event belongs to
   */
  @JpaOnly
  private void setLocalGroup(LocalGroup localGroup) {
    this.localGroup = localGroup;
  }

  /**
   * @return whether the event belongs to a local group
   */
  @Transient
  public boolean isForLocalGroup() {
    return localGroup != null;
  }

  /**
   * @return the project the event belongs to
   */
  public Project getProject() {
    return project;
  }

  /**
   * @param project the project the event belongs to
   */
  @JpaOnly
  private void setProject(Project project) {
    this.project = project;
  }

  /**
   * @return whether the event belongs to a project
   */
  @Transient
  public boolean isForProject() {
    return project != null;
  }

  @Override
  public int compareTo(Event other) {
    int cmpStartTime = this.startTime.compareTo(other.startTime);

    if (cmpStartTime != 0) {
      return cmpStartTime;
    }

    return this.endTime.compareTo(other.endTime);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Event)) {
      return false;
    }
    Event other = (Event) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Event [id=" + id + ", name=" + name + ", startTime=" + startTime + ", endTime="
        + endTime + ", participantsCount=" + getParticipantsCount() + ", bookedOut=" + isBookedOut()
        + "]";
  }

  public enum EventStatus {
    PLANNED, RUNNING, PAST, CANCELLED
  }
}
