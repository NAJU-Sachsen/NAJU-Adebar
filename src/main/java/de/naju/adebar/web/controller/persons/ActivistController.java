package de.naju.adebar.web.controller.persons;

import de.naju.adebar.model.persons.ActivistProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.activist.EditActivistProfileConverter;
import de.naju.adebar.web.validation.persons.activist.EditActivistProfileForm;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles all requests to activist profiles
 *
 * @author Rico Bergmann
 * @see ActivistProfile
 * @see ActivistReferentController
 */
@Controller
@Transactional
public class ActivistController {

  private final PersonRepository personRepo;
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
  public ActivistController(PersonRepository personRepo,
      EditActivistProfileConverter profileConverter) {
    Assert2.noNullArguments("No parameter may be null", personRepo, profileConverter);
    this.personRepo = personRepo;
    this.profileConverter = profileConverter;
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
    return "redirect:/persons/" + person.getId() + "/activist-referent";
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
    return "redirect:/persons/" + personId + "/activist-referent";
  }

}
