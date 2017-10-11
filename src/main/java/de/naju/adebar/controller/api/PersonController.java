package de.naju.adebar.controller.api;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.naju.adebar.api.data.SimplePersonJSON;
import de.naju.adebar.api.forms.FilterPersonForm;
import de.naju.adebar.api.util.DataFormatter;
import de.naju.adebar.api.util.PersonFilterDataExtractor;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.app.human.filter.ActivistFilter;
import de.naju.adebar.app.human.filter.AddressFilter;
import de.naju.adebar.app.human.filter.NameFilter;
import de.naju.adebar.app.human.filter.PersonFilterBuilder;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Person;

/**
 * REST controller to access person data.
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_personController")
@RequestMapping("/api/persons")
public class PersonController {
    private PersonManager personManager;
    private DataFormatter dataFormatter;
    private PersonFilterDataExtractor filterExtractor;

    @Autowired
    public PersonController(PersonManager personManager, DataFormatter dataFormatter, PersonFilterDataExtractor filterExtractor) {
        Object[] params = {personManager, dataFormatter, filterExtractor};
        Assert.noNullElements(params, "No parameter may be null, but at least one was: " + Arrays.toString(params));
        this.personManager = personManager;
        this.dataFormatter = dataFormatter;
        this.filterExtractor = filterExtractor;
    }

    /**
     * Performs a simplified search for persons with specific data. If a criteria should not be included it may be left empty or even {@code null}
     * @param firstName the person's first name if required
     * @param lastName the person's last name if required
     * @param city the person's address if required
     * @return all persons who matched the given criteria
     */
    @RequestMapping("/simpleSearch")
    @Transactional
    public Iterable<SimplePersonJSON> sendMatchingPersons(@RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName, @RequestParam("city") String city) {
    	firstName = dataFormatter.adjustFirstLetterCase(firstName);
    	lastName = dataFormatter.adjustFirstLetterCase(lastName);
    	city = dataFormatter.adjustFirstLetterCase(city);

    	Address address = new Address("", "", city);
        Stream<Person> persons = personManager.repository().streamAll();
        PersonFilterBuilder filterBuilder = new PersonFilterBuilder(persons);
        filterBuilder
                .applyFilter(new NameFilter(firstName, lastName))
                .applyFilter(new AddressFilter(address, MatchType.IF_DEFINED));
        Stream<SimplePersonJSON> jsonObjects = filterBuilder.resultingStream().map(SimplePersonJSON::new);
        List<SimplePersonJSON> result = jsonObjects.collect(Collectors.toList());

        persons.close();
        jsonObjects.close();

        return result;
    }

    /**
     * Performs a simplified search for activists with specific data. If a criteria should not be included it may be left empty or even {@code null}
     * @param firstName the activist's first name if required
     * @param lastName the activist's last name if required
     * @param city the activist's address if required
     * @return all activists who matched the given criteria
     */
    @RequestMapping("/activists/simpleSearch")
    @Transactional
    public Iterable<SimplePersonJSON> sendMatchingActivists(@RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName, @RequestParam("city") String city) {
    	firstName = dataFormatter.adjustFirstLetterCase(firstName);
    	lastName = dataFormatter.adjustFirstLetterCase(lastName);
    	city = dataFormatter.adjustFirstLetterCase(city);

    	Address address = new Address("", "", city);
    	Stream<Person> activists = personManager.repository().streamAll();
        PersonFilterBuilder filterBuilder = new PersonFilterBuilder(activists);
        filterBuilder
                .applyFilter(new ActivistFilter(FilterType.ENFORCE))
                .applyFilter(new NameFilter(firstName, lastName))
                .applyFilter(new AddressFilter(address, MatchType.IF_DEFINED));
        Stream<SimplePersonJSON> jsonObjects = filterBuilder.resultingStream().map(SimplePersonJSON::new);
        List<SimplePersonJSON> result = jsonObjects.collect(Collectors.toList());

        activists.close();
        jsonObjects.close();

        return result;
    }

    /**
     * Performs a full-fledged search for persons
     * @param form form containing the search criteria
     * @return all matching persons
     */
    @RequestMapping(value = "/search", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Transactional
    public Iterable<SimplePersonJSON> filterPersons(FilterPersonForm form) {
    	dataFormatter.adjustFilterPersonForm(form);
    	Stream<Person> persons = personManager.repository().streamAll();
    	PersonFilterBuilder filterBuilder = new PersonFilterBuilder(persons);
    	filterExtractor.extractAllFilters(form).forEach(filterBuilder::applyFilter);

    	Stream<Person> matches = filterBuilder.resultingStream();
    	List<SimplePersonJSON> result = matches.map(SimplePersonJSON::new).collect(Collectors.toList());

    	persons.close();
    	matches.close();

    	return result;
    }

}
