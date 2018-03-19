package de.naju.adebar.web.controller.persons;

import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.ActivistProfileRepository;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.activist.EditActivistProfileConverter;
import de.naju.adebar.web.validation.persons.activist.EditActivistProfileForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles all requests to activist profiles
 *
 * @author Rico Bergmann
 * @see ActivistProfile
 */
@Controller
public class ActivistController {

  private final PersonRepository personRepo;
  private final ActivistProfileRepository activistRepo;
  private final EditActivistProfileConverter profileConverter;

  /**
   * Full constructor.
   *
   * @param personRepo repository containing the person instances. This is necessary to update
   *     persons if their activist profiles are being edited.
   * @param activistRepo repository containing the person's activists. While this repo is not
   *     strictly necessary it makes certain operations a lot easier.
   * @param profileConverter service to convert an {@link ActivistProfile} to a corresponding
   *     {@link EditActivistProfileForm} instance and vice-versa.
   */
  public ActivistController(PersonRepository personRepo, ActivistProfileRepository activistRepo,
      EditActivistProfileConverter profileConverter) {
    Assert2.noNullArguments("No parameter may be null", personRepo, activistRepo, profileConverter);
    this.personRepo = personRepo;
    this.activistRepo = activistRepo;
    this.profileConverter = profileConverter;
  }

  /**
   * Renders the activist details fragment for a specific person.
   *
   * @param profileId the ID of the person to display.
   * @param model model to put the data to render into
   * @return the fragment code for the activist profile. Rendering will be handled by Spring and
   *     Thymeleaf
   */
  @GetMapping("/persons/{pid}/activist-profile")
  public String showActivistDetails(@PathVariable("pid") PersonId profileId,
      Model model) {

    // We may not request the profile directly as parameter, because the person may not be an
    // activist.
    ActivistProfile activistProfile = activistRepo.findById(profileId).orElse(null);

    // If the profile is not present (because the person is no activist) we will add null, which is
    // fine as well.
    model.addAttribute("activist", activistProfile);

    // As the activist may not be presents, we need to add the person's ID, too.
    // This is necessary to submit the form to actually make the person an activist to the right
    // path.
    model.addAttribute("personId", profileId);

    model.addAttribute("editActivistForm", profileConverter.toForm(activistProfile));

    return "persons/activistProfile :: profile";
  }

  /**
   * Turns the given person into an activist.
   *
   * @param person the person
   * @return a redirection to the person's detail page
   */
  @PostMapping("/persons/{pid}/activist-profile/create")
  public String createActivistProfile(@PathVariable("pid") Person person,
      RedirectAttributes redirAttr) {
    person.makeActivist();
    personRepo.save(person);
    return "redirect:/persons/" + person.getId();
  }

  /**
   * Updates an activist profile according to the submitted data.
   *
   * @param personId the ID of the profile to update
   * @param form the form containing the updated activist data
   * @param redirAttr attributes to put in the modal after redirection
   * @return a redirection to the person details page
   */
  @PostMapping("/persons/{pid}/activist-profile/update")
  public String updateActivistProfile(@PathVariable("pid") PersonId personId,
      @ModelAttribute("editActivistForm") EditActivistProfileForm form,
      RedirectAttributes redirAttr) {
    Person activist = personRepo.findOne(personId);
    profileConverter.applyFormToEntity(form, activist.getActivistProfile());
    personRepo.save(activist);

    redirAttr.addAttribute("tab", "activist-referent");
    return "redirect:/persons/" + personId;
  }

}
