package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.persons.HumanDataProcessor;
import de.naju.adebar.services.conversion.chapter.AddLocalGroupFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.BoardFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.BoardToBoardFormConverter;
import de.naju.adebar.services.conversion.chapter.LocalGroupFormDataExtractor;
import de.naju.adebar.services.conversion.chapter.LocalGroupToLocalGroupFormConverter;

/**
 * Data processor for the {@link LocalGroupController}
 *
 * @author Rico Bergmann
 */
@Component
class LocalGroupControllerDataProcessors {
  public final LocalGroupFormDataExtractor localGroupExtractor;
  public final AddLocalGroupFormDataExtractor addLocalGroupExtractor;
  public final LocalGroupToLocalGroupFormConverter localGroupConverter;
  public final BoardFormDataExtractor boardExtractor;
  public final BoardToBoardFormConverter boardConverter;
  public final HumanDataProcessor persons;

  @Autowired
  public LocalGroupControllerDataProcessors(LocalGroupFormDataExtractor localGroupFormDataExtractor,
      AddLocalGroupFormDataExtractor addLocalGroupFormDataExtractor,
      LocalGroupToLocalGroupFormConverter localGroupFormConverter,
      BoardFormDataExtractor boardFormDataExtractor, BoardToBoardFormConverter boardFormConverter,
      HumanDataProcessor humanDataProcessor) {
    this.localGroupExtractor = localGroupFormDataExtractor;
    this.addLocalGroupExtractor = addLocalGroupFormDataExtractor;
    this.localGroupConverter = localGroupFormConverter;
    this.boardExtractor = boardFormDataExtractor;
    this.boardConverter = boardFormConverter;
    this.persons = humanDataProcessor;
  }
}
