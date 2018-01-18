package de.naju.adebar.test.mockups;

import de.naju.adebar.model.persons.Gender;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;

public class PersonMockup {

  private static PersonMockup instance;

  private PersonFactory personFactory;

  private PersonMockup() {
    this.personFactory = new PersonFactory();
  }

  public static Person mockupPerson(String firstName, String lastName, String email) {
    if (instance == null) {
      initInstance();
    }
    return instance.personFactory.buildNew(firstName, lastName, email).create();
  }

  public static Person mockupParticipant(String firstName, String lastName, String email,
      Gender gender) {
    Person person = mockupPerson(firstName, lastName, email);
    person.makeParticipant().updateGender(gender);
    return person;
  }

  private static void initInstance() {
    instance = new PersonMockup();
  }

}
