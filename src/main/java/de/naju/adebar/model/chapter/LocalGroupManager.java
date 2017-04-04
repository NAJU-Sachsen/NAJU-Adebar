package de.naju.adebar.model.chapter;

import de.naju.adebar.model.human.Address;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to take care of {@link LocalGroup local groups}
 * @author Rico Bergmann
 * @see LocalGroup
 */
@Service
public interface LocalGroupManager {

    /**
     * Saves the given local group. It may or may not be saved already
     * @param localGroup the chapter to save
     * @return the saved chapter. As its internal state may differ after the save, this instance should be used
     * for future operations
     */
    LocalGroup saveLocalGroup(LocalGroup localGroup);

    /**
     * Creates a new local group
     * @param name the local group's name
     * @param address the local group's address
     * @return the freshly created local group instance
     */
    LocalGroup createLocalGroup(String name, Address address);

    /**
     * Changes the state of a saved local group. This will overwrite the complete state of the group to update
     * @param id the identifier of the local group to update
     * @param newLocalGroup the new local group's data
     * @return the updated chapter
     */
    LocalGroup updateLocalGroup(long id, LocalGroup newLocalGroup);

    /**
     * Changes the state of a saved local group. In difference to {@link #updateLocalGroup(long, LocalGroup)} this does
     * only modify "static" information, such as name and address, but leaves "dynamic" fields like events and projects
     * untouched
     * @param id the identifier of the local group to update
     * @param localGroupData the local groups "static" data to adopt
     * @return the updated chapter
     */
    LocalGroup adoptLocalGroupData(long id, LocalGroup localGroupData);

    /**
     * Queries for a specific local group
     * @param id the local group's id
     * @return an optional containing the chapter if it exists, otherwise the optional is empty
     */
    Optional<LocalGroup> findLocalGroup(long id);

    /**
     * Provides access to the underlying data
     * @return a read only instance of the database
     */
    ReadOnlyLocalGroupRepository repository();

    /**
     * Deletes the newsletter from a local group
     * @param localGroup the chapter to remove the newsletter from
     */
    void removeNewsletter(LocalGroup localGroup);

    /**
     * Chains a new project and a local group together.
     * @param localGroup the local group
     * @param projectName the project's name
     * @return the created project
     */
    Project addProjectToLocalGroup(LocalGroup localGroup, String projectName);

}
