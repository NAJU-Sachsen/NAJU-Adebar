package de.naju.adebar.web.controller;

import java.security.Principal;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.naju.adebar.app.security.user.UserAccountManager;

@Controller
public class SettingsController {
  private UserAccountManager accountManager;

  @Autowired
  public SettingsController(UserAccountManager accountManager) {
    Object[] params = {accountManager};
    Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
    this.accountManager = accountManager;
  }


  @RequestMapping("/settings")
  public String showSettings() {
    return "settings";
  }

  @RequestMapping("/settings/password/update")
  public String updatePassword(@RequestParam("current-password") String currentPassword,
      @RequestParam("new-password") String newPassword, Principal principal) {
    accountManager.updatePassword(principal.getName(), currentPassword, newPassword, false);
    return "redirect:/settings";
  }

}
