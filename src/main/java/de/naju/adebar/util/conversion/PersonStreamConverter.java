package de.naju.adebar.util.conversion;

import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonRepository;
import de.naju.adebar.model.human.Referent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * Service to convert streams of related objects to streams of {@link Person} instances
 * @author Rico Bergmann
 * @see Stream
 * @see Person
 */
@Service
public class PersonStreamConverter {
    private PersonRepository personRepo;

    @Autowired
    public PersonStreamConverter(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    public Stream<Person> convertActivistStream(Stream<Activist> activistStream) {
        Stream.Builder<Person> personStreamBuilder = Stream.builder();
        activistStream.forEach(activist -> personStreamBuilder.add(personRepo.findOne(activist.getAssociatedPerson())));
        return personStreamBuilder.build();
    }

    public Stream<Person> convertReferentStream(Stream<Referent> referentStream) {
        Stream.Builder<Person> personStreamBuilder = Stream.builder();
        referentStream.forEach(referent -> personStreamBuilder.add(personRepo.findOne(referent.getAssociatedPerson())));
        return personStreamBuilder.build();
    }
}
