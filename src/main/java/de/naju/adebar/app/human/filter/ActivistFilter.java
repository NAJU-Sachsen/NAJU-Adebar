package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.util.Streams;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their activist status. It features two constructors depending on what one
 * actually wants to filter. Keep in mind that it is generally filtered for persons. That's why actually
 * no activists are needed here. For easy conversion there is a dedicated package
 * @author Rico Bergmann
 * @see de.naju.adebar.model.human.ActivistProfile
 * @see de.naju.adebar.util.conversion conversion
 */
public class ActivistFilter implements PersonFilter {
    private FilterType filterType;
    private LocalDate juleicaExpiryDate;
    private DateFilterType dateFilterType;

    /**
     * This constructor should be used if just a filter on general activist status is needed. That is, whether
     * persons have to be activists, or whether they may not.
     * @param filterType the kind of filter we want (enforcing or ignoring)
     */
    public ActivistFilter(FilterType filterType) {
        this.filterType = filterType;
        this.juleicaExpiryDate = null;
        this.dateFilterType = null;
    }

    /**
     * This constructor should be used if only activists with a specific juleica expiry date are wanted
     * @param juleicaExpiryDate the date to filter for
     * @param dateFilterType the way the expiry date should be treated (as minimum, maximum or exact)
     */
    public ActivistFilter(LocalDate juleicaExpiryDate, DateFilterType dateFilterType) {
        this.filterType = null;
        this.juleicaExpiryDate = juleicaExpiryDate;
        this.dateFilterType = dateFilterType;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
        // as the internals are quite simple we do not use the state pattern here
        // although it would have been a cleaner solution

        // if the filter type is not null, we only need to filter for the general activist status
        if (filterType != null) {
            switch (filterType) {
                case ENFORCE:
                    return personStream.filter(Person::isActivist);
                case IGNORE:
                    return personStream.filter(p -> !p.isActivist());
            }
        } else {
            // if we filter for activists with a specific kind of juleica expiry date, we should only consider activists
            Stream<Person> activists = personStream.filter(Person::isActivist);

            // furthermore we should only filter for activists that actually have a JuLeiCa
            activists = activists.filter(a -> a.getActivistProfile().hasJuleica());
            return activists.filter(person -> dateFilterType.matching(juleicaExpiryDate, person.getActivistProfile().getJuleicaCard().getExpiryDate()));
        }
        return null;
    }
}
