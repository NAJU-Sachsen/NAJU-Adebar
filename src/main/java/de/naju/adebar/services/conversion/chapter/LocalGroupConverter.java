package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

/**
 * Converter to create {@link LocalGroup} instances from their primary key.
 *
 * <p> Used internally by Spring to provide these instances in controllers, forms, etc.
 */
public class LocalGroupConverter implements Converter<String, LocalGroup> {

  private final ReadOnlyLocalGroupRepository localGroupRepo;

  /**
   * @param localGroupRepo the local group repository
   */
  public LocalGroupConverter(ReadOnlyLocalGroupRepository localGroupRepo) {
    Assert.notNull(localGroupRepo, "localGroupRepo may not be null");
    this.localGroupRepo = localGroupRepo;
  }

  @Override
  public LocalGroup convert(String source) {
    return localGroupRepo.findById(Long.parseLong(source))
        .orElseThrow(() -> new IllegalArgumentException("No local group with ID " + source));
  }
}
