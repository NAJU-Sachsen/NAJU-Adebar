package de.naju.adebar.model.persons.qualifications;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access {@link Qualification} instances
 *
 * @author Rico Bergmann
 * @see Qualification
 */
public interface QualificationRepository extends CrudRepository<Qualification, String> {
}
