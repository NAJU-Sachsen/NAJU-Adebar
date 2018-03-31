package de.naju.adebar.model.persons.family;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

/**
 * Repository to query for persons based on their relatives
 *
 * @author Rico Bergmann
 */
interface FamilyRelationsRepository extends
    Repository<Person, PersonId>,
    QuerydslPredicateExecutor<Person> {

  /**
   * @param child the person to query for. May never be {@code null}
   * @return all registered parents of the person
   */
  @Query("SELECT ps FROM person p JOIN p.parents ps WHERE p = ?1")
  List<Person> findParentsOf(Person child);

  /**
   * @param parent the person to query for. May never be {@code null}
   * @return all registered children of the person
   */
  @Query("SELECT p FROM person p JOIN p.parents ps WHERE ps = ?1")
  List<Person> findChildrenOf(Person parent);

  /**
   * @param person the person to query for. May never be {@code null}
   * @return all registered siblings of the person
   */
  @Query(value = "SELECT DISTINCT sib.* "
      + "FROM person sib"
      + "  JOIN parents p ON sib.id = p.child "
      + "WHERE p.parent IN ( "
      + "  SELECT DISTINCT par.parent "
      + "  FROM person p "
      + "    JOIN parents par"
      + "      ON p.id = par.child"
      + "  WHERE p.id = ?1) "
      + "AND NOT sib.id = ?1", nativeQuery = true)
  List<Person> findSiblingsOf(Person person);
}
