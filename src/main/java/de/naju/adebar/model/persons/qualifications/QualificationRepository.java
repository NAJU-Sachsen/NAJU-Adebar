package de.naju.adebar.model.persons.qualifications;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to access {@link Qualification} instances
 *
 * @author Rico Bergmann
 * @see Qualification
 */
public interface QualificationRepository extends CrudRepository<Qualification, String> {

  /**
   * Searches for all qualifications which a referent does not have
   *
   * @param referentId the ID of the referent
   * @return the qualifications
   */
  @Query(value = "SELECT q.* FROM qualification q  WHERE q.name NOT IN"
      + "(SELECT qualifications_name FROM referent JOIN referent_qualifications ON referent.id = referent_qualifications.referent_id WHERE referent.id = ?1)", nativeQuery = true)
  Iterable<Qualification> findAllQualificationsNotPossessedBy(String referentId);

}
