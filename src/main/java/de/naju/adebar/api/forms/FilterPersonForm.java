package de.naju.adebar.api.forms;

/**
 * POJO for the criteria for a person filter used by the API. As this may and in the future very
 * likely also will differ from the filters used by the UI, a separate POJO is used.
 * 
 * @author Rico Bergmann
 */
public class FilterPersonForm {
  private String firstName;
  private String lastName;
  private String city;
  private Boolean activist;
  private Boolean referent;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Boolean isActivist() {
    return activist;
  }

  public void setActivist(boolean activist) {
    this.activist = activist;
  }

  public Boolean isReferent() {
    return referent;
  }

  public void setReferent(boolean referent) {
    this.referent = referent;
  }

  public boolean hasActivistInfo() {
    return activist != null;
  }

  public boolean hasReferentInfo() {
    return referent != null;
  }
}
