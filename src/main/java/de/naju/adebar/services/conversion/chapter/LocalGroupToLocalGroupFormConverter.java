package de.naju.adebar.services.conversion.chapter;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.web.validation.chapters.LocalGroupForm;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

/**
 * Service to convert a {@link LocalGroup} to corresponding {@link LocalGroupForm} objects
 * 
 * @author Rico Bergmann
 */
@Service
public class LocalGroupToLocalGroupFormConverter {

  /**
   * Performs the conversion
   * 
   * @param localGroup the local group to convert
   * @return the created form
   */
  public LocalGroupForm convertToLocalGroupForm(LocalGroup localGroup) {
    LocalGroupForm groupForm = new LocalGroupForm();
    groupForm.setName(localGroup.getName());

    if (localGroup.getAddress() != null) {
      groupForm.setStreet(localGroup.getAddress().getStreet());
      groupForm.setZip(localGroup.getAddress().getZip());
      groupForm.setCity(localGroup.getAddress().getCity());
    }

    if (localGroup.getNabuGroupLink() != null) {
      groupForm.setNabuGroup(localGroup.getNabuGroupLink().toString());
    }

    groupForm.setContactPersons(extractContactPersonsFrom(localGroup));

    return groupForm;
  }

  /**
   * Extracts all contact persons from a local group
   * 
   * @param localGroup the group to take the contact persons from
   * @return the contact persons' IDs
   */
  private List<String> extractContactPersonsFrom(LocalGroup localGroup) {
    List<String> contactPersons = new LinkedList<>();
    localGroup.getContactPersons().forEach(p -> contactPersons.add(p.getId().toString()));
    return contactPersons;
  }
}
