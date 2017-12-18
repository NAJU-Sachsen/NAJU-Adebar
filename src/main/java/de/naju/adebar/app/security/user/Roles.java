package de.naju.adebar.app.security.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * A set of predefined roles used within the application. Roles and authorities are used
 * interchangeably within the security architecture.
 *
 * @author Rico Bergmann
 *
 */
public class Roles {

  private Roles() {}

  /**
   * The admin. May do everything.
   */
  public static final SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");

  /**
   * The Jugendbildungsreferent (referent for youth education) is a special kind of employee who may
   * also access the extended certificates of good conduct
   */
  public static final SimpleGrantedAuthority ROLE_JUBIREF =
      new SimpleGrantedAuthority("ROLE_JUBIREF");

  /**
   * Employees may access and create all data of the application
   */
  public static final SimpleGrantedAuthority ROLE_EMPLOYEE =
      new SimpleGrantedAuthority("ROLE_EMPLOYEE");

  /**
   * Volunteers of the FÖJ (Voluntary ecological year) may access and create all the data related to
   * their local group as well as access (read-only) all personal data of the application
   */
  public static final SimpleGrantedAuthority ROLE_FOEJ = new SimpleGrantedAuthority("ROLE_FOEJ");

  /**
   * A chairman may access all the data related to his local group, as well as edit it
   */
  public static final SimpleGrantedAuthority ROLE_CHAIRMAN =
      new SimpleGrantedAuthority("ROLE_CHAIRMAN");

  /**
   * A treasurer may update the participation info of all events which belong to his local group
   */
  public static final SimpleGrantedAuthority ROLE_TREASURER =
      new SimpleGrantedAuthority("ROLE_TREASURER");

  /**
   * A board member may access all the data related to his local group and edit it, excluding the
   * board itself
   */
  public static final SimpleGrantedAuthority ROLE_BOARD_MEMBER =
      new SimpleGrantedAuthority("ROLE_BOARD_MEMBER");

  /**
   * A simple user
   */
  public static final SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");

}
