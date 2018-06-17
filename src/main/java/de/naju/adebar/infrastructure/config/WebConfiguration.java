package de.naju.adebar.infrastructure.config;

import de.naju.adebar.infrastructure.config.security.WebSecurityConfiguration;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.chapter.ReadOnlyProjectRepository;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import de.naju.adebar.model.persons.ReadOnlyPersonRepository;
import de.naju.adebar.services.conversion.chapter.LocalGroupConverter;
import de.naju.adebar.services.conversion.core.AgeConverter;
import de.naju.adebar.services.conversion.core.ArrivalOptionConverter;
import de.naju.adebar.services.conversion.core.CapacityConverter;
import de.naju.adebar.services.conversion.core.EmailConverter;
import de.naju.adebar.services.conversion.core.MoneyConverter;
import de.naju.adebar.services.conversion.core.NumericEntityIdConverter;
import de.naju.adebar.services.conversion.core.PhoneNumberConverter;
import de.naju.adebar.services.conversion.events.EventConverter;
import de.naju.adebar.services.conversion.events.ProjectConverter;
import de.naju.adebar.services.conversion.events.ReservationConverter;
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
  private final ReadOnlyProjectRepository projectRepo;

  public WebConfiguration(ReadOnlyPersonRepository personRepo, ReadOnlyEventRepository eventRepo,
      ReadOnlyLocalGroupRepository localGroupRepo, ReadOnlyProjectRepository projectRepo) {
    Assert2.noNullArguments("No parameter may be null", personRepo, eventRepo, localGroupRepo,
        projectRepo);
    this.personRepo = personRepo;
    this.eventRepo = eventRepo;
    this.localGroupRepo = localGroupRepo;
    this.projectRepo = projectRepo;
  }

  /**
   * Registering new converters
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new NumericEntityIdConverter());
    registry.addConverter(new PersonConverter(personRepo));
    registry.addConverter(new EventConverter(eventRepo));
    registry.addConverter(new LocalGroupConverter(localGroupRepo));
    registry.addConverter(new ProjectConverter(projectRepo));
    registry.addConverter(new ReservationConverter(eventRepo));
    registry.addConverter(new EmailConverter());
    registry.addConverter(new PhoneNumberConverter());
    registry.addConverter(new AgeConverter());
    registry.addConverter(new CapacityConverter());
    registry.addConverter(new MoneyConverter());
    registry.addConverter(new ArrivalOptionConverter());
  }

  /**
   * Registering new controllers, in this case the login view
   */
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController(WebSecurityConfiguration.LOGIN_ROUTE).setViewName("login");
  }

}

