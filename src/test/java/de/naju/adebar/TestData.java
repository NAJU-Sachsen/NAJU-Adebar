package de.naju.adebar;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.test.mockups.PersonMockup;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Provides a number of instantiated persons which optionally may be activists.
 * <p>
 * Beware that persons and their activist counterpart are not the same objects. Hence {@code
 * person.equals(activist)} will return {@code false}.
 * <p>
 * The following persons are available:
 * <ul>
 * <li>hans</li>
 * <li>berta</li>
 * <li>dieter</li>
 * <li>fritz</li>
 * <li>martha</li>
 * <li>nadine</li>
 * </ul>
 *
 * @author Rico Bergmann
 */
@Service
public class TestData {

  private static TestData instance;

  private Map<String, Person> persons;
  private Map<String, Gender> genders;

  private TestData() {
    persons = new HashMap<>(10);
    genders = new HashMap<>(10);

    persons.put("hans", PersonMockup.mockupPerson("Hans", "Wurst", Email.of("hw@web.de")));
    persons.put("berta", PersonMockup.mockupPerson("Berta", "Beate", Email.of("bbeate@gmx.net")));
    persons.put("dieter",
        PersonMockup.mockupPerson("Dieter", "Dancer", Email.of("tanzbaer@gmx.de")));
    persons.put("fritz",
        PersonMockup.mockupPerson("Fritz", "Fröhlich", Email.of("froehlich@googlemail.com")));
    persons.put("martha",
        PersonMockup.mockupPerson("Marta", "Mächtig", Email.of("diemaechtigemarta@web.de")));
    persons.put("nadine",
        PersonMockup.mockupPerson("Nadine", "Nett", Email.of("dienette@aol.com")));

    genders.put("hans", Gender.MALE);
    genders.put("berta", Gender.FEMALE);
    genders.put("dieter", Gender.MALE);
    genders.put("fritz", Gender.MALE);
    genders.put("martha", Gender.FEMALE);
    genders.put("nadine", Gender.FEMALE);
  }

  public static Person getPerson(String name) {
    return getInstance().persons.get(name);
  }

  public static Person getParticipant(String name) {
    Person person = getPerson(name);
    Person participant = PersonMockup.mockupParticipant( //
        person.getFirstName(), //
        person.getLastName(), //
        person.getEmail(), //
        getInstance().genders.get(name));
    return participant;
  }

  public static Collection<String> availablePersons() {
    return Collections.unmodifiableCollection(getInstance().persons.keySet());
  }

  private static TestData getInstance() {
    if (instance == null) {
      instance = new TestData();
    }
    return instance;
  }

}
