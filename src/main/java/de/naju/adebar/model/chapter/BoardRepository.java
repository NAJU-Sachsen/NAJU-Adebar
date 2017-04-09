package de.naju.adebar.model.chapter;

import de.naju.adebar.model.human.Activist;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access {@link Board} instances
 * @author Rico Bergmann
 */
public interface BoardRepository extends CrudRepository<Board, Long> {

    /**
     * @param activist the activist to query for
     * @return all boards with the given activist as member
     */
    Iterable<Board> findByMembersContains(Activist activist);

}
