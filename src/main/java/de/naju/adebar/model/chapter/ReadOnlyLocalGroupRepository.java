package de.naju.adebar.model.chapter;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.newsletter.Newsletter;

import java.util.Optional;

/**
 * @author Rico Bergmann
 */
public interface ReadOnlyLocalGroupRepository extends ReadOnlyRepository<LocalGroup, Long> {

    Optional<LocalGroup> findByName(String name);

    Iterable<LocalGroup> findByMembersContains(Activist activist);

    Optional<LocalGroup> findByNewsletter(Newsletter newsletter);

}
