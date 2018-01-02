package de.naju.adebar.app.security.user;

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
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonId;
import de.naju.adebar.util.Validation;

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
public class UserAccount extends AbstractAggregateRoot implements UserDetails {

  private static final long serialVersionUID = 756690351442752594L;

  @Id
  @Column(name = "username")
  private String username;

  @Embedded
  private Password password;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "id", column = @Column(name = "associatedPerson")))
  private PersonId associatedPerson;

  private String firstName;
  private String lastName;
  private String email;

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
    this.authorities = authorities;
    this.enabled = enabled;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private UserAccount() {}

  /**
   * @return the ID of the person this account is created for
   */
  public PersonId getAssociatedPerson() {
    return associatedPerson;
  }

  /**
   * @return the person's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return the person's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @return the person's email
   */
  public String getEmail() {
    return email;
  }

  @Override
  public Collection<SimpleGrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password.getValue();
  }

  @Override
  public String getUsername() {
    return username;
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
  UserAccount updatePersonalInformation(String firstName, String lastName, String email) {
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
   * Sets the username. Just for JPA's sake
   *
   * @param username
   */
  @SuppressWarnings("unused")
  private void setUsername(String username) {
    Assert.hasText(username, "User name may not be empty");
    this.username = username;
  }

  /**
   * Sets the associated person. Just for JPA's sake
   * 
   * @param associatedPerson
   */
  @SuppressWarnings("unused")
  private void setAssociatedPerson(PersonId associatedPerson) {
    Assert.notNull(associatedPerson, "Associated person may not null");
    this.associatedPerson = associatedPerson;
  }

  /**
   * Updates the password
   *
   * @param password the new password
   */
  @SuppressWarnings("unused")
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

  /**
   * @param firstName the first name
   */
  private void setFirstName(String firstName) {
    Assert.hasText(firstName, "First name may not be empty");
    this.firstName = firstName;
  }

  /**
   * @param lastName the last name
   */
  private void setLastName(String lastName) {
    Assert.hasText(lastName, "Last name may not be empty");
    this.lastName = lastName;
  }

  /**
   * @param email the email
   */
  private void setEmail(String email) {
    Assert.isTrue(Validation.isEmail(email), "Not a valid email address " + email);
    this.email = email;
  }

  /**
   * @param enabled whether the account is enabled
   */
  @SuppressWarnings("unused")
  private void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UserAccount other = (UserAccount) obj;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return String.format("Username: %s, Password: %s", username, password);
  }

}
