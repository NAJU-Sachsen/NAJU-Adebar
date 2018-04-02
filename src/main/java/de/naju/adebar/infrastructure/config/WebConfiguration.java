package de.naju.adebar.infrastructure.config;

import de.naju.adebar.infrastructure.config.security.WebSecurityConfiguration;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import de.naju.adebar.model.persons.ReadOnlyPersonRepository;
import de.naju.adebar.services.conversion.chapter.LocalGroupConverter;
import de.naju.adebar.services.conversion.core.AgeConverter;
import de.naju.adebar.services.conversion.core.EmailConverter;
import de.naju.adebar.services.conversion.core.PhoneNumberConverter;
import de.naju.adebar.services.conversion.events.EventConverter;
import de.naju.adebar.services.conversion.persons.PersonConverter;
import de.naju.adebar.util.Assert2;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  private final ReadOnlyPersonRepository personRepo;
  private final ReadOnlyEventRepository eventRepo;
  private final ReadOnlyLocalGroupRepository localGroupRepo;

  public WebConfiguration(ReadOnlyPersonRepository personRepo, ReadOnlyEventRepository eventRepo,
      ReadOnlyLocalGroupRepository localGroupRepo) {
    Assert2.noNullArguments("No parameter may be null", personRepo, eventRepo, localGroupRepo);
    this.personRepo = personRepo;
    this.eventRepo = eventRepo;
    this.localGroupRepo = localGroupRepo;
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
    registry.addConverter(new LocalGroupConverter(localGroupRepo));
    registry.addConverter(new EmailConverter());
    registry.addConverter(new PhoneNumberConverter());
    registry.addConverter(new AgeConverter());
  }

}

