package de.naju.adebar.app.security.user;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import de.naju.adebar.model.human.Person;

/**
 * Service to take care of the {@link UserAccountManager} lifecycle
 * 
 * @author Rico Bergmann
 *
 */
@Service
public interface UserAccountManager {

  /**
   * Creates a new user account for the given person
   * 
   * @param username the person's username
   * @param password the person's password
   * @param person the person the account is created for
   * @return the user account
   */
  UserAccount createFor(String username, String password, Person person);

  /**
   * Creates a new user account for the given person
   * 
   * @param username the person's username
   * @param password the person's password
   * @param person the person the account is created for
   * @param encrypted whether the password is already encrypted
   * @return the user account
   */
  UserAccount createFor(String username, String password, Person person, boolean encrypted);

  /**
   * Creates a new user account for the given person
   * 
   * @param username the person's username
   * @param password the person's password
   * @param person the person the account is created for
   * @param authorities the authorities the account should possess
   * @param encrypted whether the password is already encrypted
   * @return the user account
   */
  UserAccount createFor(String username, String password, Person person,
      List<SimpleGrantedAuthority> authorities, boolean encrypted);

  /**
   * Searches for a user account
   * 
   * @param username the user name
   * @return the account or an empty {@link Optional}
   */
  Optional<UserAccount> find(String username);

  /**
   * Removes a user account
   * 
   * @param username the user name of the account
   */
  void deleteAccount(String username);

  /**
   * Searches for a given username
   * 
   * @param username the username
   * @return whether the username exists
   */
  boolean usernameExists(String username);

  /**
   * Replaces the a user account's current password
   * 
   * @param username the username of the account
   * @param currentPassword the current password (encrypted!)
   * @param newPassword the new password
   * @param encrypted whether the new password is already encrypted
   * @return the updated user account
   */
  UserAccount updatePassword(String username, String currentPassword, String newPassword,
      boolean encrypted);

  /**
   * Resets a user account's current password. This may only be done by the administrator!
   * 
   * @param username the username of the account
   * @param newPassword the new password
   * @param encrypted whether the new password is already encrypted
   * @return the updated user account
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  UserAccount resetPassword(String username, String newPassword, boolean encrypted);

  /**
   * Updates the roles a user account has. This may only be done by the administrator!
   * 
   * @param account the account
   * @param newAuthorities the new authorities
   * @return the updated authorities
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  UserAccount updateAuthorities(UserAccount account, List<SimpleGrantedAuthority> newAuthorities);

}
