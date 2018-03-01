package de.naju.adebar.web.validation.chapters;

import javax.validation.constraints.NotNull;

/**
 * Model POJO for new local groups to add. The fields are set by Thymeleaf when the associated form
 * is submitted.
 *
 * @author Rico Bergmann
 */
public class AddLocalGroupForm extends LocalGroupForm {

  private boolean createNewsletter;

  @NotNull
  private String newsletterName;

  public AddLocalGroupForm() {
    newsletterName = "";
  }

  public boolean isCreateNewsletter() {
    return createNewsletter;
  }

  public void setCreateNewsletter(boolean createNewsletter) {
    this.createNewsletter = createNewsletter;
  }

  public String getNewsletterName() {
    return newsletterName;
  }

  public void setNewsletterName(String newsletterName) {
    this.newsletterName = newsletterName;
  }
}
