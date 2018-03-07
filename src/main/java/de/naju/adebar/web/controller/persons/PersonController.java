package de.naju.adebar.web.controller.persons;

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
import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.EditPersonForm;
import de.naju.adebar.web.validation.persons.EditPersonFormConverter;
import de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator;

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
    return "persons";
  }

  @GetMapping("/persons/search")
  public String searchPersons(@RequestParam("query") String query,
      @PageableDefault(size = 20) Pageable pageable, Model model) {
    Predicate predicate = searchPredicateCreator.createPredicate(query.trim());
    model.addAttribute("persons", personRepo.findAll(predicate, pageable));

    return "persons";
  }

  @GetMapping("/persons/add")
  public String showAddPersonView(Model model) {

    return "addPerson";
  }

  @PostMapping("/persons/add")
  public String addPerson(RedirectAttributes redirAttr) {
    Person person = null;
    return "redirect:/persons/" + person.getId();
  }

  @GetMapping("/persons/filter")
  public String filterPersons(Model model) {
    return "persons";
  }

  @GetMapping("/persons/{pid}")
  public String showPersonDetailsOverview(@PathVariable("pid") Person person, Model model) {

    model.addAttribute("person", person);
    model.addAttribute("editPersonForm", editPersonFormConverter.toForm(person));

    return "personDetails";
  }

  @PostMapping("/persons/{pid}/update")
  public String updatePersonalInformation(@PathVariable("pid") Person person,
      @ModelAttribute("editPersonForm") EditPersonForm data) {

    editPersonFormConverter.applyFormToEntity(data, person);
    personRepo.save(person);

    return "redirect:/persons/" + person.getId();
  }

}
