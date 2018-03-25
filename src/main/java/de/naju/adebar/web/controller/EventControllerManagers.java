package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.chapter.ProjectManager;
import de.naju.adebar.app.events.EventManager;
import de.naju.adebar.model.persons.PersonManager;

/**
 * Managers for the {@link EventController}
 *
 * @author Rico Bergmann
 */
@Component
public class EventControllerManagers {
  public final PersonManager persons;
  public final EventManager events;
  public final LocalGroupManager localGroups;
  public final ProjectManager projects;

  @Autowired
  public EventControllerManagers(PersonManager personManager, EventManager eventManager,
      LocalGroupManager localGroupManager, ProjectManager projectManager) {
    this.persons = personManager;
    this.events = eventManager;
    this.localGroups = localGroupManager;
    this.projects = projectManager;
  }

}
