package de.naju.adebar.infrastructure.config.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.stereotype.Component;

/**
 * {@link AccessDeniedHandler} to show the login page if the CSRF-token is invalid.
 *
 * @author Rico Bergmann
 * @see <a href= "https://stackoverflow.com/a/41633492/5161760">StackOverflow-Post</a>
 */
@Component
public class RedirectingAccessDeniedHandler implements AccessDeniedHandler {

  private static final Logger log = LoggerFactory.getLogger(RedirectingAccessDeniedHandler.class);

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

    log.info("Exception occurred for request to {} with response {} - exception is {}", request,
        response, accessDeniedException);

    if (!response.isCommitted() && accessDeniedException instanceof CsrfException) {
      request.logout();
      response.sendRedirect(WebSecurityConfiguration.LOGIN_ROUTE + "?expired");
    }

  }

}
