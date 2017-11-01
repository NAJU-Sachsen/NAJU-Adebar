package de.naju.adebar.app.security.user;

import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * Just a password. Each instance is immutable
 * 
 * @author Rico Bergmann
 *
 */
@Embeddable
public class Password {
  private String password;

  /**
   * @param password the password
   */
  public Password(String password) {
    Assert.hasText(password, "Password may not be null nor empty, but was: " + password);
    this.password = password;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private Password() {}

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password the password
   */
  @SuppressWarnings("unused")
  private void setPassword(String password) {
    Assert.hasText(password, "Password may not be null nor empty, but was: " + password);
    this.password = password;
  }

  // overridden from Object

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((password == null) ? 0 : password.hashCode());
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
    Password other = (Password) obj;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "XXXXXXX";
  }
}
