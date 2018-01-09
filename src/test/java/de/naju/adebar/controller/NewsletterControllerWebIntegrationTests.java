package de.naju.adebar.controller;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.collect.Lists;
import de.naju.adebar.app.newsletter.NewsletterManager;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.NewsletterRepository;

@Transactional
public class NewsletterControllerWebIntegrationTests extends WebIntegrationTestsBase {

  @Autowired
  private NewsletterManager newsletterManager;

  @Autowired
  private NewsletterRepository newsletterRepository;

  @Before
  public void setup() {
    initializeNewsletters();
  }

  @Test
  public void requiresAuthorizationToAccess() throws Exception {
    ensureRequiresAuthentication("/newsletters");
  }

  @Test
  public void showNewslettersContainsAllNewsletters() throws Exception {
    Newsletter[] allNewsletters =
        Lists.newArrayList(newsletterRepository.findAll()).toArray(new Newsletter[] {});

    mvc.perform(get("/newsletters") //
        .with(user(admin()))) //
        .andExpect(status().isOk()) //
        .andExpect(model().attribute("newsletters", hasItems(allNewsletters)));
  }

  private void initializeNewsletters() {
    for (int i = 1; i <= 100; ++i) {
      newsletterManager.createNewsletter("Newsletter " + i);
    }
  }

}
