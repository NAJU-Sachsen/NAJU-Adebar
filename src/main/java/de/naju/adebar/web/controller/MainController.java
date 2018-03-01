package de.naju.adebar.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.app.news.ReleaseNotesManager;

/**
 * Main controller of the application featuring mappings for the homepage and other off-topic views.
 * 
 * @author Rico Bergmann
 */
@Controller
public class MainController {

  private final ReleaseNotesManager releaseNotesManager;

  public MainController(ReleaseNotesManager releaseNotesManager) {
    Assert.notNull(releaseNotesManager, "Release notes manager may not be null");
    this.releaseNotesManager = releaseNotesManager;
  }

  /**
   * Currently redirects to the events overview-page
   * 
   * @param redirAttr attributes for the events overview model
   * @return the redirection string
   */
  @GetMapping({"/", "/index", "/overview"})
  public String showOverview(RedirectAttributes redirAttr) {
    redirAttr.addFlashAttribute("releaseNotes", releaseNotesManager.findLatest().orElse(null));
    return "redirect:/events";
  }

  /**
   * Displays the site's imprint (German law thing...)
   * 
   * @return the imprint template
   */
  @GetMapping("/imprint")
  public String showImprint() {
    return "imprint";
  }

}
