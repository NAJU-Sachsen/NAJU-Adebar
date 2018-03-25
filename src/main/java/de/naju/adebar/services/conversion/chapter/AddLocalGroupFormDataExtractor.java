package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.web.validation.chapters.AddLocalGroupForm;
import de.naju.adebar.model.newsletter.Newsletter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to extract the necessary data from an 'add local group' form
 * 
 * @author Rico Bergmann
 */
@Service
public class AddLocalGroupFormDataExtractor extends LocalGroupFormDataExtractor {

  @Autowired
  public AddLocalGroupFormDataExtractor(PersonManager personManager) {
    super(personManager);
  }

  /**
   * @param addLocalGroupForm the form encoding the data to extract
   * @return the {@link LocalGroup} object encoded by the form
   */
  public LocalGroup extractLocalGroup(AddLocalGroupForm addLocalGroupForm) {
    LocalGroup localGroup = super.extractLocalGroup(addLocalGroupForm);
    if (addLocalGroupForm.isCreateNewsletter()) {
      localGroup.addNewsletter(extractNewsletter(addLocalGroupForm));
    }
    return localGroup;
  }

  /**
   * @param addLocalGroupForm the form encoding the data to extract
   * @return the encoded newsletter
   * @throws IllegalStateException if the form does not contain data about a newsletter
   */
  public Newsletter extractNewsletter(AddLocalGroupForm addLocalGroupForm) {
    if (!addLocalGroupForm.isCreateNewsletter()) {
      throw new IllegalStateException("Form does not contain information about a newsletter");
    }
    return new Newsletter(addLocalGroupForm.getNewsletterName());
  }

}
