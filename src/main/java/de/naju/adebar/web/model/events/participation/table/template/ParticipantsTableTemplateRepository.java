package de.naju.adebar.web.model.events.participation.table.template;

import de.naju.adebar.app.security.user.Username;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository to store {@link ParticipantsTableTemplate} instances.
 *
 * @author Rico Bergmann
 */
public interface ParticipantsTableTemplateRepository
    extends CrudRepository<ParticipantsTableTemplate, ParticipantsTableTemplateId> {

  /**
   * Queries for all templates that were created by a certain user.
   */
  List<ParticipantsTableTemplate> findByIdCreatedBy(Username createdBy);

  /**
   * Queries for a specific template.
   */
  Optional<ParticipantsTableTemplate> findByIdCreatedByAndIdName(Username createdBy, String name);

  /**
   * Queries for all templates that were created by a certain user.
   * <p>
   * This is just a better-sounding alias for {@link #findByIdCreatedBy(Username)}.
   */
  default Optional<ParticipantsTableTemplate> findByCreatorAndName(Username creator,
      String templateName) {
    return findByIdCreatedByAndIdName(creator, templateName);
  }

  /**
   * Queries for a specific template.
   * <p>
   * This is just a better-sounding alias for {@link #findByIdCreatedByAndIdName(Username,
   * String)}.
   */
  default List<ParticipantsTableTemplate> findByCreator(Username creator) {
    return findByIdCreatedBy(creator);
  }

}
