package de.naju.adebar.app.news;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access {@link ReleaseNotes} instances
 * 
 * @author Rico Bergmann
 */
interface ReleaseNewsRepository extends CrudRepository<ReleaseNotes, Long> {

  /**
   * Searches for active release notes. Generally there should only be one or none.
   * 
   * @return the release notes if there are any active ones
   */
  Optional<ReleaseNotes> findFirstByActiveIsTrue();

  /**
   * Searches for all archived release notes
   * 
   * @return the release notes
   */
  Iterable<ReleaseNotes> findAllByActiveIsFalseOrderByDateDesc();

}
