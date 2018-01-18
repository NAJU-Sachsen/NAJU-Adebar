package de.naju.adebar.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import de.naju.adebar.infrastructure.config.security.WebSecurityConfiguration;
import de.naju.adebar.infrastructure.formatters.EventConverter;
import de.naju.adebar.infrastructure.formatters.PersonConverter;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import de.naju.adebar.model.persons.ReadOnlyPersonRepository;

/**
 * The general configuration of the web controllers.
 * 
 * We will use this to register our login page and multiple formatters:
 * <ul>
 * <li>a {@link PersonConverter}</li>
 * <li>a {@link EventConverter}</li>
 * </ul>
 * 
 * @author Rico Bergmann
 *
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

  private final ReadOnlyPersonRepository personRepo;
  private final ReadOnlyEventRepository eventRepo;

  public WebConfiguration(ReadOnlyPersonRepository personRepo, ReadOnlyEventRepository eventRepo) {
    Assert.notNull(personRepo, "Person repository may not be null");
    Assert.notNull(eventRepo, "Event repository may not be null");
    this.personRepo = personRepo;
    this.eventRepo = eventRepo;
  }

  /**
   * Registering new controllers, in this case the login view
   */
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(WebSecurityConfiguration.LOGIN_ROUTE).setViewName("login");
  }

  /**
   * Registering new converters
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new PersonConverter(personRepo));
    registry.addConverter(new EventConverter(eventRepo));
  }

}

