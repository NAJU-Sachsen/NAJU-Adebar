package de.naju.adebar.infrastructure.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Registering the method security (i.e. only users with specific roles may invoke certain methods)
 * <p>
 * The role hierarchy is applied automatically, as it is published as a bean in the
 * {@link WebSecurityConfiguration}.
 *
 * @author Rico Bergmann
 *
 */
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfiguration {

}
