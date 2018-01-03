package de.naju.adebar.infrastructure.thymeleaf;

/**
 * Provides useful helpers for dealing with objects within Thymeleaf templates
 * 
 * @author Rico Bergmann
 */
public class ObjectUtility {

  public boolean isSpecified(Object obj) {
    return obj != null;
  }

  public boolean isSpecified(Boolean bool) {
    return bool != null && bool;
  }

}
