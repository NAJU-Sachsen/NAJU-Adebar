/**
 *
 */
package de.naju.adebar.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import de.naju.adebar.TestData;
import de.naju.adebar.app.security.user.Roles;
import de.naju.adebar.app.security.user.UserAccount;
import de.naju.adebar.app.security.user.UserAccountManager;

@Transactional
public class AccountControllerWebIntegrationTests extends WebIntegrationTestsBase {

	private static final String TEST_USERNAME = "fritz";
	private static final SimpleGrantedAuthority TEST_AUTHORITY =
			new SimpleGrantedAuthority("ROLE_TEST");

	@Autowired
	private UserAccountManager accountManager;

	private UserAccount accountUnderTest;

	@Before
	public void setup() {
		accountUnderTest = accountManager.createFor(TEST_USERNAME, "123",
				TestData.getPerson(TestData.PERSON_FRITZ), Arrays.asList(TEST_AUTHORITY), false);
	}

	@Test // #58
	public void removingAllExtraAuthoritiesSetsDefaultAuthority() throws Exception {
		assertThat(accountUnderTest.getAuthorities()).doesNotContain(Roles.ROLE_USER);
		assertThat(accountUnderTest.getAuthorities()).contains(TEST_AUTHORITY);

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add("account", TEST_USERNAME);

		mvc.perform(post("/accounts/update") //
				.params(formData) //
				.with(user(admin())) //
				.with(csrf())) //
				.andExpect(redirectedUrl("/accounts"));

		assertThat(accountUnderTest.getAuthorities()).contains(Roles.ROLE_USER);
		assertThat(accountUnderTest.getAuthorities()).doesNotContain(TEST_AUTHORITY);
	}

}
