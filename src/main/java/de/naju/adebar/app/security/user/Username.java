package de.naju.adebar.app.security.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

/**
 * A {@code Username} uniquely identifies a user within the system.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class Username implements Serializable {

  private static final long serialVersionUID = 3251914712410663160L;

  @Column(name = "username")
  private String value;

  /**
   * Generates a new {@code Username}.
   *
   * @param value the username. May not be {@code null} nor empty.
   */
  public static Username of(String value) {
    return new Username(value);
  }

  /**
   * Full constructor.
   *
   * @param value the username. May not be {@code null} nor empty.
   */
  private Username(String value) {
    Assert.hasText(value, "Username may not be null nor empty");
    this.value = value;
  }

  /**
   * Default constructor just for JPA's sake.
   */
  @JpaOnly
  private Username() {}

  /**
   * Gets the username.
   */
  public final String getValue() {
    return value;
  }

  /**
   * Sets the username.
   *
   * @param value the username. May not be {@code null nor empty}.
   */
  @JpaOnly
  private void setValue(String value) {
    Assert.hasText(value, "Value may not be null nor empty");
    this.value = value;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Username username = (Username) o;

    return value.equals(username.value);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return value.hashCode();
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
