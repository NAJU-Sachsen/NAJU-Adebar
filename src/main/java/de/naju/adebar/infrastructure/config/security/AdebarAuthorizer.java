package de.naju.adebar.infrastructure.config.security;

import java.util.Collection;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

/**
 * The AdebarAuthorizer is responsible for deciding, whether a specific request should be executed.
 *
 * This mainly includes deciding, whether an user may access a certain site.
 *
 * @author Rico Bergmann
 *
 */
@Service
public class AdebarAuthorizer implements AccessDecisionVoter<FilterInvocation> {

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return clazz == FilterInvocation.class;
  }

  @Override
  public int vote(Authentication authentication, FilterInvocation object,
      Collection<ConfigAttribute> attributes) {
    return ACCESS_ABSTAIN;
  }



}
