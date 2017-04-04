package de.naju.adebar.model.chapter;

import de.naju.adebar.model.events.EventManager;
import de.naju.adebar.model.human.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;

/**
 * A {@link LocalGroupManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentLocalGroupManager implements LocalGroupManager {
    private LocalGroupRepository localGroupRepo;
    private ProjectRepository projectRepo;
    private ReadOnlyLocalGroupRepository roRepo;

    @Autowired
    public PersistentLocalGroupManager(LocalGroupRepository localGroupRepo, ProjectRepository projectRepo, ReadOnlyLocalGroupRepository roRepo) {
        Object[] params = {localGroupRepo, projectRepo, roRepo};
        Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
        this.localGroupRepo = localGroupRepo;
        this.projectRepo = projectRepo;
        this.roRepo = roRepo;
    }

    @Override
    public LocalGroup saveLocalGroup(LocalGroup localGroup) {
        return localGroupRepo.save(localGroup);
    }

    @Override
    public LocalGroup createLocalGroup(String name, Address address) {
        return localGroupRepo.save(new LocalGroup(name, address));
    }

    @Override
    public LocalGroup updateLocalGroup(long id, LocalGroup newLocalGroup) {
        newLocalGroup.setId(id);
        return localGroupRepo.save(newLocalGroup);
    }

    @Override
    public LocalGroup adoptLocalGroupData(long id, LocalGroup localGroupData) {
        LocalGroup localGroup = findLocalGroup(id).orElseThrow(() -> new IllegalArgumentException("No local group with id " + id));
        localGroup.setName(localGroupData.getName());
        localGroup.setAddress(localGroupData.getAddress());
        return localGroupRepo.save(localGroup);
    }

    @Override
    public Optional<LocalGroup> findLocalGroup(long id) {
        return localGroupRepo.exists(id) ? Optional.of(localGroupRepo.findOne(id)) : Optional.empty();
    }

    @Override
    public ReadOnlyLocalGroupRepository repository() {
        return roRepo;
    }

    @Override
    public void removeNewsletter(LocalGroup localGroup) {
        localGroup.setNewsletter(null);
        updateLocalGroup(localGroup.getId(), localGroup);
    }

    @Override
    public Project addProjectToLocalGroup(LocalGroup localGroup, String projectName) {
        Project project = new Project(projectName, localGroup);
        localGroup.addProject(project);
        project = projectRepo.save(project);
        localGroupRepo.save(localGroup);
        return project;
    }
}
