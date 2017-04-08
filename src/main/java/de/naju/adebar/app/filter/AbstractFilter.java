package de.naju.adebar.app.filter;

import java.util.stream.Stream;

/**
 * Interface for filters. We only need them to filter
 * @author Rico Bergmann
 */
public interface AbstractFilter<T> {

    /**
     * That's why we call it "filter". It receives elements, filters them and gives elements again.
     * @param input the stream to filter
     * @return the filtered stream
     */
    Stream<T> filter(Stream<T> input);

}
