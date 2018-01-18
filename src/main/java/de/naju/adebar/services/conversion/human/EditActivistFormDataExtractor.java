package de.naju.adebar.services.conversion.human;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;
import de.naju.adebar.controller.forms.human.EditActivistForm;
import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.JuleicaCard;
import de.naju.adebar.model.persons.Person;

/**
 * @author Rico Bergmann
 */
@Service
public class EditActivistFormDataExtractor {

  public ActivistProfile updateActivist(Person person, EditActivistForm activistForm) {
    JuleicaCard juleica = null;
    if (!activistForm.isActivist()) {
      return null;
    } else if (activistForm.isOwningJuleica()) {
      juleica = new JuleicaCard(LocalDate.parse(activistForm.getJuleicaExpiryDate(),
          DateTimeFormatter.ofPattern(EditActivistForm.DATE_FORMAT, Locale.GERMAN)));

    }
    return person.getActivistProfile().updateJuleicaCard(juleica);
  }

}
