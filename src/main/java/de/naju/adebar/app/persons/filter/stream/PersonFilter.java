package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.app.filter.streams.AbstractStreamBasedFilter;
import de.naju.adebar.model.persons.Person;

/**
 * An filter for persons
 * 
 * @author Rico Bergmann
 * @see Person
 * @see PersonFilterBuilder
 */
public interface PersonFilter extends AbstractStreamBasedFilter<Person> {
}
