package de.naju.adebar.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.naju.adebar.infrastructure.thymeleaf.ExtendedDialect;

/**
 * Thymeleaf specific configuration
 * 
 * @author Rico Bergmann
 *
 */
@Configuration
public class ThymeleafConfiguration {

  /**
   * Our custom dialect
   * 
   * @return the dialect
   */
  @Bean
  public ExtendedDialect extendedDialect() {
    return new ExtendedDialect();
  }

}
