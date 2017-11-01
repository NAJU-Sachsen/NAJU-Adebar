package de.naju.adebar.controller.forms.chapter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Model POJO for local groups. The fields are set by Thymeleaf when the associated form is
 * submitted.
 * 
 * @author Rico Bergmann
 */
public class LocalGroupForm {
  @NotNull
  private String name;
  private String street;
  private String zip;
  private String city;
  private String nabuGroup;
  private List<String> contactPersons;

  // constructor

  public LocalGroupForm(String name, String street, String zip, String city, String nabuGroup,
      List<String> contactPersons) {
    this.name = name;
    this.street = street;
    this.zip = zip;
    this.city = city;
    this.nabuGroup = nabuGroup;
    this.contactPersons = contactPersons;
  }

  public LocalGroupForm() {}

  // getter and setter

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
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
