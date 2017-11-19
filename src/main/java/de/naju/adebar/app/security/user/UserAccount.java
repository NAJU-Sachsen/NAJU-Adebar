package de.naju.adebar.app.security.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import de.naju.adebar.model.human.Person;

/**
 * A user account. Each account is created for an activist who thereby gets access to the
 * application.
 * 
 * @author Rico Bergmann
 *
 */
@Entity(name = "userAccount")
public class UserAccount implements UserDetails {
  private static final long serialVersionUID = 756690351442752594L;

  @Id
  @Column(name = "username")
  private String username;

  @Embedded
  private Password password;

  @OneToOne
  @JoinColumn(name = "person")
  private Person associatedPerson;

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
    this.associatedPerson = person;
    this.authorities = authorities;
    this.enabled = enabled;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private UserAccount() {}

  /**
   * @return the person this account is created for
   */
  public Person getAssociatedPerson() {
    return associatedPerson;
  }

  @Override
  public Collection<SimpleGrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password.getPassword();
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
   * Updates the password
   * 
   * @param password the new password
   */
  void setPassword(String password) {
    Assert.hasText(password, "New password may not be empty");
    this.password = new Password(password);
  }

  /**
   * Updates the password
   * 
   * @param password the new password
   */
  void setPassword(Password password) {
    Assert.notNull(password, "New password may not be null");
    this.password = password;
  }

  /**
   * Updates the authorities
   * 
   * @param authorities the new authorities
   */
  void setAuthorities(List<SimpleGrantedAuthority> authorities) {
    this.authorities = authorities;
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
