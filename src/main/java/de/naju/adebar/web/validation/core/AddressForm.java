package de.naju.adebar.web.validation.core;

/**
 * Model POJO for addresses. For most use-cases a subclass should be used. The fields are set by
 * Thymeleaf when the associated form is submitted.
 *
 * @author Rico Bergmann
 */
public class AddressForm {

  private String street;
  private String zip;
  private String city;

  /**
   * Full constructor. None of the parameters may be {@code null}
   *
   * @param street the street
   * @param zip the zip
   * @param city the city
   */
  public AddressForm(String street, String zip, String city) {
    this.street = street;
    this.zip = zip;
    this.city = city;
  }

  /**
   * Default constructor
   */
  public AddressForm() {
  }

  /**
   * @return the street
   */
  public String getStreet() {
    return street;
  }

  /**
   * @param street the street. May not be {@code null}
   */
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * @param zip the zip. May not be {@code null}
   */
  public void setZip(String zip) {
    this.zip = zip;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city the city. May not be {@code null}
   */
  public void setCity(String city) {
    this.city = city;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AddressForm [street=" + street + ", zip=" + zip + ", city=" + city + "]";
  }

}
