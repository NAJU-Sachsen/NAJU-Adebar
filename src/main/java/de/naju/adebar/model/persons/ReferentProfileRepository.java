package de.naju.adebar.model.persons;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Rico Bergmann
 */
public interface ReferentProfileRepository extends CrudRepository<ReferentProfile, PersonId> {
}
