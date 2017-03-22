package de.naju.adebar.app.human.filter;

import de.naju.adebar.model.human.Person;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builder to collect all the needed filters and finally apply them. The class follows a variation of the builder pattern
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 */
public class PersonFilterBuilder {
    private Stream<Person> personStream;
    private Set<PersonFilter> filters;

    /**
     * @param personStream the persons to be filtered
     */
    public PersonFilterBuilder(Stream<Person> personStream) {
        Assert.notNull(personStream, "Person stream may not be null!");
        this.personStream = personStream;
        this.filters = new HashSet<>();
    }

    /**
     * @param filter the filter to apply to the given persons
     * @return the builder instance for easy chaining
     * @throws ConflictingFilterCriteriaException if the builder already contains a filter of the same class
     */
    public PersonFilterBuilder applyFilter(PersonFilter filter) {
        Assert.notNull(filter, "Filter may not be null!");
        for (PersonFilter f : filters) {
            if (filter.getClass() == f.getClass()) {
                throw new ConflictingFilterCriteriaException("Already containing a filter of class: " + f.getClass());
            }
        }
        filters.add(filter);
        return this;
    }

    /**
     * Executes the filters
     * @return the persons who matched all of the criteria
     */
    public Iterable<Person> filter() {
        return resultingStream().collect(Collectors.toList());
    }

    /**
     * Executes the filters but returns the result uncollected. The return type is the only difference to {@link #filter()}
     * @return the persons who matched all of the criteria
     */
    public Stream<Person> resultingStream() {
        filters.forEach(filter -> personStream = filter.filter(personStream));
        return personStream;
    }
}
