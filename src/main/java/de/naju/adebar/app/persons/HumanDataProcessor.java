package de.naju.adebar.app.persons;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import de.naju.adebar.model.persons.Person;

/**
 * Service to conveniently extract data from {@link Person} instances
 *
 * @author Rico Bergmann
 */
@Service
public class HumanDataProcessor {

  /**
   * @param persons the persons to process
   * @return an {@link Iterable} of all email-addresses
   */
  public Iterable<String> extractEmailAddresses(Iterable<Person> persons) {
    List<String> emailAddresses = new LinkedList<>();
    persons.forEach(p -> {
      if (p.hasEmail())
        emailAddresses.add(p.getEmail().getValue());
    });
    return emailAddresses;
  }

  /**
   * @param persons the persons to process
   * @return a {@link Stream} of all email-addresses
   */
  public Stream<String> extractEmailAddressesAsStream(Stream<Person> persons) {
    return persons.filter(Person::hasEmail).map(p -> p.getEmail().getValue());
  }

  /**
   * Concatenates the persons' email-addresses using a delimiter
   *
   * @param persons the persons to process
   * @param delimiter the delimiter to use for the concatenation. It will be inserted between two
   *        addresses.
   * @return the concatenated String
   */
  public String extractEmailAddressesAsString(Iterable<Person> persons, String delimiter) {
    StringJoiner stringJoiner = new StringJoiner(delimiter);
    extractEmailAddresses(persons).forEach(stringJoiner::add);
    return stringJoiner.toString();
  }

  /**
   * Concatenates the persons' email-addresses using a delimiter
   *
   * @param persons the persons to process
   * @param delimiter the delimiter to use for the concatenation. It will be inserted between two
   *        addresses.
   * @return the concatenated String
   */
  public String extractEmailAddressesAsString(Stream<Person> persons, String delimiter) {
    StringJoiner stringJoiner = new StringJoiner(delimiter);
    extractEmailAddressesAsStream(persons).forEach(stringJoiner::add);
    return stringJoiner.toString();
  }
}
