package de.naju.adebar.model.persons;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Rico Bergmann
 */
public interface ParticipantProfileRepository extends CrudRepository<ParticipantProfile, PersonId> {
}
