package de.naju.adebar.app.human.filter;

import de.naju.adebar.model.human.Person;

import java.time.LocalDate;
import java.util.stream.Stream;


/**
 * Filter for persons depending on their date of birth
 * @author Rico Bergmann
 */
public class DateOfBirthFilter implements PersonFilter {

    private LocalDate dob;
    private DateFilterType matchType;

    /**
     * @param dob the date of birth to use as an offset
     * @param matchType the way to treat the offset
     * @see DateFilterType
     */
    public DateOfBirthFilter(LocalDate dob, DateFilterType matchType) {
        this.dob = dob;
        this.matchType = matchType;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
        return personStream.filter(p -> matchType.matching(dob, p.getDateOfBirth()));
    }
}
