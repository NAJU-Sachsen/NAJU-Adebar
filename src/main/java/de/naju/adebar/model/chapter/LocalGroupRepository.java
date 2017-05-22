package de.naju.adebar.model.chapter;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.newsletter.Newsletter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to access {@link LocalGroup} instances
 * @author Rico Bergmann
 */
@Repository("localGroupRepo")
public interface LocalGroupRepository extends ReadOnlyLocalGroupRepository, CrudRepository<LocalGroup, Long> {}
