package de.naju.adebar.model.human;

import javax.persistence.Embeddable;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Abstraction of an address. Each address consists of street, zip and city and may contain additional info - e.g.
 * room numbers or the like.
 * @author Rico Bergmann
 */
@Embeddable
public class Address implements Serializable {
	public final static int ZIP_LENGTH = 5;
	
	private String street;
	private String zip;
	private String city;
	private String additionalInfo;

	// constructors
	
	public Address(String street, String zip, String city) {
		this(street, zip, city, "");
	}
	
	/**
	 * Full constructor
	 * @param street the street, may not be empty
	 * @param zip the zip, must be 5 characters long
	 * @param city the city, may not be empty
	 * @param additionalInfo the additional info, may be empty but not {@code null}
     * @throws IllegalArgumentException if any of the parameters is null or empty (when it should not be)
	 */
	public Address(String street, String zip, String city, String additionalInfo) {
		String[] params = {street, zip, city, additionalInfo};
		Assert.noNullElements(params, "No parameter may be null!");
		Assert.hasText(street, "Street may not be empty, but was " + street);
		Assert.hasText(city, "City may not be empty, but was " + city);

		zip = zip.trim();
		Assert.isTrue(zip.length() == ZIP_LENGTH, "Zip must be " + ZIP_LENGTH + " long, but was: " + zip);
		
		this.street = street;
		this.zip = zip;
		this.city = city;
		this.additionalInfo = additionalInfo;
	}
	
	/**
	 * Default constructor for JPA's sake
	 */
	Address() {
		
	}
	
	// getter
	
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the additional info. May be empty
	 */
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	// setter
	
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		Assert.notNull(street, "Street may not be null!");
		this.street = street;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		Assert.notNull(zip, "Zip may not be null!");
		this.zip = zip;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		Assert.notNull(city, "City may not be null!");
		this.city = city;
	}

	/**
	 * @param additionalInfo the additional info to set
	 */
	public void setAdditionalInfo(String additionalInfo) {
		Assert.notNull(additionalInfo, "Additional info may not be null!");
		this.additionalInfo = additionalInfo;
	}
	
	// overwritten from Object
	
	/* (non-Javadoc)
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

	/* (non-Javadoc)
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
		if (!(obj instanceof Address)) {
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Address [street=" + street + ", zip=" + zip + ", city=" + city + ", additionalInfo=" + additionalInfo
				+ "]";
	}
	
}
