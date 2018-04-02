package de.naju.adebar.model.core;

import de.naju.adebar.documentation.ddd.ValueObject;
import de.naju.adebar.util.Validation;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * Wrapper for email addresses
 *
 * @author Rico Bergmann
 */
@ValueObject
@Embeddable
public class Email implements Serializable {

  private static final long serialVersionUID = 1714106666349190494L;

  @Column(name = "email")
  private String value;

  /**
   * "Full" constructor
   *
   * @param email the email
   */
  private Email(String email) {
    Assert.isTrue(Validation.isEmail(email), "Not a valid email: " + email);
    this.value = email.toLowerCase();
  }

  /**
   * Default constructor just for JPA
   */
  private Email() {}

  /**
   * Creates a new email-address
   *
   * @param email the raw address
   * @return an email value-object
   *
   * @throws IllegalArgumentException if the email is not valid
   */
  public static Email of(String email) {
    return new Email(email);
  }

  /**
   * @return the email address
   */
  public String getValue() {
    return value;
  }

  /**
   * Just for JPA
   *
   * @param email the email address
   */
  private void setValue(String email) {
    Assert.isTrue(Validation.isEmail(email), "Not a valid email: " + email);
    this.value = email.toLowerCase();
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
    result = prime * result + ((value == null) ? 0 : value.hashCode());
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
    Email other = (Email) obj;
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return value;
  }

}
