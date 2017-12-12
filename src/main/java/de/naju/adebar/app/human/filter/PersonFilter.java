package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.filter.AbstractStreamBasedFilter;
import de.naju.adebar.model.human.Person;

/**
 * An filter for persons
 * 
 * @author Rico Bergmann
 * @see Person
 * @see PersonFilterBuilder
 */
public interface PersonFilter extends AbstractStreamBasedFilter<Person> {
}
