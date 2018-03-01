package de.naju.adebar.web.validation.accounts;

import java.util.List;

/**
 * Model POJO for new accounts. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 *
 */
public class CreateAccountForm {

  private String person;
  private String username;
  private String password;
  private List<String> roles;

  public String getPerson() {
    return person;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setPerson(String person) {
    this.person = person;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

}
