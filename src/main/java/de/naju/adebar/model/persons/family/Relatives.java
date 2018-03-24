package de.naju.adebar.model.persons.family;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;

/**
 * Wrapper to provide convenient access to the family relations of a {@link Person}
 *
 * @author Rico Bergmann
 */
public class Relatives {

  private List<Person> parents;
  private List<Person> children;
  private List<Person> siblings;
  private Person person;

  /**
   * Just the constructor
   *
   * @param person the person the relatives are created for
   */
  private Relatives(Person person) {
    Assert.notNull(person, "person may not be null");
    this.person = person;
  }

  /**
   * Factory method for creating new instances
   *
   * @param person the person to create the relatives for
   * @return the person's relatives. If no relatives are known, all fields will be empty (except the
   *     person itself)
   */
  static RelativesBuilder buildRelativesFor(Person person) {
    return new RelativesBuilder(person);
  }

  /**
   * @return the person's parents
   */
  public List<Person> getParents() {
    return Collections.unmodifiableList(parents);
  }

  /**
   * @param parents the person's parents
   */
  private void setParents(List<Person> parents) {
    Assert.notNull(parents, "parents may not be null");
    Assert2.noNullElements(parents, "No parent may be null");
    this.parents = parents;
  }

  /**
   * @return the person's children
   */
  public List<Person> getChildren() {
    return Collections.unmodifiableList(children);
  }

  /**
   * @param children the person's children
   */
  private void setChildren(List<Person> children) {
    Assert.notNull(children, "children may not be null");
    Assert2.noNullElements(children, "No child may be null");
    this.children = children;
  }

  /**
   * @return the person's siblings
   */
  public List<Person> getSiblings() {
    return Collections.unmodifiableList(siblings);
  }

  /**
   * @param siblings the person's siblings
   */
  private void setSiblings(List<Person> siblings) {
    Assert.notNull(siblings, "siblings may not be null");
    Assert2.noNullElements(siblings, "No sibling may be null");
    this.siblings = siblings;
  }

  /**
   * @return the person the relatives belong to
   */
  public Person getPerson() {
    return person;
  }

  /**
   * Builder to handle the construction of {@link Relatives} instances.
   *
   * <p> Note that calling the same method twice will override the previous call. That means that
   * {@code builder.withParents(p1).withParents(p2)} will result in a {@code Relatives} instance
   * with parents {@code p2}.
   *
   * <p> Note further that the builder actually is not stateless. That means that calling any of the
   * {@code withXYZ} methods after the {@link #done()} has been used, the relatives returned by
   * {@code done} will reflect these new changes as well.
   */
  static class RelativesBuilder {

    private Relatives theRelatives;

    /**
     * Just the constructor
     *
     * @param person the person to create the relatives for
     */
    private RelativesBuilder(Person person) {
      theRelatives = new Relatives(person);
    }

    /**
     * Specifies the person's parents
     *
     * @param parents the parents
     * @return the builder for easy method chaining
     */
    public RelativesBuilder withParents(List<Person> parents) {
      theRelatives.setParents(parents);
      return this;
    }

    /**
     * Specifies the person's children
     *
     * @param children the children
     * @return the builder for easy method chaining
     */
    public RelativesBuilder withChildren(List<Person> children) {
      theRelatives.setChildren(children);
      return this;
    }

    /**
     * Specifies the person's siblings
     *
     * @param siblings the siblings
     * @return the builder for easy method chaining
     */
    public RelativesBuilder withSiblings(List<Person> siblings) {
      theRelatives.setSiblings(siblings);
      return this;
    }

    /**
     * Finishes the construction
     *
     * @return the relatives
     */
    public Relatives done() {
      return theRelatives;
    }

  }

}
