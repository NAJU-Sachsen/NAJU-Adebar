package de.naju.adebar.util.conversion.chapter;

import de.naju.adebar.controller.forms.chapter.LocalGroupForm;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.human.Address;

/**
 * Service to extract the necessary data from a local group form
 * @author Rico Bergmann
 */
public class LocalGroupFormDataExtractor {

    /**
     * @param localGroupForm form containing the information to extract
     * @return the {@link LocalGroup} object described by the form
     */
    public LocalGroup extractLocalGroup(LocalGroupForm localGroupForm) {
        Address address = new Address(localGroupForm.getStreet(), localGroupForm.getZip(), localGroupForm.getCity());
        LocalGroup localGroup = new LocalGroup(localGroupForm.getName(), address);
        return localGroup;
    }

}
