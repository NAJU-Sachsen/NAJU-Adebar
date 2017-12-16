package de.naju.adebar.controller;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.human.DataProcessor;
import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.controller.forms.chapter.AddLocalGroupForm;
import de.naju.adebar.controller.forms.chapter.BoardForm;
import de.naju.adebar.controller.forms.chapter.LocalGroupForm;
import de.naju.adebar.controller.forms.chapter.ProjectForm;
import de.naju.adebar.controller.forms.events.EventForm;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.chapter.ExistingMemberException;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonId;
import de.naju.adebar.services.conversion.chapter.AddLocalGroupFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.BoardFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.BoardToBoardFormConverter;
import de.naju.adebar.services.conversion.chapter.LocalGroupFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.LocalGroupToLocalGroupFormConverter;

/**
 * Local group related controller mappings
 *
 * @author Rico Bergmann
 * @see LocalGroup
 */
@Controller
public class LocalGroupController {
  private final static String EMAIL_DELIMITER = ";";

  private LocalGroupManager localGroupManager;
  private PersonManager personManager;
  private LocalGroupFormDataExtractor localGroupFormDataExtractor;
  private AddLocalGroupFormDataExtractor addLocalGroupFormDataExtractor;
  private LocalGroupToLocalGroupFormConverter localGroupFormConverter;
  private BoardFormDataExtractor boardFormDataExtractor;
  private BoardToBoardFormConverter boardFormConverter;
  private DataProcessor humanDataProcessor;

  @Autowired
  public LocalGroupController(LocalGroupManager localGroupManager, PersonManager personManager,
      AddLocalGroupFormDataExtractor addLocalGroupFormDataExtractor,
      LocalGroupFormDataExtractor localGroupFormDataExtractor,
      LocalGroupToLocalGroupFormConverter localGroupFormConverter,
      BoardFormDataExtractor boardFormDataExtractor, BoardToBoardFormConverter boardFormConverter,
      DataProcessor humanDataProcessor) {
    Object[] params = {localGroupManager, personManager, localGroupFormDataExtractor,
        addLocalGroupFormDataExtractor, localGroupFormConverter, boardFormDataExtractor,
        boardFormConverter, humanDataProcessor};
    Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
    this.localGroupManager = localGroupManager;
    this.personManager = personManager;
    this.localGroupFormDataExtractor = localGroupFormDataExtractor;
    this.addLocalGroupFormDataExtractor = addLocalGroupFormDataExtractor;
    this.localGroupFormConverter = localGroupFormConverter;
    this.boardFormDataExtractor = boardFormDataExtractor;
    this.boardFormConverter = boardFormConverter;
    this.humanDataProcessor = humanDataProcessor;
  }

  /**
   * Displays an overview of all local groups
   *
   * @param model model from which the displayed data should be taken
   * @return the local groups' overview view
   */
  @RequestMapping("/localGroups")
  public String showLocalGroupsOverview(Model model) {
    model.addAttribute("localGroups", localGroupManager.repository().findAll());
    model.addAttribute("localGroupForm", new AddLocalGroupForm());
    return "localGroups";
  }

