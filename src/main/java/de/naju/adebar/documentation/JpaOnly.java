package de.naju.adebar.documentation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The whole purpose of the method is to enable some JPA functionality.
 *
 * <p>
 * It is not supposed to used by normal program code and should normally be {@code private}
 *
 * @author Rico Bergmann
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface JpaOnly {
}
