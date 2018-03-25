package de.naju.adebar.web.controller.persons;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.family.VitalRecord;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.persons.participant.SimplifiedAddParticipantForm;
import de.naju.adebar.web.validation.persons.participant.SimplifiedAddParticipantFormConverter;
import de.naju.adebar.web.validation.persons.relatives.AddParentForm;
import de.naju.adebar.web.validation.persons.relatives.AddParentFormConverter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
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
 * Handles all updates to family relations
 *
 * @author Rico Bergmann
 */
@Controller
public class FamilyRelationsController {

  private static final Logger log = LoggerFactory.getLogger(FamilyRelationsController.class);
  private static final QPerson person = QPerson.person;
  private static final String FIRST_NAME_LAST_NAME_REGEX = "^(?<firstName>[a-zA-ZöÖäÄüÜß]+) (?<lastName>[a-zA-ZöÖäÄüÜß]+)$";

  private final PersonRepository personRepo;
  private final VitalRecord vitalRecord;
  private final AddParentFormConverter addParentFormConverter;
  private final SimplifiedAddParticipantFormConverter simplifiedAddParticipantFormConverter;

  /**
   * Full constructor
   *
   * @param personRepo repository containing all persons
   * @param vitalRecord service to retain information about family relations as well as to
   *     update them
   * @param addParentFormConverter service to convert an {@link AddParentForm} to new persons
   * @param simplifiedAddParticipantFormConverter service to convert a {@link
   *     SimplifiedAddParticipantForm} to new persons. This is necessary to handle the creation of
   *     children instances correctly.
   */
  public FamilyRelationsController(PersonRepository personRepo, VitalRecord vitalRecord,
      AddParentFormConverter addParentFormConverter,
      SimplifiedAddParticipantFormConverter simplifiedAddParticipantFormConverter) {
    Assert2.noNullArguments("No parameter may be null",
        personRepo, vitalRecord, addParentFormConverter, simplifiedAddParticipantFormConverter);
    this.personRepo = personRepo;
    this.vitalRecord = vitalRecord;
    this.addParentFormConverter = addParentFormConverter;
    this.simplifiedAddParticipantFormConverter = simplifiedAddParticipantFormConverter;
  }

  /**
   * Searches for all potential relatives of a person.
   *
   * <p> The search will try to recognize queries containing the first name as well as the last name
   * of person and act accordingly.
   *
   * <p> We use this method because the persons that may match the query have to fulfill custom
   * constraints which are not present in the default search as specified in the {@link
   * PersonController} or the JSON API controllers
   *
   * @param personId the ID of the person from which should be searched. This person will never
   *     be part of the result set (which makes sense as persons may never be parents, siblings or
   *     children of themselves)
   * @param query the query. May either be the person's name, email or phone number
   * @param returnAction what to do after the results have been displayed (will most likely be
   *     evaluated by some client-side JavaScript)
   * @param returnTab the tab to display after the results have been displayed. May be left out
   *     if not appropriate.
   * @param redirAttr the attributes to use in the model after redirection as well as GET
   *     parameters for the resulting URI
   * @return a redirection to the detail page of the person the search was issued from. Correct
   *     evaluation and rendering of the resulting view will be handled through the return action as
   *     well as the redirection attributes
   */
  @GetMapping("/persons/{id}/relatives/search")
  public String searchPersons(@PathVariable("id") PersonId personId,
      @RequestParam("person-search-query") String query,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction,
      @RequestParam(value = "return-tab", defaultValue = "") String returnTab,
      RedirectAttributes redirAttr) {

    redirAttr.addFlashAttribute(
        "matchingPersons", personRepo.findAll(createSearchPredicate(personId, query)));

    redirAttr.addAttribute("search", query);
    redirAttr.addAttribute("do", returnAction);

    if (!returnTab.isEmpty()) {
      redirAttr.addAttribute("tab", returnTab);
    }

    return "redirect:/persons/" + personId;
  }

  /**
   * Adds a parent to the current person
   *
   * @param person person to "receive" the parent, automatically retained from the requested
   *     URI
   * @param form form containing the data about the new parent
   * @param errors validation errors for the form
   * @param redirAttr attributes to use in the model after redirection as well as GET parameters
   *     for the resulting url
   * @return a redirection to the person's detail page
   */
  @PostMapping("/persons/{id}/relatives/parents/add")
  public String addParent(@PathVariable("id") Person person,
      @ModelAttribute("addParentForm") @Valid AddParentForm form, BindingResult errors,
      RedirectAttributes redirAttr) {

    if (errors.hasErrors()) {

      // by passing the errors as flash attribute (with a cryptic attribute name) they will actually
      // be retained even after redirection and therefore be available in the template used after
      // redirection
      redirAttr.addFlashAttribute(
          "org.springframework.validation.BindingResult.addParentForm", errors);

      // retaining the errors however only works if we retain the form with the bad data as well
      redirAttr.addFlashAttribute("addParentForm", form);

      // the next parameters will initialize the template in a natural way (i.e. after submitting a
      // form with bad data, the new page will automatically the form again and therefore display
      // the error
      redirAttr.addAttribute("do", "add-parent");
      redirAttr.addAttribute("tab", "add-parent-show-new");

      return "redirect:/persons/" + person.getId();
    }

    Person parent = addParentFormConverter.toEntity(form);

    // if the parent should use the child's address as well and we actually want to create a new
    // person, we have to update the address manually (as the converter does not have the context
    // about the child available)
    if (form.isNewEntity() && form.isNewRetainAddress()) {
      parent.updateAddress(person.getAddress());
    }

    if (form.isNewEntity()) {
      personRepo.save(parent);
    }

    vitalRecord.addParentTo(person, parent);

    return "redirect:/persons/" + person.getId();
  }

