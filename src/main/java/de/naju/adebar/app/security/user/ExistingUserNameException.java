package de.naju.adebar.app.security.user;

/**
 * Exception to indicate that an account with the given username does already exist
 * 
 * @author Rico Bergmann
 *
 */
public class ExistingUserNameException extends RuntimeException {
  private static final long serialVersionUID = 6936590480076957351L;

  public ExistingUserNameException() {
    super();
  }

  public ExistingUserNameException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public ExistingUserNameException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public ExistingUserNameException(String arg0) {
    super(arg0);
  }

  public ExistingUserNameException(Throwable arg0) {
    super(arg0);
  }

}
