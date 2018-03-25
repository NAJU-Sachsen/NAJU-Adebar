package de.naju.adebar.documentation.infrastructure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The whole purpose of the method, constructor or field is to enable some JPA functionality.
 *
 * <p> It is not supposed to used by normal program code and should normally be {@code private}
 *
 * @author Rico Bergmann
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD})
public @interface JpaOnly {

}
