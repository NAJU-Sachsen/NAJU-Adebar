package de.naju.adebar.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import de.naju.adebar.app.security.user.UserAccount;
import de.naju.adebar.app.security.user.UserAccountManager;
import de.naju.adebar.app.security.user.UserAccountRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.web.validation.accounts.CreateAccountForm;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AccountController {

  private static final String REDIRECT_ACCOUNTS = "redirect:/accounts";

  private final PersonManager personManager;
  private final UserAccountManager accountManager;
  private final UserAccountRepository accountRepo;

  @Autowired
  public AccountController(PersonManager personManager, UserAccountManager accountManager,
      UserAccountRepository accountRepo) {
    Object[] params = {personManager, accountManager, accountRepo};
    Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
    this.personManager = personManager;
    this.accountManager = accountManager;
    this.accountRepo = accountRepo;
  }


  @RequestMapping("/accounts")
  public String showAccounts(Model model) {
    model.addAttribute("accounts", accountRepo.findAll());
    model.addAttribute("createAccountForm", new CreateAccountForm());
    return "accounts";
  }

  @RequestMapping("/accounts/create")
  public String createAccount(
      @ModelAttribute("createAccountForm") CreateAccountForm createAccountForm) {
    Person person = personManager.findPerson(createAccountForm.getPerson())
        .orElseThrow(IllegalArgumentException::new);

    List<SimpleGrantedAuthority> authorities = createAccountForm.getRoles().stream()
        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    accountManager.createFor(createAccountForm.getUsername(), createAccountForm.getPassword(),
        person, authorities, false);

    return REDIRECT_ACCOUNTS;
  }

  @RequestMapping("/accounts/update")
  public String editAuthorities(@RequestParam("account") String username,
      @RequestParam("roles") List<String> roles) {
    UserAccount account = accountManager.find(username).orElseThrow(IllegalArgumentException::new);

    List<SimpleGrantedAuthority> newAuthorities =
        roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    accountManager.updateAuthorities(account, newAuthorities);

    return REDIRECT_ACCOUNTS;
  }

  @RequestMapping("/accounts/reset-password")
  public String resetPassword(@RequestParam("account") String username,
      @RequestParam("password") String password) {
    accountManager.resetPassword(username, password, false);

    return REDIRECT_ACCOUNTS;
  }

  @RequestMapping("/accounts/delete")
  public String deleteAccount(@RequestParam("account") String username) {
    accountManager.deleteAccount(username);
    return REDIRECT_ACCOUNTS;
  }

}
