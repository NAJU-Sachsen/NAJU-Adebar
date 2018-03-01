package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.model.persons.PersonManager;

/**
 * Mangers for the {@link LocalGroupController}
 * 
 * @author Rico Bergmann
 */
@Component
class LocalGroupControllerManagers {
  public final LocalGroupManager localGroups;
  public final PersonManager persons;

  @Autowired
  public LocalGroupControllerManagers(LocalGroupManager localGroupManager,
      PersonManager personManager) {
    this.localGroups = localGroupManager;
    this.persons = personManager;
  }
}
