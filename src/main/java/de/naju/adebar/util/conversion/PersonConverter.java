package de.naju.adebar.util.conversion;

import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonRepository;
import de.naju.adebar.model.human.Referent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Service to convert streams of related objects to streams of {@link Person} instances
 * @author Rico Bergmann
 * @see Stream
 * @see Person
 */
@Service
public class PersonConverter {
    private PersonRepository personRepo;

    @Autowired
    public PersonConverter(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    /**
     * @param activist the activist to convert
     * @return the associated person
     */
    public Person convertActivist(Activist activist) {
        return personRepo.findOneByIdAndActiveIsTrue(activist.getAssociatedPerson());
    }

    /**
     * @param activistStream the activists to convert
     * @return a stream consisting of the persons associated to the activists
     */
    public Stream<Person> convertActivistStream(Stream<Activist> activistStream) {
        Stream.Builder<Person> personStreamBuilder = Stream.builder();
        activistStream.forEach(activist -> personStreamBuilder.accept(personRepo.findOne(activist.getAssociatedPerson())));
        return personStreamBuilder.build();
    }

    /**
     * @param referentStream the referents to convert
     * @return a stream consisting of the persons associated to the referents
     */
    public Stream<Person> convertReferentStream(Stream<Referent> referentStream) {
        Stream.Builder<Person> personStreamBuilder = Stream.builder();
        referentStream.forEach(referent -> personStreamBuilder.accept(personRepo.findOne(referent.getAssociatedPerson())));
        return personStreamBuilder.build();
    }

    /**
     * @param activists the activists to convert
     * @return an {@link Iterable} of the persons associated to the activists
     */
    public Iterable<Person> convertActivists(Iterable<Activist> activists) {
        List<Person> persons = new LinkedList<>();
        activists.forEach(a -> persons.add(personRepo.findOne(a.getAssociatedPerson())));
        return persons;
    }

    /**
     * @param referent the referent to convert
     * @return the associated person
     */
    public Person converReferent(Referent referent) {
        return personRepo.findOneByIdAndActiveIsTrue(referent.getAssociatedPerson());
    }

    /**
     * @param referents the referents to convert
     * @return an {@link Iterable} of the persons associated to the referents
     */
    public Iterable<Person> convertReferents(Iterable<Referent> referents) {
        List<Person> persons = new LinkedList<>();
        referents.forEach(r -> persons.add(personRepo.findOne(r.getAssociatedPerson())));
        return persons;
    }
}
