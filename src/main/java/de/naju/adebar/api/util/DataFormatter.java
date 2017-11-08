package de.naju.adebar.api.util;

import org.springframework.stereotype.Service;
import de.naju.adebar.api.forms.FilterPersonForm;

/**
 * Service to format user-submitted data to a normalized structure
 * 
 * @author Rico Bergmann
 */
@Service
public class DataFormatter {

  /**
   * Converts the first letter of a string to uppercase, trimming it if necessary
   * 
   * @param str the string to convert
   * @return the converted string
   */
  public String adjustFirstLetterCase(String str) {
    if (str == null || str.isEmpty()) {
      return str;
    }
    str = str.trim();
    Character fl = str.charAt(0);
    fl = Character.toUpperCase(fl);
    return fl + str.substring(1);
  }

  /**
   * Normalizes a {@link FilterPersonForm}. This basically mean that name and city will start with
   * capital letters afterwards.
   * 
   * @param form the form to adjust
   */
  public void adjustFilterPersonForm(FilterPersonForm form) {
    form.setFirstName(adjustFirstLetterCase(form.getFirstName()));
    form.setLastName(adjustFirstLetterCase(form.getLastName()));
    form.setCity(adjustFirstLetterCase(form.getCity()));
  }

}
