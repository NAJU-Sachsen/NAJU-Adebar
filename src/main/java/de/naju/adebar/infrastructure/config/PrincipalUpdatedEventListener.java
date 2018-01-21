package de.naju.adebar.infrastructure.config;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.security.user.UserAccount;
import de.naju.adebar.app.security.user.UserAccountUpdatedEvent;

/**
 * Component which updates the application's current principal every time its user account is
 * updated.
 * 
 * @author Rico Bergmann
 */
@Component
public class PrincipalUpdatedEventListener {

  /**
   * Checks for the updated user account and passes its changes to the principal if the account
   * belongs to it.
   * 
   * @param event the update event
   */
  @EventListener
  void updatePrincipalIfNecessary(UserAccountUpdatedEvent event) {
    if (isPrincipal(event.getEntity())) {
      UserAccount updated = event.getEntity();
      Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(updated,
          updated.getPassword(), updated.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
    }
  }

  /**
   * @param account the account to check
   * @return whether the account belongs to the current principal
   */
  private boolean isPrincipal(UserAccount account) {
    if (!hasSecurityContext() || !contextHasPrincipal()) {
      return false;
    }
    return SecurityContextHolder.getContext().getAuthentication().getName()
        .equals(account.getUsername());
  }

  /**
   * @return whether a security context was already set up
   */
  private boolean hasSecurityContext() {
    return SecurityContextHolder.getContext() != null;
  }

  /**
   * @return whether the security context exists and has a principal attached to it
   */
  private boolean contextHasPrincipal() {
    return hasSecurityContext() && SecurityContextHolder.getContext().getAuthentication() != null;
  }

}
