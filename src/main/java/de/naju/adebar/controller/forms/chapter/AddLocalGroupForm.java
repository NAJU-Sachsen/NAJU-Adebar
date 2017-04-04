package de.naju.adebar.controller.forms.chapter;

/**
 * Model POJO for new local groups to add. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class AddLocalGroupForm extends LocalGroupForm {
    private boolean createNewsletter;
    private String newsletterName;

    public AddLocalGroupForm() {}

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
