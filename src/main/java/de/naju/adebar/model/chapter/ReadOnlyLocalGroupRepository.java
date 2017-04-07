package de.naju.adebar.model.chapter;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.newsletter.Newsletter;

import java.util.Optional;

/**
 * A repository that provides read-only access to the saved local groups
 * @author Rico Bergmann
 */
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
    Iterable<LocalGroup> findByMembersContains(Activist activist);

    /**
     * @param newsletter the newsletter to query for
     * @return an optional containing the local group with that newsletter, otherwise the optional is empty
     */
    Optional<LocalGroup> findByNewsletter(Newsletter newsletter);

}
