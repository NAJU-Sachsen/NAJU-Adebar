package de.naju.adebar.infrastructure.config.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import de.naju.adebar.app.security.user.UserAccountService;

/**
 * Controller configuration specifically regarding security aspects
 *
 * @author Rico Bergmann
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

	public static final String LOGIN_ROUTE = "/login";
	public static final String LOGOUT_ROUTE = "/logout";

	/**
	 * Create the role hierarchy. It is picked up by the web security, the method security as well as
	 * the {@code sec:authorize} expressions within the templates.
	 */
	@Bean
	static RoleHierarchy roleHierarchy() {
		return RoleHierarchyImpl.fromHierarchy( //
				"ROLE_ADMIN > ROLE_JUBIREF\n" //
						+ "ROLE_JUBIREF > ROLE_EMPLOYEE\n" //
						+ "ROLE_EMPLOYEE > ROLE_FOEJ\n" //
						+ "ROLE_FOEJ > ROLE_CHAIRMAN\n" //
						+ "ROLE_CHAIRMAN > ROLE_TREASURER\n" //
						+ "ROLE_TREASURER > ROLE_BOARD_MEMBER\n" //
						+ "ROLE_BOARD_MEMBER > ROLE_USER");
	}

	/**
	 * Configure which paths should be protected
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, AccessDeniedHandler accessDenied)
			throws Exception {
		// @formatter:off
    http.authorizeHttpRequests(authorize -> authorize
        // error pages and forwards are rendered for requests which already passed the security checks
        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR)
          .permitAll()
        // resources should be available without login
        .requestMatchers("/webjars/**", "/resources/**", "/imprint", "/unsupported-browser", "/robots.txt")
          .permitAll()
        // only an admin may access actuator info
        .requestMatchers("/actuator/**")
          .hasRole("ADMIN")
        // for every other request, a login is necessary
        .anyRequest()
          .authenticated())
      // excluding the login page from necessity to login
      .formLogin(login -> login
        .loginPage(LOGIN_ROUTE).loginProcessingUrl(LOGIN_ROUTE).permitAll())
      .logout(logout -> logout
        .logoutUrl(LOGOUT_ROUTE).logoutSuccessUrl(LOGIN_ROUTE + "?logout").permitAll())
      .exceptionHandling(exceptions -> exceptions
        .accessDeniedHandler(accessDenied));
    // @formatter:on
		return http.build();
	}

	/**
	 * The password encoder. Together with the {@link UserAccountService} it is used to authenticate
	 * the users.
	 */
	@Bean(name = "PasswordEncoder")
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
