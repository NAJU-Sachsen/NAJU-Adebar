package de.naju.adebar.controller.persons;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import de.naju.adebar.model.persons.PersonRepository;

@Controller("personController_beta")
@RequestMapping(path = "/beta")
public class PersonController {

  private static final int PAGE_OFFSET = 2;

  private final PersonRepository personRepo;

  public PersonController(PersonRepository personRepo) {
    Assert.notNull(personRepo, "personRepo may not be null");
    this.personRepo = personRepo;
  }

  @GetMapping("/persons")
  public String showAllPersons(Model model, @PageableDefault(size = 20) Pageable pageable) {
    model.addAttribute("persons", personRepo.findAllPagedOrderByLastName(pageable));
    return "persons";
  }

}
