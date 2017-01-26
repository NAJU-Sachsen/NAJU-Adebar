package de.naju.adebar.model.human;

/**
 * Exception to indicate that a person is not a referent but was treated as one.
 * @author Rico Bergmann
 * @see Person
 * @see Referent
 * @see PersonId
 */
public class NoReferentException extends  RuntimeException {

    public NoReferentException() {
    }

    public NoReferentException(String message) {
        super(message);
    }

    public NoReferentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoReferentException(Throwable cause) {
        super(cause);
    }

    public NoReferentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
