package de.naju.adebar.app.security.user;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import de.naju.adebar.app.news.ReleaseNotesPublishedEvent;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;

/**
 * Service to take care of the {@link UserAccountManager} lifecycle
 * 
 * @author Rico Bergmann
 *
 */
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
   * Searches for a user account
   * 
   * @param personId the Id of the person to which the account belongs
   * @return the account if it exists
   */
  Optional<UserAccount> find(PersonId personId);

  /**
   * Searches for a user account
   * 
   * @param person the person to who owns the account
   * @return the account if it exists
   */
  Optional<UserAccount> find(Person person);

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
   * Checks if a person has an user account
   * 
   * @param person the person
   * @return whether the person has a user account
   */
  boolean hasUserAccount(Person person);

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
  UserAccount updateAuthorities(UserAccount account, List<SimpleGrantedAuthority> newAuthorities);

  /**
   * Adapts the user account of a person whenever the person's data has changed
   * 
   * @param event the update event for a person. The person may or may not have an user account.
   */
  void updateUserAccountIfNecessary(PersonDataUpdatedEvent event);

  /**
   * Marks all user accounts to have not read the latest release notes
   * 
   * @param event the event for the new release notes
   */
  void notifyAboutNewReleaseNotes(ReleaseNotesPublishedEvent event);

  /**
   * Marks an user account to have read the latest release notes
   * 
   * @param account the user account
   */
  void readReleaseNotes(UserAccount account);

}
