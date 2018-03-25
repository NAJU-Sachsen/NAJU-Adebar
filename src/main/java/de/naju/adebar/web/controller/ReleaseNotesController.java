package de.naju.adebar.web.controller;

import javax.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import de.naju.adebar.app.news.ReleaseNotes;
import de.naju.adebar.app.news.ReleaseNotesManager;
import de.naju.adebar.web.validation.notifications.ReleaseNotesForm;

/**
 * Release notes related controller mappings
 * 
 * @author Rico Bergmann
 */
@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ReleaseNotesController {

  private static final String RELEASE_NOTES_TEMPLATE = "releaseNotes";
  private static final String RELEASE_NOTES_VIEW = "release-notes";

  private final ReleaseNotesManager releaseNotesManager;

  public ReleaseNotesController(ReleaseNotesManager releaseNotesManager) {
    Assert.notNull(releaseNotesManager, "Release notes manager may not be null");
    this.releaseNotesManager = releaseNotesManager;
  }

  /**
   * Displays the release notes view featuring the current as well as past notes
   * 
   * @param model the model to put the notes into
   * @return the overview template
   */
  @GetMapping("/release-notes")
  public String showReleaseNotes(Model model) {
    model.addAttribute("outdatedNotes", releaseNotesManager.findOutdated());
    model.addAttribute("currentNotes", releaseNotesManager.findLatest().orElse(null));
    model.addAttribute("newNotes", new ReleaseNotesForm());
    return RELEASE_NOTES_TEMPLATE;
  }

  /**
   * Save the most recent release notes
   * 
   * @param form the form containing the release notes data
   * @return a redirection to the release notes overview
   */
  @PostMapping("/release-notes")
  public String addReleaseNotes(@Valid @ModelAttribute("newNotes") ReleaseNotesForm form) {

    releaseNotesManager.specifyLatestNotes( //
        new ReleaseNotes(form.getTitle(), form.getDescription()));

    return "redirect:/" + RELEASE_NOTES_VIEW;
  }

  /**
   * Archives the current notes
   * 
   * @return a redirection to the release notes overview
   */
  @PostMapping("/release-notes/outdated")
  public String markNotesOutdated() {
    releaseNotesManager.markLatestNotesOutdated();
    return "redirect:/" + RELEASE_NOTES_VIEW;
  }

}
