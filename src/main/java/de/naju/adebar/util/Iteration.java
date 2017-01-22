package de.naju.adebar.util;

/**
 * Helper functionality for {@link Iterable Iterables}
 * @author Rico Bergmann
 */
public class Iteration {

    /**
     * @param i the iterable to count
     * @return the number of elements in the iterable
     */
	@SuppressWarnings({"rawtypes", "unused"})
	public static int countElements(Iterable i) {
		int count = 0;
		for (Object o : i) {
			count++;
		}
		return count;
	}

    /**
     * @param i the iterable to check
     * @return {@code true} if the iterable does not have any elements to iterate over
     */
	@SuppressWarnings("unused")
	public static boolean isEmpty(Iterable i) {
	    return !i.iterator().hasNext();
    }
	
}
