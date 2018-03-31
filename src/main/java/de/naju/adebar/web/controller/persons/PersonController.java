package de.naju.adebar.web.controller.persons;

import de.naju.adebar.app.search.persons.PersonSearchServer;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.family.VitalRecord;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.EditPersonForm;
import de.naju.adebar.web.validation.persons.EditPersonFormConverter;
import de.naju.adebar.web.validation.persons.participant.SimplifiedAddParticipantForm;
import de.naju.adebar.web.validation.persons.relatives.AddParentForm;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles all requests directly related to persons. This includes displaying their details,
 * searching and creating profiles, etc.
 *
 * @author Rico Bergmann
 */
@Controller
public class PersonController {

  private static final String PERSON_OVERVIEW_TEMPLATE = "persons/overview";
  private static final String EDIT_PERSON_FORM = "editPersonForm";

  private final PersonRepository personRepo;
  private final PersonSearchServer searchServer;
  private final EditPersonFormConverter editPersonFormConverter;
  private final VitalRecord vitalRecord;

  /**
   * Full constructor. No parameter may be {@code null}
   *
   * @param personRepo repository containing all available persons
   * @param searchServer service to search persons based on queries
   * @param editPersonFormConverter service to convert a {@link EditPersonForm} to a
   *     corresponding {@link Person} and vice-versa
   */
  public PersonController(PersonRepository personRepo, //
      PersonSearchServer searchServer, //
      EditPersonFormConverter editPersonFormConverter, //
      VitalRecord vitalRecord) {

    Assert2.noNullArguments("No parameter may be null", //
        personRepo, searchServer, editPersonFormConverter, vitalRecord);

    this.personRepo = personRepo;
    this.searchServer = searchServer;
    this.editPersonFormConverter = editPersonFormConverter;
    this.vitalRecord = vitalRecord;
  }

  /**
   * Renders a list of all persons
   *
   * @param model model to put the data to render into
   * @param pageable the requested page. As there may be quite many, will not display all
   *     persons at once but rather show them in smaller slices. Navigation will be offered to move
   *     to the next/previous slice.
   * @return the person overview template
   */
  @GetMapping("/persons")
  public String showAllPersons(Model model, @PageableDefault(size = 20) Pageable pageable) {
    model.addAttribute("persons", personRepo.findAllPagedOrderByLastName(pageable));
    return PERSON_OVERVIEW_TEMPLATE;
  }

  /**
   * Renders a list of all persons matching a search criteria.
   * <p>
   * Persons will match the query if either their first name, last name, e-mail address or the city
   * they live in matches.
   *
   * @param query the search query
   * @param pageable the requested result page. As with {@link #showAllPersons(Model, Pageable)}
   *     the result will not be presented all at once.
   * @param model model to put the data to render into
   * @return the person overview template, adapted to the search
   */
  @GetMapping("/persons/search")
  public String searchPersons(@RequestParam("query") String query,
      @PageableDefault(size = 20) Pageable pageable, Model model) {
    model.addAttribute("persons", searchServer.runQuery(query.trim(), pageable));

    return PERSON_OVERVIEW_TEMPLATE;
  }

  /**
   * Renders the template to filter persons
   *
   * @return the filter persons template
   */
  @GetMapping("/persons/filter")
  public String filterPersons() {
    return PERSON_OVERVIEW_TEMPLATE;
  }

  /**
   * Renders the details page for a specific person.
   * <p>
   * This includes general information, the participant profile (if applicable) as well as
   * information about relatives
   *
   * @param person the person to display
   * @param model model to put the data to render into
   * @return the person detail page
   */
  @GetMapping("/persons/{pid}")
  public String showPersonDetailsOverview(@PathVariable("pid") Person person, Model model) {

    model.addAttribute("tab", "general");
    model.addAttribute("person", person);

    // depending on the page we come from, some forms may already be present
    // (if we got redirected due to an error when processing the form). To ensure the correct
    // workings of the validation messages in the template, we will not add the form in that case

    if (!model.containsAttribute(EDIT_PERSON_FORM)) {
      model.addAttribute(EDIT_PERSON_FORM, editPersonFormConverter.toForm(person));
    }

    if (!model.containsAttribute("addParentForm")) {
      model.addAttribute("addParentForm", new AddParentForm(person));
    }

    if (!model.containsAttribute("addSiblingForm")) {
      model.addAttribute("addSiblingForm", new SimplifiedAddParticipantForm(person));
    }

    if (!model.containsAttribute("addChildrenForm")) {
      model.addAttribute("addChildForm", new SimplifiedAddParticipantForm(person));
    }

    model.addAttribute("relatives", vitalRecord.findRelativesOf(person));

    return "persons/personDetails";
  }

  /**
   * Edits the data of a specific person according to the submitted form
   *
   * @param person the person to update
   * @param data the data to use for the update
   * @return the person detail page
   */
  @PostMapping("/persons/{pid}/update")
  public String updatePersonalInformation(@PathVariable("pid") Person person,
      @ModelAttribute("editPersonForm") @Valid EditPersonForm data, BindingResult errors,
      RedirectAttributes redirAttr) {

    if (errors.hasErrors()) {
      redirAttr.addFlashAttribute( //
          "org.springframework.validation.BindingResult.editPersonForm", errors);
      redirAttr.addFlashAttribute(EDIT_PERSON_FORM, data);
      redirAttr.addAttribute("form", "edit-person");
      return "redirect:/persons/" + person.getId();
    }

    editPersonFormConverter.applyFormToEntity(data, person);
    personRepo.save(person);

    return "redirect:/persons/" + person.getId();
  }

  @InitBinder("editPersonForm")
  protected void initBinder(WebDataBinder binder) {
    binder.addValidators(editPersonFormConverter);
  }

}
