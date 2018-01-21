package de.naju.adebar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.persons.HumanDataProcessor;
import de.naju.adebar.services.conversion.persons.AddQualificationFormDataExtractor;
import de.naju.adebar.services.conversion.persons.CreatePersonFormDataExtractor;
import de.naju.adebar.services.conversion.persons.EditActivistFormDataExtractor;
import de.naju.adebar.services.conversion.persons.EditPersonFormDataExtractor;
import de.naju.adebar.services.conversion.persons.FilterPersonFormFilterExtractor;

/**
 * Data processor for the {@link PersonController}
 * 
 * @author Rico Bergmann
 */
@Component
class PersonControllerDataProcessors {
  public final CreatePersonFormDataExtractor createPersonExtractor;
  public final EditPersonFormDataExtractor editPersonExtractor;
  public final EditActivistFormDataExtractor editActivistExtractor;
  public final FilterPersonFormFilterExtractor filterPersonExtractor;
  public final AddQualificationFormDataExtractor addQualificationExtractor;
  public final HumanDataProcessor persons;

  @Autowired
  public PersonControllerDataProcessors(CreatePersonFormDataExtractor createPersonFormDataExtractor,
      EditPersonFormDataExtractor editPersonFormDataExtractor,
      EditActivistFormDataExtractor editActivistFormDataExtractor,
      FilterPersonFormFilterExtractor filterPersonFormFilterExtractor,
      AddQualificationFormDataExtractor addQualificationFormDataExtractor,
      HumanDataProcessor dataProcessor) {
    this.createPersonExtractor = createPersonFormDataExtractor;
    this.editPersonExtractor = editPersonFormDataExtractor;
    this.editActivistExtractor = editActivistFormDataExtractor;
    this.filterPersonExtractor = filterPersonFormFilterExtractor;
    this.addQualificationExtractor = addQualificationFormDataExtractor;
    this.persons = dataProcessor;
  }
}
