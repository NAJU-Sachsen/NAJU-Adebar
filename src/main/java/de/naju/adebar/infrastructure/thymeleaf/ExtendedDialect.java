package de.naju.adebar.infrastructure.thymeleaf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * Extension of the standard Thymeleaf dialect to make use of our {@link TimeFormatter} formatting
 * methods
 *
 * @author Rico Bergmann
 * @see <a href="http://www.thymeleaf.org/documentation.html">Thymeleaf doc</a>
 * @see <a href= "https://stackoverflow.com/questions/37905520/thymeleaf-how-to-add-a-custom-util">On
 *     writing custom dialects</a>
 * @see TimeFormatter
 */
public class ExtendedDialect extends AbstractDialect implements IExpressionObjectDialect {

  private static final int NO_OF_EXPRESSION_OBJECTS = 5;

  public ExtendedDialect() {
    super("adebar-custom");
  }

  @Override
  public IExpressionObjectFactory getExpressionObjectFactory() {
    return new DialectFactory();
  }

  /**
   * Factory to add all the necessary expression objects
   */
  private class DialectFactory implements IExpressionObjectFactory {

    private Map<String, Object> expressionObjects;

    DialectFactory() {
      expressionObjects = new HashMap<>(NO_OF_EXPRESSION_OBJECTS);
      expressionObjects.put("time", new TimeFormatter());
      expressionObjects.put("money", new CurrencyFormatter());
      expressionObjects.put("cstStrings", new StringsFormatter());
      expressionObjects.put("iterables", new IterableUtility());
      expressionObjects.put("objects", new ObjectUtility());
    }

    @Override
    public Set<String> getAllExpressionObjectNames() {
      return expressionObjects.keySet();
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
      return expressionObjects.get(expressionObjectName);
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
      return true;
    }
  }
}