  /**
   * Adds a new local group to the database
   *
   * @param addLocalGroupForm the submitted local group data
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/add")
  public String addLocalGroup(@ModelAttribute("localGroupForm") AddLocalGroupForm addLocalGroupForm,
      RedirectAttributes redirAttr) {
    LocalGroup localGroup = localGroupManager
        .saveLocalGroup(addLocalGroupFormDataExtractor.extractLocalGroup(addLocalGroupForm));

    redirAttr.addFlashAttribute("localGroupCreated", true);
    return "redirect:/localGroups/" + localGroup.getId();
  }

  /**
   * Detail view for a local group
   *
   * @param groupId the id of the group to display
   * @param model model from which the displayed data should be taken
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/{gid}")
  public String showLocalGroupDetails(@PathVariable("gid") long groupId, Model model) {
    LocalGroup localGroup =
        localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
    Board board = localGroup.getBoard();
    Iterable<Person> members = localGroup.getMembers();
    Iterable<Person> contactPersons = localGroup.getContactPersons();
    Iterable<Person> boardMembers = board != null ? board.getMembers() : null;

    model.addAttribute("localGroup", localGroup);
    model.addAttribute("members", members);
    model.addAttribute("contactPersons", contactPersons);
    model.addAttribute("chairman", board != null ? board.getChairman() : null);
    model.addAttribute("board", boardMembers);

    model.addAttribute("memberEmails",
        humanDataProcessor.extractEmailAddressesAsString(members, EMAIL_DELIMITER));
    model.addAttribute("boardEmails",
        board != null
            ? humanDataProcessor.extractEmailAddressesAsString(boardMembers, EMAIL_DELIMITER)
            : "");
    model.addAttribute("contactPersonsEmails",
        humanDataProcessor.extractEmailAddressesAsString(contactPersons, EMAIL_DELIMITER));

    model.addAttribute("localGroupForm",
        localGroupFormConverter.convertToLocalGroupForm(localGroup));
    model.addAttribute("boardForm", boardFormConverter.convertToBoardForm(board));
    model.addAttribute("projectForm", new ProjectForm());
    model.addAttribute("addEventForm", new EventForm());

    return "localGroupDetails";
  }

  /**
   * Updates information about a local group
   *
   * @param groupId the id of the group to update
   * @param localGroupForm the submitted new local group data
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/{gid}/edit")
  public String editLocalGroup(@PathVariable("gid") long groupId,
      @ModelAttribute("localGroupForm") LocalGroupForm localGroupForm,
      RedirectAttributes redirAttr) {
    Assert.isTrue(localGroupManager.findLocalGroup(groupId).isPresent(),
        "There is no local group with ID " + groupId);

    localGroupManager.adoptLocalGroupData(groupId,
        localGroupFormDataExtractor.extractLocalGroup(localGroupForm));

    redirAttr.addFlashAttribute("localGroupUpdated", true);
    return "redirect:/localGroups/" + groupId;
  }

  /**
   * Adds a member to a local group
   *
   * @param groupId the id of the local group to add the member to
   * @param personId the id of the person to add as member
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/{gid}/members/add")
  public String addMember(@PathVariable("gid") long groupId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    LocalGroup localGroup =
        localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      localGroup.addMember(activist);
      localGroupManager.updateLocalGroup(groupId, localGroup);
      redirAttr.addFlashAttribute("memberAdded", true);
    } catch (ExistingMemberException e) {
      redirAttr.addFlashAttribute("existingMember", true);
    }

    return "redirect:/localGroups/" + groupId;
  }

  /**
   * Removes a member from a local group
   *
   * @param groupId the chapter to remove the member from
   * @param memberId the id of the person to remove
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/{gid}/members/remove")
  public String removeMember(@PathVariable("gid") long groupId,
      @RequestParam("member-id") String memberId, RedirectAttributes redirAttr) {
    LocalGroup localGroup =
        localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
    Person activist = personManager.findPerson(memberId).orElseThrow(IllegalArgumentException::new);

    localGroup.removeMember(activist);
    localGroupManager.updateLocalGroup(groupId, localGroup);

    redirAttr.addFlashAttribute("memberRemoved", true);
    return "redirect:/localGroups/" + groupId;
  }

  /**
   * Adds a non-activist as member to the local group. This will make the person an activist.
   *
   * @param groupId the chapter to which the person should be added
   * @param personId the id of the person who should be added as counselor
   * @param redirAttr attributes for the view to display some result information
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/{gid}/members/add-new")
  public String addNewMember(@PathVariable("gid") long groupId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    LocalGroup localGroup =
        localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
    Person person = personManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      person.makeActivist();
      localGroup.addMember(person);;
      personManager.updatePerson(new PersonId(personId), person);
      localGroupManager.updateLocalGroup(groupId, localGroup);
      redirAttr.addFlashAttribute("counselorAdded", true);
    } catch (IllegalStateException e) {
      // the person is already an activist
      redirAttr.addFlashAttribute("existingActivist", true);

    } catch (ExistingMemberException e) {
      // the person is already an organizer
      redirAttr.addFlashAttribute("existingMember", true);
    }

    return "redirect:/localGroups/" + groupId;
  }

  /**
   * Updates the local group's board
   *
   * @param groupId the group whose board is to be updated
   * @param boardForm the submitted data describing the board
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the local group's detail view
   */
  @RequestMapping("/localGroups/{gid}/board")
  public String updateBoard(@PathVariable("gid") long groupId,
      @ModelAttribute("boardForm") BoardForm boardForm, RedirectAttributes redirAttr) {
    Assert.isTrue(localGroupManager.findLocalGroup(groupId).isPresent(),
        "There is no local group with ID " + groupId);


    localGroupManager.updateBoard(groupId, boardFormDataExtractor.extractBoard(boardForm));

    redirAttr.addFlashAttribute("boardUpdated", true);
    return "redirect:/localGroups/" + groupId;
  }

}
