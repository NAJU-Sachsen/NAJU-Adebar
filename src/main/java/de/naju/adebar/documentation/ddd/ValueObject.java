package de.naju.adebar.documentation.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Instances of the class do not possess an own identity. Instead they are solely distinguished by
 * the values of their attributes.
 *
 * @author Rico Bergmann
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.TYPE_PARAMETER})
public @interface ValueObject {

}
