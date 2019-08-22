package de.naju.adebar.app.security.user;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.TestData;
import de.naju.adebar.model.persons.Person;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PersistentUserAccountManagerTest {

	@Autowired
	private PersistentUserAccountManager accountManager;

	@Autowired
	private UserAccountRepository accountRepo;

	private Person person;

	@Before
	public void setUp() {
		person = TestData.getPerson(TestData.PERSON_FRITZ);
	}

	@Test // #57
	public void testDeleteAccount() {
		String username = "fritz";
		final long currentNumberOfAccounts = accountRepo.count();

		UserAccount someAccount = accountManager.createFor(username, "123", person);

		assertThat(accountRepo.count()).isEqualTo(currentNumberOfAccounts + 1);
		assertThat(accountRepo.findAll()).contains(someAccount);

		accountManager.deleteAccount(Username.of(username));

		assertThat(accountRepo.count()).isEqualTo(currentNumberOfAccounts);
		assertThat(accountRepo.findAll()).doesNotContain(someAccount);
	}

}
