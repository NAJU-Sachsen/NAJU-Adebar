package de.naju.adebar.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import de.naju.adebar.infrastructure.config.security.WebSecurityConfiguration;

/**
 * The general configuration of the web controllers.
 * 
 * We will use this to register our login page.
 * 
 * @author Rico Bergmann
 *
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

  /**
   * Registering new controllers, in this case the login view
   */
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(WebSecurityConfiguration.LOGIN_ROUTE).setViewName("login");
  }

}

