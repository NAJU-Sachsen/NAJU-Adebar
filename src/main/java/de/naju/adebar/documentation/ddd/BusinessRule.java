package de.naju.adebar.documentation.ddd;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Encapsulates a specification of a workflow or decision-procedure inherent to the domain.
 *
 * @author Rico Bergmann
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface BusinessRule {

}
