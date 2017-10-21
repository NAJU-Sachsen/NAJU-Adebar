package de.naju.adebar.services.conversion.human;

import de.naju.adebar.controller.forms.human.EditActivistForm;
import de.naju.adebar.model.human.ActivistProfile;
import de.naju.adebar.model.human.JuleicaCard;
import de.naju.adebar.model.human.Person;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Service;

/**
 * @author Rico Bergmann
 */
@Service
public class EditActivistFormDataExtractor {

  public ActivistProfile extractActivistForm(Person person, EditActivistForm activistForm) {
    JuleicaCard juleica = null;
    if (!activistForm.isActivist()) {
      return null;
    } else if (activistForm.isOwningJuleica()) {
      juleica = new JuleicaCard(LocalDate.parse(activistForm.getJuleicaExpiryDate(),
          DateTimeFormatter.ofPattern(EditActivistForm.DATE_FORMAT, Locale.GERMAN)));

    }
    ActivistProfile profile = person.getActivistProfile().clone();
    profile.setJuleicaCard(juleica);
    return profile;
  }

}
