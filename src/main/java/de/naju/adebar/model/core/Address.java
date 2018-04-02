package de.naju.adebar.model.core;

import de.naju.adebar.documentation.ddd.ValueObject;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

/**
 * Abstraction of an address. Each address consists of street, zip and city and may contain
 * additional info - e.g. room numbers or the like.
 * <p>
 * Mind that an address is a value-object - once created it may not be modified any more.
 *
 * @author Rico Bergmann
 */
@ValueObject
@Embeddable
public class Address {

  public static final int ZIP_LENGTH = 5;

  @Column(name = "street")
  private String street;

  @Column(name = "zip")
  private String zip;

  @Column(name = "city")
  private String city;

  @Column(name = "hints")
  private String additionalInfo;

  /**
   * Simplified constructor
   *
   * @param street the street, may not be empty
   * @param zip the zip, must be 5 characters long
   * @param city the city, may not be empty
   * @throws IllegalArgumentException if any of the parameters is {@code null}
   */
  public Address(String street, String zip, String city) {
    this(street, zip, city, "");
  }

  /**
   * Full constructor
   *
   * @param street the street, may not be empty
   * @param zip the zip, must be 5 characters long
   * @param city the city, may not be empty
   * @param additionalInfo the additional info, may be empty but not {@code null}
   * @throws IllegalArgumentException if any of the parameters is {@code null}
   */
  public Address(String street, String zip, String city, String additionalInfo) {
    String[] params = {street, zip, city, additionalInfo};
    Assert.noNullElements(params, "No parameter may be null!");

    zip = zip.trim();
    Assert.isTrue(zip.isEmpty() || zip.length() == ZIP_LENGTH,
        "Zip must be " + ZIP_LENGTH + " long or empty, but was: " + zip);

    this.street = street;
    this.zip = zip;
    this.city = city;
    this.additionalInfo = additionalInfo;
  }

  /**
   * Default constructor
   */
  public Address() {
    street = zip = city = additionalInfo = "";
  }

  /**
   * Creates a new address
   *
   * @param street the street, may not be empty
   * @param zip the zip, must be 5 characters long
   * @param city the city, may not be empty
   * @return the address
   */
  public static Address of(String street, String zip, String city) {
    return new Address(street, zip, city);
  }

  /**
   * Creates a new address
   *
   * @param street the street, may not be empty
   * @param zip the zip, must be 5 characters long
   * @param city the city, may not be empty
   * @param additionalInfo the additional info, may be empty but not {@code null}
   * @return the address
   */
  public static Address of(String street, String zip, String city, String additionalInfo) {
    return new Address(street, zip, city, additionalInfo);
  }

  /**
   * @return the street
   */
  public String getStreet() {
    return street;
  }

  /**
   * @param street the street to set
   */
  @JpaOnly
  protected void setStreet(String street) {
    Assert.notNull(street, "Street may not be null!");
    this.street = street;
  }

  /**
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * @param zip the zip to set
   */
  protected void setZip(String zip) {
    Assert.notNull(zip, "Zip may not be null!");
    this.zip = zip;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city the city to set
   */
  @JpaOnly
  protected void setCity(String city) {
    Assert.notNull(city, "City may not be null!");
    this.city = city;
  }

  /**
   * @return the additional info. May be empty
   */
  public String getAdditionalInfo() {
    return additionalInfo;
  }

  /**
   * @param additionalInfo the additional info to set
   */
  @JpaOnly
  protected void setAdditionalInfo(String additionalInfo) {
    Assert.notNull(additionalInfo, "Additional info may not be null!");
    this.additionalInfo = additionalInfo;
  }

  /**
   * @return whether no field is set
   */
  public boolean empty() {
    return street.isEmpty() && zip.isEmpty() && city.isEmpty() && additionalInfo.isEmpty();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((street == null) ? 0 : street.hashCode());
    result = prime * result + ((zip == null) ? 0 : zip.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Address other = (Address) obj;
    if (additionalInfo == null) {
      if (other.additionalInfo != null) {
        return false;
      }
    } else if (!additionalInfo.equals(other.additionalInfo)) {
      return false;
    }
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (street == null) {
      if (other.street != null) {
        return false;
      }
    } else if (!street.equals(other.street)) {
      return false;
    }
    if (zip == null) {
      if (other.zip != null) {
        return false;
      }
    } else if (!zip.equals(other.zip)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Address [street=" + street + ", zip=" + zip + ", city=" + city + ", additionalInfo="
        + additionalInfo + "]";
  }

}
