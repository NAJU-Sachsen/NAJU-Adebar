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
 * @author Rico Bergmann
 */
@Entity(name = "event")
public class Event {
    public enum EventStatus {PLANNED, RUNNING, PAST, CANCELLED}

    @Id @GeneratedValue @Column(name = "id") private long id;
    @Column(name = "name") private String name;
    @Column(name = "startTime") private LocalDateTime startTime;
    @Column(name = "endTime") private LocalDateTime endTime;
    @Column(name = "participantsLimit") private int participantsLimit;
    @Column(name = "minParticipantAge") private int minimumParticipantAge;
    @Column(name = "intParticipationFee", length = 2048) private Money internalParticipationFee;
    @Column(name = "extParticipationFee", length = 2048) private Money externalParticipationFee;
    @Embedded private Address place;
    @ManyToMany(cascade = CascadeType.ALL) private List<Person> participants;
    @ManyToMany(cascade = CascadeType.ALL) private List<Person> counselors;
    @ManyToMany(cascade = CascadeType.ALL) private List<Person> organizers;
    @OneToMany(cascade = CascadeType.ALL) private Map<Person, ParticipationInfo> participationInfo;
    @ElementCollection private List<Lecture> lectures;

    /**
     * Simplified constructor initializing the most important data
     * @param name the event's name
     * @param startTime the event's start time
     * @param endTime the event's end time
     */
    public Event(String name, LocalDateTime startTime, LocalDateTime endTime) {
        this(name, startTime, endTime, Integer.MAX_VALUE, 0, null, null, new Address("", "", ""));
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
    public Event(String name, LocalDateTime startTime, LocalDateTime endTime, int participantsLimit, int minimumParticipantAge, Money internalParticipationFee, Money externalParticipationFee, Address place) {
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

        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantsLimit = participantsLimit;
        this.minimumParticipantAge = minimumParticipantAge;
        this.internalParticipationFee = internalParticipationFee;
        this.externalParticipationFee = externalParticipationFee;
        this.place = place;
        this.participants = new LinkedList<>();
        this.counselors = new LinkedList<>();
        this.organizers = new LinkedList<>();
        this.participationInfo = new HashMap<>();
        this.lectures = new LinkedList<>();
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
    public long getId() {
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
    public int getParticipantsLimit() {
        return participantsLimit;
    }

    /**
     * @param participantsLimit the number of persons that may at most participate
     * @throws IllegalArgumentException if the participants limit is non-positive
     */
    public void setParticipantsLimit(int participantsLimit) {
        Assert.isTrue(participantsLimit > 0, "Participants limit must be positive, but was: " + participantsLimit);
        this.participantsLimit = participantsLimit;
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
    public Iterable<Person> getParticipants() {
        return participants;
    }

    /**
     * @return the persons who participate in the event but have not payed the fee yet
     */
    @Transient
    public Iterable<Person> getParticipantsWithFeeNotPayed() {
        List<Person> persons = new LinkedList<>();
        participants.forEach(p -> {
            if (!participationInfo.get(p).isParticipationFeePayed()) {
                persons.add(p);
            }
        });
        return persons;
    }

    /**
     * @return the persons who participate in the event but have not sent the "real" participation form yet
     */
    @Transient
    public Iterable<Person> getParticipantsWithFormNotReceived() {
        List<Person> persons = new LinkedList<>();
        participants.forEach(p -> {
            if (!participationInfo.get(p).isRegistrationFormReceived()) {
                persons.add(p);
            }
        });
        return persons;
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
    public Map<Person, ParticipationInfo> getParticipationInfo() {
        return Collections.unmodifiableMap(participationInfo);
    }

    /**
     * @param id the event's id
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * @param lectures the lectures held on the event
     */
    protected void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    /**
     * @param participants the event's participants
     */
    protected void setParticipants(List<Person> participants) {
        this.participants = participants;
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

    /**
     * @param participationInfo information about each participant
     */
    protected void setParticipationInfo(Map<Person, ParticipationInfo> participationInfo) {
        this.participationInfo = participationInfo;
    }

    // "advanced" getter

    /**
     * @return the number of participants
     */
    @Transient public int getParticipantsCount() {
        return participants.size();
    }

    /**
     * @return {@code true} if an participant limit was specified, {@code false} otherwise
     */
    public boolean hasParticipantsLimit() {
        return participantsLimit > 0 && participantsLimit != Integer.MAX_VALUE;
    }

    /**
     * @return the number of spare participation slots
     */
    @Transient
    public int getRemainingCapacity() {
        if (hasParticipantsLimit()) {
            return Integer.MAX_VALUE;
        }
        return participantsLimit - getParticipantsCount();
    }

    /**
     * @return {@code true} if no more persons may participate
     */
    @Transient
    public boolean isBookedOut() {
        return hasParticipantsLimit() && getParticipantsCount() == getParticipantsLimit();
    }

    /**
     * @param person the person to check
     * @return {@code true} if the person may participate in the event regarding to age-restrictions, {@code false} otherwise
     */
    @Transient
    public boolean isOldEnough(Person person) {
        try {
            return person.getParticipantProfile().calculateAge() > minimumParticipantAge;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    // modification methods

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
     * @throws NoParticipantException if the person is no participant
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
        } else if (!isOldEnough(person)) {
            throw new PersonIsTooYoungException(String.format("Person is too young: must be %d years old but was born on %s", minimumParticipantAge, person.getParticipantProfile().getDateOfBirth()));
        } else if (isBookedOut()) {
            throw new BookedOutException("The event is booked out: " + getParticipantsCount() + " participants");
        }
        participants.add(person);
        participationInfo.put(person, new ParticipationInfo());
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
        } else if (isParticipant(person)) {
            throw new ExistingParticipantException("Person does already participate: " + person);
        } else if (isBookedOut()) {
            throw new BookedOutException("The event is booked out: " + getParticipantsCount() + " participants");
        }
        participants.add(person);
        participationInfo.put(person, new ParticipationInfo());
    }

    /**
     * @param person the person to remove from the list of participants
     * @throws IllegalArgumentException if the person did not participate
     */
    public void removeParticipant(Person person) {
        if (!isParticipant(person)) {
            throw new IllegalArgumentException("Person does not participate: " + person);
        }
        participants.remove(person);
        participationInfo.remove(person);
    }

    /**
     * @param person the person to check
     * @return {@code true} if the person participates in the event, {@code false} otherwise
     */
    public boolean isParticipant(Person person) {
        return participants.contains(person);
    }

    /**
     * @param person the person to get the participation info for
     * @return the participation info
     */
    public ParticipationInfo getParticipationInfo(Person person) {
        if (!isParticipant(person)) {
            throw new IllegalArgumentException("Person does not participate: " + person);
        }
        return participationInfo.get(person);
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
        participationInfo.put(person, newInfo);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (participantsLimit != event.participantsLimit) return false;
        if (minimumParticipantAge != event.minimumParticipantAge) return false;
        if (!name.equals(event.name)) return false;
        if (!startTime.equals(event.startTime)) return false;
        if (!endTime.equals(event.endTime)) return false;
        if (internalParticipationFee != null ? !internalParticipationFee.equals(event.internalParticipationFee) : event.internalParticipationFee != null)
            return false;
        if (place != null ? !place.equals(event.place) : event.place != null) return false;
        if (!participants.equals(event.participants)) return false;
        if (!counselors.equals(event.counselors)) return false;
        if (!organizers.equals(event.organizers)) return false;
        if (!participationInfo.equals(event.participationInfo)) return false;
        return lectures.equals(event.lectures);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + participantsLimit;
        result = 31 * result + minimumParticipantAge;
        result = 31 * result + (internalParticipationFee != null ? internalParticipationFee.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + participants.hashCode();
        result = 31 * result + counselors.hashCode();
        result = 31 * result + organizers.hashCode();
        result = 31 * result + participationInfo.hashCode();
        result = 31 * result + lectures.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", place=" + place +
                ", participants=" + participants +
                ", counselors=" + counselors +
                '}';
    }
}
