package de.naju.adebar.model.chapter;

import de.naju.adebar.infrastructure.ReadOnlyRepository;
import de.naju.adebar.model.persons.Person;

/**
 * @author Rico Bergmann
 */
public interface ReadOnlyBoardRepository extends ReadOnlyRepository<Board, Long> {
  /**
   * @param activist the activist to query for
   * @return all boards with the given activist as member
   */
  Iterable<Board> findByMembersContains(Person activist);
}
