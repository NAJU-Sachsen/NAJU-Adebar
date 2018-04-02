package de.naju.adebar.app.chapter;

import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoActivistException;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Service to take care of {@link LocalGroup local groups}
 *
 * @author Rico Bergmann
 * @see LocalGroup
 */
@Service
public interface LocalGroupManager {

  /**
   * Saves the given local group. It may or may not be saved already
   *
   * @param localGroup the chapter to save
   * @return the saved chapter. As its internal state may differ after the save, this instance
   *     should be used for future operations
   */
  LocalGroup saveLocalGroup(LocalGroup localGroup);

  /**
   * Creates a new local group
   *
   * @param name the local group's name
   * @param address the local group's address
   * @return the freshly created local group instance
   */
  LocalGroup createLocalGroup(String name, Address address);

  /**
   * Changes the state of a saved local group. This will overwrite the complete state of the group
   * to update
   *
   * @param id the identifier of the local group to update
   * @param newLocalGroup the new local group's data
   * @return the updated chapter
   */
  LocalGroup updateLocalGroup(long id, LocalGroup newLocalGroup);

  /**
   * Changes the state of a saved local group. In difference to {@link #updateLocalGroup(long,
   * LocalGroup)} this does only modify "static" information, such as name and address, but leaves
   * "dynamic" fields like events and projects untouched
   *
   * @param id the identifier of the local group to update
   * @param localGroupData the local groups "static" data to adopt
   * @return the updated chapter
   */
  LocalGroup adoptLocalGroupData(long id, LocalGroup localGroupData);

  /**
   * Updates the board of a local group
   *
   * @param groupId the identifier of the local group to update
   * @param boardData the board's data
   * @return the updated chapter
   */
  LocalGroup updateBoard(long groupId, Board boardData);

  /**
   * Queries for a specific local group
   *
   * @param id the local group's id
   * @return an optional containing the chapter if it exists, otherwise the optional is empty
   */
  Optional<LocalGroup> findLocalGroup(long id);

  /**
   * Provides access to the underlying data
   *
   * @return a read only instance of the database
   */
  ReadOnlyLocalGroupRepository repository();

  /**
   * Adds a newsletter to a local group.. Surprise surprise
   *
   * @param localGroup the local group to add the newsletter to
   * @param newsletter the newsletter to add
   */
  void addNewsletterToLocalGroup(LocalGroup localGroup, Newsletter newsletter);

  /**
   * Deletes the newsletter from a local group
   *
   * @param localGroup the chapter to remove the newsletter from
   * @param newsletter the newsletter to remove
   */
  void removeNewsletter(LocalGroup localGroup, Newsletter newsletter);

  /**
   * Creates a new project for a local group
   *
   * @param localGroup the local group
   * @param projectName the project's name
   * @return the created project
   */
  Project createProject(LocalGroup localGroup, String projectName);

  /**
   * Chains a new project and a local group together.
   *
   * @param localGroup the local group
   * @param project the project
   * @return the persisted project
   *
   * @throws IllegalStateException if the project is already hosted by another group
   */
  Project addProjectToLocalGroup(LocalGroup localGroup, Project project);

  /**
   * Provides access to all local groups where the given activist is member of the board
   *
   * @param activist the activist
   * @return all local groups where the given activist is member of the board
   */
  Iterable<LocalGroup> findAllLocalGroupsForBoardMember(Person activist);

  /**
   * Makes an activist part of a local group if is no member yet.
   *
   * @param group the group the member is added to
   * @param activist the person
   * @throws NoActivistException if the person is no activist
   */
  void addActivistToLocalGroupIfNecessary(LocalGroup group, Person activist);

  /**
   * Ensures that an activist is part of exactly the local groups specified This means especially
   * that if the person was member of other chapters before, those will be removed.
   *
   * @param activist the activist to update
   * @param localGroups the local groups the activist should be part of
   */
  void updateLocalGroupMembership(Person activist, Iterable<LocalGroup> localGroups);

}
