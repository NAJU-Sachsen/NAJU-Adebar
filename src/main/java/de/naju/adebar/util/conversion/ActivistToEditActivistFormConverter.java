package de.naju.adebar.util.conversion;

import de.naju.adebar.controller.forms.EditActivistForm;
import de.naju.adebar.model.human.Activist;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service to convert an {@link Activist} to a corresponding {@link EditActivistForm}
 * @author Rico Bergmann
 */
@Service
public class ActivistToEditActivistFormConverter {

    /**
     * Performs the conversion
     * @param activist the activist to convert
     * @return the created form
     */
    public EditActivistForm convertToEditActivistForm(Activist activist) {
        String juleicaExpiryDate = activist.hasJuleica() ? activist.getJuleicaExpiryDate().format(DateTimeFormatter.ofPattern(EditActivistForm.DATE_FORMAT, Locale.GERMAN)) : "";
        return new EditActivistForm(true, activist.hasJuleica(), juleicaExpiryDate);
    }

}
