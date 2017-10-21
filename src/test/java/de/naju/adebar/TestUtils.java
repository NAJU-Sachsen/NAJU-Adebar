package de.naju.adebar;

/**
 * @author Rico Bergmann
 */
public class TestUtils {

  public static <T> boolean iterableContains(Iterable<T> iterable, T t) {
    for (T obj : iterable) {
      if (t.equals(obj)) {
        return true;
      }
    }
    return false;
  }

}
