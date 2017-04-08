package de.naju.adebar.controller;

import de.naju.adebar.app.human.DataProcessor;
import de.naju.adebar.controller.forms.chapter.AddLocalGroupForm;
import de.naju.adebar.controller.forms.chapter.BoardForm;
import de.naju.adebar.controller.forms.chapter.LocalGroupForm;
import de.naju.adebar.controller.forms.chapter.ProjectForm;
import de.naju.adebar.controller.forms.events.EventForm;
import de.naju.adebar.model.chapter.Board;
import de.naju.adebar.model.chapter.ExistingMemberException;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.LocalGroupManager;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.HumanManager;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.newsletter.NewsletterManager;
import de.naju.adebar.util.conversion.PersonConverter;
import de.naju.adebar.util.conversion.chapter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

/**
 * Local group related controller mappings
 * @author Rico Bergmann
 * @see LocalGroup
 */
@Controller
public class LocalGroupController {
    private final static String EMAIL_DELIMITER = ";";

    private LocalGroupManager localGroupManager;
    private HumanManager humanManager;
    private NewsletterManager newsletterManager;
    private PersonConverter personConverter;
    private LocalGroupFormDataExtractor localGroupFormDataExtractor;
    private AddLocalGroupFormDataExtractor addLocalGroupFormDataExtractor;
    private LocalGroupToLocalGroupFormConverter localGroupFormConverter;
    private BoardFormDataExtractor boardFormDataExtractor;
    private BoardToBoardFormConverter boardFormConverter;
    private DataProcessor humanDataProcessor;

    @Autowired
    public LocalGroupController(LocalGroupManager localGroupManager, HumanManager humanManager, NewsletterManager newsletterManager, PersonConverter personConverter, AddLocalGroupFormDataExtractor addLocalGroupFormDataExtractor, LocalGroupFormDataExtractor localGroupFormDataExtractor, LocalGroupToLocalGroupFormConverter localGroupFormConverter, BoardFormDataExtractor boardFormDataExtractor, BoardToBoardFormConverter boardFormConverter, DataProcessor humanDataProcessor) {
        Object[] params = {localGroupManager, humanManager, newsletterManager, personConverter, localGroupFormDataExtractor, addLocalGroupFormDataExtractor, localGroupFormConverter, boardFormDataExtractor, boardFormConverter, humanDataProcessor};
        Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
        this.localGroupManager = localGroupManager;
        this.humanManager = humanManager;
        this.newsletterManager = newsletterManager;
        this.personConverter = personConverter;
        this.localGroupFormDataExtractor = localGroupFormDataExtractor;
        this.addLocalGroupFormDataExtractor = addLocalGroupFormDataExtractor;
        this.localGroupFormConverter = localGroupFormConverter;
        this.boardFormDataExtractor = boardFormDataExtractor;
        this.boardFormConverter = boardFormConverter;
        this.humanDataProcessor = humanDataProcessor;
    }

    /**
     * Displays an overview of all local groups
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
     * @param addLocalGroupForm the submitted local group data
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the local group's detail view
     */
    @RequestMapping("/localGroups/add")
    public String addLocalGroup(@ModelAttribute("localGroupForm") AddLocalGroupForm addLocalGroupForm, RedirectAttributes redirAttr) {
        LocalGroup localGroup = localGroupManager.saveLocalGroup(addLocalGroupFormDataExtractor.extractLocalGroup(addLocalGroupForm));

        redirAttr.addFlashAttribute("localGroupCreated", true);
        return "redirect:/localGroups/" + localGroup.getId();
    }

    /**
     * Detail view for a local group
     * @param groupId the id of the group to display
     * @param model model from which the displayed data should be taken
     * @return the local group's detail view
     */
    @RequestMapping("/localGroups/{gid}")
    public String showLocalGroupDetails(@PathVariable("gid") long groupId, Model model) {
        LocalGroup localGroup = localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
        Board board = localGroup.getBoard();
        Iterable<Person> members = personConverter.convertActivists(localGroup.getMembers());
        Iterable<Person> boardMembers = board != null ? personConverter.convertActivists(board.getMembers()) : null;

        model.addAttribute("localGroup", localGroup);
        model.addAttribute("members", members);
        model.addAttribute("chairman", board != null ? personConverter.convertActivist(board.getChairman()) : null);
        model.addAttribute("board", boardMembers);

        model.addAttribute("memberEmails", humanDataProcessor.extractEmailAddressesAsString(members, EMAIL_DELIMITER));
        model.addAttribute("boardEmails", board != null ? humanDataProcessor.extractEmailAddressesAsString(boardMembers, EMAIL_DELIMITER) : "");

        model.addAttribute("localGroupForm", localGroupFormConverter.convertToLocalGroupForm(localGroup));
        model.addAttribute("boardForm", boardFormConverter.convertToBoardForm(board));
        model.addAttribute("projectForm", new ProjectForm());
        model.addAttribute("addEventForm", new EventForm());

        return "localGroupDetails";
    }

    /**
     * Updates information about a local group
     * @param groupId the id of the group to update
     * @param localGroupForm the submitted new local group data
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the local group's detail view
     */
    @RequestMapping("/localGroups/{gid}/edit")
    public String editLocalGroup(@PathVariable("gid") long groupId, @ModelAttribute("localGroupForm") LocalGroupForm localGroupForm, RedirectAttributes redirAttr) {
        LocalGroup localGroup = localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);

        localGroupManager.adoptLocalGroupData(groupId, localGroupFormDataExtractor.extractLocalGroup(localGroupForm));

        redirAttr.addFlashAttribute("localGroupUpdated", true);
        return "redirect:/localGroups/" + groupId;
    }

    /**
     * Adds a member to a local group
     * @param groupId the id of the local group to add the member to
     * @param personId the id of the person to add as member
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the local group's detail view
     */
    @RequestMapping("/localGroups/{gid}/members/add")
    public String addMember(@PathVariable("gid") long groupId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        LocalGroup localGroup = localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new));

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
     * @param groupId the chapter to remove the member from
     * @param memberId the id of the person to remove
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the local group's detail view
     */
    @RequestMapping("/localGroups/{gid}/members/remove")
    public String removeMember(@PathVariable("gid") long groupId, @RequestParam("member-id") String memberId, RedirectAttributes redirAttr) {
        LocalGroup localGroup = localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
        Activist activist = humanManager.findActivist(humanManager.findPerson(memberId).orElseThrow(IllegalArgumentException::new));

        localGroup.removeMember(activist);
        localGroupManager.updateLocalGroup(groupId, localGroup);

        redirAttr.addFlashAttribute("memberRemoved", true);
        return "redirect:/localGroups/" + groupId;
    }

    /**
     * Updates the local group's board
     * @param groupId the group whose board is to be updated
     * @param boardForm the submitted data describing the board
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the local group's detail view
     */
    @RequestMapping("/localGroups/{gid}/board")
    public String updateBoard(@PathVariable("gid") long groupId, @ModelAttribute("boardForm") BoardForm boardForm, RedirectAttributes redirAttr) {
        LocalGroup localGroup = localGroupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);

        localGroup.setBoard(boardFormDataExtractor.extractBoard(boardForm));
        localGroupManager.updateLocalGroup(groupId, localGroup);

        redirAttr.addFlashAttribute("boardUpdated", true);
        return "redirect:/localGroups/" + groupId;
    }

}
