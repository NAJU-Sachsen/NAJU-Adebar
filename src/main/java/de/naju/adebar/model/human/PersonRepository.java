package de.naju.adebar.model.human;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Person} instances
 * @author Rico Bergmann
 */
@Repository("personRepo")
public interface PersonRepository extends CrudRepository<Person, PersonId>, ReadOnlyPersonRepository {
    Person findOne(PersonId id);
}
