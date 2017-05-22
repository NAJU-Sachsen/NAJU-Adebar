package de.naju.adebar.app.chapter;

import com.google.common.collect.Lists;
import de.naju.adebar.model.chapter.*;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.newsletter.Newsletter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A {@link LocalGroupManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentLocalGroupManager implements LocalGroupManager {
    private LocalGroupRepository localGroupRepo;
    private ProjectRepository projectRepo;
    private BoardRepository boardRepo;
    private ReadOnlyLocalGroupRepository roRepo;

    @Autowired
    public PersistentLocalGroupManager(LocalGroupRepository localGroupRepo, ProjectRepository projectRepo, BoardRepository boardRepo, @Qualifier("ro_localGroupRepo") ReadOnlyLocalGroupRepository roRepo) {
        Object[] params = {localGroupRepo, projectRepo, boardRepo, roRepo};
        Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
        this.localGroupRepo = localGroupRepo;
        this.projectRepo = projectRepo;
        this.boardRepo = boardRepo;
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
        setLocalGroupId(newLocalGroup, id);
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
    public LocalGroup updateBoard(long groupId, Board boardData) {
        LocalGroup localGroup = findLocalGroup(groupId).orElseThrow(() -> new IllegalArgumentException("No local group with id " + groupId));
        Board board = localGroup.getBoard();

        if (board == null) {
            board = boardData;
        } else {
            board.setChairman(boardData.getChairman());
            board.setEmail(boardData.getEmail());
            setBoardMembers(board, Lists.newLinkedList(boardData.getMembers()));
        }

        boardRepo.save(board);
        localGroup.setBoard(board);

        return updateLocalGroup(groupId, localGroup);
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
    public void addNewsletterToLocalGroup(LocalGroup localGroup, Newsletter newsletter) {
        localGroup.addNewsletter(newsletter);
        updateLocalGroup(localGroup.getId(), localGroup);
    }

    @Override
    public void removeNewsletter(LocalGroup localGroup, Newsletter newsletter) {
        localGroup.removeNewsletter(newsletter);
        updateLocalGroup(localGroup.getId(), localGroup);
    }

    @Override
    public Project createProject(LocalGroup localGroup, String projectName) {
        Project project = new Project(projectName, localGroup);
        localGroup.addProject(project);
        project = projectRepo.save(project);
        localGroupRepo.save(localGroup);
        return project;
    }

    @Override
    public Project addProjectToLocalGroup(LocalGroup localGroup, Project project) {
        project.setLocalGroup(localGroup);
        localGroup.addProject(project);
        localGroup = updateLocalGroup(localGroup.getId(), localGroup);
        return localGroup.getProject(project.getName()).orElseThrow(() -> new IllegalStateException("Project could not be saved"));
    }

    @Override
    public Iterable<LocalGroup> findAllLocalGroupsForBoardMember(Person activist) {
        Iterable<Board> boards = boardRepo.findByMembersContains(activist);
        LinkedList<LocalGroup> localGroups = new LinkedList<>();
        boards.forEach(board -> localGroups.add(localGroupRepo.findByBoard(board)));
        return localGroups;
    }

    /**
     * Updates a local group's ID. To be used extremely cautiously.
     * @param localGroup the local group to update
     * @param id the new ID
     */
    protected void setLocalGroupId(LocalGroup localGroup, long id) {
        try {
            Method changeId = LocalGroup.class.getDeclaredMethod("setId", long.class);
            changeId.setAccessible(true);
            changeId.invoke(localGroup, id);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error during invocation of reflection", e);
        }
    }

    /**
     * Updates the members of a local group's board
     * @param board the board to update
     * @param members the board's members
     */
    protected void setBoardMembers(Board board, List<Person> members) {
        try {
            Method changeBoardMembers = Board.class.getDeclaredMethod("setMembers", List.class);
            changeBoardMembers.setAccessible(true);
            changeBoardMembers.invoke(board, members);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error during invocation of reflection", e);
        }
    }
}
