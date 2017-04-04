package de.naju.adebar.util.conversion.chapter;

import de.naju.adebar.controller.forms.chapter.LocalGroupForm;
import de.naju.adebar.model.chapter.LocalGroup;
import org.springframework.stereotype.Service;

/**
 * Service to convert a {@link LocalGroup} to corresponding {@link LocalGroupForm} objects
 * @author Rico Bergmann
 */
@Service
public class LocalGroupToLocalGroupFormConverter {

    /**
     * Performs the conversion
     * @param localGroup the local group to convert
     * @return the created form
     */
    public LocalGroupForm convertToLocalGroupForm(LocalGroup localGroup) {
        return new LocalGroupForm(localGroup.getName(), localGroup.getAddress().getStreet(), localGroup.getAddress().getZip(), localGroup.getAddress().getCity());
    }
}
