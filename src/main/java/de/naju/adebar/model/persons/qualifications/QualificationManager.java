package de.naju.adebar.model.persons.qualifications;

import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service to take care of {@link Qualification qualifications}
 * 
 * @author Rico Bergmann
 */
@Service
public interface QualificationManager {

  /**
   * Creates a new qualification
   * 
   * @param name the qualification's name
   * @param description the qualification's description
   * @return the freshly created qualification object
   * @throws ExistingQualificationException if the qualification's name is already used
   */
  Qualification createQualification(String name, String description);

  /**
   * @param name the qualification's name
   * @return the associated qualification object if present
   */
  Optional<Qualification> findQualification(String name);

  /**
   * @param name the qualification's name to query for
   * @return {@code true} if there is an qualification with that name
   */
  boolean hasQualification(String name);

  /**
   * @return the qualification repository
   */
  QualificationRepository repository();

}
