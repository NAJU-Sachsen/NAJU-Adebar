package de.naju.adebar.app.security.user;

/**
 * Exception to indicate that a provided password is not correct
 * 
 * @author Rico Bergmann
 *
 */
public class PasswordMismatchException extends RuntimeException {
  private static final long serialVersionUID = 1820860429951678130L;

  public PasswordMismatchException() {
    super();
  }

  public PasswordMismatchException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public PasswordMismatchException(String message, Throwable cause) {
    super(message, cause);
  }

  public PasswordMismatchException(String message) {
    super(message);
  }

  public PasswordMismatchException(Throwable cause) {
    super(cause);
  }



}
