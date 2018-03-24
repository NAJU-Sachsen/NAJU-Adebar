package de.naju.adebar.model.persons.family;

import de.naju.adebar.model.persons.Person;

/**
 * Service to conveniently update the family relations of a person
 */
public interface VitalRecord {

  /**
   * Searches for all data about family relation of a person
   *
   * @param person the person
   * @return all known relatives. If non are known, the object will be empty, but never {@code null}
   */
  Relatives findRelativesOf(Person person);

  /**
   * Saves a person as child of another person
   *
   * @param parent the parent-to-be
   * @param theChild the child
   */
  void addChildTo(Person parent, Person theChild);

  /**
   * Saves a person as parent of another person
   *
   * @param child the child-to-be
   * @param theParent the parent
   */
  void addParentTo(Person child, Person theParent);

  /**
   * Saves a person as sibling of another person.
   *
   * <p> Note that this currently requires at least one parent of the person to be known as
   * relatives are saved in terms of their parent-child relationship. All other relations are
   * inferred from this one.
   *
   * <p> This also means, that {@code addSiblingTo(p, s) == addSiblingTo(s, p)} and after saving one
   * person as sibling of another, the inverse call is not necessary and may in fact lead to an
   * error.
   *
   * @param person the person to add the sibling to
   * @param theSibling the sibling
   */
  void addSiblingTo(Person person, Person theSibling);
}
