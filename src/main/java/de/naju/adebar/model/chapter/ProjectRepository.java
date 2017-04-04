package de.naju.adebar.model.chapter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Project} instances
 * @author Rico Bergmann
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
}
