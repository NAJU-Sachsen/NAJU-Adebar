package de.naju.adebar.model.chapter;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access {@link Board} instances
 * 
 * @author Rico Bergmann
 */
public interface BoardRepository extends ReadOnlyBoardRepository, CrudRepository<Board, Long> {
}
