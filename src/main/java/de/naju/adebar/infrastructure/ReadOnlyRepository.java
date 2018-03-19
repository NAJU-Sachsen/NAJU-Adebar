package de.naju.adebar.infrastructure;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Base interface for repositories which should not provide write access to the underlying database.
 * Works exactly like the {@link CrudRepository}, just without modifying methods. Even the JavaDoc
 * is copied from there.
 *
 * @author Rico Bergmann
 * @see CrudRepository
 * @see <a href= "https://spring.io/blog/2011/07/27/fine-tuning-spring-data-repositories">Read-only
 *     repositories</a>
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T, I extends Serializable> extends Repository<T, I> {

  /**
   * Retrieves an entity by its id.
   *
   * @param id must not be {@code null}.
   * @return the entity with the given id or {@code null} if none found
   */
  Optional<T> findById(I id);

  /**
   * Returns all instances of the type.
   *
   * @return all entities
   */
  Iterable<T> findAll();

  /**
   * Returns all instances of the type with the given IDs.
   */
  Iterable<T> findAllById(Iterable<I> ids);

  /**
   * Checks, whether an entity with the given id exists.
   *
   * @param id must not be {@code null}.
   */
  boolean existsById(I id);

  /**
   * Returns the number of entities available
   *
   * @return the number of entities
   */
  long count();
}
