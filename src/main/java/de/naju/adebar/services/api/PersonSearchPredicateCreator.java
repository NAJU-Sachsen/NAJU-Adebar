package de.naju.adebar.services.api;

import org.springframework.stereotype.Service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import de.naju.adebar.model.human.QPerson;

/**
 * Service to create predicates for searches
 * @author Rico Bergmann
 */
@Service
public class PersonSearchPredicateCreator {

	/**
	 * The predicate will filter for all persons whose name, address (city) or email is like the query specified
	 * @param query the criteria
	 * @return the predicate
	 */
	public Predicate createPredicate(String query) {
		QPerson person = QPerson.person;
		BooleanBuilder predicate = new BooleanBuilder();

		String[] args = query.split(" ");

		for (String arg : args) {
			predicate.or(person.firstName.containsIgnoreCase(arg));
			predicate.or(person.lastName.containsIgnoreCase(arg));
			predicate.or(person.email.isNotNull().and(person.email.containsIgnoreCase(arg)));
			predicate.or(person.address.city.isNotNull().and(person.address.city.equalsIgnoreCase(arg)));
		}

		return predicate;
	}

}
