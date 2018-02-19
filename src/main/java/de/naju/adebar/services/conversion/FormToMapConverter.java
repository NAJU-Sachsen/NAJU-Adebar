package de.naju.adebar.services.conversion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.google.common.collect.Lists;
import de.naju.adebar.util.Arrays2;

/**
 * Service to extract all publicly accessible fields from a form and put them in a map.
 *
 * @author Rico Bergmann
 */
@Service
public class FormToMapConverter {

  private static final Pattern GETTER_REGEX = Pattern.compile("^get(?<varname>([A-Z][a-z0-9]*)+)$");
  private static final Pattern QUERY_REGEX = Pattern.compile("^is(?<varname>([A-Z][a-z0-9]*)+)$");

  /**
   * Extracts all fields which are available through a public getter-method and puts their values
   * into a map which will map the field names to their respective values.
   *
   * <p>
   * The following rules are being applied:
   * <ul>
   * <li>Each field which is available through a method like {@code public Object getField()} will
   * be put into the map as {@code field -> value of getField()}</li>
   * <li>Furthermore the same applies to query methods like {@code public boolean isField()}</li>
   * <li>All methods whose signature does not match the expected format will be ignored</li>
   * <li>If the return type is an instance of {@link Iterable} or an array, the elements will be
   * unwrapped and placed into the map as multiple values</li>
   * <li>Technically the conversions work for any object, there is no restriction to "forms"</li>
   * </ul>
   *
   * @param form the
   * @return
   */
  public MultiValueMap<String, Object> convert(Object form) {
    Method[] allMethods = form.getClass().getMethods();

    MultiValueMap<String, Object> res = new LinkedMultiValueMap<>(allMethods.length);
    try {
      for (Method m : allMethods) {
        if (Arrays2.linearSearch(Object.class.getMethods(), m) != -1) {
          continue;
        }

        if (GETTER_REGEX.matcher(m.getName()).matches() && methodHasGetterOrQuerySignature(m)) {
          res.put(extractAttributeNameFromGetter(m), zipAttribute(m.invoke(form)));
        } else if (QUERY_REGEX.matcher(m.getName()).matches()
            && methodHasGetterOrQuerySignature(m)) {
          res.put(extractAttributeNameFromQuery(m), zipAttribute(m.invoke(form)));
        }
      }
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new FormInspectionError(e);
    }

    return res;
  }

  /**
   * @param m the method to check
   * @return whether the method is a getter ({@code Object getField()}) or query
   *         ({@code boolean isField()})
   */
  private boolean methodHasGetterOrQuerySignature(Method m) {
    return m.getParameterTypes().length == 0 && m.getReturnType() != Void.TYPE;
  }

  /**
   * @param m the method to parse
   * @return the name of the field described by the getter (for {@code getField()} this would be
   *         {@code field})
   */
  private String extractAttributeNameFromGetter(Method m) {
    return extractAttributeName(m, GETTER_REGEX);
  }

  /**
   * @param m the method to parse
   * @return the name of the field described by the query (for {@code isField()} this would be
   *         {@code field})
   */
  private String extractAttributeNameFromQuery(Method m) {
    return extractAttributeName(m, QUERY_REGEX);
  }

  /**
   * Wraps the attribute into a list, unzipping it if is an array or {@link Iterable}
   *
   * @param attr the attribute
   * @return either a singleton list or a list containing all the attributes elements
   */
  @SuppressWarnings("unchecked")
  private List<Object> zipAttribute(Object attr) {
    if (attr instanceof Iterable<?>) {
      return Lists.newArrayList((Iterable<Object>) attr);
    } else if (attr.getClass().isArray()) {
      Object[] casted = (Object[]) attr;
      return Arrays.asList(casted);
    }
    return Lists.newArrayList(attr);
  }

  /**
   * @param m the method to parse
   * @param pattern the pattern to use for extraction. Has to contain a group {@code varname} which
   *        matches the attribute format
   * @return the portion of the methods name matched by {@code pattern}, with the first letter
   *         converted to lower-case
   */
  private String extractAttributeName(Method m, Pattern pattern) {
    Matcher matcher = pattern.matcher(m.getName());
    boolean matches = matcher.matches();
    Assert.isTrue(matches, "Method name has to match the pattern");
    String rawName = matcher.group("varname");
    String initialLowercase = rawName.substring(0, 1).toLowerCase();
    String remainder = rawName.length() > 1 //
        ? rawName.substring(1) //
        : "";
    return initialLowercase + remainder;
  }

}
