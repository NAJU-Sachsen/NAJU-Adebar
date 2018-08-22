package de.naju.adebar.model.events;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.util.Assert2;

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

  @EmbeddedId
  @Column(name = "id")
  private EventId id;

  @Column(name = "name")
  private String name;

  @Column(name = "startTime")
  private LocalDateTime startTime;

  @Column(name = "endTime")
  private LocalDateTime endTime;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "street", column = @Column(name = "locationStreet")),
      @AttributeOverride(name = "zip", column = @Column(name = "locationZip")),
      @AttributeOverride(name = "city", column = @Column(name = "locationCity")),
      @AttributeOverride(name = "additionalInfo", column = @Column(name = "locationHints"))})
  private Address place;

  @Embedded
  private ParticipantsList participantsList;

  @Embedded
  private ParticipationInfo participationInfo;

  @Embedded
  private OrganizationInfo organizationInfo;

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

  private transient Collection<AbstractEventRelatedDomainEvent> domainEvents = new ArrayList<>();

  /**
   * Creates a new event. As the construction of new instances should be handled by the
   * {@link EventFactory}, this method is {@code package-private}.
   *
   * @param id the event's id
   * @param name the event's name
   * @param startTime the event's start time
   * @param endTime the event's end time. May not be before the {@code startTime}
   * @throws IllegalArgumentException if any of the parameters is {@code null}
   */
  Event(EventId id, String name, LocalDateTime startTime, LocalDateTime endTime) {
    Assert2.noNullArguments("No argument may be null", id, name, startTime, endTime);
    this.id = id;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.place = new Address();
    this.participantsList = new ParticipantsList(this);
    this.participationInfo = new ParticipationInfo(this);
    this.organizationInfo = new OrganizationInfo(this);
  }

  /**
   * Default constructor just for JPA's sake
   */
  @JpaOnly
  Event() {}

  /**
   * An event will be identified through its ID. If two events share the same ID, they are expected
   * to be equal.
   */
  @Nonnull
  public EventId getId() {
    return id;
  }

  /**
   * Each event has a name which is likely unique. However, if there are follow-ups these may have
   * the same name. Furthermore there may be events in different local groups, which share the same
   * name.
   */
  @Nonnull
  public String getName() {
    return name;
  }

  /**
   * The event's start time. If the start time does not matter, the time portion will be set to
   * {@link LocalTime#MIDNIGHT} (which is a quite unusual start time).
   */
  @Nonnull
  public LocalDateTime getStartTime() {
    return startTime;
  }

  /**
   * The event's end time. If the end time does not matter, the time portion will be set to
   * {@link LocalTime#MIDNIGHT} (which is a quite unusual end time).
   * <p>
   * The end time is guaranteed to be after (or equal to) the start time.
   */
  @Nonnull
  public LocalDateTime getEndTime() {
    return endTime;
  }

  /**
   * The location where the event takes place. May be empty (according to {@link Address#empty()}).
   */
  @Nonnull
  public Address getPlace() {
    return place;
  }

  /**
   * The persons who participate in the event, as well as some related information.
   */
  @Nonnull
  public ParticipantsList getParticipantsList() {
    participantsList.provideRelatedEvent(this);
    return participantsList;
  }

  /**
   * Information for persons who want to, or will be participating in an event.
   */
  @Nonnull
  public ParticipationInfo getParticipationInfo() {
    participationInfo.provideRelatedEvent(this);
    return participationInfo;
  }

  /**
   * Information about who organizes an event and who will be counseling.
   */
  @Nonnull
  public OrganizationInfo getOrganizationInfo() {
    organizationInfo.provideRelatedEvent(this);
    return organizationInfo;
  }

  /**
   * Provides access to the local group this event belongs to, if there is any.
   *
   * Each event will either belong to a local group, or a project.
   *
   * @return the local group or {@code null} otherwise
   */
  @Nullable
  public LocalGroup getLocalGroup() {
    return localGroup;
  }

  /**
   * Provides access to the project this event belongs to, if there is any.
   *
   * Each event will either belong to a local group, or a project.
   *
   * @return the project or {@code null} otherwise
   */
  public Project getProject() {
    return project;
  }

  /**
   * Checks, whether this event is taking place right now (according to the time provided by
   * {@link LocalDateTime#now()}).
   */
  @Transient
  public boolean isOngoing() {
    final LocalDateTime now = LocalDateTime.now();
    if (startTime.isAfter(now)) {
      return false;
    }

    LocalDateTime effectiveEndTime = endTime;
    if (endTime.getHour() == 0 && endTime.getMinute() == 0) {
      effectiveEndTime = effectiveEndTime.plusDays(1);
    }

    return !effectiveEndTime.isBefore(now);
  }

  /**
   * Checks, whether this event is over already (according to the time provided by
   * {@link LocalDateTime.now()}).
   */
  @Transient
  public boolean isPast() {
    final LocalDateTime now = LocalDateTime.now();
    LocalDateTime effectiveEndTime = endTime;

    // if the
    if (endTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
      effectiveEndTime = effectiveEndTime.plusDays(1);
    }

    return effectiveEndTime.isBefore(now);
  }

  /**
   * Checks, whether this event will take place in the future (according to the time provided by
   * {@link LocalDateTime#now()}).
   */
  @Transient
  public boolean isProspective() {
    return startTime.isAfter(LocalDateTime.now());
  }

  /**
   * Returns {@code true} if this event belongs to a {@link LocalGroup}.
   */
  @Transient
  public boolean isForLocalGroup() {
    return localGroup != null;
  }

  /**
   * Returns {@code true} if this is event takes place as part of a {@link Project}.
   */
  @Transient
  public boolean isForProject() {
    return project != null;
  }

  /**
   * Checks, whether the maximum number of participants is reached.
   */
  @Transient
  public boolean isBookedOut() {
    if (participantsList.hasAccommodationInfo()) {
      return false;
    }

    return participantsList.isBookedOut();
  }

  /**
   * Sets a new name for this event.
   *
   * @param name May not be {@code null} nor empty.
   * @return this event
   */
  public Event updateName(String name) {
    setName(name);

    if (!updateEventWasRegistered()) {
      registerEvent(EventUpdatedDomainEvent.forEvent(this));
    }
    return this;
  }

  /**
   * Sets a new time span for this event.
   * <p>
   * The end time must be after (or equal to) the start time. If only the date portion is important,
   * the time should be set to 00:00.
   *
   * @param startTime May not be {@code null}.
   * @param endTime May not be {@code null}.
   * @return this event
   */
  public Event updateStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime) {
    Assert.notNull(startTime, "startTime may not be null");
    Assert.notNull(endTime, "endTime may not be null");
    Assert2.isFalse(endTime.isBefore(startTime), "End time may not be before start time");
    this.startTime = startTime;
    this.endTime = endTime;

    if (!updateEventWasRegistered()) {
      registerEvent(EventUpdatedDomainEvent.forEvent(this));
    }
    return this;
  }

  /**
   * Moves the event to another location.
   *
   * @param place May be {@code null} if the place is not known yet.
   * @return this event
   */
  public Event updatePlace(Address place) {
    setPlace(place);

    if (!updateEventWasRegistered()) {
      registerEvent(EventUpdatedDomainEvent.forEvent(this));
    }
    return this;
  }

  /**
   * @param place the place
   */
  void setPlace(Address place) {
    this.place = place;
  }

  /**
   * @param participantsList may not be {@code null}
   */
  void setParticipantsList(ParticipantsList participantsList) {
    Assert.notNull(participantsList, "ParticipantsList may not be null");
    participantsList.provideRelatedEvent(this);
    this.participantsList = participantsList;
  }

  /**
   * @param participationInfo may not be {@code null}
   */
  void setParticipationInfo(ParticipationInfo participationInfo) {
    Assert.notNull(participationInfo, "ParticipationInfo may not be null");
    participationInfo.provideRelatedEvent(this);
    this.participationInfo = participationInfo;
  }

  /**
   * @param organizationInfo may not be {@code null}
   */
  void setOrganizationInfo(OrganizationInfo organizationInfo) {
    Assert.notNull(organizationInfo, "OrganizationInfo may not be null");
    organizationInfo.provideRelatedEvent(this);
    this.organizationInfo = organizationInfo;
  }

  /**
   * Saves a domain event for publication.
   *
   * @param event may not be {@code null}
   */
  void registerEvent(AbstractEventRelatedDomainEvent event) {
    Assert.notNull(event, "Event may not be null");
    if (!event.aggregateMayContainMultipleInstances() && hasRegisteredEventOf(event.getClass())) {
      return;
    }
    domainEvents.add(event);
  }

  /**
   * Checks, whether a certain type of domain event was already saved for publication.
   *
   * @param clazz the event's class
   * @return whether such an event is already registered but not published yet
   */
  boolean hasRegisteredEventOf(Class<? extends AbstractEventRelatedDomainEvent> clazz) {
    return domainEvents.stream().anyMatch(e -> e.getClass().equals(clazz));
  }

  /**
   * Checks, whether some kind of {@link EventUpdatedDomainEvent} was already saved for publication.
   * <p>
   * This basically is just syntactic sugar for {@link #hasRegisteredEventOf(Class)}.
   *
   * @return whether such an event is already registered but not published yet.
   */
  boolean updateEventWasRegistered() {
    return hasRegisteredEventOf(EventUpdatedDomainEvent.class);
  }

  /**
   * @param id may not be {@code null}
   */
  @JpaOnly
  private void setId(EventId id) {
    Assert.notNull(id, "Id may not be null");
    this.id = id;
  }

  /**
   * @param name may not be empty
   */
  @JpaOnly
  private void setName(String name) {
    Assert.hasText(name, "Name may not be null nor empty");
    this.name = name;
  }

  /**
   * @param startTime may not be {@code null}
   */
  @JpaOnly
  private void setStartTime(LocalDateTime startTime) {
    Assert.notNull(startTime, "StartTime may not be null");
    this.startTime = startTime;
  }

  /**
   * @param endTime may not be {@code null}
   */
  @JpaOnly
  private void setEndTime(LocalDateTime endTime) {
    Assert.notNull(endTime, "EndTime may not be null");
    this.endTime = endTime;
  }

  /**
   * @param localGroup may be {@code null} if {@link #project} is not
   */
  @JpaOnly
  private void setLocalGroup(LocalGroup localGroup) {
    Assert.isTrue(localGroup != null || this.project != null,
        "LocalGroup may not be null if project is null already");
    this.localGroup = localGroup;
  }

  /**
   * @param project may be {@code null} if {@link #localGroup} is not
   */
  @JpaOnly
  private void setProject(Project project) {
    Assert.isTrue(project != null || this.localGroup != null,
        "Project may not be null if local group is null already");
    this.project = project;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Event other) {
    int cmpStartTime = this.startTime.compareTo(other.startTime);
    if (cmpStartTime != 0) {
      return cmpStartTime;
    }
    return this.endTime.compareTo(other.endTime);
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Event [id=" + id + ", name=" + name + ", startTime=" + startTime + ", endTime="
        + endTime + ", place=" + place + ", participantsList=" + participantsList
        + ", organizationInfo=" + organizationInfo + ", localGroup=" + localGroup + ", project="
        + project + "]";
  }

}
