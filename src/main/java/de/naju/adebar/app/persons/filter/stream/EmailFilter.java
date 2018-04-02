package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import java.util.stream.Stream;

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
