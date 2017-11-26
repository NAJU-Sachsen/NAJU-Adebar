package de.naju.adebar.app.security.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.human.Person;

/**
 * A {@link UserAccountManager} that persists its data in a database
 * 
 * @author Rico Bergmann
 *
 */
@Service
public class PersistentUserAccountManager implements UserAccountManager {
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
    UserAccount account = accountRepo.findOne(userName);
    return account != null ? Optional.of(account) : Optional.empty();
  }

  @Override
  public void deleteAccount(String username) {
    if (!usernameExists(username)) {
      throw new UsernameNotFoundException("For username " + username);
    }
    accountRepo.delete(username);
  }

  @Override
  public boolean usernameExists(String username) {
    return accountRepo.exists(username);
  }

  @Override
  public UserAccount updatePassword(String username, String currentPassword, String newPassword,
      boolean encrypted) {
    UserAccount account =
        find(username).orElseThrow(() -> new UsernameNotFoundException("For username " + username));

    if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
      throw new PasswordMismatchException("For user " + username);
    }

    account.setPassword(generatePassword(newPassword, encrypted));
    return accountRepo.save(account);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserAccount resetPassword(String username, String newPassword, boolean encrypted) {
    UserAccount account =
        find(username).orElseThrow(() -> new UsernameNotFoundException("For username " + username));
    account.setPassword(generatePassword(newPassword, encrypted));
    return accountRepo.save(account);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserAccount updateAuthorities(UserAccount account,
      List<SimpleGrantedAuthority> newAuthorities) {

    if (newAuthorities.isEmpty()) {
      newAuthorities.add(Roles.ROLE_USER);
    }

    account.setAuthorities(newAuthorities);
    return accountRepo.save(account);
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
