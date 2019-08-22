package de.naju.adebar.app.filter.legacy.streams;

import java.util.stream.Stream;
import de.naju.adebar.app.filter.legacy.AbstractFilter;

/**
 * An {@link AbstractFilter} which consumes and produces a {@link Stream} of objects
 * 
 * @author Rico Bergmann
 *
 * @param <T> the kind of objects in the stream
 */
public interface AbstractStreamBasedFilter<T> extends AbstractFilter<Stream<T>> {
}
