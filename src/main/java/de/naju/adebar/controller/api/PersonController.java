package de.naju.adebar.controller.api;

import de.naju.adebar.api.data.SimplePersonJSON;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.app.human.PersonManager;
import de.naju.adebar.app.human.filter.*;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * REST controller to access person data.
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_personController")
@RequestMapping("/api/persons")
public class PersonController {
    private PersonManager personManager;

    @Autowired
    public PersonController(PersonManager personManager) {
        Assert.notNull(personManager, "Person manager may not be null");
        this.personManager = personManager;
    }

    /**
     * Performs a simplified search for persons with specific data. If a criteria should not be included it may be left empty or even {@code null}
     * @param firstName the person's first name if required
     * @param lastName the person's last name if required
     * @param city the person's address if required
     * @return all persons who matched the given criteria
     */
    @RequestMapping("/simpleSearch")
    public Iterable<SimplePersonJSON> sendMatchingPersons(@RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName, @RequestParam("city") String city) {
        Address address = new Address("", "", city);
        List<Person> persons = personManager.repository().streamAll().collect(Collectors.toList());
        PersonFilterBuilder filterBuilder = new PersonFilterBuilder(persons.stream());
        filterBuilder
                .applyFilter(new NameFilter(firstName, lastName))
                .applyFilter(new AddressFilter(address, MatchType.IF_DEFINED));
        Stream<SimplePersonJSON> jsonObjects = filterBuilder.resultingStream().map(SimplePersonJSON::new);
        return jsonObjects.collect(Collectors.toList());
    }

    /**
     * Performs a simplified search for activists with specific data. If a criteria should not be included it may be left empty or even {@code null}
     * @param firstName the activist's first name if required
     * @param lastName the activist's last name if required
     * @param city the activist's address if required
     * @return all activists who matched the given criteria
     */
    @RequestMapping("/activists/simpleSearch")
    public Iterable<SimplePersonJSON> sendMatchingActivists(@RequestParam("firstname") String firstName, @RequestParam("lastname") String lastName, @RequestParam("city") String city) {
        Address address = new Address("", "", city);
        List<Person> activists = personManager.repository().streamAll().collect(Collectors.toList());
        PersonFilterBuilder filterBuilder = new PersonFilterBuilder(activists.stream());
        filterBuilder
                .applyFilter(new ActivistFilter(FilterType.ENFORCE))
                .applyFilter(new NameFilter(firstName, lastName))
                .applyFilter(new AddressFilter(address, MatchType.IF_DEFINED));
        Stream<SimplePersonJSON> jsonObjects = filterBuilder.resultingStream().map(SimplePersonJSON::new);
        return jsonObjects.collect(Collectors.toList());
    }

}
