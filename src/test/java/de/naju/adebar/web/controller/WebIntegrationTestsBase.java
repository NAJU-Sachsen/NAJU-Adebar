package de.naju.adebar.web.controller;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import de.naju.adebar.app.security.user.UserAccountManager;

/**
 * The base class for all web integration tests to ease their setup. This class will provide helper
 * methods and take care of setting up an appropriate security environment
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class WebIntegrationTestsBase {

  protected MockMvc mvc;

  @Autowired
  private UserAccountManager userAccountManager;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private FilterChainProxy securityFilterChain;

  @Before
  public void setUp() {
    context.getServletContext()
        .setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);

    mvc = MockMvcBuilders.webAppContextSetup(context) //
        .addFilter(securityFilterChain) //
        .build();
  }

  protected UserDetails admin() {
    return userAccountManager.find("hans").orElseThrow(IllegalStateException::new);
  }

  /**
   * Asserts that a GET-request to some URI redirects to the login page
   *
   * @param uri the URI to check
   * @throws Exception whatever may go wrong...
   */
  protected void ensureRequiresAuthentication(String uri) throws Exception {
    mvc.perform(get(uri)) //
        .andExpect(status().isFound()) //
        .andExpect(header().string("Location", endsWith("/login")));
  }

}
