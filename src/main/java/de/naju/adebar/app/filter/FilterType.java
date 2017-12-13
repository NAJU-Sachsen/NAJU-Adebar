package de.naju.adebar.app.filter;

/**
 * Enumeration to specify whether to search only for objects possessing a special attribute or only
 * for those which do not
 * 
 * @author Rico Bergmann
 */
public enum FilterType {

  /**
   * Searches only for those, which have the attribute
   */
  ENFORCE {
    @Override
    public boolean matching(Object first, Object second) {
      return first.equals(second);
    }
  },

  /**
   * Ignores all, which have the attribute
   */
  IGNORE {
    @Override
    public boolean matching(Object first, Object second) {
      return !first.equals(second);
    }
  };

  /**
   * @param first the object to check
   * @param second the object to check for
   * @return {@code true} if the object matches (=equals) the criteria
   */
  public abstract boolean matching(Object first, Object second);
}
