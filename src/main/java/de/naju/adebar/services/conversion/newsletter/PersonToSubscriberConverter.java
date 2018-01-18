package de.naju.adebar.services.conversion.newsletter;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.newsletter.Subscriber;
import org.springframework.stereotype.Service;

/**
 * @author Rico Bergmann
 */
@Service
public class PersonToSubscriberConverter {

  public Subscriber convertPerson(Person person) {
    return new Subscriber(person.getFirstName(), person.getLastName(), person.getEmail());
  }

}
