package de.naju.adebar.util;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rico Bergmann
 * @see Stream
 */
public class Streams {

    /**
     * Calculates a stream c, such that {@code c := a \u2229 b}
     */
    public static <T> Stream<T> intersect(Stream<T> a, Stream<T> b) {
        List<T> listA = a.collect(Collectors.toList());
        List<T> listB = b.collect(Collectors.toList());
        List<T> result = new LinkedList<T>();
        listA.forEach(elem -> {
            if (listB.contains(elem)) {
                result.add(elem);
            }
        });
        return result.stream();
    }

    /**
     * Calculates a stream c, such that {@code c := a \ b}
     */
    public static <T> Stream<T> subtract(Stream<T> a, Stream<T> b) {
        List<T> listA = a.collect(Collectors.toList());
        List<T> listB = b.collect(Collectors.toList());
        listA.removeAll(listB);
        return listA.stream();
    }

    /**
     * Checks, if a stream contains a certain element. This will waste the stream!
     * @param s the stream to check
     * @param e the element to check for
     * @return {@code true \u21D4  elem \u2208 stream}
     */
    public static <T> boolean contains(Stream<T> s, T e) {
        return s.collect(Collectors.toList()).contains(e);
    }

}
