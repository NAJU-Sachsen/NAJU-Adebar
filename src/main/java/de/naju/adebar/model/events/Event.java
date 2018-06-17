package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.util.Assert2;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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

  Event(EventId id, String name, LocalDateTime startTime, LocalDateTime endTime) {
    this.id = id;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.place = new Address();
    this.participantsList = new ParticipantsList(this);
    this.participationInfo = new ParticipationInfo(this);
    this.organizationInfo = new OrganizationInfo(this);
  }

  @JpaOnly
  Event() {}

  public EventId getId() {
    return id;
  }

  @JpaOnly
  private void setId(EventId id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  @JpaOnly
  private void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  @JpaOnly
  private void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  @JpaOnly
  private void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public Address getPlace() {
    return place;
  }

  void setPlace(Address place) {
    this.place = place;
  }

  public ParticipantsList getParticipantsList() {
    participantsList.provideRelatedEvent(this);
    return participantsList;
  }

  void setParticipantsList(ParticipantsList participantsList) {
    participantsList.provideRelatedEvent(this);
    this.participantsList = participantsList;
  }

  public ParticipationInfo getParticipationInfo() {
    participationInfo.provideRelatedEvent(this);
    return participationInfo;
  }

  void setParticipationInfo(ParticipationInfo participationInfo) {
    participationInfo.provideRelatedEvent(this);
    this.participationInfo = participationInfo;
  }

  public OrganizationInfo getOrganizationInfo() {
    organizationInfo.provideRelatedEvent(this);
    return organizationInfo;
  }

  void setOrganizationInfo(OrganizationInfo organizationInfo) {
    organizationInfo.provideRelatedEvent(this);
    this.organizationInfo = organizationInfo;
  }

  public LocalGroup getLocalGroup() {
    return localGroup;
  }

  @JpaOnly
  private void setLocalGroup(LocalGroup localGroup) {
    this.localGroup = localGroup;
  }

  public Project getProject() {
    return project;
  }

  @JpaOnly
  private void setProject(Project project) {
    this.project = project;
  }

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

  @Transient
  public boolean isPast() {
    final LocalDateTime now = LocalDateTime.now();
    LocalDateTime effectiveEndTime = endTime;

    if (endTime.getHour() == 0 && endTime.getMinute() == 0) {
      effectiveEndTime = effectiveEndTime.plusDays(1);
    }

    return effectiveEndTime.isBefore(now);
  }

  @Transient
  public boolean isProspective() {
    return startTime.isAfter(LocalDateTime.now());
  }

  @Transient
  public boolean isForLocalGroup() {
    return localGroup != null;
  }

  @Transient
  public boolean isForProject() {
    return project != null;
  }

  @Transient
  public boolean isBookedOut() {
    if (participantsList.hasAccommodationInfo()) {
      return false;
    }

    return participantsList.isBookedOut();
  }

  public Event updateName(String name) {
    setName(name);

    if (!updateEventWasRegistered()) {
      registerEvent(EventUpdatedDomainEvent.forEvent(this));
    }
    return this;
  }

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

  public Event updatePlace(Address place) {
    setPlace(place);

    if (!updateEventWasRegistered()) {
      registerEvent(EventUpdatedDomainEvent.forEvent(this));
    }
    return this;
  }

  void registerEvent(AbstractEventRelatedDomainEvent event) {
    if (!event.aggregateMayContainMultipleInstances() && hasRegisteredEventOf(event.getClass())) {
      return;
    }
    domainEvents.add(event);
  }

  boolean hasRegisteredEventOf(Class<? extends AbstractEventRelatedDomainEvent> clazz) {
    return domainEvents.stream().anyMatch(e -> e.getClass().equals(clazz));
  }

  boolean updateEventWasRegistered() {
    return hasRegisteredEventOf(EventUpdatedDomainEvent.class);
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
