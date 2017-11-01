package de.naju.adebar.app.security.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Repository to access {@link UserAccount} instances
 * 
 * @author Rico Bergmann
 *
 */
public interface UserAccountRepository extends CrudRepository<UserAccount, String> {

  /**
   * Queries for a useraccount by its username. Basically the same as {@link #findOne(String)}, just
   * for returning {@link Optional}
   * 
   * @param username the username
   * @return the useraccount if it exists or an empty optional otherwise
   */
  Optional<UserAccount> findByUsername(String username);

  /**
   * Queries for all user accounts with a given authority
   * 
   * @param authority the authority
   * @return the accounts
   */
  List<UserAccount> findByAuthoritiesContains(SimpleGrantedAuthority authority);

}
