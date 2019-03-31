package de.naju.adebar.documentation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a visible method, etc. should not be used outside of some scope.
 * <p>
 * It is only visible due to some limitation of a framework or the used JAVA version.
 *
 * @author Rico Bergmann
 */
@Documented
@Retention(SOURCE)
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR})
public @interface Internal {

  /**
   * A scope denotes which classes are intended to access some element.
   *
   * @author Rico Bergmann
   */
  public enum Scope {

    /**
     * Only methods in the surrounding class should have access.
     */
    PRIVATE,

    /**
     * Only classes in the same package should have access.
     */
    PACKAGE,

    /**
     * Only classes in the same package or subclasses of the surrounding class should have access.
     */
    PROTECTED
  }

  /**
   * The scope which is intended for the annotated element.
   */
  Scope scope() default Scope.PRIVATE;

}
