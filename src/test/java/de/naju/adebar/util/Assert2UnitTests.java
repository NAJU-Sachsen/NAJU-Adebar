package de.naju.adebar.util;

import java.util.Arrays;
import org.junit.Test;

public class Assert2UnitTests {

  @Test(expected = IllegalArgumentException.class)
  public void noNullElementsDetectsSingleNull() {
    Assert2.noNullElements(Arrays.asList("bli", "bla", null, "blup"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noNullElementsDetectsMultipleNulls() {
    Assert2.noNullElements(Arrays.asList("bli", null, "bla", null, "blup", null, null));
  }

  @Test
  public void noNullElementsDoesNotMistakenly() {
    Assert2.noNullElements(Arrays.asList("bli", "bla", "blup"));
  }

  @Test
  public void noNullElementsDoesNotThrowOnEmptyIterable() {
    Assert2.noNullElements(Arrays.asList());
  }

}
