package de.naju.adebar.app.human.filter;

import de.naju.adebar.model.human.Activist;
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
 * @see Activist
 * @see de.naju.adebar.util.conversion conversion
 */
public class ActivistFilter implements PersonFilter {
    private Stream<Person> activists;
    private FilterType filterType;
    private LocalDate juleicaExpiryDate;
    private DateFilterType dateFilterType;
    private Map<Person, LocalDate> juleicaExpiryDates;

    /**
     * This constructor should be used if just a filter on general activist status is needed. That is, whether
     * persons have to be activists, or whether they may not.
     * @param activists all persons that are activists
     * @param filterType the kind of filter we want (enforcing or ignoring)
     */
    public ActivistFilter(Stream<Person> activists, FilterType filterType) {
        this.activists = activists;
        this.filterType = filterType;
        this.juleicaExpiryDate = null;
        this.dateFilterType = null;
        this.juleicaExpiryDates = null;
    }

    /**
     * This constructor should be used if only activists with a specific juleica expiry date are wanted
     * @param activists all persons that are activists
     * @param juleicaExpiryDate the date to filter for
     * @param dateFilterType the way the expiry date should be treated (as minimum, maximum or exact)
     * @param juleicaExpiryDates a map containing the juleica expiry dates of the activists
     */
    public ActivistFilter(Stream<Person> activists, LocalDate juleicaExpiryDate, DateFilterType dateFilterType,
                          Map<Person, LocalDate> juleicaExpiryDates) {
        this.activists = activists;
        this.filterType = null;
        this.juleicaExpiryDate = juleicaExpiryDate;
        this.dateFilterType = dateFilterType;
        this.juleicaExpiryDates = juleicaExpiryDates;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
        // as the internals are quite simple we do not use the state pattern here
        // although it would have been a cleaner solution

        // if the filter type is not null, we only need to filter for the general activist status
        if (filterType != null) {
            //noinspection Duplicates - this code is used in the referent filter, too. But the two filters got nothing in common
            switch (filterType) {
                // we can abstract the situation as operating upon different sets
                case ENFORCE:
                    // if all persons have to be activists, we need the intersection between persons and activists
                    return Streams.intersect(personStream, activists);
                case IGNORE:
                    // if no person may be an activist, we need the relative complement between persons and activists
                    return Streams.subtract(personStream, activists);
            }
        } else {
            // if we filter for activists with a specific kind of juleica expiry date, we should only consider activists
            Stream<Person> activists = Streams.intersect(personStream, this.activists);

            // furthermore we should only filter for activists that actually have a JuLeiCa
            activists = Streams.intersect(activists, juleicaExpiryDates.keySet().stream());
            return activists.filter(person ->
                    dateFilterType.matching(juleicaExpiryDate, juleicaExpiryDates.get(person)));
        }
        return null;
    }
}
