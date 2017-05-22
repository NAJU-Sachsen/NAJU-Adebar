package de.naju.adebar.model.human;

/**
 * Exception to indicate that a person is not an activist but was treated as one
 * @author Rico Bergmann
 * @see Person
 * @see ActivistProfile
 * @see PersonId
 */
public class NoActivistException extends RuntimeException {

    public NoActivistException() {
    }

    public NoActivistException(String message) {
        super(message);
    }

    public NoActivistException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoActivistException(Throwable cause) {
        super(cause);
    }

    public NoActivistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
