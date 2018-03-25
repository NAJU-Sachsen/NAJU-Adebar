package de.naju.adebar.app.security.user;

import de.naju.adebar.app.news.ReleaseNotesPublishedEvent;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

  @Override
  public UserAccount createFor(String userName, String password, Person person) {
    return createFor(userName, password, person, false);
  }

  @Override
  public UserAccount createFor(String userName, String password, Person person, boolean encrypted) {
    return createFor(userName, password, person, Collections.emptyList(), encrypted);
  }

  @Override
  public UserAccount createFor(String userName, String password, Person person,
      List<SimpleGrantedAuthority> authorities, boolean encrypted) {
    if (usernameExists(userName)) {
      throw new ExistingUserNameException(userName);
    }
    Password pw = generatePassword(password, encrypted);

    if (authorities.isEmpty()) {
      authorities.add(Roles.ROLE_USER);
    }

    return accountRepo.save(new UserAccount(userName, pw, person, authorities, true));
  }

  @Override
  public Optional<UserAccount> find(String userName) {
    return accountRepo.findById(userName);
  }

  @Override
  public Optional<UserAccount> find(PersonId personId) {
    return accountRepo.findByAssociatedPerson(personId);
  }

  @Override
  public Optional<UserAccount> find(Person person) {
    return find(person.getId());
  }

  @Override
  public void deleteAccount(String username) {
    if (!usernameExists(username)) {
      throw new UsernameNotFoundException(USERNAME_NOT_FOUND_MSG + username);
    }
    accountRepo.deleteById(username);
  }

  @Override
  public boolean usernameExists(String username) {
    return accountRepo.existsById(username);
  }

  @Override
  public boolean hasUserAccount(Person person) {
    return accountRepo.findByAssociatedPerson(person.getId()).isPresent();
  }

  @Override
  public UserAccount updatePassword(String username, String currentPassword, String newPassword,
      boolean encrypted) {
    UserAccount account = find(username)
        .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_MSG + username));

    if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
      throw new PasswordMismatchException("For user " + username);
    }

    return accountRepo.save(account.updatePassword(generatePassword(newPassword, encrypted)));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserAccount resetPassword(String username, String newPassword, boolean encrypted) {
    UserAccount account = find(username)
        .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_MSG + username));

    return accountRepo.save(account.updatePassword(generatePassword(newPassword, encrypted)));
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserAccount updateAuthorities(UserAccount account,
      List<SimpleGrantedAuthority> newAuthorities) {

    if (newAuthorities.isEmpty()) {
      newAuthorities.add(Roles.ROLE_USER);
    }

    return accountRepo.save(account.updateAuthorities(newAuthorities));
  }

  @Override
  @EventListener
  public void updateUserAccountIfNecessary(PersonDataUpdatedEvent event) {
    find(event.getEntity()).ifPresent(account -> updateUserAccount(account, event.getEntity()));
  }

  @Override
  @EventListener
  public void notifyAboutNewReleaseNotes(ReleaseNotesPublishedEvent event) {
    Iterable<UserAccount> accounts = accountRepo.findAll();
    accounts.forEach(UserAccount::notifyAboutNewReleaseNotes);
    accountRepo.saveAll(accounts);
  }

  @Override
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
