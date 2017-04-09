package de.naju.adebar.model.chapter;

import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.newsletter.Newsletter;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository to access {@link LocalGroup} instances
 * @author Rico Bergmann
 */
public interface LocalGroupRepository extends CrudRepository<LocalGroup, Long> {

    /**
     * @param name the local group's name to query for
     * @return an optional containing the local group with that name, otherwise the optional is empty
     */
    Optional<LocalGroup> findByName(String name);

    /**
     * @param activist the activist to query for
     * @return all local groups with the specified person as member
     */
    Iterable<LocalGroup> findByMembersContains(Activist activist);

    /**
     * @param newsletter the newsletter to query for
     * @return an optional containing the local group with that newsletter, otherwise the optional is empty
     */
    Optional<LocalGroup> findByNewsletter(Newsletter newsletter);

    /**
     * @param board the board to query for
     * @return the local group with the given board
     */
    LocalGroup findByBoard(Board board);

}
