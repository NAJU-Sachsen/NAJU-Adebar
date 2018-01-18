package de.naju.adebar.model.persons;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Person} instances
 * 
 * @author Rico Bergmann
 */
@Repository("personRepo")
public interface PersonRepository
    extends CrudRepository<Person, PersonId>, ReadOnlyPersonRepository {

  /*
   * We just need to declare this method explicitly again, as both CrudRepository and
   * ReadOnlyPersonRepository demand it (non-Javadoc)
   * 
   * @see org.springframework.data.repository.CrudRepository#findOne(java.io. Serializable)
   */
  @Override
  Person findOne(PersonId id);

}
