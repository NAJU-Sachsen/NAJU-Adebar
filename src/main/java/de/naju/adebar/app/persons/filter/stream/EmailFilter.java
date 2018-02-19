package de.naju.adebar.app.persons.filter.stream;

import java.util.stream.Stream;
import de.naju.adebar.model.Email;
import de.naju.adebar.model.persons.Person;

/**
 * Filter for persons depending on their email address
 *
 * @author Rico Bergmann
 */
public class EmailFilter implements PersonFilter {
  private Email email;

  public EmailFilter(Email email) {
    this.email = email;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    return personStream.filter(p -> p.getEmail().equals(email));
  }
}
