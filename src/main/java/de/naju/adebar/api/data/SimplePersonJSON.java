package de.naju.adebar.api.data;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.persons.Person;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * JSON-object for persons in simplified form. In contrast to the normal {@link Person} these
 * objects contain only the most important data: ID, name, date of birth and address. Instances are
 * immutable.
 *
 * @author Rico Bergmann
 */
public class SimplePersonJSON {

  private static final String DATE_FORMAT = "dd.MM.yyyy";
  private static final String ADDRESS_COMPONENT_SEPARATOR = " ";
  private static final int ADDRESS_COMPONENTS = 5;

  private String id;
  private String name;
  private String email;
  private String dob;
  private String address;

  /**
   * Objects will be created depending on an existing {@link Person} instance
   *
   * @param person the person to simplify
   */
  public SimplePersonJSON(Person person) {
    this.id = person.getId().toString();
    this.name = person.getName();
    this.email = person.hasEmail() ? person.getEmail().getValue() : "";
    this.address = formatAddress(person.getAddress());

    if (person.isParticipant()) {
      LocalDate d = person.getParticipantProfile().getDateOfBirth();
      this.dob = d != null ? d.format(DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.GERMAN)) : "";
    }
  }

  /**
   * @return the person's id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the person's (full) name, i. e. first name and last name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the person's email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the person's date of birth in localized form, i. e. as dd.MM.yyyy
   */
  public String getDob() {
    return dob;
  }

  /**
   * @return the person's address as one large String
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address the address to format
   * @return the address formatted as one large String
   */
  private String formatAddress(Address address) {
    StringBuilder stringBuilder = new StringBuilder(ADDRESS_COMPONENTS);
    stringBuilder.append(address.getStreet()).append(ADDRESS_COMPONENT_SEPARATOR)
        .append(address.getZip());
    stringBuilder.append(ADDRESS_COMPONENT_SEPARATOR).append(address.getCity());
    return stringBuilder.toString();
  }

}
