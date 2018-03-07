package de.naju.adebar.web.validation.chapters;

import java.util.List;
import javax.validation.constraints.NotNull;
import de.naju.adebar.web.validation.core.AddressForm;

/**
 * Model POJO for local groups. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class LocalGroupForm extends AddressForm {

  @NotNull
  private String name;
  private String nabuGroup;
  private List<String> contactPersons;

  // constructor

  public LocalGroupForm(String name, String street, String zip, String city, String nabuGroup,
      List<String> contactPersons) {
    super(street, zip, city);
    this.name = name;
    this.nabuGroup = nabuGroup;
    this.contactPersons = contactPersons;
  }

  public LocalGroupForm() {
    this.name = "";
  }

  // getter and setter

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNabuGroup() {
    return nabuGroup;
  }

  public void setNabuGroup(String nabuGroup) {
    this.nabuGroup = nabuGroup;
  }

  public List<String> getContactPersons() {
    return contactPersons;
  }

  public void setContactPersons(List<String> contactPersons) {
    this.contactPersons = contactPersons;
  }

  // query methods

  public boolean hasNabuGroup() {
    return nabuGroup != null && !nabuGroup.isEmpty();
  }

  public boolean hasContactPersons() {
    return contactPersons != null;
  }
}
