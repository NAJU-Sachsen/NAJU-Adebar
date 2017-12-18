package de.naju.adebar.app.security.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * Just a password. Each instance is immutable
 *
 * @author Rico Bergmann
 *
 */
@Embeddable
public class Password implements Serializable {

  private static final long serialVersionUID = -1198768616494735194L;

  @Column(name = "password")
  private String value;

  /**
   * @param password the password
   */
  public Password(String password) {
    Assert.hasText(password, "Password may not be null nor empty, but was: " + password);
    this.value = password;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private Password() {}

  /**
   * @return the password
   */
  public String getValue() {
    return value;
  }

  /**
   * @param value the password
   */
  @SuppressWarnings("unused")
  private void setValue(String value) {
    Assert.hasText(value, "Password may not be null nor empty, but was: " + value);
    this.value = value;
  }

  // overridden from Object

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "XXXXXXX";
  }
}
