package de.naju.adebar.web.controller.persons;

import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.ActivistProfileRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.ReferentProfile;
import de.naju.adebar.model.persons.ReferentProfileRepository;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.activist.EditActivistProfileConverter;
import de.naju.adebar.web.validation.persons.activist.EditActivistProfileForm;
import de.naju.adebar.web.validation.persons.referent.AddQualificationForm;
import java.util.Collections;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Handles the rendering of the combined activist/referent profile view.
 *
 * <p> All modification methods are handled by dedicated controllers
 *
 * @author Rico Bergmann
 * @see ActivistController
 * @see ReferentController
 */
@Controller
public class ActivistReferentController {

  private final ActivistProfileRepository activistRepo;
  private final ReferentProfileRepository referentRepo;
  private final QualificationRepository qualificationRepo;
  private final EditActivistProfileConverter activistProfileConverter;

  /**
   * Full constructor.
   *
   * <p> The controller needs access to the repositories in order to check whether a person is an
   * activist or referent. Qualifications are needed to render the 'add qualification' form.
   *
   * @param activistRepo repository containing all activist profiles
   * @param referentRepo repository containing all referent profiles
   * @param qualificationRepo repository containing all qualifications
   * @param activistProfileConverter service to create an {@link EditActivistProfileForm} from a
   *     corresponding {@link ActivistProfile}
   */
  public ActivistReferentController(
      ActivistProfileRepository activistRepo,
      ReferentProfileRepository referentRepo,
      QualificationRepository qualificationRepo,
      EditActivistProfileConverter activistProfileConverter) {

    Assert2.noNullArguments("No parameter may be null", //
        activistRepo, referentRepo, qualificationRepo, //
        activistProfileConverter);

    this.activistRepo = activistRepo;
    this.referentRepo = referentRepo;
    this.qualificationRepo = qualificationRepo;
    this.activistProfileConverter = activistProfileConverter;
  }

  /**
   * Renders the activist/referent profile page.
   *
   * @param person the person to display
   * @param model model to put the data to render into
   * @return the profile page
   */
  @GetMapping("/persons/{id}/activist-referent")
  public String showActivistReferentTab(@PathVariable("id") Person person, Model model) {

    model.addAttribute("person", person);
    model.addAttribute("tab", "activist-referent");

    fillModelWithActivistData(person.getId(), model);
    fillModelWithReferentData(person.getId(), model);

    return "persons/personDetails";
  }

  /**
   * Puts all activist related data into the model
   *
   * @param profileId the ID of the person to display.
   * @param model the model
   */
  private void fillModelWithActivistData(PersonId profileId, Model model) {

    // We may not request the profile directly as parameter, because the person may not be an
    // activist.
    ActivistProfile activistProfile = activistRepo.findById(profileId).orElse(null);

    // If the profile is not present (because the person is no activist) we will add null, which is
    // fine as well.
    model.addAttribute("activist", activistProfile);

    // As the activist may not be present, we need to add the person's ID, too.
    // This is necessary to submit the form to make the person an activist to the right
    // path.
    model.addAttribute("personId", profileId);

    // if we are redirected after the editing of an activist form failed, we already have the form
    // which in this case contains the "bad" data. Therefore we will continue to use it to display
    // error messages in the template
    if (!model.containsAttribute("editActivistForm")) {
      model.addAttribute("editActivistForm", activistProfileConverter.toForm(activistProfile));
    }

  }

  /**
   * Puts all referent related data into the model. Works analogous to {@link
   * #fillModelWithActivistData(PersonId, Model)} for the most part.
   *
   * @param personId the ID of the person
   * @param model the model
   */
  private void fillModelWithReferentData(PersonId personId, Model model) {

    // We may not request the profile directly as parameter, because the person may not be a
    // referent.
    ReferentProfile profile = referentRepo.findById(personId).orElse(null);

    // If the profile is not present (because the person is no referent) we will add null, which is
    // fine as well.
    model.addAttribute("referent", profile);

    // As the referent may not be present, we need to add the person's ID, too.
    // This is necessary to submit the form to make the person an activist to the right
    // path.
    model.addAttribute("personId", personId);

    // if we are redirected after the editing of an add qualification form failed, we already have
    // the form which in this case contains the "bad" data. Therefore we will continue to use it to
    // display error messages in the template
    if (!model.containsAttribute("addQualificationForm")) {
      model.addAttribute("addQualificationForm", new AddQualificationForm());
    }

    // This may seem counter-intuitive: the qualificationsToAdd contain all those qualifications the
    // referent does not have yet (and may therefore still be added). Therefore all available
    // qualifications should be added if the profile is null. However a null-profile means the
    // person is no referent at all and we do display something completely different in this case.
    Iterable<Qualification> qualificationsToAdd = profile != null //
        ? qualificationRepo.findAllQualificationsNotPossessedBy(personId.toString()) //
        : Collections.emptyList();
    model.addAttribute("qualificationsToAdd", qualificationsToAdd);

  }

}
