package de.naju.adebar.documentation.infrastructure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that in order for the method to work properly, it should be executed in an
 * transactional context.
 * <p>
 * Running the method without such a context may work, but is likely going to produce unexpected
 * results.
 *
 * @author Rico Bergmann
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface RequiresTransactionalContext {

}
