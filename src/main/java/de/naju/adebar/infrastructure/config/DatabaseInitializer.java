package de.naju.adebar.infrastructure.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import de.naju.adebar.app.security.user.Roles;
import de.naju.adebar.app.security.user.UserAccountManager;
import de.naju.adebar.app.security.user.UserAccountRepository;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonFactory;
import de.naju.adebar.model.persons.PersonRepository;

/**
 * Generates the admin account for the application if necessary.
 * <p>
 * The admin's data is specified in the {@code startup.properties} file.
 *
 * @author Rico Bergmann
 */
@PropertySource("classpath:startup.properties")
@Component
public class DatabaseInitializer {

	private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);

	private final Environment environment;
	private final PersonRepository personRepo;
	private final PersonFactory personFactory;
	private final UserAccountManager userAccountManager;
	private final UserAccountRepository userAccountRepo;

	@Autowired
	public DatabaseInitializer(Environment environment, PersonRepository personRepo,
			PersonFactory personFactory, UserAccountManager userAccountManager,
			UserAccountRepository userAccountRepo) {
		Object[] params = {environment, personRepo, personFactory, userAccountManager, userAccountRepo};
		Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
		this.environment = environment;
		this.personRepo = personRepo;
		this.personFactory = personFactory;
		this.userAccountManager = userAccountManager;
		this.userAccountRepo = userAccountRepo;
	}

	/**
	 * Inflates the database with the necessary data.
	 */
	@PostConstruct
	public void setup() {
		if (!isEmptyDatabase()) {
			return;
		}
		log.info("Database appears to be empty. Setting up admin account.");
		Person admin = loadPersonFromProperties();
		String username = loadUsernameFromProperties();
		String password = loadInitialPasswordFromProperties();
		personRepo.save(admin);
		userAccountManager.createFor(username, password, admin, Lists.newArrayList(Roles.ROLE_ADMIN),
				false);
	}

	/**
	 * Checks if the database contains an user with admin privileges.
	 */
	private boolean isEmptyDatabase() {
		return userAccountRepo.findByAuthoritiesContains(Roles.ROLE_ADMIN).isEmpty();
	}

	/**
	 * Generates the admin person according to the properties stated in {@code startup.properties}
	 */
	private Person loadPersonFromProperties() {
		String firstName = environment.getProperty("adebar.admin.first-name");
		String lastName = environment.getProperty("adebar.admin.last-name");
		String email = environment.getProperty("adebar.admin.email");
		return personFactory.buildNew(firstName, lastName, Email.of(email)).create();
	}

	/**
	 * Fetches the username of the admin from the properties.
	 */
	private String loadUsernameFromProperties() {
		return environment.getProperty("adebar.admin.username");
	}

	/**
	 * Fetches the admin's password from the properties.
	 */
	private String loadInitialPasswordFromProperties() {
		return environment.getProperty("adebar.admin.password");
	}

}
