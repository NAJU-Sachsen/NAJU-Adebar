package de.naju.adebar.infrastructure.formatters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonId;
import de.naju.adebar.model.human.ReadOnlyPersonRepository;

public class PersonConverter implements Converter<String, Person> {

  private final ReadOnlyPersonRepository personRepo;

  public PersonConverter(ReadOnlyPersonRepository personRepo) {
    Assert.notNull(personRepo, "Person repo may not be null");
    this.personRepo = personRepo;
  }

  @Override
  public Person convert(String source) {
    PersonId personId = new PersonId(source);
    Person person = personRepo.findOne(personId);

    if (person == null) {
      throw new IllegalArgumentException("No person with with ID " + source);
    }

    return person;
  }

}
