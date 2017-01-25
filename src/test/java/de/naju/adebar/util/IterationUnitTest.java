package de.naju.adebar.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Basic testing of the {@link Iteration} functions
 * @author Rico Bergmann
 */
public class IterationUnitTest {
    private List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
    private List<Character> charList = Arrays.asList('A', 'B', 'C');
    private List emptyList = Collections.EMPTY_LIST;

    @Test
    public void testCountElements() {
        Assert.assertEquals("List has " + intList.size() + " elements",
                intList.size(), Iteration.countElements(intList));
        Assert.assertEquals("List has " + charList + " elements",
                charList.size(), Iteration.countElements(charList));
        Assert.assertEquals("List has no elements", emptyList.size(), Iteration.countElements(emptyList));
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse("List is not empty", Iteration.isEmpty(intList));
        Assert.assertFalse("List is not empty", Iteration.isEmpty(charList));
        Assert.assertTrue("List is empty", Iteration.isEmpty(emptyList));
    }

}
