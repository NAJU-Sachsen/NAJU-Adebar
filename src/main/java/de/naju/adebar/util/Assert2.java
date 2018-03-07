package de.naju.adebar.util;

import org.springframework.util.Assert;

/**
 * Provides some more assertion functions than {@link Assert} does.
 *
 * @author Rico Bergmann
 */
public class Assert2 {

  private Assert2() {}

  /**
   * Assert that an {@link Iterable} contains no {@code null} elements.
   *
   * @param elems the elements to check
   * @throws IllegalArgumentException if any of the elements in the {@link Iterable} was
   *         {@code null}
   */
  public static void noNullElements(Iterable<?> elems) {
    elems.forEach(it -> {
      if (it == null) {
        reportFailure("element was null");
      }
    });
  }

  /**
   * Assert that none of the given arguments is {@code null}
   *
   * @param message the message to display if the assertion fails
   * @param args the arguments to check
   * @throws IllegalArgumentException if any of the arguments was {@code null}
   */
  public static void noNullArguments(String message, Object... args) {
    for (Object arg : args) {
      if (arg == null) {
        reportFailure(message);
      }
    }
  }

  /**
   * Throws the {@link IllegalArgumentException} with a dedicated message
   *
   * @param msg the message
   */
  private static void reportFailure(String msg) {
    throw new IllegalArgumentException("Assertion failed: " + msg);
  }

}
