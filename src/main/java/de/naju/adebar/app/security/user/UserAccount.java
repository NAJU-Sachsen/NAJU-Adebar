package de.naju.adebar.app.security.user;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * A user account. Each account is created for an activist who thereby gets access to the
 * application.
 * <p>
 * Each account maintains its own copy of the personal information it cares about. Therefore there
 * is no necessity link to a {@link Person} directly.
 *
 * @author Rico Bergmann
 */
@Entity(name = "userAccount")
public class UserAccount extends AbstractAggregateRoot<UserAccount> implements UserDetails {

  private static final long serialVersionUID = 756690351442752594L;

  @Id
  @Column(name = "username")
  private String username;

  @Embedded
  private Password password;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "associatedPerson")))
  private PersonId associatedPerson;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Embedded
  private Email email;

  @Column(name = "readReleaseNotes")
  private boolean readLatestReleaseNotes;

  @ElementCollection(fetch = FetchType.EAGER)
  @JoinTable(name = "userAuthorities", joinColumns = @JoinColumn(name = "userAccount"))
  private List<SimpleGrantedAuthority> authorities;

  private boolean enabled;

  /**
   * Full constructor
   *
   * @param username the username, must be unique
   * @param password the password
   * @param person the person the account is created for
   * @param authorities the authorities the person has
   * @param enabled whether the account is enabled
   */
  public UserAccount(String username, Password password, Person person,
      List<SimpleGrantedAuthority> authorities, boolean enabled) {
    Object[] params = {username, password, person, authorities};
    Assert.noNullElements(params,
        "No parameter may be null, but at least one was: " + Arrays.toString(params));
    Assert.noNullElements(authorities.toArray(), "No authority may be null");
    this.username = username;
    this.password = password;
    this.associatedPerson = person.getId();
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.email = person.getEmail();
    this.readLatestReleaseNotes = false;
    this.authorities = authorities;
    this.enabled = enabled;
  }

  /**
   * Default constructor just for JPA's sake
   */
  private UserAccount() {}

  /**
   * @return the ID of the person this account is created for
   */
  public PersonId getAssociatedPerson() {
    return associatedPerson;
  }

  /**
   * Sets the associated person. Just for JPA's sake
   *
   * @param associatedPerson the associated person
   */
  private void setAssociatedPerson(PersonId associatedPerson) {
    Assert.notNull(associatedPerson, "Associated person may not null");
    this.associatedPerson = associatedPerson;
  }

  /**
   * @return the person's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the first name
   */
  private void setFirstName(String firstName) {
    Assert.hasText(firstName, "First name may not be empty");
    this.firstName = firstName;
  }

  /**
   * @return the person's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the last name
   */
  private void setLastName(String lastName) {
    Assert.hasText(lastName, "Last name may not be empty");
    this.lastName = lastName;
  }

  /**
   * @return whether the user has already read the latest release notes
   */
  public boolean hasReadReleaseNotes() {
    return readLatestReleaseNotes;
  }

  /**
   * @return the person's email
   */
  public Email getEmail() {
    return email;
  }

  /**
   * @param email the email
   */
  private void setEmail(Email email) {
    this.email = email;
  }

  @Override
  public Collection<SimpleGrantedAuthority> getAuthorities() {
    return authorities;
  }

  /**
   * Updates the authorities
   *
   * @param authorities the new authorities
   */
  private void setAuthorities(List<SimpleGrantedAuthority> authorities) {
    if (authorities == null) {
      this.authorities = new ArrayList<>();
    }
    this.authorities = authorities;
  }

  @Override
  public String getPassword() {
    return password.getValue();
  }

  /**
   * Updates the password
   *
   * @param password the new password
   */
  private void setPassword(String password) {
    Assert.hasText(password, "New password may not be empty");
    this.password = new Password(password);
  }

  /**
   * Updates the password
   *
   * @param password the new password
   */
  private void setPassword(Password password) {
    Assert.notNull(password, "New password may not be null");
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username. Just for JPA's sake
   *
   * @param username the username
   */
  private void setUsername(String username) {
    Assert.hasText(username, "User name may not be empty");
    this.username = username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * @param enabled whether the account is enabled
   */
  private void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Checks whether the user has a certain authority
   *
   * @param authority the authority to check
   * @return whether the user has this role
   */
  public boolean hasRole(SimpleGrantedAuthority authority) {
    return authorities.contains(authority);
  }

  /**
   * Sets the user's personal data
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param email the person's last name
   * @return the updated account
   */
  UserAccount updatePersonalInformation(String firstName, String lastName, Email email) {
    setFirstName(firstName);
    setLastName(lastName);
    setEmail(email);
    registerEvent(UserAccountUpdatedEvent.forAccount(this));
    return this;
  }

  /**
   * Sets the user's authorities
   *
   * @param authorities the authorities
   * @return the updated account
   */
  UserAccount updateAuthorities(List<SimpleGrantedAuthority> authorities) {
    setAuthorities(authorities);
    registerEvent(UserAccountUpdatedEvent.forAccount(this));
    return this;
  }

  /**
   * Sets the user's password
   *
   * @param password the new password
   * @return the updated account
   */
  UserAccount updatePassword(Password password) {
    setPassword(password);
    registerEvent(UserAccountUpdatedEvent.forAccount(this));
    return this;
  }

  /**
   * Marks that the user read the latest release notes
   *
   * @return the updated account
   */
  UserAccount readReleaseNotes() {
    setReadLatestReleaseNotes(true);
    registerEvent(UserAccountUpdatedEvent.forAccount(this));
    return this;
  }

  /**
   * Notifies the user about the existence of new release notes
   *
   * @return the updated account
   */
  UserAccount notifyAboutNewReleaseNotes() {
    setReadLatestReleaseNotes(false);
    registerEvent(UserAccountUpdatedEvent.forAccount(this));
    return this;
  }

  /**
   * Just for JPA
   *
   * @return whether the user has read the latest release notes
   */
  private boolean isReadLatestReleaseNotes() {
    return readLatestReleaseNotes;
  }

  /**
   * Just for JPA
   *
   * @param readLatestReleaseNotes whether the user has read the latest release notes
   */
  private void setReadLatestReleaseNotes(boolean readLatestReleaseNotes) {
    this.readLatestReleaseNotes = readLatestReleaseNotes;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    UserAccount other = (UserAccount) obj;
    if (username == null) {
      if (other.username != null) {
        return false;
      }
    } else if (!username.equals(other.username)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return String.format("Username: %s, Password: %s", username, password);
  }

}
