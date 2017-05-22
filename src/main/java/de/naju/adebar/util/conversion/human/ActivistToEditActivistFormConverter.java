package de.naju.adebar.util.conversion.human;

import de.naju.adebar.model.human.ActivistProfile;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.controller.forms.human.EditActivistForm;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to convert an {@link Person activist} to a corresponding {@link EditActivistForm}
 * @author Rico Bergmann
 */
@Service
public class ActivistToEditActivistFormConverter {

    /**
     * Performs the conversion
     * @param person the activist to convert
     * @return the created form
     */
    public EditActivistForm convertToEditActivistForm(Person person) {
        if (!person.isActivist()) {
            return new EditActivistForm(person.isArchived(), false, null);
        }

        ActivistProfile activistProfile = person.getActivistProfile();

        String juleicaExpiryDate = null;
        if (activistProfile.hasJuleica()) {
            juleicaExpiryDate = activistProfile.getJuleicaCard().getExpiryDate() != null ? activistProfile.getJuleicaCard().getExpiryDate().format(DateTimeFormatter.ofPattern(EditActivistForm.DATE_FORMAT, Locale.GERMAN)) : "";
        }

        return new EditActivistForm(person.isArchived(), activistProfile.hasJuleica(), juleicaExpiryDate);
    }

}
