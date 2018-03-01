package de.naju.adebar.web.controller.api;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.naju.adebar.app.news.ReleaseNotes;
import de.naju.adebar.app.security.user.UserAccount;
import de.naju.adebar.app.security.user.UserAccountManager;
import de.naju.adebar.model.persons.PersonId;

/**
 * REST-mappings for the {@link ReleaseNotes}
 * 
 * @author Rico Bergmann
 */
@RestController("api_releaseNotesController")
public class ReleaseNotesController {

  private final UserAccountManager userAccountManager;

  public ReleaseNotesController(UserAccountManager userAccountManager) {
    Assert.notNull(userAccountManager, "User account manager may not be null");
    this.userAccountManager = userAccountManager;
  }

  /**
   * Marks an {@link UserAccount} to have read the current release notes
   * 
   * @param userId the ID of the user
   */
  @PostMapping("/release-notes/read")
  public void readReleaseNotes(@RequestParam("user") String userId) {
    userAccountManager.find(new PersonId(userId)).ifPresent(userAccountManager::readReleaseNotes);
  }

}
