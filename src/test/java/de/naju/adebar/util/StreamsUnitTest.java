package de.naju.adebar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Basic testing of the {@link Streams} functions
 * 
 * @author Rico Bergmann
 */
public class StreamsUnitTest {
	private final static int EMPTY = 0;

	private List<Integer> allNumbers = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	private List<Integer> oddNumbers = Arrays.asList(1, 3, 5, 7, 9);
	private List<Integer> evenNumbers = Arrays.asList(0, 2, 4, 6, 8);
	private List<Integer> emptyList = new ArrayList<>();

	@Test
	public void testIntersect() {
		Assertions.assertArrayEquals(oddNumbers.toArray(), Streams.intersect(allNumbers.stream(), oddNumbers.stream()).collect(Collectors.toList())
						.toArray(),
				"Should only contain odd numbers");
		Assertions.assertEquals(EMPTY, Streams.intersect(oddNumbers.stream(), evenNumbers.stream()).count(),
				"Should not contain any elements");
		Assertions.assertEquals(EMPTY, Streams
				.intersect(allNumbers.stream(), emptyList.stream()).collect(Collectors.toList()).size(), "Should not contain any elements");
	}

	@Test
	public void testUnion() {
		Assertions.assertArrayEquals(allNumbers.toArray(), Streams.union(oddNumbers.stream(), evenNumbers.stream()).toArray(),
				"Should contain all numbers");
		Assertions.assertArrayEquals(oddNumbers.toArray(), Streams.union(emptyList.stream(), oddNumbers.stream()).toArray(),
				"Should contain all odd numbers");
	}

	@Test
	public void testSubtract() {
		Assertions.assertArrayEquals(evenNumbers.toArray(), Streams
				.subtract(allNumbers.stream(), oddNumbers.stream()).collect(Collectors.toList()).toArray(), "Should only contain even numbers!");

		Stream<Integer> firstSubtraction = Streams.subtract(allNumbers.stream(), oddNumbers.stream());
		Assertions.assertEquals(EMPTY, Streams
				.subtract(firstSubtraction, evenNumbers.stream()).collect(Collectors.toList()).size(), "Should contain no more elements!");
		Assertions.assertArrayEquals(evenNumbers.toArray(), Streams
				.subtract(evenNumbers.stream(), emptyList.stream()).collect(Collectors.toList()).toArray(), "Should not have removed any elements");
	}

	@Test
	public void testContains() {
		Assertions.assertTrue(Streams.contains(allNumbers.stream(), 7), "Stream contains element!");
		Assertions.assertTrue(Streams.contains(oddNumbers.stream(), 3), "Stream contains element!");
		Assertions.assertFalse(Streams.contains(evenNumbers.stream(), 7),
				"Stream does not contain element!");
		Assertions.assertFalse(Streams.contains(emptyList.stream(), 42), "Stream is empty!");
	}
}
