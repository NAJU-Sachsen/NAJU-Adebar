package de.naju.adebar.web.controller.persons;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;
import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.querydsl.QueryPredicateGeneratingVisitor;
import de.naju.adebar.app.persons.PersonDataProcessor;
import de.naju.adebar.app.persons.filter.PersonFieldToPathConverter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.persons.filter.FilterLabellingService;
import de.naju.adebar.web.model.persons.filter.FilterRenderingService;
import de.naju.adebar.web.model.persons.filter.PersonFilter;
import de.naju.adebar.web.model.persons.filter.WebFilterForm;
import de.naju.adebar.web.services.persons.filter.FilterInitializer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FilterController {

	private static final String EXCLUDE_MARKETING_OPT_OUT = "exclude-marketing-optout";
	private static final String USE_PARENTS = "use-parents";
	private static final Logger log = LoggerFactory.getLogger(FilterController.class);

	private final PersonFilter filter;
	private final FilterRenderingService renderingService;
	private final FilterLabellingService labellingService;
	private final FilterInitializer filterInitializer;
	private final PersonFieldToPathConverter pathConverter;
	private final EntityManager entityManager;
	private final PersonDataProcessor personDataProcessor;

	public FilterController(PersonFilter filter,
			FilterRenderingService renderingService,
			FilterLabellingService labellingService,
			FilterInitializer filterInitializer,
			PersonFieldToPathConverter pathConverter,
			EntityManager entityManager,
			PersonDataProcessor personDataProcessor) {

		Assert2.noNullArguments("No argument may be null", filter, renderingService, labellingService,
				filterInitializer, pathConverter, entityManager, personDataProcessor);
		this.filter = filter;
		this.renderingService = renderingService;
		this.labellingService = labellingService;
		this.filterInitializer = filterInitializer;
		this.pathConverter = pathConverter;
		this.entityManager = entityManager;
		this.personDataProcessor = personDataProcessor;
	}

	@GetMapping("/persons/filter")
	public String showPersonFilter(Model model) {
		final WebFilterForm form = filter.getForm();
		model.addAttribute("renderingService", renderingService);
		model.addAttribute("labellingService", labellingService);
		model.addAttribute("filterForm", form);
		return "persons/filterPersons";
	}

	@PostMapping("/persons/filter")
	public String filterPersons(@RequestParam MultiValueMap<String, String> postData, Model model) {

		AbstractFilter resultingFilter = filterInitializer.initializeFrom(filter, postData);

		QueryPredicateGeneratingVisitor<Person> predicateGenerator = new QueryPredicateGeneratingVisitor<>(
				pathConverter, entityManager,
				QPerson.person);
		resultingFilter.accept(predicateGenerator);

		final JPAQuery<Person> query = predicateGenerator.getResultingQuery();
		List<Person> result = query.fetch();

		result = postProcessFilterResults(result, postData);

		model.addAttribute("persons", result);
		model.addAttribute("emailAddresses", personDataProcessor.extractEmailAddressesAsString(result, ";"));

		return "persons/filterPersonsResults";
	}

	private List<Person> postProcessFilterResults(
			List<Person> results,
			MultiValueMap<String, String> configData) {

		Stream<Person> intermediateResults = results.stream();

		if (configData.containsKey(EXCLUDE_MARKETING_OPT_OUT)) {
			intermediateResults = intermediateResults.filter(person -> !person.optedOutOfMarketing());
		}

		if (configData.containsKey(USE_PARENTS)) {
			intermediateResults = intermediateResults //
					.flatMap(person -> Lists.newArrayList(person.getParents()).stream()) //
					.distinct();
		}

		return intermediateResults.collect(Collectors.toList());
	}

}
