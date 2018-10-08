package de.naju.adebar.app.security.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import de.naju.adebar.model.persons.PersonId;

/**
 * Repository to access {@link UserAccount} instances
 *
 * @author Rico Bergmann
 *
 */
public interface UserAccountRepository extends CrudRepository<UserAccount, Username> {

  /**
   * Queries for an user account by its username.
   * <p>
   * This is just a better-sounding alias for {@link #findById(Username)}.
   *
   * @param username the username. May never be {@code null}.
   * @return the user account if it exists or an empty optional otherwise
   */
  Optional<UserAccount> findByUsername(Username username);

  /**
   * Queries for an user account by the person it is associated to.
   *
   * @param id the person's id. May never be {@code null}.
   * @return the user account if it exists
   */
  Optional<UserAccount> findByAssociatedPerson(PersonId id);

  /**
   * Queries for all user accounts with a given authority
   *
   * @param authority the authority. May never be {@code null}.
   * @return the accounts
   */
  List<UserAccount> findByAuthoritiesContains(SimpleGrantedAuthority authority);

  /**
   * Deletes an {@code UserAccount} based on its {@link Username}.
   * <p>
   * This is just a better-sounding alias for {@link #deleteById(Username)}.
   *
   * @param id the username. May never be {@code null}.
   */
  void deleteByUsername(Username id);

  /**
   * Searches for an {@code UserAccount} baed on its {@link Username}.
   * <p>
   * This is just a better-sounding alias for {@link #existsById(Username)}.
   *
   * @param id the username. May never be {@code null}.
   * @return whether there is an {@code UserAccount} with the given {@code id}.
   */
  boolean existsByUsername(Username id);

}
