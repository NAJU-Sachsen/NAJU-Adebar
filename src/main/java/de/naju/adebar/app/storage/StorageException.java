package de.naju.adebar.app.storage;

/**
 * Exception to indicate that a file could not be loaded or stored by some {@link StorageService}.
 *
 * @author Rico Bergmann
 * @see StorageService
 */
public class StorageException extends RuntimeException {

  private static final long serialVersionUID = -8543598931973068040L;

  /**
   * Creates a new exception without any details attached.
   *
   * @see RuntimeException#RuntimeException()
   */
  public StorageException() {}

  /**
   * Creates a new exception with an error message.
   *
   * @see RuntimeException#RuntimeException(String)
   */
  public StorageException(String message) {
    super(message);
  }

  /**
   * Creates a new exception with an error message and a causing exception.
   *
   * @see RuntimeException#RuntimeException(String, Throwable)
   */
  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new exception with a causing exception.
   *
   * @see RuntimeException#RuntimeException(Throwable)
   */
  public StorageException(Throwable cause) {
    super(cause);
  }

}
