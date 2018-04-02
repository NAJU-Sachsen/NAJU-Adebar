package de.naju.adebar.model.newsletter;

import de.naju.adebar.model.core.Email;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to access {@link Newsletter}
 *
 * @author Rico Bergmann
 */
@Repository
public interface NewsletterRepository extends CrudRepository<Newsletter, Long> {

  /**
   * @param name the newsletter's name
   * @return all newsletters with the given name
   */
  Iterable<Newsletter> findByName(String name);

  /**
   * @param subscriber the subscriber to query for
   * @return all newsletters that the subscriber signed up to
   */
  Iterable<Newsletter> findBySubscribersContains(Subscriber subscriber);

  /**
   * @param email the subscriber's email
   * @return all newsletters that the subscriber signed up to
   */
  Iterable<Newsletter> findBySubscribersEmail(Email email);

  /**
   * @param id the subscriber's id
   * @return all newsletters that the subscriber signed up to
   */
  Iterable<Newsletter> findBySubscribersId(Long id);
}
