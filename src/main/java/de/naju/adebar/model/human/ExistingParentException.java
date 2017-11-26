package de.naju.adebar.model.human;

/**
 * Exception to indicate that a parent is already connected to a person
 * 
 * @author Rico Bergmann
 */
public class ExistingParentException extends RuntimeException {
  private static final long serialVersionUID = 799521946635294458L;

  public ExistingParentException() {
    super();
  }

  public ExistingParentException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
    super(arg0, arg1, arg2, arg3);
  }

  public ExistingParentException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public ExistingParentException(String arg0) {
    super(arg0);
  }

  public ExistingParentException(Throwable arg0) {
    super(arg0);
  }

}
