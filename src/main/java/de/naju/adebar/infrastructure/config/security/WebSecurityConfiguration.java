package de.naju.adebar.infrastructure.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.util.Assert;
import com.google.common.collect.Lists;
import de.naju.adebar.app.security.user.UserAccountService;

/**
 * Controller configuration specifically regarding security aspects
 *
 * @author Rico Bergmann
 *
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  public static final String LOGIN_ROUTE = "/login";
  public static final String LOGOUT_ROUTE = "/logout";

  private final UserAccountService userAccountService;

  @Autowired
  public WebSecurityConfiguration(UserAccountService userAccountService) {
    Assert.notNull(userAccountService, "User account manager may not be null");
    this.userAccountService = userAccountService;
  }

  /**
   * Configure which paths should be protected
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http.authorizeRequests()
      // resources should be available without login
      .antMatchers("/webjars/**", "/resources/**", "/imprint")
        .permitAll()
      // for every other request, a login is necessary
      .anyRequest()
        .authenticated().accessDecisionManager(accessDecisionManager())
        .expressionHandler(webExpressionHandler())
      // excluding the login page from necessity to login
      .and().formLogin()
        .loginPage(LOGIN_ROUTE).loginProcessingUrl(LOGIN_ROUTE).permitAll().and().logout()
        .logoutUrl(LOGOUT_ROUTE).logoutSuccessUrl(LOGIN_ROUTE + "?logout").permitAll()
      .and()
        .exceptionHandling().accessDeniedHandler(accessDenied());
    // @formatter:on
  }

  /**
   * Configure how to receive the user account information for authentication
   */
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authProvider());
  }

  /**
   * Configure our access decision voters
   */
  @Bean
  protected AffirmativeBased accessDecisionManager() {
    AccessDecisionVoter<?>[] voters = {new AdebarAuthorizer(), new WebExpressionVoter(),
        new RoleVoter(), new AuthenticatedVoter()};
    return new AffirmativeBased(Lists.newArrayList(voters));
  }

  /**
   * Register the password encoder for the authentication process
   */
  @Bean
  protected DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userAccountService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /**
   * Create the role hierarchy
   */
  @Bean
  protected RoleHierarchyImpl roleHierarchy() {
    RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
    roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_JUBIREF\nROLE_JUBIREF > ROLE_EMPLOYEE\n"
        + "ROLE_EMPLOYEE > ROLE_FOEJ\nROLE_FOEJ > ROLE_CHAIRMAN\nROLE_CHAIRMAN > ROLE_TREASURER\n"
        + "ROLE_TREASURER > ROLE_BOARD_MEMBER\nROLE_BOARD_MEMBER > ROLE_USER");
    return roleHierarchy;
  }

  /**
   * The password encoder
   */
  @Bean(name = "PasswordEncoder")
  protected PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected AccessDeniedHandler accessDenied() {
    return new RedirectingAccessDeniedHandler();
  }

  /**
   * Register the role hierarchy
   */
  private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
    DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler =
        new DefaultWebSecurityExpressionHandler();
    defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
    return defaultWebSecurityExpressionHandler;
  }

}

