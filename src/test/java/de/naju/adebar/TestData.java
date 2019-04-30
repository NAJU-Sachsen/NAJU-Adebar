package de.naju.adebar;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.test.mockups.PersonMockup;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
 * <li>marta</li>
 * <li>nadine</li>
 * </ul>
 *
 * @author Rico Bergmann
 */
@Service
public class TestData {

  public static final int TEST_ENTRIES_COUNT = 6;
  public static final String PERSON_HANS = "hans";
  public static final String PERSON_BERTA = "berta";
  public static final String PERSON_DIETER = "dieter";
  public static final String PERSON_FRITZ = "fritz";
  public static final String PERSON_MARTA = "marta";
  public static final String PERSON_NADINE = "nadine";
  public static final String EATING_HABIT_CONVENTIONAL = "";
  public static final String EATING_HABIT_VEGETARIAN = "vegetarian";
  public static final String EATING_HABIT_VEGAN = "vegan";
  private static TestData instance;

  private Map<String, Person> persons;
  private Map<String, Person> participants;

  private TestData() {
    persons = new HashMap<>(TEST_ENTRIES_COUNT);
    participants = new HashMap<>(TEST_ENTRIES_COUNT);

    persons.put(PERSON_HANS, PersonMockup.mockupPerson("Hans", "Wurst", Email.of("hw@web.de")));
    persons
        .put(PERSON_BERTA, PersonMockup.mockupPerson("Berta", "Beate", Email.of("bbeate@gmx.net")));
    persons.put(PERSON_DIETER,
        PersonMockup.mockupPerson("Dieter", "Dancer", Email.of("tanzbaer@gmx.de")));
    persons.put(PERSON_FRITZ,
        PersonMockup.mockupPerson("Fritz", "Fröhlich", Email.of("froehlich@googlemail.com")));
    persons.put(PERSON_MARTA,
        PersonMockup.mockupPerson("Marta", "Mächtig", Email.of("diemaechtigemarta@web.de")));
    persons.put(PERSON_NADINE,
        PersonMockup.mockupPerson("Nadine", "Nett", Email.of("dienette@aol.com")));

    HashMap<String, Gender> genders = new HashMap<>(TEST_ENTRIES_COUNT);
    genders.put(PERSON_HANS, Gender.MALE);
    genders.put(PERSON_BERTA, Gender.FEMALE);
    genders.put(PERSON_DIETER, Gender.MALE);
    genders.put(PERSON_FRITZ, Gender.MALE);
    genders.put(PERSON_MARTA, Gender.FEMALE);
    genders.put(PERSON_NADINE, Gender.FEMALE);

    HashMap<String, LocalDate> dateOfBirths = new HashMap<>(TEST_ENTRIES_COUNT);
    dateOfBirths.put(PERSON_HANS, LocalDate.of(1999, 1, 1));
    dateOfBirths.put(PERSON_BERTA, LocalDate.of(1998, 1, 1));
    dateOfBirths.put(PERSON_DIETER, LocalDate.of(1998, 2, 2));
    dateOfBirths.put(PERSON_FRITZ, LocalDate.of(1998, 2, 3));
    dateOfBirths.put(PERSON_MARTA, LocalDate.of(2000, 12, 12));
    dateOfBirths.put(PERSON_NADINE, LocalDate.of(2001, 12, 12));

    HashMap<String, String> eatingHabits = new HashMap<>(TEST_ENTRIES_COUNT);
    eatingHabits.put(PERSON_HANS, EATING_HABIT_VEGAN);
    eatingHabits.put(PERSON_BERTA, EATING_HABIT_VEGAN);
    eatingHabits.put(PERSON_DIETER, EATING_HABIT_CONVENTIONAL);
    eatingHabits.put(PERSON_FRITZ, EATING_HABIT_CONVENTIONAL);
    eatingHabits.put(PERSON_MARTA, EATING_HABIT_VEGAN);
    eatingHabits.put(PERSON_NADINE, EATING_HABIT_CONVENTIONAL);

    for (Entry<String, Person> personEntry : persons.entrySet()) {
      Person person = personEntry.getValue();
      Person participant = PersonMockup.mockupParticipant( //
          person.getFirstName(), //
          person.getLastName(), //
          person.getEmail(), //
          genders.get(personEntry.getKey()));
      participant.getParticipantProfile().updateDateOfBirth(dateOfBirths.get(personEntry.getKey()));
      participant.getParticipantProfile()
          .updateEatingHabits(eatingHabits.get(personEntry.getKey()));
      participants.put(personEntry.getKey(), participant);
    }

  }

  public static Person getPerson(String name) {
    return getInstance().persons.get(name);
  }

  public static Person getParticipant(String name) {
    return getInstance().participants.get(name);
  }

  public static Collection<Person> getAllParticipants() {
    return getInstance().participants.values();
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
