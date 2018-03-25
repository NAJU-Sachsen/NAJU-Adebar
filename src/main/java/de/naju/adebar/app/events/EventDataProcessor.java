package de.naju.adebar.app.events;

import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.chapter.ProjectManager;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.events.Event;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to conveniently access {@link Event} instances and to collect data about them
 *
 * @author Rico Bergmann
 */
@Service
public class EventDataProcessor {

  private static final int FIRST_TIME_SLICE = 0;
  private static final long ONE_DAY = 1;
  private EventManager eventManager;
  private LocalGroupManager localGroupManager;
  private ProjectManager projectManager;

  public EventDataProcessor(EventManager eventManager, LocalGroupManager localGroupManager,
      ProjectManager projectManager) {
    Object[] params = {eventManager, localGroupManager, projectManager};
    Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
    this.eventManager = eventManager;
    this.localGroupManager = localGroupManager;
    this.projectManager = projectManager;
  }

  /**
   * @param eventType the type of events to search for
   * @return all events that do belong to a local group
   */
  public Map<Event, LocalGroup> getLocalGroupBelonging(EventType eventType) {
    Map<Event, LocalGroup> belonging = new HashMap<>();
    Iterable<Event> events = fetchEvents(eventType);

    for (Event event : events) {
      Optional<LocalGroup> localGroup = localGroupManager.repository()
          .findFirstByEventsContaining(event);
      localGroup.ifPresent(l -> belonging.put(event, l));
    }

    return belonging;
  }

  /**
   * @param events the events to find the corresponding local groups for
   * @return all of the events that belong to a local group
   */
  public Map<Event, LocalGroup> getLocalGroupBelonging(Iterable<Event> events) {
    Map<Event, LocalGroup> belonging = new HashMap<>();
    for (Event event : events) {
      Optional<LocalGroup> localGroup = localGroupManager.repository()
          .findFirstByEventsContaining(event);
      localGroup.ifPresent(l -> belonging.put(event, l));
    }
    return belonging;
  }

  /**
   * @param events the events to find the corresponding projects for
   * @return all of the events that belong to a project
   */
  public Map<Event, Project> getProjectBelonging(Iterable<Event> events) {
    Map<Event, Project> belonging = new HashMap<>();
    for (Event event : events) {
      Optional<Project> project = projectManager.repository().findByEventsContains(event);
      project.ifPresent(p -> belonging.put(event, p));
    }
    return belonging;
  }

  /**
   * @param eventType the type of events to search for
   * @return all events that do belong to a project
   */
  public Map<Event, Project> getProjectBelonging(EventType eventType) {
    Map<Event, Project> belonging = new HashMap<>();
    Iterable<Event> events = fetchEvents(eventType);

    for (Event event : events) {
      Optional<Project> project = projectManager.repository().findByEventsContains(event);
      project.ifPresent(p -> belonging.put(event, p));
    }

    return belonging;
  }

  /**
   * @param eventType the type of events of search for
   * @return all events of that type
   */
  public Iterable<Event> fetchEvents(EventType eventType) {
    LocalDateTime now = LocalDateTime.now();
    Iterable<Event> events = null;
    switch (eventType) {
      case ALL:
        events = eventManager.repository().findAll();
        break;
      case RUNNING:
        events = eventManager.findOngoingEvents();
        break;
      case FUTURE:
        events = eventManager.repository().findByStartTimeIsAfter(now);
        break;
      case PAST:
        events = eventManager.repository().findByEndTimeIsBefore(now);
        break;
    }
    return events;
  }

  /**
   * @param event the event to check
   * @return {@code true} if the event belongs to a local group, {@code false} otherwise
   */
  public boolean eventBelongsToLocalGroup(Event event) {
    return localGroupManager.repository().findFirstByEventsContaining(event).isPresent();
  }

  /**
   * @param event the event to check
   * @return {@code true} if the event belongs to a project, {@code false} otherwise
   */
  public boolean eventBelongsToProject(Event event) {
    return projectManager.repository().findByEventsContains(event).isPresent();
  }

  /**
   * As the events start/end time is saved in {@link LocalDateTime}, but sometimes only the date is
   * needed, the time will be set to 0:00 o'clock to encode this case. However this may lead to
   * errors, if times need to be compared exactly (if an event should end on '03-21-17' this will be
   * encoded as '03-21-17 00:00', however querying for all running events on 03-21-17 at 2 PM will
   * result in this event not being shown as it is already over strictly speaking). To provide a
   * proper adjustment to these times, this method may be used.
   *
   * @param time the time to adjust
   * @return the adjusted time
   */
  public LocalDateTime adjustTime(LocalDateTime time) {
    LocalDateTime adjusted = LocalDateTime.from(time);
    if (time.getHour() == FIRST_TIME_SLICE && time.getMinute() == FIRST_TIME_SLICE) {
      adjusted = adjusted.plusDays(ONE_DAY);
    }
    return adjusted;
  }

  /**
   * Simple classification of the dates events take place
   */
  public enum EventType {
    ALL, RUNNING, FUTURE, PAST
  }

}
