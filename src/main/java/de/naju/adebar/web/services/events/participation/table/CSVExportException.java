package de.naju.adebar.web.services.events.participation.table;

/**
 * Exception to indicate that the CSV file may not be written.
 *
 * @author Rico Bergmann
 */
public class CSVExportException extends RuntimeException {

  private static final long serialVersionUID = 7564943328057089146L;

  /**
   * Creates a new exception without any details attached.
   *
   * @see RuntimeException#RuntimeException()
   */
  public CSVExportException() {}

  /**
   * Creates a new exception with an error message.
   *
   * @see RuntimeException#RuntimeException(String)
   */
  public CSVExportException(String message) {
    super(message);
  }

  /**
   * Creates a new exception with an error message and a causing exception.
   *
   * @see RuntimeException#RuntimeException(String, Throwable)
   */
  public CSVExportException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new exception with a causing exception.
   *
   * @see RuntimeException#RuntimeException(Throwable)
   */
  public CSVExportException(Throwable cause) {
    super(cause);
  }

}
