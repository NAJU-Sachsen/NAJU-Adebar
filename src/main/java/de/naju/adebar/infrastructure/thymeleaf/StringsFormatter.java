package de.naju.adebar.infrastructure.thymeleaf;

import java.util.StringJoiner;

/**
 * Custom formatter for sets of strings
 *
 * @author Rico Bergmann
 */
public class StringsFormatter {

  /**
   * Default formatter to use if no other was specified
   */
  private static final String DEFAULT_FORMATTER = ", ";

  /**
   * Joins the objects to one large string separated by the {@link #DEFAULT_FORMATTER}
   *
   * @param objs the objects to join
   * @return the joined objects
   */
  public String join(Object... objs) {
    return joinCustom(DEFAULT_FORMATTER, objs);
  }

  /**
   * Joins the objects to one large string, using a custom separator
   *
   * @param separator the separator to use
   * @param objs the objects to join
   * @return the joined objects
   */
  public String joinCustom(String separator, Object... objs) {
    StringJoiner sj = new StringJoiner(separator);
    for (Object obj : objs) {
      if (obj == null) {
        continue;
      }
      String elem = joinElem(separator, obj);
      if (!elem.isEmpty()) {
        sj.add(joinElem(separator, obj));
      }
    }
    return sj.toString();
  }

  /**
   * Joins a single object. Depending on the concrete type (e.g. for arrays) the join may be
   * delegated or done directly done through the function.
   *
   * @param separator
   * @param obj
   * @return
   */
  private String joinElem(String separator, Object obj) {
    if (obj instanceof Iterable) {
      return joinIterable(separator, (Iterable<?>) obj);
    } else if (obj instanceof Object[]) {
      return joinArray(separator, (Object[]) obj);
    }
    return obj.toString();
  }

  /**
   * Joins an array of objects
   *
   * @param separator the separator to use for the join
   * @param objs the objects to join
   * @return the joined objects
   */
  private String joinArray(String separator, Object[] objs) {
    StringJoiner sj = new StringJoiner(separator);
    for (Object obj : objs) {
      sj.add(joinElem(separator, obj));
    }
    return sj.toString();
  }

  /**
   * Joins an iterable
   *
   * @param separator the separator to use for the join
   * @param objs the objects to join
   * @return the joined objects
   */
  private String joinIterable(String separator, Iterable<?> objs) {
    StringJoiner sj = new StringJoiner(separator);
    objs.forEach(obj -> sj.add(obj.toString()));
    return sj.toString();
  }
}
