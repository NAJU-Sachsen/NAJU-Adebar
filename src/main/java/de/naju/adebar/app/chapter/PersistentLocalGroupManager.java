package de.naju.adebar.app.chapter;

import com.google.common.collect.Lists;
import de.naju.adebar.app.IdUpdateFailedException;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.chapter.BoardRepository;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ProjectRepository;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoActivistException;
import de.naju.adebar.util.Streams;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@link LocalGroupManager} that persists its data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentLocalGroupManager implements LocalGroupManager {

  private LocalGroupRepository localGroupRepo;
  private ProjectRepository projectRepo;
  private BoardRepository boardRepo;
  private ReadOnlyLocalGroupRepository roRepo;

  @Autowired
  public PersistentLocalGroupManager(LocalGroupRepository localGroupRepo,
      ProjectRepository projectRepo, BoardRepository boardRepo,
      @Qualifier("ro_localGroupRepo") ReadOnlyLocalGroupRepository roRepo) {
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
    LocalGroup localGroup = findLocalGroup(id)
        .orElseThrow(() -> new IllegalArgumentException("No local group with id " + id));
    localGroup.setName(localGroupData.getName());
    localGroup.setAddress(localGroupData.getAddress());
    localGroup.setNabuGroupLink(localGroupData.getNabuGroupLink());
    localGroup.setContactPersons(Lists.newLinkedList(localGroupData.getContactPersons()));
    return localGroupRepo.save(localGroup);
  }

  @Override
  public LocalGroup updateBoard(long groupId, Board boardData) {
    LocalGroup localGroup = findLocalGroup(groupId)
        .orElseThrow(() -> new IllegalArgumentException("No local group with id " + groupId));
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
    return localGroupRepo.findById(id);
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
    return localGroup.getProject(project.getName())
        .orElseThrow(() -> new IllegalStateException("Project could not be saved"));
  }

  @Override
  public Iterable<LocalGroup> findAllLocalGroupsForBoardMember(Person activist) {
    Iterable<Board> boards = boardRepo.findByMembersContains(activist);
    LinkedList<LocalGroup> localGroups = new LinkedList<>();
    boards.forEach(board -> localGroups.add(localGroupRepo.findByBoard(board)));
    return localGroups;
  }

  @Override
  public void addActivistToLocalGroupIfNecessary(LocalGroup group, Person activist) {
    if (!activist.isActivist()) {
      throw new NoActivistException("For person " + activist);
    } else if (!group.isMember(activist)) {
      group.addMember(activist);
      updateLocalGroup(group.getId(), group);
    }
  }

  @Override
  public void updateLocalGroupMembership(Person activist, Iterable<LocalGroup> localGroups) {
    List<LocalGroup> currentMembership = Lists.newArrayList(roRepo.findByMembersContains(activist));
    List<LocalGroup> updatedMembership = Lists.newArrayList(localGroups);

    Stream<LocalGroup> groupsToRemove =
        Streams.subtract(currentMembership.stream(), updatedMembership.stream());
    Stream<LocalGroup> groupsToAdd =
        Streams.subtract(updatedMembership.stream(), currentMembership.stream());

    groupsToRemove.forEach(chapter -> {
      chapter.removeMember(activist);
      updateLocalGroup(chapter.getId(), chapter);
    });

    groupsToAdd.forEach(chapter -> {
      chapter.addMember(activist);
      updateLocalGroup(chapter.getId(), chapter);
    });

  }

  /**
   * Updates a local group's ID. To be used extremely cautiously.
   *
   * @param localGroup the local group to update
   * @param id the new ID
   */
  protected void setLocalGroupId(LocalGroup localGroup, long id) {
    try {
      Method changeId = LocalGroup.class.getDeclaredMethod("setId", long.class);
      changeId.setAccessible(true);
      changeId.invoke(localGroup, id);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IdUpdateFailedException("Error during invocation of reflection", e);
    }
  }

  /**
   * Updates the members of a local group's board
   *
   * @param board the board to update
   * @param members the board's members
   */
  protected void setBoardMembers(Board board, List<Person> members) {
    try {
      Method changeBoardMembers = Board.class.getDeclaredMethod("setMembers", List.class);
      changeBoardMembers.setAccessible(true);
      changeBoardMembers.invoke(board, members);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new IdUpdateFailedException("Error during invocation of reflection", e);
    }
  }
}
