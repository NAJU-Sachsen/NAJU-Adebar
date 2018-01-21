package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.model.persons.Person;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their email address
 * 
 * @author Rico Bergmann
 */
public class EmailFilter implements PersonFilter {
  private String email;

  public EmailFilter(String email) {
    this.email = email;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    return personStream.filter(p -> p.getEmail().equals(email));
  }
}
