package de.naju.adebar.util;

import org.springframework.util.Assert;

/**
 * Provides some more assertion functions than {@link Assert} does.
 *
 * @author Rico Bergmann
 */
public class Assert2 {

  private Assert2() {
  }

  /**
   * Assert that an {@link Iterable} contains no {@code null} elements.
   *
   * @param elems the elements to check
   * @throws IllegalArgumentException if any of the elements in the {@link Iterable} was {@code
   *     null}
   */
  public static void noNullElements(Iterable<?> elems) {
    elems.forEach(it -> {
      if (it == null) {
        reportFailure("element was null");
      }
    });
  }

  /**
   * Asserts that an {@link Iterable} contains no {@code null} elements.
   *
   * @param elems the elements to check
   * @param msg the exception message to use if some element was {@code null}
   * @throws IllegalArgumentException if any of the elements in the {@link Iterable} was {@code
   *     null}
   */
  public static void noNullElements(Iterable<?> elems, String msg) {
    elems.forEach(it -> {
      if (it == null) {
        reportFailure(msg, true);
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
    reportFailure(msg, false);
  }

  /**
   * Throws the {@link IllegalArgumentException} with a dedicated message
   *
   * @param msg the message
   * @param customMsg whether the message should completely replace the default message. If
   *     {@code false} the message will be appended to some default prefix
   */
  private static void reportFailure(String msg, boolean customMsg) {
    String exceptionMsg = customMsg ? msg : ("Assertion failed: " + msg);
    throw new IllegalArgumentException(exceptionMsg);
  }

}
