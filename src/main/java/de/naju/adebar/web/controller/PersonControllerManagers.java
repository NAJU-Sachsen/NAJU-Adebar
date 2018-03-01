package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.model.persons.qualifications.QualificationManager;

/**
 * Managers for the {@link PersonController}
 * 
 * @author Rico Bergmann
 */
@Component
class PersonControllerManagers {
  public final PersonManager persons;
  public final QualificationManager qualifications;
  public final LocalGroupManager localGroups;

  @Autowired
  public PersonControllerManagers(PersonManager personManager,
      QualificationManager qualificationManager, LocalGroupManager localGroupManager) {
    this.persons = personManager;
    this.qualifications = qualificationManager;
    this.localGroups = localGroupManager;
  }

}
