package de.naju.adebar.web.controller.persons;

import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.EditPersonForm;
import de.naju.adebar.web.validation.persons.EditPersonFormConverter;
import de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PersonController {

  private final PersonRepository personRepo;
  private final PersonSearchPredicateCreator searchPredicateCreator;
  private final EditPersonFormConverter editPersonFormConverter;

  public PersonController(PersonRepository personRepo, //
      PersonSearchPredicateCreator predicateCreator, //
      EditPersonFormConverter editPersonFormConverter) {

    Assert2.noNullArguments("No parameter may be null", //
        personRepo, predicateCreator, editPersonFormConverter);

    this.personRepo = personRepo;
    this.searchPredicateCreator = predicateCreator;
    this.editPersonFormConverter = editPersonFormConverter;
  }

  @GetMapping("/persons")
  public String showAllPersons(Model model, @PageableDefault(size = 20) Pageable pageable) {
    model.addAttribute("persons", personRepo.findAllPagedOrderByLastName(pageable));
    return "persons/overview";
  }

  @GetMapping("/persons/search")
  public String searchPersons(@RequestParam("query") String query,
      @PageableDefault(size = 20) Pageable pageable, Model model) {
    Predicate predicate = searchPredicateCreator.createPredicate(query.trim());
    model.addAttribute("persons", personRepo.findAll(predicate, pageable));

    return "persons/overview";
  }

  @GetMapping("/persons/add")
  public String showAddPersonView(Model model) {

    return "persons/addPerson";
  }

  @PostMapping("/persons/add")
  public String addPerson(RedirectAttributes redirAttr) {
    Person person = null;
    return "redirect:/persons/" + person.getId();
  }

  @GetMapping("/persons/filter")
  public String filterPersons(Model model) {
    return "persons/overview";
  }

  @GetMapping("/persons/{pid}")
  public String showPersonDetailsOverview(@PathVariable("pid") Person person, Model model) {

    model.addAttribute("person", person);
    model.addAttribute("editPersonForm", editPersonFormConverter.toForm(person));

    if (!model.containsAttribute("tab")) {
      // The tab becomes the section of the template which will be displayed initially.
      // It may already be set if we are being redirected so we only add it if it is not present.
      // Otherwise another part of the template will be displayed.
      // E.g. if the activist profile was edited, tab="activist" will be set before the redirect. As
      // we leave the tab untouched, the activist profile-view will become active in the template,
      // resulting in a nicer user-experience
      model.addAttribute("tab", "general");
    }

    return "persons/personDetails";
  }

  @PostMapping("/persons/{pid}/update")
  public String updatePersonalInformation(@PathVariable("pid") Person person,
      @ModelAttribute("editPersonForm") EditPersonForm data) {

    editPersonFormConverter.applyFormToEntity(data, person);
    personRepo.save(person);

    return "redirect:/persons/" + person.getId();
  }

}
