package de.naju.adebar.app.security.user;

import java.io.Serializable;
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
public interface UserAccountRepository extends CrudRepository<UserAccount, String> {

  /**
   * Queries for an user account by its username. Basically the same as
   * {@link #findOne(Serializable)}, just for receiving an {@link Optional}
   * 
   * @param username the username
   * @return the user account if it exists or an empty optional otherwise
   */
  Optional<UserAccount> findByUsername(String username);

  /**
   * Queries for an user account by the person it is associated to.
   * 
   * @param id the person's id
   * @return the user account if it exists
   */
  Optional<UserAccount> findByAssociatedPerson(PersonId id);

  /**
   * Queries for all user accounts with a given authority
   * 
   * @param authority the authority
   * @return the accounts
   */
  List<UserAccount> findByAuthoritiesContains(SimpleGrantedAuthority authority);

}
