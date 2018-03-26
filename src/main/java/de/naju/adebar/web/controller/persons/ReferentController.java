package de.naju.adebar.web.controller.persons;

import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.ReferentProfile;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.web.validation.persons.referent.AddQualificationForm;
import de.naju.adebar.web.validation.persons.referent.AddQualificationFormConverter;

/**
 * Handles all requests related to referent profiles of persons as well as their lifecycle.
 *
 * @author Rico Bergmann
 * @see ReferentProfile
 * @see ActivistReferentController
 */
@Controller
@Transactional
public class ReferentController {

  private final AddQualificationFormConverter addQualificationFormConverter;

  /**
   * Full constructor.
   *
   * @param personRepo repository containing all available persons. This is necessary to make a
   *        person a referent.
   * @param addQualificationFormConverter service to convert an {@link AddQualificationForm} to a
   *        corresponding {@link Qualification} and vice-versa.
   */
  public ReferentController(AddQualificationFormConverter addQualificationFormConverter) {

    Assert.notNull(addQualificationFormConverter, "addQualificationFormConverter may not be null");

    this.addQualificationFormConverter = addQualificationFormConverter;
  }

  /**
   * Adds a qualification to a referent.
   *
   * @param person the referent
   * @param form form containing the qualification to add
   * @param result errors within the form
   * @param redirAttr attributes to use after redirection
   * @return a redirection to the person's activist/referent profile
   */
  @PostMapping("/persons/{pid}/referent-profile/qualifications/add")
  public String addQualification(@PathVariable("pid") Person person,
      @ModelAttribute("addQualificationForm") @Valid AddQualificationForm form,
      BindingResult result, RedirectAttributes redirAttr) {

    Assert.isTrue(person.isReferent(), "Person must be a referent " + person);

    ReferentProfile profile = person.getReferentProfile();
    if (result.hasErrors()) {
      redirAttr.addFlashAttribute("addQualificationForm", form);
      redirAttr.addFlashAttribute(
          "org.springframework.validation.BindingResult.addQualificationForm", result);
      redirAttr.addAttribute("form", "add-qualification");
      redirAttr.addAttribute("tab", form.getAddTypeShort());
    } else {
      profile.addQualification(addQualificationFormConverter.toEntity(form));
    }

    return "redirect:/persons/" + person.getId() + "/activist-referent";
  }

  /**
   * Removes a qualification from a referent
   *
   * @param person the referent
   * @param qualification the qualification
   * @return a redirection to the person's activist/referent profile
   */
  @PostMapping("/persons/{pid}/referent-profile/qualifications/remove")
  public String removeQualification(@PathVariable("pid") Person person,
      @RequestParam("qualification-id") Qualification qualification) {

    Assert.isTrue(person.isReferent(), "Person must be a referent " + person);

    ReferentProfile profile = person.getReferentProfile();
    profile.removeQualification(qualification);

    return "redirect:/persons/" + person.getId() + "/activist-referent";
  }

  /**
   * Turns the given person into a referent
   *
   * @param person the referent to-be
   * @return a redirection to the person's activist/referent profile
   */
  @PostMapping("/persons/{pid}/referent-profile/create")
  public String makeReferent(@PathVariable("pid") Person person) {
    person.makeReferent();
    return "redirect:/persons/" + person.getId() + "/activist-referent";
  }

  /**
   * Registers the {@link AddQualificationFormConverter} as validator for this controller
   *
   * @param binder the binder which accepts the requests and delegates to the validator
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(addQualificationFormConverter);
  }

}
