package de.naju.adebar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;

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
    Assert.assertArrayEquals("Should only contain odd numbers", oddNumbers.toArray(),
        Streams.intersect(allNumbers.stream(), oddNumbers.stream()).collect(Collectors.toList())
            .toArray());
    Assert.assertEquals("Should not contain any elements", EMPTY,
        Streams.intersect(oddNumbers.stream(), evenNumbers.stream()).count());
    Assert.assertEquals("Should not contain any elements", EMPTY, Streams
        .intersect(allNumbers.stream(), emptyList.stream()).collect(Collectors.toList()).size());
  }

  @Test
  public void testUnion() {
    Assert.assertArrayEquals("Should contain all numbers", allNumbers.toArray(),
        Streams.union(oddNumbers.stream(), evenNumbers.stream()).toArray());
    Assert.assertArrayEquals("Should contain all odd numbers", oddNumbers.toArray(),
        Streams.union(emptyList.stream(), oddNumbers.stream()).toArray());
  }

  @Test
  public void testSubtract() {
    Assert.assertArrayEquals("Should only contain even numbers!", evenNumbers.toArray(), Streams
        .subtract(allNumbers.stream(), oddNumbers.stream()).collect(Collectors.toList()).toArray());

    Stream<Integer> firstSubtraction = Streams.subtract(allNumbers.stream(), oddNumbers.stream());
    Assert.assertEquals("Should contain no more elements!", EMPTY, Streams
        .subtract(firstSubtraction, evenNumbers.stream()).collect(Collectors.toList()).size());
    Assert.assertArrayEquals("Should not have removed any elements", evenNumbers.toArray(), Streams
        .subtract(evenNumbers.stream(), emptyList.stream()).collect(Collectors.toList()).toArray());
  }

  @Test
  public void testContains() {
    Assert.assertTrue("Stream contains element!", Streams.contains(allNumbers.stream(), 7));
    Assert.assertTrue("Stream contains element!", Streams.contains(oddNumbers.stream(), 3));
    Assert.assertFalse("Stream does not contain element!",
        Streams.contains(evenNumbers.stream(), 7));
    Assert.assertFalse("Stream is empty!", Streams.contains(emptyList.stream(), 42));
  }
}
