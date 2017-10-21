package de.naju.adebar.app.human.filter;

import de.naju.adebar.model.human.Person;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their name
 *
 * @author Rico Bergmann
 */
public class NameFilter implements PersonFilter {

  private String firstName;
  private String lastName;

  public NameFilter(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    if (firstName != null && !firstName.isEmpty()) {
      personStream = personStream.filter(p -> p.getFirstName().contains(firstName));
    }
    if (lastName != null && !lastName.isEmpty()) {
      personStream = personStream.filter(p -> p.getLastName().contains(lastName));
    }
    return personStream;
  }
}
