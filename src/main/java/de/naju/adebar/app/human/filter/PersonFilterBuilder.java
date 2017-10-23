package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.filter.AbstractFilter;
import de.naju.adebar.app.filter.FilterBuilder;
import de.naju.adebar.model.human.Person;
import org.springframework.util.Assert;

import java.util.stream.Stream;

/**
 * Builder to collect all the needed filters and finally apply them. The class follows a variation of the builder pattern
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 */
public class PersonFilterBuilder extends FilterBuilder<Person> {

    /**
     * @param personStream the persons to be filtered
     */
    public PersonFilterBuilder(Stream<Person> personStream) {
        super(personStream);
    }

    /**
     * @param filter the filter to apply to the given persons
     * @return the builder instance for easy chaining
     * @throws ConflictingFilterCriteriaException if the builder already contains a filter of the same class
     */
    public PersonFilterBuilder applyFilter(PersonFilter filter) {
        Assert.notNull(filter, "Filter may not be null!");
        for (AbstractFilter<Person> f : filters) {
            if (filter.getClass() == f.getClass()) {
                throw new ConflictingFilterCriteriaException("Already containing a filter of class: " + f.getClass());
            }
        }
        super.applyFilter(filter);
        return this;
    }
}
