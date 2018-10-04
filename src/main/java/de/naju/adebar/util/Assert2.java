package de.naju.adebar.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.springframework.util.Assert;

/**
 * Provides some more assertion functions than {@link Assert} does.
 *
 * @author Rico Bergmann
 */
public class Assert2 {

  private Assert2() {}

  /**
   * Provides an assertion utility that will produce {@link IllegalStateException}s it they fail.
   */
  public static AssertState state() {
    return new AssertState();
  }

  /**
   * Asserts that some condition evaluates to {@code false}.
   */
  public static void isFalse(boolean expr) {
    if (expr) {
      reportFailure("expression was true");
    }
  }

  /**
   * Asserts that some condition evaluates to {@code false}.
   *
   * @param expr the condition
   * @param msg an error message to use if the assertion fails
   */
  public static void isFalse(boolean expr, String msg) {
    if (expr) {
      reportFailureWithCustomMessage(msg);
    }
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
        reportFailureWithCustomMessage(msg);
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
        reportFailureWithCustomMessage(message);
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
   * Throws the {@link IllegalArgumentException} with a completely user-defined message
   *
   * @param msg the message
   */
  private static void reportFailureWithCustomMessage(String msg) {
    reportFailure(msg, true);
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

  /**
   * Assertions which throw an {@link IllegalStateException} on failure.
   */
  public static class AssertState {

    private AssertState() {}

    /**
     * @see Assert2#noNullArguments(String, Object...)
     */
    public void noNullArguments(String message, Object... args) {
      try {
        Assert2.noNullArguments(message, args);
      } catch (IllegalArgumentException e) {
        reportFailure(message, IllegalStateException.class);
      }
    }

    /**
     * Throws the {@link IllegalStateException} with a user defined message.
     *
     * @param msg the exception's message
     * @param exToThrow the kind of exception to throw. If it may not be created, an {@link
     *     IllegalArgumentException} with error details attached will be thrown.
     */
    private void reportFailure(String msg, Class<? extends RuntimeException> exToThrow) {
      try {
        Constructor<? extends RuntimeException> exCons = exToThrow.getConstructor(String.class);
        throw exCons.newInstance(msg);
      } catch (NoSuchMethodException
          | InstantiationException
          | IllegalAccessException
          | InvocationTargetException e) {
        throw new IllegalArgumentException("Could not generate exception of class" + exToThrow, e);
      }
    }

  }

}