  /**
   * Adds a sibling to the current person. Basically works the same as {@link #addParent(Person,
   * AddParentForm, BindingResult, RedirectAttributes)} just with siblings.
   *
   * @param person person to "receive" the sibling, automatically retained from the requested
   *     URI
   * @param form form containing the data about the new parent
   * @param errors validation errors for the form
   * @param redirAttr attributes to use in the model after redirection as well as GET parameters
   *     for the resulting url
   * @return a redirection to the person's detail page
   */
  @PostMapping("/persons/{id}/relatives/siblings/add")
  public String addSibling(@PathVariable("id") Person person,
      @ModelAttribute("addSiblingForm") @Valid SimplifiedAddParticipantForm form,
      BindingResult errors,
      RedirectAttributes redirAttr) {

    if (errors.hasErrors()) {
      redirAttr.addFlashAttribute(
          "org.springframework.validation.BindingResult.addSiblingForm", errors);
      redirAttr.addFlashAttribute("addSiblingForm", form);
      redirAttr.addAttribute("do", "add-sibling");
      redirAttr.addAttribute("tab", "add-sibling-show-new");
      return "redirect:/persons/" + person.getId();
    }

    Person sibling = simplifiedAddParticipantFormConverter.toEntity(form);

    if (form.isNewRetainAddress()) {
      sibling.updateAddress(person.getAddress());
    }

    if (form.isNewEntity()) {
      personRepo.save(sibling);
    }

    vitalRecord.addSiblingTo(person, sibling);

    return "redirect:/persons/" + person.getId();
  }

  /**
   * Adds a child to the current person. Basically works the same as {@link #addParent(Person,
   * AddParentForm, BindingResult, RedirectAttributes)} just with siblings.
   *
   * @param person the parent-to-be, automatically retained from the requested URI
   * @param form form containing the data about the new parent
   * @param errors validation errors for the form
   * @param redirAttr attributes to use in the model after redirection as well as GET parameters
   *     for the resulting url
   * @return a redirection to the person's detail page
   */
  @PostMapping("/persons/{id}/relatives/children/add")
  public String addChild(@PathVariable("id") Person person,
      @ModelAttribute("addChildForm") @Valid SimplifiedAddParticipantForm form,
      BindingResult errors,
      RedirectAttributes redirAttr) {

    if (errors.hasErrors()) {
      redirAttr.addFlashAttribute(
          "org.springframework.validation.BindingResult.addChildForm", errors);
      redirAttr.addFlashAttribute("addChildForm", form);
      redirAttr.addAttribute("do", "add-child");
      redirAttr.addAttribute("tab", "add-child-show-new");
      return "redirect:/persons/" + person.getId();
    }

    Person child = simplifiedAddParticipantFormConverter.toEntity(form);

    if (form.isNewRetainAddress()) {
      child.updateAddress(person.getAddress());
    }

    if (form.isNewEntity()) {
      personRepo.save(child);
    }

    vitalRecord.addChildTo(person, child);

    return "redirect:/persons/" + person.getId();
  }

  /**
   * Registers the validator for the {@link AddParentForm}
   *
   * @param binder the binder to take care of mapping the data in a web request to the
   *     corresponding Java classes
   */
  @InitBinder("addParentForm")
  protected void initBinderForAddParentForm(WebDataBinder binder) {
    binder.addValidators(addParentFormConverter);
  }

  /**
   * Registers the validator for the {@link SimplifiedAddParticipantForm}
   *
   * @param binder the binder to take care of mapping the data in a web request to the
   *     corresponding Java classes
   */
  @InitBinder("addSiblingForm")
  protected void initBinderForSimplifiedAddParticipantForm(WebDataBinder binder) {
    binder.addValidators(simplifiedAddParticipantFormConverter);
  }

  /**
   * Creates the search predicate for the current person by interpreting the search query
   *
   * @param personID the person
   * @param query the query
   * @return a predicate for filtering for the necessary persons
   */
  private Predicate createSearchPredicate(PersonId personID, String query) {
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(person.id.ne(personID));

    // if the query is for first name and last name, we do only want to filter for these
    if (createFirstNameLastNameQueryFor(predicate, query)) {
      return predicate;
    }

    // otherwise we use a much broader filter
    // it is important to use the values of phone number and email here instead of their wrapper
    // classes as the query may not be a valid email/phone number. Wrapping them by default would
    // result in an exception in that case.
    predicate.and(person.firstName.containsIgnoreCase(query)
        .or(person.lastName.containsIgnoreCase(query))
        .or(person.phoneNumber.value.eq(query))
        .or(person.email.value.eq(query)));

    return predicate;
  }

  /**
   * Analyzes the query if it is for first name and last name. If that's the case the predicate will
   * be prepared accordingly.
   *
   * @param predicate the predicate to fill (potentially)
   * @param query the query to analyze
   * @return whether the predicate was actually modified by the method
   */
  private boolean createFirstNameLastNameQueryFor(BooleanBuilder predicate, String query) {
    Pattern pattern = Pattern.compile(FIRST_NAME_LAST_NAME_REGEX);
    Matcher matcher = pattern.matcher(query);

    if (!matcher.matches()) {
      return false;
    }

    String firstName = matcher.group("firstName");
    String lastName = matcher.group("lastName");
    predicate.and(person.firstName.containsIgnoreCase(firstName));
    predicate.and(person.lastName.containsIgnoreCase(lastName));

    return true;
  }

}
