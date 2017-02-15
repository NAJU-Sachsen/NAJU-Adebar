package de.naju.adebar.model.human;

/**
 * Exception to indicate that a qualification with the same name already exists
 * @author Rico Bergmann
 */
public class ExistingQualificationException extends RuntimeException {

    public ExistingQualificationException() {
    }

    public ExistingQualificationException(String message) {
        super(message);
    }

    public ExistingQualificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingQualificationException(Throwable cause) {
        super(cause);
    }

    public ExistingQualificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
