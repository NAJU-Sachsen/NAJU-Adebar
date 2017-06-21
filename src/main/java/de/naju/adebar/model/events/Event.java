package de.naju.adebar.model.events;

import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.NoActivistException;
import de.naju.adebar.model.human.NoParticipantException;
import de.naju.adebar.model.human.Person;
import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Abstraction of an event. It may be a regular camp or any other kind of event such as workshops or presentations.
 * Maybe there will be more precise classes for the different event-types one day..
 * @author Rico Bergmann
 */
@Entity(name = "event")
public class Event {
    public enum EventStatus {PLANNED, RUNNING, PAST, CANCELLED}

    @EmbeddedId @Column(name = "id") private EventId id;
    @Column(name = "name") private String name;
    @Column(name = "startTime") private LocalDateTime startTime;
    @Column(name = "endTime") private LocalDateTime endTime;

    @Column(name = "minParticipantAge") private int minimumParticipantAge;
    @Column(name = "intParticipationFee", length = 2048) private Money internalParticipationFee;
    @Column(name = "extParticipationFee", length = 2048) private Money externalParticipationFee;
    @Embedded private Address place;
    @ManyToMany(cascade = CascadeType.ALL) private List<Person> counselors;
    @ManyToMany(cascade = CascadeType.ALL) private List<Person> organizers;
    @ElementCollection private List<Lecture> lectures;

    @OneToOne(cascade = CascadeType.ALL) @PrimaryKeyJoinColumn private ParticipantsList participantsList;

    /**
     * Simplified constructor initializing the most important data
     * @param name the event's name
     * @param startTime the event's start time
     * @param endTime the event's end time
     */
    Event(EventId id, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this(id, name, startTime, endTime, Integer.MAX_VALUE, 0, null, null, new Address("", "", ""));
    }

    /**
     * Full constructor. All parameters may be {@code null} if not stated otherwise
     * @param name the event's name, may not be {@code null}
     * @param startTime the event's start time, may not be {@code null}
     * @param endTime the event's end time, may not be {@code null}
     * @param participantsLimit the number of persons that may at most participate
     * @param minimumParticipantAge the age which new participants must be at least
     * @param internalParticipationFee the fee to pay in order to participate
     * @param place the address where the event takes place
     * @throws IllegalArgumentException if any of the fields' contracts is violated. Refer to the setter methods for further information about those contracts.
     */
    Event(EventId id, String name, LocalDateTime startTime, LocalDateTime endTime, int participantsLimit, int minimumParticipantAge, Money internalParticipationFee, Money externalParticipationFee, Address place) {
        Assert.notNull(id, "Event id may not be null!");
    	Assert.hasText(name, "Name must contain text but was: " + name);
        Assert.notNull(startTime, "Start time must not be null!");
        Assert.notNull(endTime, "End time must not be null!");
        Assert.isTrue(!startTime.isAfter(endTime), "Start time must be before end time");
        Assert.isTrue(participantsLimit > 0, "Participants limit must be positive, but was: " + participantsLimit);
        Assert.isTrue(minimumParticipantAge >= 0, "Minimum participant age must not be negative but was: " + minimumParticipantAge);

        if (internalParticipationFee != null) {
            Assert.isTrue(internalParticipationFee.isPositiveOrZero(), "Internal participation fee may not be negative, but was: " + internalParticipationFee);
        }
        if (externalParticipationFee != null) {
            Assert.isTrue(externalParticipationFee.isPositiveOrZero(), "External participation fee may not be negative, but was: " + externalParticipationFee);
        }

        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minimumParticipantAge = minimumParticipantAge;
        this.internalParticipationFee = internalParticipationFee;
        this.externalParticipationFee = externalParticipationFee;
        this.place = place;
        this.counselors = new LinkedList<>();
        this.organizers = new LinkedList<>();
        this.lectures = new LinkedList<>();
        this.participantsList = new ParticipantsList(this, participantsLimit);
    }

    /**
     * Default constructor just for JPA's sake. Not to be used from outside, hence {@code private}.
     */
    @SuppressWarnings("unused")
    private Event() {}

    // getter and setter

