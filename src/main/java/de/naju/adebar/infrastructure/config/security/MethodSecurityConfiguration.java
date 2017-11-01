package de.naju.adebar.infrastructure.config.security;

import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Registering the method security (i.e. only users with specific roles may invoke certain methods)
 * 
 * @author Rico Bergmann
 *
 */
@Configuration
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

  @Resource
  private RoleHierarchy roleHierarchy;

  /**
   * Registering the role hierarchy
   */
  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    DefaultMethodSecurityExpressionHandler expressionHandler =
        (DefaultMethodSecurityExpressionHandler) super.createExpressionHandler();
    expressionHandler.setRoleHierarchy(roleHierarchy);
    return expressionHandler;
  }

}
