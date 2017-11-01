package de.naju.adebar.infrastructure.thymeleaf;

import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;

/**
 * Extension of the standard Thymeleaf dialect to make use of our {@link TimeFormatter} formatting
 * methods
 * 
 * @author Rico Bergmann
 * @see <a href="http://www.thymeleaf.org/documentation.html">Thymeleaf doc</a>
 * @see <a href=
 *      "https://stackoverflow.com/questions/37905520/thymeleaf-how-to-add-a-custom-util">On writing
 *      custom dialects</a>
 * @see TimeFormatter
 */
public class ExtendedDialect extends AbstractDialect implements IExpressionEnhancingDialect {

  @Override
  public String getPrefix() {
    return "adebar-custom";
  }

  @Override
  public Map<String, Object> getAdditionalExpressionObjects(IProcessingContext processingContext) {
    Map<String, Object> expressions = new HashMap<>();
    expressions.put("time", new TimeFormatter());
    expressions.put("money", new CurrencyFormatter());
    expressions.put("cstStrings", new StringsFormatter());
    expressions.put("iterables", new IterableFormatter());
    return expressions;
  }

}
