package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.controller.forms.chapter.LocalGroupForm;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Person;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to extract the necessary data from a local group form
 * 
 * @author Rico Bergmann
 */
@Service
public class LocalGroupFormDataExtractor {
  private PersonManager personManager;

  public LocalGroupFormDataExtractor(PersonManager personManager) {
    Assert.notNull(personManager, "Person manager may not be null!");
    this.personManager = personManager;
  }

  /**
   * @param localGroupForm form containing the information to extract
   * @return the {@link LocalGroup} object described by the form
   */
  public LocalGroup extractLocalGroup(LocalGroupForm localGroupForm) {
    Address address =
        new Address(localGroupForm.getStreet(), localGroupForm.getZip(), localGroupForm.getCity());
    LocalGroup localGroup = new LocalGroup(localGroupForm.getName(), address);

    if (localGroupForm.hasNabuGroup()) {
      try {
        localGroup.setNabuGroupLink(new URL(localGroupForm.getNabuGroup()));
      } catch (MalformedURLException e) {
        // URL does not work, but we are fine with that
      }
    }

    if (localGroupForm.hasContactPersons()) {
      localGroup.setContactPersons(extractContactPersonsFrom(localGroupForm));
    }

    return localGroup;
  }

  /**
   * @param localGroupForm form containing the data to extract
   * @return the contact persons contained in the form
   */
  private List<Person> extractContactPersonsFrom(LocalGroupForm localGroupForm) {
    List<Person> contactPersons = new ArrayList<>(localGroupForm.getContactPersons().size());
    localGroupForm.getContactPersons()
        .forEach(pId -> contactPersons.add(personManager.findPerson(pId)
            .orElseThrow(() -> new IllegalStateException("No person exists for ID " + pId))));
    return contactPersons;
  }

}
