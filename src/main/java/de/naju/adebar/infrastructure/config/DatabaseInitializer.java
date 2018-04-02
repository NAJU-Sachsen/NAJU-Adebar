package de.naju.adebar.infrastructure.config;

import com.google.common.collect.Lists;
import de.naju.adebar.app.security.user.Roles;
import de.naju.adebar.app.security.user.UserAccountManager;
import de.naju.adebar.app.security.user.UserAccountRepository;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonManager;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@PropertySource("classpath:startup.properties")
@Component
public class DatabaseInitializer {

  private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

  private Environment environment;
  private PersonManager personManager;
  private PersonFactory personFactory;
  private UserAccountManager userAccountManager;
  private UserAccountRepository userAccountRepo;

  @Autowired
  public DatabaseInitializer(Environment environment, PersonManager personManager,
      PersonFactory personFactory, UserAccountManager userAccountManager,
      UserAccountRepository userAccountRepo) {
    Object[] params =
        {environment, personManager, personFactory, userAccountManager, userAccountRepo};
    Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
    this.environment = environment;
    this.personManager = personManager;
    this.personFactory = personFactory;
    this.userAccountManager = userAccountManager;
    this.userAccountRepo = userAccountRepo;
  }

  @PostConstruct
  public void setup() {
    if (!isEmptyDatabase()) {
      return;
    }
    log.info("Database appears to be empty. Setting up admin account.");
    Person admin = loadPersonFromProperties();
    String username = loadUsernameFromProperties();
    String password = loadInitialPasswordFromProperties();
    personManager.savePerson(admin);
    userAccountManager.createFor(username, password, admin, Lists.newArrayList(Roles.ROLE_ADMIN),
        false);
  }

  private boolean isEmptyDatabase() {
    return userAccountRepo.findByAuthoritiesContains(Roles.ROLE_ADMIN).isEmpty();
  }

  private Person loadPersonFromProperties() {
    String firstName = environment.getProperty("adebar.admin.first-name");
    String lastName = environment.getProperty("adebar.admin.last-name");
    String email = environment.getProperty("adebar.admin.email");
    return personFactory.buildNew(firstName, lastName, Email.of(email)).create();
  }

  private String loadUsernameFromProperties() {
    return environment.getProperty("adebar.admin.username");
  }

  private String loadInitialPasswordFromProperties() {
    return environment.getProperty("adebar.admin.password");
  }

}
