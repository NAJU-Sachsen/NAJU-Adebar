package de.naju.adebar.model.chapter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link LocalGroup} instances
 * @author Rico Bergmann
 */
@Repository("localGroupRepo")
public interface LocalGroupRepository extends ReadOnlyLocalGroupRepository, CrudRepository<LocalGroup, Long> {}
