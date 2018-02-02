package de.naju.adebar.infrastructure.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Rico Bergmann
 * @see https://stackoverflow.com/a/41633492/5161760
 */
@Component
public class RedirectingAccessDeniedHandler implements AccessDeniedHandler {

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.
   * HttpServletRequest, javax.servlet.http.HttpServletResponse,
   * org.springframework.security.access.AccessDeniedException)
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    if (!response.isCommitted()) {
      request.logout();
      response.sendRedirect(WebSecurityConfiguration.LOGIN_ROUTE + "?expired");
    }

  }

}