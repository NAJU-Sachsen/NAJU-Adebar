package de.naju.adebar.services.conversion.persons;

import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.google.common.collect.Streams;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.ReadOnlyLocalGroupRepository;
import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.validation.persons.EditActivistForm;

/**
 * Service to convert an {@link Person activist} to a corresponding {@link EditActivistForm}
 * 
 * @author Rico Bergmann
 */
@Service
public class ActivistToEditActivistFormConverter {
  private ReadOnlyLocalGroupRepository groupRepo;

  @Autowired
  public ActivistToEditActivistFormConverter(
      @Qualifier("ro_localGroupRepo") ReadOnlyLocalGroupRepository groupRepo) {
    this.groupRepo = groupRepo;
  }

  /**
   * Performs the conversion
   * 
   * @param person the activist to convert
   * @return the created form
   */
  public EditActivistForm convertToEditActivistForm(Person person) {
    if (!person.isActivist()) {
      return new EditActivistForm(false, false, null, new LinkedList<>());
    }

    ActivistProfile activistProfile = person.getActivistProfile();

    String juleicaExpiryDate = null;
    if (activistProfile.hasJuleica()) {
      juleicaExpiryDate = activistProfile.getJuleicaCard().getExpiryDate() != null
          ? activistProfile.getJuleicaCard().getExpiryDate().format(
              DateTimeFormatter.ofPattern(EditActivistForm.DATE_FORMAT, Locale.GERMAN))
          : "";
    }

    Stream<LocalGroup> groups = Streams.stream(groupRepo.findByMembersContains(person));
    return new EditActivistForm(true, activistProfile.hasJuleica(), juleicaExpiryDate,
        groups.map(LocalGroup::getId).collect(Collectors.toList()));
  }

}
