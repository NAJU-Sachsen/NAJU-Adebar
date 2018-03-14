package de.naju.adebar.documentation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The component is designed poorly or could be structured much better.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface DesignSmell {

  /**
   * @return what the smell is
   */
  String description();

  /**
   * @return why the smell was introduced
   */
  String reason() default "";

}
