package de.naju.adebar.web.validation.persons;

/**
 * Model POJO for new parents. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class CreateParentForm extends PersonForm {

  private boolean useChildAddress;

  public boolean isUseChildAddress() {
    return useChildAddress;
  }

  public void setUseChildAddress(boolean useChildAddress) {
    this.useChildAddress = useChildAddress;
  }

}
