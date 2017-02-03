package de.naju.adebar.app.human.filter;

import com.google.common.collect.Lists;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.Qualification;
import de.naju.adebar.model.human.Referent;
import de.naju.adebar.util.Streams;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their referent status. It features two constructors depending on what one
 * actually wants to filter. Keep in mind that it is generally filtered for persons. That's why actually
 * no activists are needed here. For easy conversion there is a dedicated package
 * @author Rico Bergmann
 * @see Referent
 * @see de.naju.adebar.util.conversion conversion
 */
public class ReferentFilter implements PersonFilter {
    private Stream<Person> referents;
    private FilterType filterType;
    private List<Qualification> qualifications;
    private Map<Person, Iterable<Qualification>> referentQualifications;

    /**
     * This constructor should be used if just a filter on general referent status is needed. That is, whether
     * persons have to be referents, or whether they may not.
     * @param referents all persons that are activists
     * @param filterType the kind of filter we want (enforcing or ignoring)
     */
    public ReferentFilter(Stream<Person> referents, FilterType filterType) {
        this.referents = referents;
        this.filterType = filterType;
        this.qualifications = null;
        this.referentQualifications = null;
    }

    /**
     * This constructor should be used if only referents with specific qualifications are wanted
     * @param referents all persons that are activists
     * @param qualifications the qualifications to filter for
     * @param referentQualifications a map containing the qualifications of the referents
     */
    public ReferentFilter(Stream<Person> referents, List<Qualification> qualifications,
                          Map<Person, Iterable<Qualification>> referentQualifications) {
        this.referents = referents;
        this.filterType = null;
        this.qualifications = qualifications;
        this.referentQualifications = referentQualifications;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
        // as the internals are quite simple we do not use the state pattern here
        // although it would have been a cleaner solution

        // if the filter type is not null, we only need to filter for the general referent status
        if (filterType != null) {
            //noinspection Duplicates - this code is used in the activist filter, too. But the two filters got nothing in common
            switch (filterType) {
                // we can abstract the situation as operating upon different sets
                case ENFORCE:
                    // if all persons have to be activists, we need the intersection between persons and activists
                    return Streams.intersect(personStream, referents);
                case IGNORE:
                    // if no person may be a referent, we need the relative complement between persons and referents
                    return Streams.subtract(personStream, referents);
            }
        } else {
            // if we filter for referent with specific qualifications, we should only consider referents
            Stream<Person> referents = Streams.intersect(personStream, this.referents);
            return referents.filter(person ->
                    Lists.newLinkedList(referentQualifications.get(person)).containsAll(qualifications));
        }
        return null;
    }
}