    /**
     * @return the event's ID (= primary key)
     */
    public EventId getId() {
        return id;
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

        // If an instance is re-initialized from database, JPA will use it's empty constructor and set all the fields
        // afterwards. Therefore end time may be null and we must check this before validating the start date
        if (endTime != null) {
            Assert.isTrue(!endTime.isBefore(startTime));
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
     * @throws IllegalArgumentException if the start time is after the end time or the end time is {@code null}
     */
    public void setEndTime(LocalDateTime endTime) {
        Assert.notNull(endTime, "End time may not be null");

        // If an instance is re-initialized from database, JPA will use it's empty constructor and set all the fields
        // afterwards. Therefore start time may be null and we must check this before validating the end date
        if (startTime != null) {
            Assert.isTrue(!endTime.isBefore(startTime));
        }
        this.endTime = endTime;
    }

    /**
     * @return the number of persons that may at most participate
     */
    @Transient public int getParticipantsLimit() {
        return participantsList.getParticipantsLimit();
    }

    /**
     * @param participantsLimit the number of persons that may at most participate
     * @throws IllegalArgumentException if the participants limit is non-positive
     */
    public void setParticipantsLimit(int participantsLimit) {
        Assert.isTrue(participantsLimit > 0, "Participants limit must be positive, but was: " + participantsLimit);
        this.participantsList.setParticipantsLimit(participantsLimit);
    }

    /**
     * @return the age which new participants must be at least
     */
    public int getMinimumParticipantAge() {
        return minimumParticipantAge;
    }

    /**
     * @param minimumParticipantAge the age which new participants must be at least
     * @throws IllegalArgumentException if the minimum participant age is negative
     */
    public void setMinimumParticipantAge(int minimumParticipantAge) {
        Assert.isTrue(minimumParticipantAge >= 0, "Minimum participant age must not be negative but was: " + minimumParticipantAge);
        this.minimumParticipantAge = minimumParticipantAge;
    }

    /**
     * The internal participation fee is used for NABU members.
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
            Assert.isTrue(internalParticipationFee.isPositiveOrZero(), "Participation fee may not be negative, but was: " + internalParticipationFee);
        }
        this.internalParticipationFee = internalParticipationFee;
    }

    /**
     * The external participation fee is used for all participants who are not members of the NABU.
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
            Assert.isTrue(externalParticipationFee.isPositiveOrZero(), "Participation fee may not be negative, but was: " + externalParticipationFee);
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
    @Transient public Iterable<Person> getParticipants() {
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
     * @return the persons who participate in the event but have not sent the "real" participation form yet
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
     * @return the activists who take care of the event - i. e. are in attendance when the event takes place
     */
    public Iterable<Person> getCounselors() {
        return counselors;
    }

    /**
     * @return the activists who organize the event
     */
    public Iterable<Person> getOrganizers() {
        return organizers;
    }

    /**
     * @return the lectures held during the event
     */
    public Iterable<Lecture> getLectures() {
        return lectures;
    }

    /**
     * A read-only map (participant -> participationInfo). Beware: all write-operations will result in an
     * {@link UnsupportedOperationException}!
     * @return information about each participant
     * @see ParticipationInfo
     */
    @Transient public Map<Person, ParticipationInfo> getParticipationInfo() {
        return participantsList.getParticipants();
    }

    /**
     * @param id the event's id
     */
    protected void setId(EventId id) {
        this.id = id;
    }

    /**
     * @param lectures the lectures held on the event
     */
    protected void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    /**
     * @param counselors the counselors of the event
     */
    protected void setCounselors(List<Person> counselors) {
        this.counselors = counselors;
    }

    /**
     * @param organizers the event's organizers
     */
    protected void setOrganizers(List<Person> organizers) {
        this.organizers = organizers;
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

    // "advanced" getter

    /**
     * @return the number of participants
     */
    @Transient public int getParticipantsCount() {
        return participantsList.getParticipantsCount();
    }

    /**
     * @return {@code true} if an participant limit was specified, {@code false} otherwise
     */
    public boolean hasParticipantsLimit() {
        return participantsList.hasParticipantsLimit();
    }

    /**
     * @return the number of spare participation slots
     */
    @Transient public int getRemainingCapacity() {
        return participantsList.getRemainingCapacity();
    }

    /**
     * @return {@code true} if no more persons may participate
     */
    @Transient public boolean isBookedOut() {
        return participantsList.isBookedOut();
    }
    
    /**
     * @return {@code true} if there are reservations for this event, or {@code false} otherwise
     */
    @Transient public boolean hasReservations() {
        return participantsList.hasReservations();
    }

    /**
     * @param person the person to check
     * @return {@code true} if the person may participate in the event regarding to age-restrictions, {@code false} otherwise
     * @throws IllegalArgumentException if the person is no camp participant
     */
    @Transient public boolean satisfiesAgeRestrictions(Person person) {
        if (!person.isParticipant()) {
        	throw new IllegalArgumentException("Person is not a camp participant: " + person);
        } else if (!person.getParticipantProfile().hasDateOfBirth()) {
        	return true;
        }
        return person.getParticipantProfile().calculateAge() >= minimumParticipantAge;
    }
    
    /**
     * Queries for specific reservation data
     * @param description the description (= ID) of the reservation to query for
     * @return the reservation
     */
    @Transient public Reservation getReservationFor(String description) {
    	return participantsList.getReservationFor(description);
    }

    // modification methods
    
    /**
     * Updates start and end time simultaneously. Useful to prevent contract violations that would occur when doing the
     * same through a sequential call to the related setters
     * @param startTime
     * @param endTime
     * @throws IllegalArgumentException if {@code startTime < endTime} or one of the parameters is {@code null}
     */
    public void updateTimePeriod(LocalDateTime startTime, LocalDateTime endTime) {
        Assert.notNull(startTime, "Start time may not be null!");
        Assert.notNull(endTime, "End time may not be null!");
        Assert.isTrue(!endTime.isBefore(startTime), "Start time may not be after end time");
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Adds a new participant
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
        }
        else if (isParticipant(person)) {
            throw new ExistingParticipantException("Person does already participate: " + person);
        } else if (!satisfiesAgeRestrictions(person)) {
            throw new PersonIsTooYoungException(String.format("Person is too young: must be %d years old but was born on %s", minimumParticipantAge, person.getParticipantProfile().getDateOfBirth()));
        }
        participantsList.addParticipant(person);
    }

    /**
     * Adds a new participant to the event, regardless of eventual violations of the minimum participation age
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
     * @param description the description (= ID) of the reservation
     */
    public void removeReservation(String description) {
    	participantsList.removeReservation(description);
    }
    
    /**
     * Updates a reservation
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
     * @param lecture the lecture to add
     * @throws IllegalArgumentException if <strong>exactly</strong> this lecture is already being held
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
    
    // overridden from Object
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Event))
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Event [id=" + id + ", name=" + name + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", participantsCount=" + getParticipantsCount() + ", bookedOut=" + isBookedOut() + "]";
	}
}
