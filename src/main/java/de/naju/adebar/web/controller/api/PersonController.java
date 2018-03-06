package de.naju.adebar.web.controller.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.api.data.SimplePersonJSON;
import de.naju.adebar.api.forms.FilterPersonForm;
import de.naju.adebar.api.util.DataFormatter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonManager;
import de.naju.adebar.services.conversion.persons.FilterToPredicateConverter;
import de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator;

/**
 * REST controller to access person data.
 *
 * @author Rico Bergmann
 * @see <a href= "https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_personController")
@RequestMapping("/api/persons")
public class PersonController {
  private PersonManager personManager;
  private DataFormatter dataFormatter;
  private FilterToPredicateConverter predicateConverter;
  private PersonSearchPredicateCreator searchPredicateCreator;

  @Autowired
  public PersonController(PersonManager personManager, DataFormatter dataFormatter,
      FilterToPredicateConverter predicateConverter,
      PersonSearchPredicateCreator searchPredicateCreator) {
    Object[] params = {personManager, dataFormatter, predicateConverter, searchPredicateCreator};
    Assert.noNullElements(params,
        "No parameter may be null, but at least one was: " + Arrays.toString(params));
    this.personManager = personManager;
    this.dataFormatter = dataFormatter;
    this.predicateConverter = predicateConverter;
    this.searchPredicateCreator = searchPredicateCreator;
  }

  /**
   * Searches for persons whose name, address or email match the query given
   *
   * @param query
   * @return
   */
  @RequestMapping("/defaultSearch")
  public Iterable<SimplePersonJSON> sendMatches(@RequestParam("query") String query) {
    query = query.trim();
    Predicate predicate = searchPredicateCreator.createPredicate(query);
    List<Person> matches = personManager.repository().findAll(predicate);
    matches.sort((p1, p2) -> p1.getLastName().compareTo(p2.getLastName()));
    return matches.stream().map(SimplePersonJSON::new).collect(Collectors.toList());
  }

  /**
   * Performs a simplified search for persons with specific data. If a criteria should not be
   * included it may be left empty or even {@code null}
   *
   * @param firstName the person's first name if required
   * @param lastName the person's last name if required
   * @param city the person's address if required
   * @return all persons who matched the given criteria
   */
  @RequestMapping("/simpleSearch")
  public Iterable<SimplePersonJSON> sendMatchingPersons(@RequestParam("firstname") String firstName,
      @RequestParam("lastname") String lastName, @RequestParam("city") String city) {
    firstName = dataFormatter.adjustFirstLetterCase(firstName);
    lastName = dataFormatter.adjustFirstLetterCase(lastName);
    city = dataFormatter.adjustFirstLetterCase(city);

    List<Person> matches = personManager.repository()
        .findAll(predicateConverter.fromFields(firstName, lastName, city));
    List<SimplePersonJSON> result = new ArrayList<>(matches.size());
    matches.forEach(m -> result.add(new SimplePersonJSON(m)));

    return result;
  }

  /**
   * Performs a simplified search for activists with specific data. If a criteria should not be
   * included it may be left empty or even {@code null}
   *
   * @param firstName the activist's first name if required
   * @param lastName the activist's last name if required
   * @param city the activist's address if required
   * @return all activists who matched the given criteria
   */
  @RequestMapping("/activists/simpleSearch")
  public Iterable<SimplePersonJSON> sendMatchingActivists(
      @RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName,
      @RequestParam("city") String city) {
    firstName = dataFormatter.adjustFirstLetterCase(firstName);
    lastName = dataFormatter.adjustFirstLetterCase(lastName);
    city = dataFormatter.adjustFirstLetterCase(city);

    List<Person> matches = personManager.repository()
        .findAll(predicateConverter.activistsFromFields(firstName, lastName, city));
    List<SimplePersonJSON> result = new ArrayList<>(matches.size());
    matches.forEach(m -> result.add(new SimplePersonJSON(m)));

    return result;
  }

  /**
   * Performs a full-fledged search for persons
   *
   * @param form form containing the search criteria
   * @return all matching persons
   */
  @RequestMapping(//
      value = "/search", //
      consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,
          MediaType.APPLICATION_JSON_UTF8_VALUE})
  public Iterable<SimplePersonJSON> filterPersons(FilterPersonForm form) {

    // TODO: directly use predicate here
    // return projection instead of some weird custom form

    dataFormatter.adjustFilterPersonForm(form);

    List<Person> matches =
        personManager.repository().findAll(predicateConverter.fromFields(form.getFirstName(),
            form.getLastName(), form.getCity(), form.isActivist(), form.isReferent()));
    List<SimplePersonJSON> result = new ArrayList<>(matches.size());
    matches.forEach(m -> result.add(new SimplePersonJSON(m)));

    return result;
  }

}
