package de.naju.adebar.app.security.user;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import de.naju.adebar.app.news.ReleaseNotesPublishedEvent;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;

/**
 * A {@link UserAccountManager} that persists its data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentUserAccountManager implements UserAccountManager {

  private static final String USERNAME_NOT_FOUND_MSG = "For username ";

  private UserAccountRepository accountRepo;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public PersistentUserAccountManager(UserAccountRepository accountRepo,
      PasswordEncoder passwordEncoder) {
    Object[] params = {accountRepo, passwordEncoder};
    Assert.noNullElements(params,
        "No parameter may be null, but at least one was: " + Arrays.toString(params));
    this.accountRepo = accountRepo;
    this.passwordEncoder = passwordEncoder;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#createFor(java.lang.String,
   * java.lang.String, de.naju.adebar.model.persons.Person)
   */
  @Override
  public UserAccount createFor(String userName, String password, Person person) {
    return createFor(userName, password, person, false);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#createFor(java.lang.String,
   * java.lang.String, de.naju.adebar.model.persons.Person, boolean)
   */
  @Override
  public UserAccount createFor(String userName, String password, Person person, boolean encrypted) {
    return createFor(userName, password, person, Lists.newArrayList(), encrypted);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#createFor(java.lang.String,
   * java.lang.String, de.naju.adebar.model.persons.Person, java.util.List, boolean)
   */
  @Override
  @Transactional
  public UserAccount createFor(String userName, String password, Person person,
      List<SimpleGrantedAuthority> authorities, boolean encrypted) {
    if (usernameExists(userName)) {
      throw new ExistingUserNameException(userName);
    }
    Password pw = generatePassword(password, encrypted);

    if (authorities.isEmpty()) {
      authorities.add(Roles.ROLE_USER);
    }

    return accountRepo.save(new UserAccount(Username.of(userName), pw, person, authorities, true));
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#find(de.naju.adebar.app.security.user.
   * Username)
   */
  @Override
  public Optional<UserAccount> find(Username userName) {
    return accountRepo.findByUsername(userName);
  }


  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#find(java.lang.String)
   */
  @Override
  public Optional<UserAccount> find(String username) {
    return accountRepo.findByUsername(Username.of(username));
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#find(de.naju.adebar.model.persons.PersonId)
   */
  @Override
  public Optional<UserAccount> find(PersonId personId) {
    return accountRepo.findByAssociatedPerson(personId);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#find(de.naju.adebar.model.persons.Person)
   */
  @Override
  public Optional<UserAccount> find(Person person) {
    return find(person.getId());
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#deleteAccount(de.naju.adebar.app.security.
   * user.Username)
   */
  @Override
  @Transactional
  public void deleteAccount(Username username) {
    if (!usernameExists(username.getValue())) {
      throw new UsernameNotFoundException(USERNAME_NOT_FOUND_MSG + username);
    }
    accountRepo.deleteByUsername(username);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#usernameExists(java.lang.String)
   */
  @Override
  public boolean usernameExists(String username) {
    return accountRepo.existsByUsername(Username.of(username));
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#hasUserAccount(de.naju.adebar.model.persons
   * .Person)
   */
  @Override
  public boolean hasUserAccount(Person person) {
    return accountRepo.findByAssociatedPerson(person.getId()).isPresent();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#updatePassword(de.naju.adebar.app.security.
   * user.Username, java.lang.String, java.lang.String, boolean)
   */
  @Override
  @Transactional
  public UserAccount updatePassword(Username username, String currentPassword, String newPassword,
      boolean encrypted) {
    UserAccount account = find(username)
        .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_MSG + username));

    if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
      throw new PasswordMismatchException("For user " + username);
    }

    return accountRepo.save(account.updatePassword(generatePassword(newPassword, encrypted)));
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#resetPassword(de.naju.adebar.app.security.
   * user.Username, java.lang.String, boolean)
   */
  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Transactional
  public UserAccount resetPassword(Username username, String newPassword, boolean encrypted) {
    UserAccount account = find(username)
        .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_MSG + username));

    return accountRepo.save(account.updatePassword(generatePassword(newPassword, encrypted)));
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#updateAuthorities(de.naju.adebar.app.
   * security.user.UserAccount, java.util.List)
   */
  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Transactional
  public UserAccount updateAuthorities(UserAccount account,
      List<SimpleGrantedAuthority> newAuthorities) {

    if (newAuthorities.isEmpty()) {
      newAuthorities.add(Roles.ROLE_USER);
    }

    return accountRepo.save(account.updateAuthorities(newAuthorities));
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#updateUserAccountIfNecessary(de.naju.adebar
   * .model.persons.events.PersonDataUpdatedEvent)
   */
  @Override
  @EventListener
  public void updateUserAccountIfNecessary(PersonDataUpdatedEvent event) {
    find(event.getEntity()).ifPresent(account -> updateUserAccount(account, event.getEntity()));
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.app.security.user.UserAccountManager#notifyAboutNewReleaseNotes(de.naju.adebar.
   * app.news.ReleaseNotesPublishedEvent)
   */
  @Override
  @EventListener
  @Transactional
  public void notifyAboutNewReleaseNotes(ReleaseNotesPublishedEvent event) {
    Iterable<UserAccount> accounts = accountRepo.findAll();
    accounts.forEach(UserAccount::notifyAboutNewReleaseNotes);
    accountRepo.saveAll(accounts);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.security.user.UserAccountManager#readReleaseNotes(de.naju.adebar.app.
   * security.user.UserAccount)
   */
  @Override
  @Transactional
  public void readReleaseNotes(UserAccount account) {
    account.readReleaseNotes();
    accountRepo.save(account);
  }

  /**
   * Sets the personal data of the user account
   *
   * @param account the account to update
   * @param person the person whose data should be used
   */
  @Transactional
  private void updateUserAccount(UserAccount account, Person person) {
    accountRepo.save( //
        account.updatePersonalInformation( //
            person.getFirstName(), //
            person.getLastName(), //
            person.getEmail()));
  }

  /**
   * Creates a new {@link Password} instance and encrypts it if necessary
   *
   * @param rawPassword the password to use
   * @param encrypted whether the password is already encrypted
   * @return the password
   */
  private Password generatePassword(String rawPassword, boolean encrypted) {
    if (encrypted) {
      return new Password(rawPassword);
    } else {
      return new Password(passwordEncoder.encode(rawPassword));
    }
  }

}
