package de.naju.adebar.app.human.filter;

import de.naju.adebar.model.human.Person;

import java.util.stream.Stream;

/**
 * Interface for filters. We only need them to filter
 * @author Rico Bergmann
 * @see PersonFilterBuilder
 */
public interface PersonFilter {

    /**
     * That's why we call it "filter". It receives elements, filters them and gives you elements again.
     * @param personStream the stream to filter
     * @return the filtered stream
     */
    Stream<Person> filter(Stream<Person> personStream);
}
