package de.naju.adebar.model.chapter;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.persons.Person;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * A repository that provides read-only access to the saved local groups
 *
 * @author Rico Bergmann
 */
@Repository("ro_localGroupRepo")
public interface ReadOnlyLocalGroupRepository extends ReadOnlyRepository<LocalGroup, Long> {

  /**
   * @param name the local group's name to query for
   * @return an optional containing the local group with that name, otherwise the optional is empty
   */
  Optional<LocalGroup> findByName(String name);

  /**
   * @param activist the activist to query for
   * @return all local groups with the specified person as member
   */
  Iterable<LocalGroup> findByMembersContains(Person activist);

  /**
   * @param newsletter the newsletter to query for
   * @return an optional containing the local group with that newsletter, otherwise the optional is
   *     empty
   */
  Optional<LocalGroup> findByNewslettersContains(Newsletter newsletter);

  /**
   * @param board the board to query for
   * @return the local group with the given board
   */
  LocalGroup findByBoard(Board board);

  /**
   * @param event the event to query for
   * @return an optional containing the local group which hosts the event, if such a group exists
   */
  Optional<LocalGroup> findFirstByEventsContaining(Event event);
}
