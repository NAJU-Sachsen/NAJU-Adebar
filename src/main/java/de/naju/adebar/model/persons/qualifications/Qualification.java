package de.naju.adebar.model.persons.qualifications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.util.Assert;

/**
 * Just a qualification, consisting of a name and a description.
 * <p>
 * For the model it is guaranteed that a qualification's name is unique and many other objects will
 * rely on the correctness of that constraint. Therefore qualifications should be created with great
 * caution.
 */
@Entity(name = "qualification")
public class Qualification {

  @Id
  @Column(name = "name")
  private String name;

  @Column(name = "description", length = 512)
  private String description;

  /**
   * Full constructor
   *
   * @param name the qualification's name. Should be unique as it is the primary key for
   *        persistence.
   * @param description the qualification's description. May be empty
   * @throws IllegalArgumentException if any of the parameters was null
   */
  public Qualification(String name, String description) {
    Assert.hasText(name, "Name may not be null nor empty, but was: " + name);
    Assert.notNull(description, "Description may not be null!");
    this.name = name;
    this.description = description;
  }

  /**
   * Default constructor, just for JPA's sake
   */
  protected Qualification() {

  }

  /**
   * @return the qualification's name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the qualification's description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param name the name to set
   */
  protected void setName(String name) {
    Assert.hasText(name, "Name may not be null nor empty, but was: " + name);
    this.name = name;
  }

  /**
   * @param description the description to set
   */
  protected void setDescription(String description) {
    Assert.notNull(description, "Description may not be null!");
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Qualification that = (Qualification) o;

    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return String.format("Qualification: %s (%s)", name, description);
  }
}
