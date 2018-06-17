package de.naju.adebar.infrastructure.thymeleaf;

import java.util.HashSet;
import java.util.Set;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

public class TagExtendedDialect extends AbstractProcessorDialect {

  public TagExtendedDialect() {
    super("Adebar dialect", "adebar", 1000);
  }

  @Override
  public Set<IProcessor> getProcessors(String dialectPrefix) {
    final Set<IProcessor> processors = new HashSet<>();
    processors.add(new NewlineRetainingTextFormatter(dialectPrefix));
    return processors;
  }
}
